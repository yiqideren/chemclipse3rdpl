/* Generated By:JJTree: Do not edit this line. OInteger.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

public class OInteger extends ONumber {

	protected Number value;

	public OInteger(int id) {

		super(id);
	}

	public OInteger(OrientSql p, int id) {

		super(p, id);
	}

	public Number getValue() {

		return value;
	}

	public void setValue(Number value) {

		this.value = value;
	}

	@Override
	public String toString() {

		return "" + value;
	}
}
/* JavaCC - OriginalChecksum=2e6eee6366ff4e864dd6c8184d2766f5 (do not edit this line) */
