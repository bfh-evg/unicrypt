/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.concatenative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface ConcatenativeElement<S extends ConcatenativeSemiGroup> extends Element<S> {

  public int getLength();

  /**
   * @see Group#apply(Element, Element)
   */
  public ConcatenativeElement<S> concatenate(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public ConcatenativeElement<S> selfConcatenate(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public ConcatenativeElement<S> selfConcatenate(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public ConcatenativeElement<S> selfConcatenate(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public ConcatenativeElement<S> selfConcatenate();

  //
  // The following methods override corresponding parent methods with different return type
  //
  @Override
  public ConcatenativeElement<S> apply(Element element);

  @Override
  public ConcatenativeElement<S> applyInverse(Element element);

  @Override
  public ConcatenativeElement<S> selfApply(BigInteger amount);

  @Override
  public ConcatenativeElement<S> selfApply(Element amount);

  @Override
  public ConcatenativeElement<S> selfApply(int amount);

  @Override
  public ConcatenativeElement<S> selfApply();

  @Override
  public ConcatenativeElement<S> invert();

}
