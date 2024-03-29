/*
 * Copyright 2010-2012 Luca Molino (molino.luca--at--gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientechnologies.orient.object.serialization;

import java.io.Serializable;
import java.util.Iterator;

import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.object.enhancement.OObjectEntitySerializer;

/**
 * Lazy implementation of Iterator that load the records only when accessed. It keep also track of changes to the source record
 * avoiding to call setDirty() by hand.
 * 
 * @author Luca Molino (molino.luca--at--gmail.com)
 * 
 */
@SuppressWarnings({"unchecked"})
public class OObjectCustomSerializerIterator<TYPE> implements Iterator<TYPE>, Serializable {

	private static final long serialVersionUID = -4012483076050044405L;
	private final ORecord sourceRecord;
	private final Iterator<? extends Object> underlying;
	private final Class<?> deserializeClass;

	public OObjectCustomSerializerIterator(final Class<?> iDeserializeClass, final ORecord iSourceRecord, final Iterator<? extends Object> iIterator) {

		this.sourceRecord = iSourceRecord;
		this.underlying = iIterator;
		this.deserializeClass = iDeserializeClass;
	}

	public TYPE next() {

		final Object value = underlying.next();
		return (TYPE)OObjectEntitySerializer.deserializeFieldValue(deserializeClass, value);
	}

	public boolean hasNext() {

		return underlying.hasNext();
	}

	public void remove() {

		underlying.remove();
		if(sourceRecord != null)
			sourceRecord.setDirty();
	}
}
