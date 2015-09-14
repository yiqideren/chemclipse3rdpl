package com.orientechnologies.common.concur.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.orientechnologies.common.log.OLogManager;
import com.orientechnologies.orient.core.OOrientListenerAbstract;
import com.orientechnologies.orient.core.Orient;

/**
 * This is internal API, do not use it.
 * 
 * @author Andrey Lomakin <a href="mailto:lomakin.andrey@gmail.com">Andrey Lomakin</a>
 * @since 15/12/14
 */
public class OPartitionedObjectPoolFactory<K, T> extends OOrientListenerAbstract {

	private volatile int maxPartitions = Runtime.getRuntime().availableProcessors() << 3;
	private volatile int maxPoolSize = 64;
	private boolean closed = false;
	private final Cache<K, OPartitionedObjectPool<T>> poolStore;
	private final ObjectFactoryFactory<K, T> objectFactoryFactory;
	private final RemovalListener<K, OPartitionedObjectPool<T>> evictionListener = new RemovalListener<K, OPartitionedObjectPool<T>>() {

		@Override
		public void onRemoval(RemovalNotification<K, OPartitionedObjectPool<T>> partitionedObjectPool) {

			partitionedObjectPool.getValue().close();
		}
	};

	public OPartitionedObjectPoolFactory(final ObjectFactoryFactory<K, T> objectFactoryFactory) {

		this(objectFactoryFactory, 100);
	}

	public OPartitionedObjectPoolFactory(final ObjectFactoryFactory<K, T> objectFactoryFactory, final int capacity) {

		this.objectFactoryFactory = objectFactoryFactory;
		poolStore = CacheBuilder.newBuilder().maximumWeight(capacity).removalListener(evictionListener).build();
		Orient.instance().registerWeakOrientStartupListener(this);
		Orient.instance().registerWeakOrientShutdownListener(this);
	}

	public int getMaxPoolSize() {

		return maxPoolSize;
	}

	public void setMaxPoolSize(final int maxPoolSize) {

		checkForClose();
		this.maxPoolSize = maxPoolSize;
	}

	public OPartitionedObjectPool<T> get(final K key) {

		checkForClose();
		OPartitionedObjectPool<T> pool = poolStore.getIfPresent(key);
		if(pool != null)
			return pool;
		pool = new OPartitionedObjectPool<T>(objectFactoryFactory.create(key), maxPoolSize, maxPartitions);
		final OPartitionedObjectPool<T> oldPool = poolStore.asMap().putIfAbsent(key, pool);
		if(oldPool != null) {
			pool.close();
			return oldPool;
		}
		return pool;
	}

	public int getMaxPartitions() {

		return maxPartitions;
	}

	public void setMaxPartitions(final int maxPartitions) {

		this.maxPartitions = maxPartitions;
	}

	public Collection<OPartitionedObjectPool<T>> getPools() {

		checkForClose();
		return Collections.unmodifiableCollection(poolStore.asMap().values());
	}

	public void close() {

		if(closed)
			return;
		closed = true;
		while(!poolStore.asMap().isEmpty()) {
			final Iterator<OPartitionedObjectPool<T>> poolIterator = poolStore.asMap().values().iterator();
			while(poolIterator.hasNext()) {
				final OPartitionedObjectPool<T> pool = poolIterator.next();
				try {
					pool.close();
				} catch(Exception e) {
					OLogManager.instance().error(this, "Error during pool close", e);
				}
				poolIterator.remove();
			}
		}
		for(OPartitionedObjectPool<T> pool : poolStore.asMap().values())
			pool.close();
		poolStore.asMap().clear();
	}

	@Override
	public void onShutdown() {

		close();
	}

	private void checkForClose() {

		if(closed)
			throw new IllegalStateException("Pool factory is closed");
	}

	public interface ObjectFactoryFactory<K, T> {

		OPartitionedObjectPool.ObjectFactory<T> create(K key);
	}
}
