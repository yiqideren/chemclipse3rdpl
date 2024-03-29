/* Generated By:JJTree: Do not edit this line. OArraySingleValuesSelector.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OArraySingleValuesSelector extends SimpleNode {

	protected List<OArraySelector> items = new ArrayList<OArraySelector>();

	public OArraySingleValuesSelector(int id) {

		super(id);
	}

	public OArraySingleValuesSelector(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	@Override
	public String toString() {

		boolean first = true;
		StringBuilder result = new StringBuilder();
		for(OArraySelector item : items) {
			if(!first) {
				result.append(",");
			}
			result.append(item.toString());
			first = false;
		}
		return result.toString();
	}

	public void replaceParameters(Map<Object, Object> params) {

		if(items != null) {
			for(OArraySelector item : items) {
				item.replaceParameters(params);
			}
		}
	}
}
/* JavaCC - OriginalChecksum=991998c77a4831184b6dca572513fd8d (do not edit this line) */
