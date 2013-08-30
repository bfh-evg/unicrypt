/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface MultiplicativeElement extends Element {

  /**
   * @see Group#apply(Element, Element)
   */
  public MultiplicativeElement multiply(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public MultiplicativeElement divide(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public MultiplicativeElement power(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public MultiplicativeElement power(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public MultiplicativeElement power(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public MultiplicativeElement square();

  //
  // The following methods override corresponding parent methods with different return type
  //
  @Override
  public MultiplicativeElement apply(Element element);

  @Override
  public MultiplicativeElement applyInverse(Element element);

  @Override
  public MultiplicativeElement selfApply(BigInteger amount);

  @Override
  public MultiplicativeElement selfApply(Element amount);

  @Override
  public MultiplicativeElement selfApply(int amount);

  @Override
  public MultiplicativeElement selfApply();

  @Override
  public MultiplicativeElement invert();

}
