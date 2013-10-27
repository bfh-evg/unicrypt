/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.CyclicRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.random.RandomOracle;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.security.SecureRandom;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractCyclicRing<E extends DualisticElement> extends AbstractRing<E> implements CyclicRing, Iterable<E> {

  private E defaultGenerator;

  @Override
  public final E getDefaultGenerator() {
    if (this.defaultGenerator == null) {
      this.defaultGenerator = this.abstractGetDefaultGenerator();
    }
    return this.defaultGenerator;
  }

  @Override
  public final E getRandomGenerator() {
    return this.getRandomGenerator(null);
  }

  @Override
  public final E getRandomGenerator(Random random) {
    return this.abstractGetRandomGenerator(random);
  }

  @Override
  public final E getIndependentGenerator(long i) {
    return this.getIndependentGenerator(i, RandomOracle.DEFAULT);
  }

  @Override
  public final E getIndependentGenerator(long i, RandomOracle randomOracle) {
    if (randomOracle == null) {
      throw new IllegalArgumentException();
    }
    return this.abstractGetRandomGenerator(randomOracle.getSecureRandom(i));
  }

  @Override
  public final boolean isGenerator(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    return this.abstractIsGenerator(element);
  }

  @Override
  public Iterator<E> iterator() {
    final AbstractCyclicRing<E> cyclicRing = this;
    return new Iterator<E>() {
      boolean next = true;
      E currentElement = cyclicRing.getIdentityElement();

      @Override
      public boolean hasNext() {
        return this.next;
      }

      @Override
      public E next() {
        if (this.hasNext()) {
          E nextElement = currentElement;
          currentElement = cyclicRing.apply(currentElement, cyclicRing.getDefaultGenerator());
          this.next = !currentElement.equals(cyclicRing.getIdentityElement());
          return nextElement;
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    };
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class
  //
  protected abstract E abstractGetDefaultGenerator();

  protected abstract E abstractGetRandomGenerator(Random random);

  protected abstract boolean abstractIsGenerator(Element element);

}
