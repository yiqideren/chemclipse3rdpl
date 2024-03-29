/* Generated By:JJTree: Do not edit this line. OContainsValueCondition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.db.record.OIdentifiable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OContainsValueCondition extends OBooleanExpression {

	protected OExpression left;
	protected OContainsValueOperator operator;
	protected OOrBlock condition;
	protected OExpression expression;

	public OContainsValueCondition(int id) {

		super(id);
	}

	public OContainsValueCondition(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	@Override
	public boolean evaluate(OIdentifiable currentRecord) {

		return false;
	}

	@Override
	public void replaceParameters(Map<Object, Object> params) {

		left.replaceParameters(params);
		if(condition != null) {
			condition.replaceParameters(params);
		}
		if(expression != null) {
			expression.replaceParameters(params);
		}
	}

	public String toString() {

		StringBuilder result = new StringBuilder();
		result.append(left.toString());
		result.append(" CONTAINSVALUE ");
		if(condition != null) {
			result.append("(");
			result.append(condition.toString());
			result.append(")");
		} else {
			result.append(expression.toString());
		}
		return result.toString();
	}

	@Override
	public boolean supportsBasicCalculation() {

		return true;
	}

	@Override
	protected int getNumberOfExternalCalculations() {

		if(condition == null) {
			return 0;
		}
		return condition.getNumberOfExternalCalculations();
	}

	@Override
	protected List<Object> getExternalCalculationConditions() {

		if(condition == null) {
			return Collections.EMPTY_LIST;
		}
		return condition.getExternalCalculationConditions();
	}
}
/* JavaCC - OriginalChecksum=6fda752f10c8d8731f43efa706e39459 (do not edit this line) */
