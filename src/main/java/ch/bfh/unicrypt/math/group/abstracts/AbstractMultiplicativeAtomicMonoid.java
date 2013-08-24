package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeMonoid;

public abstract class AbstractMultiplicativeAtomicMonoid extends AbstractAtomicMonoid implements MultiplicativeMonoid {

  private static final long serialVersionUID = 1L;

  @Override
  public final AtomicElement multiply(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final AtomicElement multiply(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final AtomicElement power(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AtomicElement power(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AtomicElement power(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final AtomicElement square(Element element) {
    return this.selfApply(element);
  }

  @Override
  public final AtomicElement productOfPowers(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

}