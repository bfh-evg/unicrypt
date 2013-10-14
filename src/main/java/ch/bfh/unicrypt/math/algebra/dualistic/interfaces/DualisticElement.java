/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface DualisticElement extends AdditiveElement, MultiplicativeElement {

  // The following methods are overridden from Element with an adapted return type
  @Override
  public DualisticElement apply(Element element);

  @Override
  public DualisticElement applyInverse(Element element);

  @Override
  public DualisticElement selfApply(BigInteger amount);

  @Override
  public DualisticElement selfApply(Element amount);

  @Override
  public DualisticElement selfApply(int amount);

  @Override
  public DualisticElement selfApply();

  @Override
  public DualisticElement invert();

  // The following methods are overridden from AdditiveElement with an adapted return type
  @Override
  public DualisticElement add(Element element);

  @Override
  public DualisticElement subtract(Element element);

  @Override
  public DualisticElement times(BigInteger amount);

  @Override
  public DualisticElement times(Element amount);

  @Override
  public DualisticElement times(int amount);

  @Override
  public DualisticElement timesTwo();

  @Override
  public DualisticElement minus();

  // The following methods are overridden from MultiplicativeElement with an adapted return type
  @Override
  public MultiplicativeElement multiply(Element element);

  @Override
  public MultiplicativeElement divide(Element element);

  @Override
  public MultiplicativeElement power(BigInteger amount);

  @Override
  public MultiplicativeElement power(Element amount);

  @Override
  public MultiplicativeElement power(int amount);

  @Override
  public MultiplicativeElement square();

  @Override
  public MultiplicativeElement oneOver();

}
