/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.interfaces;

import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface MultiplicativeElement extends Element {


  /**
   * @see Group#apply(Element, Element)
   */
  public Element multiply(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public Element divide(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public Element power(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public Element power(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public Element power(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public Element square();

}
