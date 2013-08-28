package ch.bfh.unicrypt.math.additive.abstracts;

import ch.bfh.unicrypt.math.additive.interfaces.AdditiveElement;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.general.abstracts.AbstractSemiGroup;
import ch.bfh.unicrypt.math.additive.interfaces.AdditiveSemiGroup;

public abstract class AbstractAdditiveSemiGroup extends AbstractSemiGroup<AdditiveElement> implements AdditiveSemiGroup {

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
}