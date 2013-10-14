package ch.bfh.unicrypt.math.algebra.multiplicative.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeMonoid;

public abstract class AbstractMultiplicativeMonoid<E extends MultiplicativeElement> extends AbstractMonoid<E> implements MultiplicativeMonoid {

  @Override
  public final E multiply(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final E multiply(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final E power(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E power(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E power(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E square(Element element) {
    return this.selfApply(element);
  }

  @Override
  public final E productOfPowers(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

  @Override
  public E getOneElement() {
    return this.getIdentityElement();
  }

  @Override
  public boolean isOneElement(Element element) {
    return this.isIdentityElement(element);
  }

}
