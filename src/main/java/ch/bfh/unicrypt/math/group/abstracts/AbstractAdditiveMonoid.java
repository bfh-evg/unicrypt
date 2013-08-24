package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.group.interfaces.AdditiveMonoid;

public abstract class AbstractAdditiveMonoid extends AbstractAtomicMonoid implements AdditiveMonoid {

  private static final long serialVersionUID = 1L;

@Override
  public final AtomicElement add(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final AtomicElement add(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final AtomicElement times(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AtomicElement times(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AtomicElement times(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public AtomicElement timesTwo(Element element) {
    return this.selfApply(element);
  }

  @Override
  public AtomicElement sumOfProducts(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

}