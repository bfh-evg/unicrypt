package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public abstract class AbstractAdditiveGroup<E extends AdditiveElement> extends AbstractGroup<E> implements AdditiveGroup {

  @Override
  public final E add(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final E add(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final E times(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E times(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E times(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E timesTwo(Element element) {
    return this.selfApply(element);
  }

  @Override
  public final E sumOfProducts(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

  @Override
  public final E subtract(final Element element1, final Element element2) {
    return this.applyInverse(element1, element2);
  }

}
