/* Generated By:JJTree: Do not edit this line. OQueryStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class OQueryStatement extends SimpleNode {

	public OQueryStatement(int id) {

		super(id);
	}

	public OQueryStatement(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}
}
/* JavaCC - OriginalChecksum=f78d23e607a64459efb18502e47359c1 (do not edit this line) */
