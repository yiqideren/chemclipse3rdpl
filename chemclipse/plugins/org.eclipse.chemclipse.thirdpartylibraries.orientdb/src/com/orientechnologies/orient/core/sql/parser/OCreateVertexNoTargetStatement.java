/* Generated By:JJTree: Do not edit this line. OCreateVertexNoTargetStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class OCreateVertexNoTargetStatement extends OCreateVertexStatement {

	public OCreateVertexNoTargetStatement(int id) {

		super(id);
	}

	public OCreateVertexNoTargetStatement(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}
}
/* JavaCC - OriginalChecksum=25d9cdfd149e7b374a84dfd166e11306 (do not edit this line) */
