/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.product.interfaces.CompoundCyclicGroup;
import ch.bfh.unicrypt.math.algebra.product.interfaces.CompoundElement;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCompoundCyclicGroup<CS extends CompoundCyclicGroup<CS, S>, CE extends CompoundElement<CE, E>, S extends CyclicGroup, E extends Element>
        extends AbstractCompoundGroup<CS, CE, S, E> implements CyclicGroup {

  private CE defaultGenerator;

  protected AbstractCompoundCyclicGroup(final S... cyclicGroups) {
    super(cyclicGroups);
  }

  protected AbstractCompoundCyclicGroup(final S cyclicGroup, final int arity) {
    super(cyclicGroup, arity);
  }

  @Override
  public final CE getDefaultGenerator() {
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
  public final CE getRandomGenerator() {
    return this.getRandomGenerator(null);
  }

  @Override
  public final CE getRandomGenerator(Random random) {
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
    CE tuple = (CE) element;
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).isGenerator(tuple.getAt(i))) {
        return false;
      }
    }
    return true;
  }

}
