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
package com.orientechnologies.orient.core.sql.filter;

import com.orientechnologies.common.parser.OBaseParser;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;

/**
 * Represents a context variable as value in the query condition.
 * 
 * @author Luca Garulli
 * 
 */
public class OSQLFilterItemVariable extends OSQLFilterItemAbstract {

	protected String name;

	public OSQLFilterItemVariable(final OBaseParser iQueryToParse, final String iName) {

		super(iQueryToParse, iName.substring(1));
	}

	public Object getValue(final OIdentifiable iRecord, Object iCurrentResult, final OCommandContext iContext) {

		if(iContext == null)
			return null;
		return transformValue(iRecord, iContext, iContext.getVariable(name));
	}

	public String getRoot() {

		return name;
	}

	public void setRoot(final OBaseParser iQueryToParse, final String iRoot) {

		this.name = iRoot;
	}

	@Override
	public String toString() {

		return "$" + super.toString();
	}
}
