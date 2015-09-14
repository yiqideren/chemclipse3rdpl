/* Generated By:JJTree: Do not edit this line. OUpdateStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OUpdateStatement extends OStatement {

	protected ORid targetRid;
	protected OIdentifier targetClass;
	protected OCluster targetCluster;
	protected OIndexIdentifier targetIndex;
	protected OStatement targetQuery;
	protected List<OUpdateOperations> operations = new ArrayList<OUpdateOperations>();
	protected boolean upsert = false;
	protected boolean returnBefore = false;
	protected boolean returnAfter = false;
	protected OProjection returnProjection;
	protected OWhereClause whereClause;
	protected boolean lockRecord = false;
	protected OLimit limit;
	protected OTimeout timeout;

	public OUpdateStatement(int id) {

		super(id);
	}

	public OUpdateStatement(OrientSql p, int id) {

		super(p, id);
	}

	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();
		result.append("UPDATE ");
		if(targetRid != null) {
			result.append(targetRid.toString());
		} else if(targetClass != null) {
			result.append(targetClass.toString());
		} else if(targetCluster != null) {
			result.append(targetCluster.toString());
		} else if(targetIndex != null) {
			result.append(targetIndex.toString());
		} else if(targetQuery != null) {
			result.append("(");
			result.append(targetQuery.toString());
			result.append(")");
		}
		for(OUpdateOperations ops : this.operations) {
			result.append(" ");
			result.append(ops.toString());
		}
		if(upsert) {
			result.append(" UPSERT");
		}
		if(returnBefore || returnAfter) {
			result.append(" RETURN");
			if(returnBefore) {
				result.append(" BEFORE");
			} else {
				result.append(" AFTER");
			}
			if(returnProjection != null) {
				result.append(" ");
				result.append(returnProjection.toString());
			}
		}
		if(whereClause != null) {
			result.append(" WHERE ");
			result.append(whereClause.toString());
		}
		if(lockRecord) {
			result.append(" LOCK RECORD");
		}
		if(limit != null) {
			result.append(limit);
		}
		if(timeout != null) {
			result.append(timeout);
		}
		return result.toString();
	}

	public void replaceParameters(Map<Object, Object> params) {

		if(this.targetQuery != null) {
			targetQuery.replaceParameters(params);
		}
		for(OUpdateOperations ops : operations) {
			ops.replaceParameters(params);
		}
		if(returnProjection != null) {
			returnProjection.replaceParameters(params);
		}
		if(whereClause != null) {
			whereClause.replaceParameters(params);
		}
		if(limit != null) {
			limit.replaceParameters(params);
		}
	}
}
/* JavaCC - OriginalChecksum=093091d7273f1073ad49f2a2bf709a53 (do not edit this line) */
