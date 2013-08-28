/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.Tuple;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.monoid.abstracts.AbstractProductMonoid;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractProductGroup<S extends Group> extends AbstractProductMonoid<S> implements Group {

  protected AbstractProductGroup(final Group[] groups) {
    super(groups);
  }

  protected AbstractProductGroup(final Group group, final int arity) {
    super(group, arity);
  }

  protected AbstractProductGroup() {
    super();
  }

  @Override
  public final Tuple invert(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    int arity = this.getArity();
    Tuple compoundElement = (Tuple) element;
    final Element[] invertedElements = new Element[arity];
    for (int i = 0; i < arity; i++) {
      invertedElements[i] = compoundElement.getAt(i).invert();
    }
    return standardGetElement(invertedElements);
  }

  @Override
  public final Tuple applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
  }

}
