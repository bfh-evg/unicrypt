/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.product.interfaces.Tuple;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractProductCyclicGroup<P extends AbstractProductCyclicGroup, S extends CyclicGroup, T extends Tuple, E extends Element> extends AbstractProductGroup<P, S, T, E> implements CyclicGroup {

  private T defaultGenerator;

  protected AbstractProductCyclicGroup(final CyclicGroup... cyclicGroups) {
    super(cyclicGroups);
  }

  protected AbstractProductCyclicGroup(final CyclicGroup cyclicGroup, final int arity) {
    super(cyclicGroup, arity);
  }

  @Override
  public final T getDefaultGenerator() {
    if (this.defaultGenerator == null) {
      E[] generators = (E[]) new Element[this.getArity()];
      for (int i = 0; i < this.getArity(); i++) {
        generators[i] = (E) this.getAt(i).getDefaultGenerator();
      }
      this.defaultGenerator = this.abstractGetElement(generators);
    }
    return this.defaultGenerator;
  }

  @Override
  public final T getRandomGenerator() {
    return this.getRandomGenerator(null);
  }

  @Override
  public final T getRandomGenerator(Random random) {
    int arity = this.getArity();
    E[] randomElements = (E[]) new Element[arity];
    for (int i = 0; i < arity; i++) {
      randomElements[i] = (E) this.getAt(i).getRandomElement(random);
    }
    return this.abstractGetElement(randomElements);
  }

  @Override
  public final boolean isGenerator(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    T tuple = (T) element;
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).isGenerator(tuple.getAt(i))) {
        return false;
      }
    }
    return true;
  }

}
