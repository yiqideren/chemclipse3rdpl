/* Generated By:JJTree: Do not edit this line. OContainsCondition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.db.record.OIdentifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OContainsCondition extends OBooleanExpression {

	protected OExpression left;
	protected OExpression right;
	protected OBooleanExpression condition;

	public OContainsCondition(int id) {

		super(id);
	}

	public OContainsCondition(OrientSql p, int id) {

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
		if(right != null) {
			right.replaceParameters(params);
		}
		if(condition != null) {
			condition.replaceParameters(params);
		}
	}

	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();
		result.append(left.toString());
		result.append(" CONTAINS ");
		if(right != null) {
			result.append(right.toString());
		} else if(condition != null) {
			result.append("(");
			result.append(condition.toString());
			result.append(")");
		}
		return result.toString();
	}

	@Override
	public boolean supportsBasicCalculation() {

		if(!left.supportsBasicCalculation()) {
			return false;
		}
		if(!right.supportsBasicCalculation()) {
			return false;
		}
		if(!condition.supportsBasicCalculation()) {
			return false;
		}
		return true;
	}

	@Override
	protected int getNumberOfExternalCalculations() {

		int total = 0;
		if(condition != null) {
			total += condition.getNumberOfExternalCalculations();
		}
		if(!left.supportsBasicCalculation()) {
			total++;
		}
		if(right != null && !right.supportsBasicCalculation()) {
			total++;
		}
		return total;
	}

	@Override
	protected List<Object> getExternalCalculationConditions() {

		List<Object> result = new ArrayList<Object>();
		if(condition != null) {
			result.addAll(condition.getExternalCalculationConditions());
		}
		if(!left.supportsBasicCalculation()) {
			result.add(left);
		}
		if(right != null && !right.supportsBasicCalculation()) {
			result.add(right);
		}
		return result;
	}
}
/* JavaCC - OriginalChecksum=bad1118296ea74860e88d66bfe9fa222 (do not edit this line) */
