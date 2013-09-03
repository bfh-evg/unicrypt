/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

/**
 *
 * @author rolfhaenni
 */
public interface AdditiveElement extends Element {

  /**
   * @see Group#apply(Element, Element)
   */
  public AdditiveElement add(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public AdditiveElement subtract(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public AdditiveElement times(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public AdditiveElement times(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public AdditiveElement times(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public AdditiveElement timesTwo();

  //
  // The following methods override corresponding parent methods with different return type
  //
  @Override
  public AdditiveElement apply(Element element);

  @Override
  public AdditiveElement applyInverse(Element element);

  @Override
  public AdditiveElement selfApply(BigInteger amount);

  @Override
  public AdditiveElement selfApply(Element amount);

  @Override
  public AdditiveElement selfApply(int amount);

  @Override
  public AdditiveElement selfApply();

  @Override
  public AdditiveElement invert();

}
