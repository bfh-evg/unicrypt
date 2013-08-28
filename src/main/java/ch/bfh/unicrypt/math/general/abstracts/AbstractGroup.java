package ch.bfh.unicrypt.math.general.abstracts;

import ch.bfh.unicrypt.math.monoid.abstracts.AbstractMonoid;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.general.interfaces.Group;

/**
 * This abstract class provides a basis implementation for objects of type {@link Group}.
 *
 * @see AbstractElement
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractGroup<T extends Element> extends AbstractMonoid<T> implements Group {

  @Override
  public final T invert(final Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    return this.abstractInvert(element);
  }

  @Override
  public final T applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
  }

  @Override
  protected T standardSelfApply(Element element, BigInteger amount) {
    if (amount.signum() < 0) {
      return this.invert(super.selfApply(element, amount.abs()));
    }
    return super.standardSelfApply(element, amount);
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class.
  //

  protected abstract T abstractInvert(Element element);

}