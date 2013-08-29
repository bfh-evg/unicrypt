/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.product.interfaces;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.product.classes.ProductGroup;
import ch.bfh.unicrypt.math.product.classes.ProductMonoid;
import ch.bfh.unicrypt.math.product.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.product.classes.ProductSet;
import ch.bfh.unicrypt.math.utility.Compound;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface Tuple extends Element, Compound<Element> {

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
