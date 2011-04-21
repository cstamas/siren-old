/*
 * Generated by classgen, version 1.3
 * 30/09/10 18:45
 */
package org.sindice.siren.qparser.ntriple.query.model;

public class URI extends Resource {

  private String v;

  public URI (String v) {
    this.v = v;
  }

  public String getV() {
    return v;
  }

  public void setV(String v) {
    this.v = v;
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

  public String toString(String tab) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(tab);
    buffer.append("URI(\n");
    buffer.append("  "+tab+v);
    buffer.append("\n");
    buffer.append(tab);
    buffer.append(") [URI]");
    return buffer.toString();
  }
}
