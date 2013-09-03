/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCompoundSemiGroup<CS extends AbstractCompoundSemiGroup<CS, CE, S, E>, CE extends AbstractCompoundElement<CS, CE, S, E>, S extends SemiGroup, E extends Element>
        extends AbstractCompoundSet<CS, CE, S, E> implements SemiGroup {

  protected AbstractCompoundSemiGroup(final S[] semiGroups) {
    super(semiGroups);
  }

  protected AbstractCompoundSemiGroup(final S semiGroup, final int arity) {
    super(semiGroup, arity);
  }

  @Override
  public final CE apply(Element element1, Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    CE tuple1 = (CE) element1;
    CE tuple2 = (CE) element2;
    final E[] results = (E[]) new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = (E) tuple1.getAt(i).apply(tuple2.getAt(i));
    }
    return this.abstractGetElement(results);
  }

  @Override
  public final CE apply(Element... elements) {
    if (elements == null || elements.length == 0) {
      throw new IllegalArgumentException();
    }
    CE[] tuples = (CE[]) elements;
    CE result = null;
    for (CE tuple : tuples) {
      if (result == null) {
        result = tuple;
      } else {
        result = this.apply(result, tuple);
      }
    }
    return result;
  }

  @Override
  public final CE selfApply(Element element, BigInteger amount) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    CE tuple = (CE) element;
    final E[] results = (E[]) new Element[arity];
    for (int i = 0; i < arity; i++) {
      results[i] = (E) tuple.getAt(i).selfApply(amount);
    }
    return abstractGetElement(results);
  }

  @Override
  public final CE selfApply(Element element, Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.selfApply(element, amount.getValue());
  }

  @Override
  public final CE selfApply(Element element, int amount) {
    return this.selfApply(element, BigInteger.valueOf(amount));
  }

  @Override
  public final CE selfApply(Element element) {
    return this.apply(element, element);
  }

  @Override
  public final CE multiSelfApply(Element[] elements, BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length) || (elements.length == 0)) {
      throw new IllegalArgumentException();
    }
    CE[] results = (CE[]) new Element[elements.length];
    for (int i = 0; i < elements.length; i++) {
      results[i] = this.selfApply(elements[i], amounts[i]);
    }
    return this.apply(results);
  }

}
