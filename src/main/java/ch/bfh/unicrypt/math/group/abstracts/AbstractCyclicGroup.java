package ch.bfh.unicrypt.math.group.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.CyclicGroup;

public abstract class AbstractCyclicGroup extends AbstractGroup implements CyclicGroup {

  private static final long serialVersionUID = 1L;

  private Element defaultGenerator;

  @Override
  public final Element getDefaultGenerator() {
    if (this.defaultGenerator == null) {
      this.defaultGenerator = this.abstractGetDefaultGenerator();
    }
    return this.defaultGenerator;
  }

  @Override
  public final Element getRandomGenerator() {
    return this.getRandomGenerator(null);
  }

  @Override
  public final Element getRandomGenerator(Random random) {
    return this.abstractGetRandomGenerator(random);
  }

  @Override
  public final boolean isGenerator(Element element) {
    if (!this.contains(element)) {
      return false;
    }
    return this.abstractIsGenerator(element);
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class
  //

  protected abstract Element abstractGetDefaultGenerator();

  protected abstract Element abstractGetRandomGenerator(Random random);

  protected abstract boolean abstractIsGenerator(Element element);

}
