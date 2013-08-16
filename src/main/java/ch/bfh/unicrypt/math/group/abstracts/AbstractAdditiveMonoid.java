package ch.bfh.unicrypt.math.group.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveSemiGroup;

public abstract class AbstractAdditiveMonoid extends AbstractMonoid implements AdditiveMonoid {

  private static final long serialVersionUID = 1L;

@Override
  public final Element add(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final Element add(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final Element times(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final Element times(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final Element times(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public Element timesTwo(Element element) {
    return this.selfApply(element);
  }

  @Override
  public Element sumOfProducts(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

}