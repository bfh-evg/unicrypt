/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.interfaces;

import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeSemiGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface MultiplicativeElement extends Element {

  /**
   *
   * @return
   */
  public MultiplicativeSemiGroup getMultiplicativeSemiGroup();

  /**
   *
   * @return
   */
  public MultiplicativeMonoid getMultiplicativeMonoid();

  /**
   *
   * @return
   */
  public MultiplicativeGroup getMultiplicativeGroup();

  /**
   *
   * @return
   */
  public MultiplicativeCyclicGroup getMultiplicativeCyclicGroup();

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
