/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.product.interfaces.CompoundElement;
import ch.bfh.unicrypt.math.algebra.product.interfaces.CompoundGroup;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCompoundGroup<CS extends CompoundGroup<CS, S>, CE extends CompoundElement<CE, E>, S extends Group, E extends Element>
        extends AbstractCompoundMonoid<CS, CE, S, E> implements Group {

  protected AbstractCompoundGroup(final S... groups) {
    super(groups);
  }

  protected AbstractCompoundGroup(final S group, final int arity) {
    super(group, arity);
  }

  @Override
  public final CE invert(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    CE tuple = (CE) element;
    final E[] invertedElements = (E[]) new Element[arity];
    for (int i = 0; i < arity; i++) {
      invertedElements[i] = (E) tuple.getAt(i).invert();
    }
    return this.abstractGetElement(invertedElements);
  }

  @Override
  public final CE applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
  }

}
