/* Generated By:JJTree: Do not edit this line. OArraySelector.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.Map;

public class OArraySelector extends SimpleNode {

	private static final Object UNSET = new Object();
	private Object inputFinalValue = UNSET;
	protected ORid rid;
	protected OInputParameter inputParam;
	protected OExpression expression;
	protected OInteger integer;

	public OArraySelector(int id) {

		super(id);
	}

	public OArraySelector(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	@Override
	public String toString() {

		if(rid != null) {
			return rid.toString();
		} else if(inputParam != null) {
			if(inputFinalValue == UNSET) {
				return inputParam.toString();
			} else if(inputFinalValue == null) {
				return "NULL";
			} else {
				return inputFinalValue.toString();
			}
		} else if(expression != null) {
			return expression.toString();
		} else if(integer != null) {
			return integer.toString();
		}
		return null;
	}

	public void replaceParameters(Map<Object, Object> params) {

		if(inputParam != null) {
			Object result = inputParam.bindFromInputParams(params);
			if(result != inputParam) {
				inputFinalValue = result;
			}
		}
		if(expression != null) {
			expression.replaceParameters(params);
		}
	}
}
/* JavaCC - OriginalChecksum=f87a5543b1dad0fb5f6828a0663a7c9e (do not edit this line) */
