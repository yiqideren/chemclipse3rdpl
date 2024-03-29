/*
 * * Copyright 2014 Orient Technologies LTD (info(at)orientechnologies.com)
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 * * http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * *
 * * For more information: http://www.orientechnologies.com
 */
package com.orientechnologies.orient.core.index;

import com.orientechnologies.common.exception.OException;
import com.orientechnologies.common.listener.OProgressListener;
import com.orientechnologies.common.log.OLogManager;
import com.orientechnologies.common.util.OMultiKey;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.core.db.ODatabaseInternal;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.record.ORecordElement;
import com.orientechnologies.orient.core.db.record.ORecordTrackedSet;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchemaShared;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.metadata.security.OSecurityNull;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.storage.OStorage;
import com.orientechnologies.orient.core.storage.impl.local.OAbstractPaginatedStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages indexes at database level. A single instance is shared among multiple databases. Contentions are managed by r/w locks.
 *
 * @author Luca Garulli (l.garulli--at--orientechnologies.com)
 * @author Artem Orobets added composite index managemement
 */
public class OIndexManagerShared extends OIndexManagerAbstract implements OIndexManager {

	private static final long serialVersionUID = 1L;
	protected volatile Thread recreateIndexesThread = null;
	private volatile boolean rebuildCompleted = false;

	public OIndexManagerShared(final ODatabaseDocument iDatabase) {

		super(iDatabase);
	}

	public OIndex<?> getIndexInternal(final String name) {

		acquireSharedLock();
		try {
			return indexes.get(name.toLowerCase());
		} finally {
			releaseSharedLock();
		}
	}

	/**
	 * Create a new index with default algorithm.
	 *
	 * @param iName
	 *            - name of index
	 * @param iType
	 *            - index type. Specified by plugged index factories.
	 * @param indexDefinition
	 *            metadata that describes index structure
	 * @param clusterIdsToIndex
	 *            ids of clusters that index should track for changes.
	 * @param progressListener
	 *            listener to track task progress.
	 * @param metadata
	 *            document with additional properties that can be used by index engine.
	 * @return a newly created index instance
	 */
	public OIndex<?> createIndex(final String iName, final String iType, final OIndexDefinition indexDefinition, final int[] clusterIdsToIndex, OProgressListener progressListener, ODocument metadata) {

		return createIndex(iName, iType, indexDefinition, clusterIdsToIndex, progressListener, metadata, null);
	}

	/**
	 * Create a new index.
	 * <p>
	 * May require quite a long time if big amount of data should be indexed.
	 *
	 * @param iName
	 *            name of index
	 * @param iType
	 *            index type. Specified by plugged index factories.
	 * @param indexDefinition
	 *            metadata that describes index structure
	 * @param clusterIdsToIndex
	 *            ids of clusters that index should track for changes.
	 * @param progressListener
	 *            listener to track task progress.
	 * @param metadata
	 *            document with additional properties that can be used by index engine.
	 * @param algorithm
	 *            tip to an index factory what algorithm to use
	 * @return a newly created index instance
	 */
	public OIndex<?> createIndex(final String iName, final String iType, final OIndexDefinition indexDefinition, final int[] clusterIdsToIndex, OProgressListener progressListener, ODocument metadata, String algorithm) {

		if(getDatabase().getTransaction().isActive())
			throw new IllegalStateException("Cannot create a new index inside a transaction");
		final Character c = OSchemaShared.checkFieldNameIfValid(iName);
		if(c != null)
			throw new IllegalArgumentException("Invalid index name '" + iName + "'. Character '" + c + "' is invalid");
		ODatabaseInternal database = getDatabase();
		OStorage storage = database.getStorage();
		algorithm = chooseTreeAlgorithm(algorithm);
		final String valueContainerAlgorithm = chooseContainerAlgorithm(iType);
		final OIndexInternal<?> index;
		acquireExclusiveLock();
		try {
			if(indexes.containsKey(iName.toLowerCase()))
				throw new OIndexException("Index with name " + iName.toLowerCase() + " already exists.");
			// manual indexes are always durable
			if(clusterIdsToIndex == null || clusterIdsToIndex.length == 0) {
				if(metadata == null)
					metadata = new ODocument().setTrackingChanges(false);
				Object durable = metadata.field("durableInNonTxMode");
				if(!(durable instanceof Boolean))
					metadata.field("durableInNonTxMode", true);
				if(metadata.field("trackMode") == null)
					metadata.field("trackMode", "FULL");
			}
			index = OIndexes.createIndex(getDatabase(), iName, iType, algorithm, valueContainerAlgorithm, metadata, -1);
			// decide which cluster to use ("index" - for automatic and "manindex" for manual)
			final String clusterName = indexDefinition != null && indexDefinition.getClassName() != null ? defaultClusterName : manualClusterName;
			if(progressListener == null)
				// ASSIGN DEFAULT PROGRESS LISTENER
				progressListener = new OIndexRebuildOutputListener(index);
			final Set<String> clustersToIndex = findClustersByIds(clusterIdsToIndex, database);
			if(metadata != null && Boolean.FALSE.equals(metadata.field("ignoreNullValues")) && indexDefinition != null)
				indexDefinition.setNullValuesIgnored(false);
			index.create(iName, indexDefinition, clusterName, clustersToIndex, true, progressListener);
			addIndexInternal(index);
			if(metadata != null) {
				final ODocument config = index.getConfiguration();
				config.field("metadata", metadata, OType.EMBEDDED);
			}
			setDirty();
			save();
		} finally {
			releaseExclusiveLock();
		}
		if(OGlobalConfiguration.INDEX_FLUSH_AFTER_CREATE.getValueAsBoolean())
			storage.synch();
		return preProcessBeforeReturn(index);
	}

	private Set<String> findClustersByIds(int[] clusterIdsToIndex, ODatabase database) {

		Set<String> clustersToIndex = new HashSet<String>();
		if(clusterIdsToIndex != null) {
			for(int clusterId : clusterIdsToIndex) {
				final String clusterNameToIndex = database.getClusterNameById(clusterId);
				if(clusterNameToIndex == null)
					throw new OIndexException("Cluster with id " + clusterId + " does not exist.");
				clustersToIndex.add(clusterNameToIndex);
			}
		}
		return clustersToIndex;
	}

	private String chooseContainerAlgorithm(String type) {

		final String valueContainerAlgorithm;
		if(OClass.INDEX_TYPE.NOTUNIQUE.toString().equals(type) || OClass.INDEX_TYPE.NOTUNIQUE_HASH_INDEX.toString().equals(type) || OClass.INDEX_TYPE.FULLTEXT_HASH_INDEX.toString().equals(type) || OClass.INDEX_TYPE.FULLTEXT.toString().equals(type)) {
			valueContainerAlgorithm = ODefaultIndexFactory.SBTREEBONSAI_VALUE_CONTAINER;
		} else {
			valueContainerAlgorithm = ODefaultIndexFactory.NONE_VALUE_CONTAINER;
		}
		return valueContainerAlgorithm;
	}

	private String chooseTreeAlgorithm(String algorithm) {

		if(algorithm == null)
			algorithm = ODefaultIndexFactory.SBTREE_ALGORITHM;
		return algorithm;
	}

	public OIndexManager dropIndex(final String iIndexName) {

		if(getDatabase().getTransaction().isActive())
			throw new IllegalStateException("Cannot drop an index inside a transaction");
		acquireExclusiveLock();
		try {
			final OIndex<?> idx = indexes.remove(iIndexName.toLowerCase());
			if(idx != null) {
				removeClassPropertyIndex(idx);
				idx.delete();
				setDirty();
				save();
			}
		} finally {
			releaseExclusiveLock();
		}
		final OStorage storage = getDatabase().getStorage();
		if(OGlobalConfiguration.INDEX_FLUSH_AFTER_CREATE.getValueAsBoolean())
			storage.synch();
		return this;
	}

	/**
	 * Binds POJO to ODocument.
	 */
	@Override
	public ODocument toStream() {

		acquireExclusiveLock();
		try {
			document.setInternalStatus(ORecordElement.STATUS.UNMARSHALLING);
			try {
				final ORecordTrackedSet idxs = new ORecordTrackedSet(document);
				for(final OIndex<?> i : indexes.values()) {
					idxs.add(((OIndexInternal<?>)i).updateConfiguration());
				}
				document.field(CONFIG_INDEXES, idxs, OType.EMBEDDEDSET);
			} finally {
				document.setInternalStatus(ORecordElement.STATUS.LOADED);
			}
			document.setDirty();
			return document;
		} finally {
			releaseExclusiveLock();
		}
	}

	@Override
	public void recreateIndexes() {

		acquireExclusiveLock();
		try {
			if(recreateIndexesThread != null && recreateIndexesThread.isAlive())
				// BUILDING ALREADY IN PROGRESS
				return;
			final ODatabaseDocument db = getDatabase();
			document = db.load(new ORecordId(getDatabase().getStorage().getConfiguration().indexMgrRecordId));
			final ODocument doc = new ODocument();
			document.copyTo(doc);
			// USE A NEW DB INSTANCE
			final ODatabaseDocumentTx newDb = new ODatabaseDocumentTx(db.getURL());
			Runnable recreateIndexesTask = new RecreateIndexesTask(newDb, doc);
			recreateIndexesThread = new Thread(recreateIndexesTask, "OrientDB rebuild indexes");
			recreateIndexesThread.start();
		} finally {
			releaseExclusiveLock();
		}
		if(OGlobalConfiguration.INDEX_SYNCHRONOUS_AUTO_REBUILD.getValueAsBoolean()) {
			waitTillIndexRestore();
			getDatabase().getMetadata().reload();
		}
	}

	@Override
	public void waitTillIndexRestore() {

		if(recreateIndexesThread != null && recreateIndexesThread.isAlive()) {
			if(Thread.currentThread().equals(recreateIndexesThread))
				return;
			OLogManager.instance().info(this, "Wait till indexes restore after crash was finished.");
			while(recreateIndexesThread.isAlive())
				try {
					recreateIndexesThread.join();
					OLogManager.instance().info(this, "Indexes restore after crash was finished.");
				} catch(InterruptedException e) {
					OLogManager.instance().info(this, "Index rebuild task was interrupted.");
				}
		}
	}

	public boolean autoRecreateIndexesAfterCrash() {

		if(rebuildCompleted)
			return false;
		final ODatabaseDocumentInternal database = ODatabaseRecordThreadLocal.INSTANCE.get();
		final OStorage storage = database.getStorage().getUnderlying();
		if(storage instanceof OAbstractPaginatedStorage) {
			OAbstractPaginatedStorage paginatedStorage = (OAbstractPaginatedStorage)storage;
			return paginatedStorage.wereDataRestoredAfterOpen() && paginatedStorage.wereNonTxOperationsPerformedInPreviousOpen();
		}
		return false;
	}

	@Override
	protected void fromStream() {

		acquireExclusiveLock();
		try {
			final Map<String, OIndex<?>> oldIndexes = new HashMap<String, OIndex<?>>(indexes);
			clearMetadata();
			final Collection<ODocument> idxs = document.field(CONFIG_INDEXES);
			if(idxs != null) {
				OIndexInternal<?> index;
				boolean configUpdated = false;
				Iterator<ODocument> indexConfigurationIterator = idxs.iterator();
				while(indexConfigurationIterator.hasNext()) {
					final ODocument d = indexConfigurationIterator.next();
					try {
						final int indexVersion = d.field(OIndexInternal.INDEX_VERSION) == null ? 1 : (Integer)d.field(OIndexInternal.INDEX_VERSION);
						OIndexInternal.IndexMetadata newIndexMetadata = OIndexAbstract.loadMetadataInternal(d, (String)d.field(OIndexInternal.CONFIG_TYPE), (String)d.field(OIndexInternal.ALGORITHM), d.<String> field(OIndexInternal.VALUE_CONTAINER_ALGORITHM));
						index = OIndexes.createIndex(getDatabase(), newIndexMetadata.getName(), newIndexMetadata.getType(), newIndexMetadata.getAlgorithm(), newIndexMetadata.getValueContainerAlgorithm(), (ODocument)d.field(OIndexInternal.METADATA), indexVersion);
						final String normalizedName = newIndexMetadata.getName().toLowerCase();
						OIndex<?> oldIndex = oldIndexes.get(normalizedName);
						if(oldIndex != null) {
							OIndexInternal.IndexMetadata oldIndexMetadata = oldIndex.getInternal().loadMetadata(oldIndex.getConfiguration());
							if(oldIndexMetadata.equals(newIndexMetadata)) {
								addIndexInternal(oldIndex.getInternal());
								oldIndexes.remove(normalizedName);
							} else if(newIndexMetadata.getIndexDefinition() == null && d.field(OIndexAbstract.CONFIG_MAP_RID).equals(oldIndex.getConfiguration().field(OIndexAbstract.CONFIG_MAP_RID))) {
								// index is manual and index definition was just detected
								addIndexInternal(oldIndex.getInternal());
								oldIndexes.remove(normalizedName);
							}
						} else {
							if(index.loadFromConfiguration(d)) {
								addIndexInternal(index);
							} else {
								indexConfigurationIterator.remove();
								configUpdated = true;
							}
						}
					} catch(Exception e) {
						indexConfigurationIterator.remove();
						configUpdated = true;
						OLogManager.instance().error(this, "Error on loading index by configuration: %s", e, d);
					}
				}
				for(OIndex<?> oldIndex : oldIndexes.values())
					try {
						OLogManager.instance().warn(this, "Index '%s' was not found after reload and will be removed", oldIndex.getName());
						oldIndex.delete();
					} catch(Exception e) {
						OLogManager.instance().error(this, "Error on deletion of index '%s'", e, oldIndex.getName());
					}
				if(configUpdated) {
					document.field(CONFIG_INDEXES, idxs);
					save();
				}
			}
		} finally {
			releaseExclusiveLock();
		}
	}

	public void removeClassPropertyIndex(final OIndex<?> idx) {

		acquireExclusiveLock();
		try {
			final OIndexDefinition indexDefinition = idx.getDefinition();
			if(indexDefinition == null || indexDefinition.getClassName() == null)
				return;
			Map<OMultiKey, Set<OIndex<?>>> map = classPropertyIndex.get(indexDefinition.getClassName().toLowerCase());
			if(map == null) {
				return;
			}
			map = new HashMap<OMultiKey, Set<OIndex<?>>>(map);
			final int paramCount = indexDefinition.getParamCount();
			for(int i = 1; i <= paramCount; i++) {
				final List<String> fields = normalizeFieldNames(indexDefinition.getFields().subList(0, i));
				final OMultiKey multiKey = new OMultiKey(fields);
				Set<OIndex<?>> indexSet = map.get(multiKey);
				if(indexSet == null)
					continue;
				indexSet = new HashSet<OIndex<?>>(indexSet);
				indexSet.remove(idx);
				if(indexSet.isEmpty()) {
					map.remove(multiKey);
				} else {
					map.put(multiKey, indexSet);
				}
			}
			if(map.isEmpty())
				classPropertyIndex.remove(indexDefinition.getClassName().toLowerCase());
			else
				classPropertyIndex.put(indexDefinition.getClassName().toLowerCase(), copyPropertyMap(map));
		} finally {
			releaseExclusiveLock();
		}
	}

	private class RecreateIndexesTask implements Runnable {

		private final ODatabaseDocumentTx newDb;
		private final ODocument doc;
		private int ok;
		private int errors;

		public RecreateIndexesTask(ODatabaseDocumentTx newDb, ODocument doc) {

			this.newDb = newDb;
			this.doc = doc;
		}

		@Override
		public void run() {

			try {
				setUpDatabase();
				final Collection<ODocument> idxs = getConfiguration();
				recreateIndexes(idxs);
			} catch(Exception e) {
				OLogManager.instance().error(this, "Error when attempt to restore indexes after crash was performed.", e);
			}
		}

		private void recreateIndexes(Collection<ODocument> idxs) {

			ok = 0;
			errors = 0;
			for(ODocument idx : idxs) {
				try {
					recreateIndex(idx);
				} catch(RuntimeException e) {
					OLogManager.instance().error(this, "Error during addition of index %s", e, idx);
					errors++;
				}
			}
			save();
			final OStorage storage = newDb.getStorage();
			if(OGlobalConfiguration.INDEX_FLUSH_AFTER_CREATE.getValueAsBoolean())
				storage.synch();
			rebuildCompleted = true;
			OLogManager.instance().info(this, "%d indexes were restored successfully, %d errors", ok, errors);
		}

		private void recreateIndex(ODocument idx) {

			final OIndexInternal<?> index = createIndex(idx);
			OIndexInternal.IndexMetadata indexMetadata = index.loadMetadata(idx);
			OIndexDefinition indexDefinition = indexMetadata.getIndexDefinition();
			if(indexDefinition != null && indexDefinition.isAutomatic()) {
				createAutomaticIndex(idx, index, indexMetadata, indexDefinition);
			} else {
				addIndexAsIs(idx, index, indexMetadata);
			}
		}

		private void createAutomaticIndex(ODocument idx, OIndexInternal<?> index, OIndexInternal.IndexMetadata indexMetadata, OIndexDefinition indexDefinition) {

			final String indexName = indexMetadata.getName();
			final Set<String> clusters = indexMetadata.getClustersToIndex();
			final String type = indexMetadata.getType();
			if(indexName != null && clusters != null && !clusters.isEmpty() && type != null) {
				OLogManager.instance().info(this, "Start creation of index %s", indexName);
				index.deleteWithoutIndexLoad(indexName);
				index.create(indexName, indexDefinition, defaultClusterName, clusters, false, new OIndexRebuildOutputListener(index));
				index.setRebuildingFlag();
				addIndexInternal(index);
				OLogManager.instance().info(this, "Index %s was successfully created and rebuild is going to be started.", indexName);
				index.rebuild(new OIndexRebuildOutputListener(index));
				index.flush();
				setDirty();
				ok++;
				OLogManager.instance().info(this, "Rebuild of %s index was successfully finished.", indexName);
			} else {
				errors++;
				OLogManager.instance().error(this, "Information about index was restored incorrectly, following data were loaded : " + "index name - %s, index definition %s, clusters %s, type %s.", indexName, indexDefinition, clusters, type);
			}
		}

		private void addIndexAsIs(ODocument idx, OIndexInternal<?> index, OIndexInternal.IndexMetadata indexMetadata) {

			OLogManager.instance().info(this, "Index %s is not automatic index and will be added as is.", indexMetadata.getName());
			if(index.loadFromConfiguration(idx)) {
				addIndexInternal(index);
				setDirty();
				ok++;
				OLogManager.instance().info(this, "Index %s was added in DB index list.", index.getName());
			} else {
				index.delete();
				errors++;
			}
		}

		private OIndexInternal<?> createIndex(ODocument idx) {

			final String indexName = idx.field(OIndexInternal.CONFIG_NAME);
			final String indexType = idx.field(OIndexInternal.CONFIG_TYPE);
			String algorithm = idx.field(OIndexInternal.ALGORITHM);
			String valueContainerAlgorithm = idx.field(OIndexInternal.VALUE_CONTAINER_ALGORITHM);
			ODocument metadata = idx.field(OIndexInternal.METADATA);
			if(indexType == null) {
				OLogManager.instance().error(this, "Index type is null, will process other record.");
				throw new OException("Index type is null, will process other record. Index configuration: " + idx.toString());
			}
			return OIndexes.createIndex(newDb, indexName, indexType, algorithm, valueContainerAlgorithm, metadata, -1);
		}

		private Collection<ODocument> getConfiguration() {

			final Collection<ODocument> idxs = doc.field(CONFIG_INDEXES);
			if(idxs == null) {
				OLogManager.instance().warn(this, "List of indexes is empty.");
				return Collections.emptyList();
			}
			return idxs;
		}

		private void setUpDatabase() {

			newDb.activateOnCurrentThread();
			newDb.resetInitialization();
			newDb.setProperty(ODatabase.OPTIONS.SECURITY.toString(), OSecurityNull.class);
			newDb.open("admin", "nopass");
			ODatabaseRecordThreadLocal.INSTANCE.set(newDb);
		}
	}
}
