/*
 * Generated by classgen, version 1.3
 * 12/04/11 12:12
 */
package org.sindice.siren.qparser.entity.query.model;

public class KClause implements SyntaxNode {

  private SyntaxNode parent;
  private String term;
  private int op;

  public KClause (String term, int op) {
    this.term = term;
    this.op = op;
  }

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public int getOp() {
    return op;
  }

  public void setOp(int op) {
    this.op = op;
  }

  public SyntaxNode getParent() {
    return parent;
  }

  public void setParent(SyntaxNode parent) {
    this.parent = parent;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public void childrenAccept(Visitor visitor) {
  }

  public void traverseTopDown(Visitor visitor) {
    accept(visitor);
  }

  public void traverseBottomUp(Visitor visitor) {
    accept(visitor);
  }

  public String toString() {
    return toString("");
  }

  public String toString(String tab) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tab);
    buffer.append("KClause(\n");
    buffer.append("  "+tab+term);
    buffer.append("\n");
    buffer.append("  "+tab+op);
    buffer.append("\n");
    buffer.append(tab);
    buffer.append(") [KClause]");
    return buffer.toString();
  }
}
