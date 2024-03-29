/* Generated By:JJTree: Do not edit this line. OTimeout.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class OTimeout extends SimpleNode {

	public static final String RETURN = "RETURN";
	public static final String EXCEPTION = "EXCEPTION";
	protected Number val;
	protected String failureStrategy;

	public OTimeout(int id) {

		super(id);
	}

	public OTimeout(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder(" TIMEOUT " + val);
		if(failureStrategy != null) {
			builder.append(" ");
			builder.append(failureStrategy);
		}
		return builder.toString();
	}
}
/* JavaCC - OriginalChecksum=fef7f5d488f7fca1b6ad0b70c6841931 (do not edit this line) */
