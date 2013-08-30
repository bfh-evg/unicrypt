/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractProductGroup<P extends AbstractProductGroup, S extends Group, T extends Tuple, E extends Element> extends AbstractProductMonoid<P, S, T, E> implements Group {

  protected AbstractProductGroup(final Group... groups) {
    super(groups);
  }

  protected AbstractProductGroup(final Group group, final int arity) {
    super(group, arity);
  }

  @Override
  public final T invert(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    T tuple = (T) element;
    final E[] invertedElements = (E[]) new Element[arity];
    for (int i = 0; i < arity; i++) {
      invertedElements[i] = (E) tuple.getAt(i).invert();
    }
    return this.abstractGetElement(invertedElements);
  }

  @Override
  public final T applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
  }

}
