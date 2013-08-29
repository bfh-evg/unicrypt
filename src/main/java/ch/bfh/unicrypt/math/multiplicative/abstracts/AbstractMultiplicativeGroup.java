package ch.bfh.unicrypt.math.multiplicative.abstracts;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.general.abstracts.AbstractGroup;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.multiplicative.interfaces.MultiplicativeGroup;

public abstract class AbstractMultiplicativeGroup<E extends MultiplicativeElement> extends AbstractGroup<E> implements MultiplicativeGroup {

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
  public final E divide(final Element element1, final Element element2) {
    return this.applyInverse(element1, element2);
  }

}
