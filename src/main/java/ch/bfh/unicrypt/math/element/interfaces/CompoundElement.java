/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.interfaces;

import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ProductMonoid;
import ch.bfh.unicrypt.math.group.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.helper.Compound;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundElement extends Element, Compound<Element> {

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
  public CompoundElement apply(Element element);

  @Override
  public CompoundElement applyInverse(Element element);

  @Override
  public CompoundElement selfApply(BigInteger amount);

  @Override
  public CompoundElement selfApply(Element amount);

  @Override
  public CompoundElement selfApply(int amount);

  @Override
  public CompoundElement selfApply();

  @Override
  public CompoundElement invert();

}
