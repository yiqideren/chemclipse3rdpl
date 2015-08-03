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
package com.orientechnologies.orient.core.sql;

import com.orientechnologies.orient.core.db.record.OIdentifiable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * SQL UPDATE command.
 * 
 * @author Luca Garulli
 * 
 */
@SuppressWarnings("unchecked")
public class OCommandExecutorSQLResultsetDelegate extends OCommandExecutorSQLDelegate implements OIterableRecordSource, Iterable<OIdentifiable> {

	@Override
	public Set<String> getInvolvedClusters() {

		return ((OCommandExecutorSQLResultsetAbstract)delegate).getInvolvedClusters();
	}

	@Override
	public Iterator<OIdentifiable> iterator() {

		return ((OCommandExecutorSQLResultsetAbstract)delegate).iterator();
	}

	@Override
	public Iterator<OIdentifiable> iterator(final Map<Object, Object> iArgs) {

		return ((OCommandExecutorSQLResultsetAbstract)delegate).iterator(iArgs);
	}
}
