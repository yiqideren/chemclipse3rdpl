/* Generated By:JJTree: Do not edit this line. OInsertBody.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.List;
import java.util.Map;

public class OInsertBody extends SimpleNode {

	protected List<OIdentifier> identifierList;
	protected List<List<OExpression>> valueExpressions;
	protected List<OInsertSetExpression> setExpressions;
	protected OSelectStatement selectStatement;
	protected boolean selectInParentheses;
	protected OJson content;
	protected OProjection returnProjection;

	public OInsertBody(int id) {

		super(id);
	}

	public OInsertBody(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	public void replaceParameters(Map<Object, Object> params) {

		if(valueExpressions != null) {
			for(List<OExpression> exprList : valueExpressions) {
				for(OExpression expr : exprList) {
					expr.replaceParameters(params);
				}
			}
		}
		if(setExpressions != null) {
			for(OInsertSetExpression expr : setExpressions) {
				expr.replaceParameters(params);
			}
		}
		if(selectStatement != null) {
			selectStatement.replaceParameters(params);
		}
		if(content != null) {
			content.replaceParameters(params);
		}
		if(returnProjection != null) {
			returnProjection.replaceParameters(params);
		}
	}

	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();
		if(identifierList != null) {
			result.append("(");
			boolean first = true;
			for(OIdentifier item : identifierList) {
				if(!first) {
					result.append(", ");
				}
				result.append(item.toString());
				first = false;
			}
			result.append(") VALUES ");
			if(valueExpressions != null) {
				boolean firstList = true;
				for(List<OExpression> itemList : valueExpressions) {
					if(firstList) {
						result.append("(");
					} else {
						result.append("),(");
					}
					first = true;
					for(OExpression item : itemList) {
						if(!first) {
							result.append(", ");
						}
						result.append(item.toString());
						first = false;
					}
					firstList = false;
				}
			}
			result.append(")");
		}
		if(setExpressions != null) {
			result.append("SET ");
			boolean first = true;
			for(OInsertSetExpression item : setExpressions) {
				if(!first) {
					result.append(", ");
				}
				result.append(item.toString());
				first = false;
			}
		}
		if(selectStatement != null) {
			result.append("FROM ");
			if(selectInParentheses) {
				result.append("( ");
			}
			result.append(selectStatement.toString());
			if(selectInParentheses) {
				result.append(")");
			}
		}
		if(content != null) {
			result.append("CONTENT ");
			result.append(content.toString());
		}
		if(returnProjection != null) {
			result.append(" RETURN ");
			result.append(returnProjection.toString());
		}
		return result.toString();
	}
}
/* JavaCC - OriginalChecksum=7d2079a41a1fc63a812cb679e729b23a (do not edit this line) */
