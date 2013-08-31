package ch.bfh.unicrypt.math.algebra.concatenative.abstracts;

import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeElement;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSemiGroup;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeSemiGroup;

public abstract class AbstractConcatenativeSemiGroup<E extends ConcatenativeElement> extends AbstractSemiGroup<E> implements ConcatenativeSemiGroup<E> {

  @Override
  public final E concatenate(final Element element1, final Element element2) {
    return this.apply(element1, element2);
  }

  @Override
  public final E concatenate(final Element... elements) {
    return this.apply(elements);
  }

  @Override
  public final E selfConcatenate(final Element element, final BigInteger amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E selfConcatenate(final Element element, final Element amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E selfConcatenate(final Element element, final int amount) {
    return this.selfApply(element, amount);
  }

  @Override
  public final E selfConcatenate(Element element) {
    return this.selfApply(element);
  }

  @Override
  public final E multiSelfConcatenate(Element[] elements, BigInteger[] amounts) {
    return this.multiSelfApply(elements, amounts);
  }

}
