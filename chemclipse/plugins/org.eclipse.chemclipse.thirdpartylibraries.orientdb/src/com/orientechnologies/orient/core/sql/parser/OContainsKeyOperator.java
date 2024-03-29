/* Generated By:JJTree: Do not edit this line. OContainsKeyOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class OContainsKeyOperator extends SimpleNode implements OBinaryCompareOperator {

	public OContainsKeyOperator(int id) {

		super(id);
	}

	public OContainsKeyOperator(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	@Override
	public boolean execute(Object left, Object right) {

		return false;
	}

	@Override
	public String toString() {

		return "CONTAINSKEY";
	}

	@Override
	public boolean supportsBasicCalculation() {

		return true;
	}
}
/* JavaCC - OriginalChecksum=1a03daaa6712eb981b070e8e94960951 (do not edit this line) */
