/*
 * Generated by classgen, version 1.3
 * 12/04/11 12:12
 */
package org.sindice.siren.qparser.ntriple.query.model;

public class ClauseQuery extends NTripleQuery {

  private Clause c;

  public ClauseQuery (Clause c) {
    this.c = c;
    if (c != null) c.setParent(this);
  }

  public Clause getC() {
    return c;
  }

  public void setC(Clause c) {
    this.c = c;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public void childrenAccept(Visitor visitor) {
    if (c != null) c.accept(visitor);
  }

  public void traverseTopDown(Visitor visitor) {
    accept(visitor);
    if (c != null) c.traverseTopDown(visitor);
  }

  public void traverseBottomUp(Visitor visitor) {
    if (c != null) c.traverseBottomUp(visitor);
    accept(visitor);
  }

  public String toString(String tab) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tab);
    buffer.append("ClauseQuery(\n");
      if (c != null)
        buffer.append(c.toString("  "+tab));
      else
        buffer.append(tab+"  null");
    buffer.append("\n");
    buffer.append(tab);
    buffer.append(") [ClauseQuery]");
    return buffer.toString();
  }
}
