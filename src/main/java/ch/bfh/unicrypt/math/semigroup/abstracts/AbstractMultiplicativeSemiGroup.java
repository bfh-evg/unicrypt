package ch.bfh.unicrypt.math.semigroup.abstracts;

import ch.bfh.unicrypt.math.element.abstracts.AbstractMultiplicativeElement;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.semigroup.interfaces.MultiplicativeSemiGroup;

public abstract class AbstractMultiplicativeSemiGroup extends AbstractSemiGroup<MultiplicativeElement> implements MultiplicativeSemiGroup {

  private static final long serialVersionUID = 1L;

  @Override
  protected MultiplicativeElement abstractGetElement(BigInteger value) {
    return new AbstractMultiplicativeElement(this, value) {};
  }

  @Override
  public final MultiplicativeElement multiply(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final MultiplicativeElement multiply(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final MultiplicativeElement power(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final MultiplicativeElement power(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final MultiplicativeElement power(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final MultiplicativeElement square(Element element) {
    return this.selfApply(element);
  }

  @Override
  public final MultiplicativeElement productOfPowers(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

}