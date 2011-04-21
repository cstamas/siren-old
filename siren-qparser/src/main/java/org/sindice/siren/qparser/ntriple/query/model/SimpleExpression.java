/*
 * Generated by classgen, version 1.3
 * 12/04/11 12:12
 */
package org.sindice.siren.qparser.ntriple.query.model;

public class SimpleExpression extends Expression {

  private TriplePattern tp;

  public SimpleExpression (TriplePattern tp) {
    this.tp = tp;
    if (tp != null) tp.setParent(this);
  }

  public TriplePattern getTp() {
    return tp;
  }

  public void setTp(TriplePattern tp) {
    this.tp = tp;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public void childrenAccept(Visitor visitor) {
    if (tp != null) tp.accept(visitor);
  }

  public void traverseTopDown(Visitor visitor) {
    accept(visitor);
    if (tp != null) tp.traverseTopDown(visitor);
  }

  public void traverseBottomUp(Visitor visitor) {
    if (tp != null) tp.traverseBottomUp(visitor);
    accept(visitor);
  }

  public String toString(String tab) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tab);
    buffer.append("SimpleExpression(\n");
      if (tp != null)
        buffer.append(tp.toString("  "+tab));
      else
        buffer.append(tab+"  null");
    buffer.append("\n");
    buffer.append(tab);
    buffer.append(") [SimpleExpression]");
    return buffer.toString();
  }
}
