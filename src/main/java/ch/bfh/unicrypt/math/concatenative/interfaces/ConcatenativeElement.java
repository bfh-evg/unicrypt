/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.concatenative.interfaces;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.general.interfaces.Group;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface ConcatenativeElement extends Element {

  public int getLength();

  /**
   * @see Group#apply(Element, Element)
   */
  public ConcatenativeElement concatenate(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public ConcatenativeElement selfConcatenate(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public ConcatenativeElement selfConcatenate(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public ConcatenativeElement selfConcatenate(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public ConcatenativeElement selfConcatenate();

  //
  // The following methods override corresponding parent methods with different return type
  //
  @Override
  public ConcatenativeElement apply(Element element);

  @Override
  public ConcatenativeElement applyInverse(Element element);

  @Override
  public ConcatenativeElement selfApply(BigInteger amount);

  @Override
  public ConcatenativeElement selfApply(Element amount);

  @Override
  public ConcatenativeElement selfApply(int amount);

  @Override
  public ConcatenativeElement selfApply();

  @Override
  public ConcatenativeElement invert();

}
