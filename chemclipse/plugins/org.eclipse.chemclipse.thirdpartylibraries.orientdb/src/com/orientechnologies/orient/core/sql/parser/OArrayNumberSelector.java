/* Generated By:JJTree: Do not edit this line. OArrayNumberSelector.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.Map;

public class OArrayNumberSelector extends SimpleNode {

	private static final Object UNSET = new Object();
	private Object inputFinalValue = UNSET;
	OInputParameter inputValue;
	OMathExpression expressionValue;
	Integer integer;

	public OArrayNumberSelector(int id) {

		super(id);
	}

	public OArrayNumberSelector(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	@Override
	public String toString() {

		if(inputValue != null) {
			if(inputFinalValue == UNSET) {
				return inputValue.toString();
			} else if(inputFinalValue == null) {
				return "NULL";
			} else {
				return inputFinalValue.toString();
			}
		} else if(expressionValue != null) {
			return expressionValue.toString();
		} else if(integer != null) {
			return integer.toString();
		}
		return super.toString();
	}

	public void replaceParameters(Map<Object, Object> params) {

		if(inputValue != null) {
			Object result = inputValue.bindFromInputParams(params);
			if(result != inputValue) {
				inputFinalValue = result;
			}
		}
		if(expressionValue != null) {
			expressionValue.replaceParameters(params);
		}
	}
}
/* JavaCC - OriginalChecksum=5b2e495391ede3ccdc6c25aa63c8e591 (do not edit this line) */
