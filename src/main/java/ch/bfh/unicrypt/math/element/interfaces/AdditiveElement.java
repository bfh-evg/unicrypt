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
public interface AdditiveElement extends Element {

  /**
   * @see Group#apply(Element, Element)
   */
  public Element add(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public Element subtract(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public Element times(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public Element times(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public Element times(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public Element timesTwo();

}
