package ch.bfh.unicrypt.math.group.abstracts;

import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;

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
public abstract class AbstractAtomicMonoid extends AbstractAtomicSemiGroup implements Monoid {

  private static final long serialVersionUID = 1L;

  private AtomicElement identityElement;

  @Override
  public final AtomicElement getIdentityElement() {
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
  protected AtomicElement standardSelfApply(Element element, BigInteger amount) {
    if (amount.signum() == 0) {
      return this.getIdentityElement();
    }
    return super.standardSelfApply(element, amount);
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class.
  //

  protected abstract AtomicElement abstractGetIdentityElement();

}