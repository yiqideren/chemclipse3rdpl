/* Generated By:JJTree: Do not edit this line. ONumber.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class ONumber extends SimpleNode {

	public ONumber(int id) {

		super(id);
	}

	public ONumber(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	public Number getValue() {

		return null;
	}
}
/* JavaCC - OriginalChecksum=ebedbca280f59eb8ba8f21dc6132ba10 (do not edit this line) */