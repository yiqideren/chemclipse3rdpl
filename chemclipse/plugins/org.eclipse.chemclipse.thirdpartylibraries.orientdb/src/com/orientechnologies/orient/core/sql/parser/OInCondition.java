/* Generated By:JJTree: Do not edit this line. OInCondition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.db.record.OIdentifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OInCondition extends OBooleanExpression {

	protected OExpression left;
	protected OBinaryCompareOperator operator;
	protected OSelectStatement rightStatement;
	protected OInputParameter rightParam;
	protected OMathExpression rightMathExpression;
	protected Object right;
	private static final Object UNSET = new Object();
	private Object inputFinalValue = UNSET;

	public OInCondition(int id) {

		super(id);
	}

	public OInCondition(OrientSql p, int id) {

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
		if(rightStatement != null) {
			rightStatement.replaceParameters(params);
		}
		if(rightParam != null) {
			Object result = rightParam.bindFromInputParams(params);
			if(rightParam != result) {
				inputFinalValue = result;
			}
		}
		if(rightMathExpression != null) {
			rightMathExpression.replaceParameters(params);
		}
	}

	public String toString() {

		StringBuilder result = new StringBuilder();
		result.append(left.toString());
		result.append(" IN ");
		if(rightStatement != null) {
			result.append("(");
			result.append(rightStatement.toString());
			result.append(")");
		} else if(right != null) {
			result.append(convertToString(right));
		} else if(rightParam != null) {
			if(inputFinalValue == UNSET) {
				result.append(rightParam.toString());
			} else if(inputFinalValue == null) {
				result.append("NULL");
			} else {
				result.append(inputFinalValue.toString());
			}
		} else if(rightMathExpression != null) {
			result.append(rightMathExpression.toString());
		}
		return result.toString();
	}

	private String convertToString(Object o) {

		if(o instanceof String) {
			return "\"" + ((String)o).replaceAll("\"", "\\\"") + "\"";
		}
		return o.toString();
	}

	@Override
	public boolean supportsBasicCalculation() {

		if(!left.supportsBasicCalculation()) {
			return false;
		}
		if(!rightMathExpression.supportsBasicCalculation()) {
			return false;
		}
		if(!operator.supportsBasicCalculation()) {
			return false;
		}
		return true;
	}

	@Override
	protected int getNumberOfExternalCalculations() {

		int total = 0;
		if(operator != null && !operator.supportsBasicCalculation()) {
			total++;
		}
		if(!left.supportsBasicCalculation()) {
			total++;
		}
		if(rightMathExpression != null && !rightMathExpression.supportsBasicCalculation()) {
			total++;
		}
		return total;
	}

	@Override
	protected List<Object> getExternalCalculationConditions() {

		List<Object> result = new ArrayList<Object>();
		if(operator != null) {
			result.add(this);
		}
		if(!left.supportsBasicCalculation()) {
			result.add(left);
		}
		if(rightMathExpression != null && !rightMathExpression.supportsBasicCalculation()) {
			result.add(rightMathExpression);
		}
		return result;
	}
}
/* JavaCC - OriginalChecksum=00df7cb1877c0a12d24205c1700653c7 (do not edit this line) */
