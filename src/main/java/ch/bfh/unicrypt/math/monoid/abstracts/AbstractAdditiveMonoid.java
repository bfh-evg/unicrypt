package ch.bfh.unicrypt.math.monoid.abstracts;

import ch.bfh.unicrypt.math.element.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.monoid.interfaces.AdditiveMonoid;

public abstract class AbstractAdditiveMonoid extends AbstractMonoid<AdditiveElement> implements AdditiveMonoid {

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
  public AdditiveElement timesTwo(Element element) {
    return this.selfApply(element);
  }

  @Override
  public AdditiveElement sumOfProducts(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }
}