/* Generated By:JJTree: Do not edit this line. ORetry.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class ORetry extends SimpleNode {

	public ORetry(int id) {

		super(id);
	}

	public ORetry(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}
}
/* JavaCC - OriginalChecksum=2a27e5f409442f80d26204c901e017c1 (do not edit this line) */
