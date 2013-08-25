/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.interfaces;

import ch.bfh.unicrypt.math.group.interfaces.AdditiveCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface AdditiveElement extends Element {

  /**
   *
   * @return
   */
  public AdditiveSemiGroup getAdditiveSemiGroup();

  /**
   *
   * @return
   */
  public AdditiveMonoid getAdditiveMonoid();

  /**
   *
   * @return
   */
  public AdditiveGroup getAdditiveGroup();

  /**
   *
   * @return
   */
  public AdditiveCyclicGroup getAdditiveCyclicGroup();

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

}
