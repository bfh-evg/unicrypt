package ch.bfh.unicrypt.math.algebra.general.abstracts;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;

/**
 * This abstract class provides a basis implementation for objects of type
 * {@link Monoid}.
 *
 * @see AbstractElement
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractMonoid<E extends Element> extends AbstractSemiGroup<E> implements Monoid {

  private E identityElement;

  @Override
  public final E getIdentityElement() {
    if (this.identityElement == null) {
      this.identityElement = this.abstractGetIdentityElement();
    }
    return this.identityElement;
  }

  @Override
  public final boolean isIdentityElement(final Element element) {
    return this.areEqual(element, this.getIdentityElement());
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //
  @Override
  protected BigInteger standardGetMinOrder() {
    return BigInteger.ONE;
  }

  @Override
  public E standardApply(final Element[] elements) {
    if (elements.length == 0) {
      return this.getIdentityElement();
    }
    return super.standardApply(elements);
  }

  @Override
  protected E standardSelfApply(Element element, BigInteger amount) {
    if (amount.signum() == 0) {
      return this.getIdentityElement();
    }
    return super.standardSelfApply(element, amount);
  }

  @Override
  protected E standardMultiSelfApply(final Element[] elements, BigInteger[] amounts) {
    if (elements.length == 0) {
      return this.getIdentityElement();
    }
    return super.standardMultiSelfApply(elements, amounts);
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class.
  //
  protected abstract E abstractGetIdentityElement();

}
