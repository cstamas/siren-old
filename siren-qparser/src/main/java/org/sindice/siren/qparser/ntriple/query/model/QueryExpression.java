/*
 * Generated by classgen, version 1.3
 * 12/04/11 12:12
 */
package org.sindice.siren.qparser.ntriple.query.model;

public class QueryExpression extends Expression {

  private NTripleQuery q;

  public QueryExpression (NTripleQuery q) {
    this.q = q;
    if (q != null) q.setParent(this);
  }

  public NTripleQuery getQ() {
    return q;
  }

  public void setQ(NTripleQuery q) {
    this.q = q;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public void childrenAccept(Visitor visitor) {
    if (q != null) q.accept(visitor);
  }

  public void traverseTopDown(Visitor visitor) {
    accept(visitor);
    if (q != null) q.traverseTopDown(visitor);
  }

  public void traverseBottomUp(Visitor visitor) {
    if (q != null) q.traverseBottomUp(visitor);
    accept(visitor);
  }

  public String toString(String tab) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tab);
    buffer.append("QueryExpression(\n");
      if (q != null)
        buffer.append(q.toString("  "+tab));
      else
        buffer.append(tab+"  null");
    buffer.append("\n");
    buffer.append(tab);
    buffer.append(") [QueryExpression]");
    return buffer.toString();
  }
}