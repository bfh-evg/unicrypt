/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.product.abstracts;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.product.abstracts.AbstractProductSet;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractProductSemiGroup<S extends SemiGroup, E extends Tuple> extends AbstractProductSet<S, E> implements SemiGroup {

  protected AbstractProductSemiGroup(final SemiGroup[] semiGroups) {
    super(semiGroups);
  }

  protected AbstractProductSemiGroup(final SemiGroup semiGroup, final int arity) {
    super(semiGroup, arity);
  }

  protected AbstractProductSemiGroup() {
    super();
  }

  @Override
  public final Tuple apply(Element element1, Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    Tuple compoundElement1 = (Tuple) element1;
    Tuple compoundElement2 = (Tuple) element2;
    final Element[] results = new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = compoundElement1.getAt(i).apply(compoundElement2.getAt(i));
    }
    return this.abstractGetElement(results);
  }

  @Override
  public final Tuple apply(Element... elements) {
    if (elements == null || elements.length == 0) {
      throw new IllegalArgumentException();
    }
    Tuple[] compoundElements = (Tuple[]) elements;
    Tuple result = null;
    for (Tuple element : compoundElements) {
      if (result == null) {
        result = element;
      } else {
        result = this.apply(result, element);
      }
    }
    return result;
  }

  @Override
  public final Tuple selfApply(Element element, BigInteger amount) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    Tuple compoundElement = (Tuple) element;
    final Element[] results = new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = compoundElement.getAt(i).selfApply(amount);
    }
    return abstractGetElement(results);
  }

  @Override
  public final Tuple selfApply(Element element, Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.selfApply(element, amount.getValue());
  }

  @Override
  public final Tuple selfApply(Element element, int amount) {
    return this.selfApply(element, BigInteger.valueOf(amount));
  }

  @Override
  public final Tuple selfApply(Element element) {
    return this.apply(element, element);
  }

  @Override
  public final Tuple multiSelfApply(Element[] elements, BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length) || (elements.length == 0)) {
      throw new IllegalArgumentException();
    }
    Tuple[] results = new Tuple[elements.length];
    for (int i = 0; i < elements.length; i++) {
      results[i] = this.selfApply(elements[i], amounts[i]);
    }
    return this.apply(results);
  }

}
