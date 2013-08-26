package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveGroup;

public abstract class AbstractAdditiveGroup extends AbstractGroup<AdditiveElement> implements AdditiveGroup {

  private static final long serialVersionUID = 1L;

  @Override
  protected AdditiveElement abstractGetElement(BigInteger value) {
    return new AbstractAdditiveElement(this, value) {};
  }

  @Override
  public final AdditiveElement add(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final AdditiveElement add(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final AdditiveElement times(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AdditiveElement times(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AdditiveElement times(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AdditiveElement timesTwo(Element element) {
    return this.selfApply(element);
  }

  @Override
  public final AdditiveElement sumOfProducts(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

  @Override
  public final AdditiveElement subtract(final Element element1, final Element element2) {
    return this.applyInverse(element1, element2);
  }
}