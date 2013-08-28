package ch.bfh.unicrypt.math.general.abstracts;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.general.abstracts.AbstractGroup;
import java.util.Random;

import ch.bfh.unicrypt.math.general.interfaces.CyclicGroup;

public abstract class AbstractCyclicGroup<T extends Element> extends AbstractGroup<T> implements CyclicGroup {

  private T defaultGenerator;

  @Override
  public final T getDefaultGenerator() {
    if (this.defaultGenerator == null) {
      this.defaultGenerator = this.abstractGetDefaultGenerator();
    }
    return this.defaultGenerator;
  }

  @Override
  public final T getRandomGenerator() {
    return this.getRandomGenerator(null);
  }

  @Override
  public final T getRandomGenerator(Random random) {
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

  protected abstract T abstractGetDefaultGenerator();

  protected abstract T abstractGetRandomGenerator(Random random);

  protected abstract boolean abstractIsGenerator(Element element);

}
