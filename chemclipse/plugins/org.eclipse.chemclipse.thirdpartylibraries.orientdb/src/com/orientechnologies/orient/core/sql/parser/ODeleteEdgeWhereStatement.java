/* Generated By:JJTree: Do not edit this line. ODeleteEdgeWhereStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class ODeleteEdgeWhereStatement extends ODeleteEdgeStatement {

	public ODeleteEdgeWhereStatement(int id) {

		super(id);
	}

	public ODeleteEdgeWhereStatement(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}
}
/* JavaCC - OriginalChecksum=1298a0baf9921378983d0722f8ebe68b (do not edit this line) */
