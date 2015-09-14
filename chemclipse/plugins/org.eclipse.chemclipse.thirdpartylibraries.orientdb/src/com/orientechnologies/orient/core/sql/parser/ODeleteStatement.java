/* Generated By:JJTree: Do not edit this line. ODeleteStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.Map;

public class ODeleteStatement extends OStatement {

	protected OFromClause fromClause;
	protected OWhereClause whereClause;
	protected boolean returnBefore = false;
	protected OLimit limit = null;
	protected boolean unsafe = false;

	public ODeleteStatement(int id) {

		super(id);
	}

	public ODeleteStatement(OrientSql p, int id) {

		super(p, id);
	}

	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();
		result.append("DELETE FROM ");
		result.append(fromClause.toString());
		if(returnBefore) {
			result.append(" RETURN BEFORE");
		}
		if(whereClause != null) {
			result.append(" WHERE ");
			result.append(whereClause.toString());
		}
		if(limit != null) {
			result.append(limit);
		}
		if(unsafe) {
			result.append(" UNSAFE");
		}
		return result.toString();
	}

	public void replaceParameters(Map<Object, Object> params) {

		fromClause.replaceParameters(params);
		if(whereClause != null) {
			whereClause.replaceParameters(params);
		}
		if(limit != null) {
			limit.replaceParameters(params);
		}
	}
}
/* JavaCC - OriginalChecksum=5fb4ca5ba648e6c9110f41d806206a6f (do not edit this line) */
