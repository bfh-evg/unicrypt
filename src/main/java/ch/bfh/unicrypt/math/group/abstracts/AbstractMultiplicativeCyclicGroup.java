package ch.bfh.unicrypt.math.group.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeCyclicGroup;

public abstract class AbstractMultiplicativeCyclicGroup extends AbstractCyclicGroup implements MultiplicativeCyclicGroup {

  private static final long serialVersionUID = 1L;

  @Override
  public final Element multiply(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final Element divide(final Element element1, final Element element2) {
    return this.applyInverse(element1, element2);
  }

  @Override
  public final Element multiply(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final Element power(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final Element power(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final Element power(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

}
