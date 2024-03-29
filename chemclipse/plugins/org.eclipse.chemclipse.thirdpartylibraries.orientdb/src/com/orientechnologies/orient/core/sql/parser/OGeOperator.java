/* Generated By:JJTree: Do not edit this line. OGeOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class OGeOperator extends SimpleNode implements OBinaryCompareOperator {

	public OGeOperator(int id) {

		super(id);
	}

	public OGeOperator(OrientSql p, int id) {

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

		return ">=";
	}

	@Override
	public boolean supportsBasicCalculation() {

		return true;
	}
}
/* JavaCC - OriginalChecksum=960da239569d393eb155f7d8a871e6d5 (do not edit this line) */
