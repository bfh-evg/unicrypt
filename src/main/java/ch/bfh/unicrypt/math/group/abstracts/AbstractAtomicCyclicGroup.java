package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import java.util.Random;

import ch.bfh.unicrypt.math.group.interfaces.CyclicGroup;

public abstract class AbstractAtomicCyclicGroup extends AbstractAtomicGroup implements CyclicGroup {

  private static final long serialVersionUID = 1L;

  private AtomicElement defaultGenerator;

  @Override
  public final AtomicElement getDefaultGenerator() {
    if (this.defaultGenerator == null) {
      this.defaultGenerator = this.abstractGetDefaultGenerator();
    }
    return this.defaultGenerator;
  }

  @Override
  public final AtomicElement getRandomGenerator() {
    return this.getRandomGenerator(null);
  }

  @Override
  public final AtomicElement getRandomGenerator(Random random) {
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

  protected abstract AtomicElement abstractGetDefaultGenerator();

  protected abstract AtomicElement abstractGetRandomGenerator(Random random);

  protected abstract boolean abstractIsGenerator(Element element);

}
