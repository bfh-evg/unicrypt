/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

/**
 *
 * @author rolfhaenni
 */
public interface MultiplicativeElement<S extends MultiplicativeSemiGroup> extends Element<S> {

  /**
   * @see Group#apply(Element, Element)
   */
  public MultiplicativeElement<S> multiply(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public MultiplicativeElement<S> divide(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public MultiplicativeElement<S> power(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public MultiplicativeElement<S> power(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public MultiplicativeElement<S> power(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public MultiplicativeElement<S> square();

  //
  // The following methods override corresponding parent methods with different return type
  //
  @Override
  public MultiplicativeElement<S> apply(Element element);

  @Override
  public MultiplicativeElement<S> applyInverse(Element element);

  @Override
  public MultiplicativeElement<S> selfApply(BigInteger amount);

  @Override
  public MultiplicativeElement<S> selfApply(Element amount);

  @Override
  public MultiplicativeElement<S> selfApply(int amount);

  @Override
  public MultiplicativeElement<S> selfApply();

  @Override
  public MultiplicativeElement<S> invert();

}
