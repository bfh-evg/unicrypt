/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.interfaces;

import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.monoid.classes.ProductMonoid;
import ch.bfh.unicrypt.math.semigroup.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.set.classes.ProductSet;
import ch.bfh.unicrypt.math.utility.Compound;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface Tuple extends Element, Compound<Element> {

  /**
   *
   * @return
   */
  public ProductSet getProductSet();

  /**
   *
   * @return
   */
  public ProductSemiGroup getProductSemiGroup();
  /**
   *
   * @return
   */
  public ProductMonoid getProductMonoid();

  /**
   *
   * @return
   */
  public ProductGroup getProductGroup();

  //
  // The following methods override corresponding parent methods with different return type
  //

  @Override
  public Tuple apply(Element element);

  @Override
  public Tuple applyInverse(Element element);

  @Override
  public Tuple selfApply(BigInteger amount);

  @Override
  public Tuple selfApply(Element amount);

  @Override
  public Tuple selfApply(int amount);

  @Override
  public Tuple selfApply();

  @Override
  public Tuple invert();

}
