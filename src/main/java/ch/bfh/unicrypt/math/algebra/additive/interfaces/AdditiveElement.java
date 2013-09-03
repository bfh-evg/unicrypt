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
public interface AdditiveElement<S extends AdditiveSemiGroup> extends Element<S> {

  /**
   * @see Group#apply(Element, Element)
   */
  public AdditiveElement<S> add(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public AdditiveElement<S> subtract(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public AdditiveElement<S> times(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public AdditiveElement<S> times(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public AdditiveElement<S> times(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public AdditiveElement<S> timesTwo();

  //
  // The following methods override corresponding parent methods with different return type
  //
  @Override
  public AdditiveElement<S> apply(Element element);

  @Override
  public AdditiveElement<S> applyInverse(Element element);

  @Override
  public AdditiveElement<S> selfApply(BigInteger amount);

  @Override
  public AdditiveElement<S> selfApply(Element amount);

  @Override
  public AdditiveElement<S> selfApply(int amount);

  @Override
  public AdditiveElement<S> selfApply();

  @Override
  public AdditiveElement<S> invert();

}
