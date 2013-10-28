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
 * @param <E>
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
    return this.standardGetRandomGenerator(random);
  }

  @Override
  public final E getIndependentGenerator(long query) {
    return this.getIndependentGenerator(query, RandomOracle.DEFAULT);
  }

  @Override
  public final E getIndependentGenerator(long query, RandomOracle randomOracle) {
    if (randomOracle == null) {
      throw new IllegalArgumentException();
    }
    return this.standardGetIndependentGenerator(query, randomOracle);
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

  // see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
  protected E standardGetRandomGenerator(Random random) {
    E element;
    do {
      element = this.getRandomElement(random);
    } while (!this.isGenerator(element));
    return element;
  }

  protected E standardGetIndependentGenerator(long query, RandomOracle randomOracle) {
    return this.standardGetRandomGenerator(randomOracle.getRandom(query));
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class
  //
  protected abstract E abstractGetDefaultGenerator();

  protected abstract boolean abstractIsGenerator(Element element);

}
