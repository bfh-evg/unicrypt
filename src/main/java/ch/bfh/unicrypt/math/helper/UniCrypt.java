/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper;

/**
 *
 * @author rolfhaenni
 */
public abstract class UniCrypt {

  @Override
  public String toString() {
    String str = this.standardToStringContent();
    if (str.length() == 0) {
      return this.standardToStringName();
    }
    return this.standardToStringName() + "[" + str + "]";
  }

  protected String standardToStringName() {
    return this.getClass().getSimpleName();
  }

  protected String standardToStringContent() {
    return "";
  }

}
