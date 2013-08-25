package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.classes.AdditiveAtomicElement;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveCyclicGroup;

public abstract class AbstractAdditiveAtomicCyclicGroup extends AbstractAtomicCyclicGroup<AdditiveAtomicElement> implements AdditiveCyclicGroup {

  private static final long serialVersionUID = 1L;

  @Override
  protected AdditiveAtomicElement abstractGetElement(BigInteger value) {
    return new AdditiveAtomicElement(this, value) {};
  }

  @Override
  public final AdditiveAtomicElement add(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final AdditiveAtomicElement add(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final AdditiveAtomicElement times(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AdditiveAtomicElement times(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AdditiveAtomicElement times(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AdditiveAtomicElement timesTwo(Element element) {
    return this.selfApply(element);
  }

  @Override
  public final AdditiveAtomicElement sumOfProducts(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

  @Override
  public final AdditiveAtomicElement subtract(final Element element1, final Element element2) {
    return this.applyInverse(element1, element2);
  }

}
