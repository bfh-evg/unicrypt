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

  /**
   * Creates a new product set which contains one set less than the given
   * product set.
   *
   * @param index The index of the set to remove
   * @return The resulting product set.
   * @throws IndexOutOfBoundsException if
   * {@code index<0} or {@code index>arity-1}
   */
  public Tuple removeAt(int index);
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
