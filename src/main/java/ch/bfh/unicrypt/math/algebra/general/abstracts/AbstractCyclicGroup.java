package ch.bfh.unicrypt.math.algebra.general.abstracts;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.utility.RandomUtil;

public abstract class AbstractCyclicGroup<E extends Element> extends AbstractGroup<E> implements CyclicGroup, Iterable<E> {

  private E defaultGenerator;
  private static final Random defaultRandomNumberGenerator = RandomUtil.getRandomNumberGenerator();

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
    return getIndependentGenerator(i, (Random) null);
  }

  @Override
  public final E getIndependentGenerator(long i, Random random) {
    if (i < 0) {
      throw new IllegalArgumentException();
    }
    if (random == null) {
      random = AbstractCyclicGroup.defaultRandomNumberGenerator;
    }
    random.setSeed(i);
    return this.abstractGetRandomGenerator(random);
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
    final AbstractCyclicGroup<E> cyclicGroup = this;
    return new Iterator<E>() {
      boolean next = true;
      E currentElement = cyclicGroup.getIdentityElement();

      @Override
      public boolean hasNext() {
        return this.next;
      }

      @Override
      public E next() {
        if (this.hasNext()) {
          E nextElement = currentElement;
          currentElement = cyclicGroup.apply(currentElement, cyclicGroup.getDefaultGenerator());
          this.next = !currentElement.equals(cyclicGroup.getIdentityElement());
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
