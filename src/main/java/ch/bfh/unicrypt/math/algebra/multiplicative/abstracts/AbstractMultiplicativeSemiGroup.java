package ch.bfh.unicrypt.math.algebra.multiplicative.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSemiGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;

public abstract class AbstractMultiplicativeSemiGroup<E extends MultiplicativeElement> extends AbstractSemiGroup<E> implements MultiplicativeSemiGroup {

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

}
