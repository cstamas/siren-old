/*
 * Generated by classgen, version 1.3
 * 12/04/11 12:12
 */
package org.sindice.siren.qparser.ntriple.query.model;

public class NestedClause extends Clause {

  private Clause lhc;
  private int op;
  private Expression rhe;

  public NestedClause (Clause lhc, int op, Expression rhe) {
    this.lhc = lhc;
    if (lhc != null) lhc.setParent(this);
    this.op = op;
    this.rhe = rhe;
    if (rhe != null) rhe.setParent(this);
  }

  public Clause getLhc() {
    return lhc;
  }

  public void setLhc(Clause lhc) {
    this.lhc = lhc;
  }

  public int getOp() {
    return op;
  }

  public void setOp(int op) {
    this.op = op;
  }

  public Expression getRhe() {
    return rhe;
  }

  public void setRhe(Expression rhe) {
    this.rhe = rhe;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public void childrenAccept(Visitor visitor) {
    if (lhc != null) lhc.accept(visitor);
    if (rhe != null) rhe.accept(visitor);
  }

  public void traverseTopDown(Visitor visitor) {
    accept(visitor);
    if (lhc != null) lhc.traverseTopDown(visitor);
    if (rhe != null) rhe.traverseTopDown(visitor);
  }

  public void traverseBottomUp(Visitor visitor) {
    if (lhc != null) lhc.traverseBottomUp(visitor);
    if (rhe != null) rhe.traverseBottomUp(visitor);
    accept(visitor);
  }

  public String toString(String tab) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tab);
    buffer.append("NestedClause(\n");
      if (lhc != null)
        buffer.append(lhc.toString("  "+tab));
      else
        buffer.append(tab+"  null");
    buffer.append("\n");
    buffer.append("  "+tab+op);
    buffer.append("\n");
      if (rhe != null)
        buffer.append(rhe.toString("  "+tab));
      else
        buffer.append(tab+"  null");
    buffer.append("\n");
    buffer.append(tab);
    buffer.append(") [NestedClause]");
    return buffer.toString();
  }
}
