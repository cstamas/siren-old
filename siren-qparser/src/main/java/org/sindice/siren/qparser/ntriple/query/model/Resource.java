/*
 * Generated by classgen, version 1.3
 * 12/04/11 12:12
 */
package org.sindice.siren.qparser.ntriple.query.model;

public abstract class Resource extends Value implements SyntaxNode {

  private SyntaxNode parent;

  public String getV() {
    throw new ClassCastException("tried to call abstract method");
  }

  public void setV(String v) {
    throw new ClassCastException("tried to call abstract method");
  }

  public SyntaxNode getParent() {
    return parent;
  }

  public void setParent(SyntaxNode parent) {
    this.parent = parent;
  }

  public abstract void accept(Visitor visitor);
  public abstract void childrenAccept(Visitor visitor);
  public abstract void traverseTopDown(Visitor visitor);
  public abstract void traverseBottomUp(Visitor visitor);
  public String toString() {
    return toString("");
  }

  public abstract String toString(String tab);
}