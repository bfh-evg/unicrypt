package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;

public abstract class AbstractCyclicGroup<E extends Element> extends AbstractGroup<E> implements CyclicGroup<E> {

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
  public final boolean isGenerator(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    return this.abstractIsGenerator(element);
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class
  //
  protected abstract E abstractGetDefaultGenerator();

  protected abstract E abstractGetRandomGenerator(Random random);

  protected abstract boolean abstractIsGenerator(Element element);

}
