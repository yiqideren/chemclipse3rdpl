/* Generated By:JJTree: Do not edit this line. OOrderBy.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import java.util.List;

public class OOrderBy extends SimpleNode {

	protected List<OOrderByItem> items;

	public OOrderBy() {

		super(-1);
	}

	public OOrderBy(int id) {

		super(id);
	}

	public OOrderBy(OrientSql p, int id) {

		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(OrientSqlVisitor visitor, Object data) {

		return visitor.visit(this, data);
	}

	public List<OOrderByItem> getItems() {

		return items;
	}

	public void setItems(List<OOrderByItem> items) {

		this.items = items;
	}

	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();
		if(items != null && items.size() > 0) {
			result.append("ORDER BY ");
			for(int i = 0; i < items.size(); i++) {
				if(i > 0) {
					result.append(", ");
				}
				result.append(items.get(i).toString());
			}
		}
		return result.toString();
	}
}
/* JavaCC - OriginalChecksum=d5529400217169f15e556e5dc6fe4f5b (do not edit this line) */
