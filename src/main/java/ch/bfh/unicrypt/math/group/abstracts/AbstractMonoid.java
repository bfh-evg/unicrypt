package ch.bfh.unicrypt.math.group.abstracts;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.utility.MathUtil;

/**
 * This abstract class provides a basis implementation for objects of type
 * {@link Monoid}.
 *
 * @see Element
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractMonoid extends AbstractSemiGroup implements Monoid {

  private static final long serialVersionUID = 1L;

  private Element identityElement;

  @Override
  public final Element getIdentityElement() {
    if (this.identityElement == null) {
      this.identityElement = this.abstractGetIdentityElement();
    }
    return this.identityElement;
  }

  @Override
  public final boolean isIdentityElement(final Element element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    return this.areEqual(element, getIdentityElement());
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //

  protected BigInteger standardGetMinOrder() {
    return BigInteger.ONE;
  }

  @Override
  protected Element standardSelfApply(Element element, BigInteger amount) {
    if (amount.signum() == 0) {
      return this.getIdentityElement();
    }
    return super.standardSelfApply(element, amount);
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class.
  //

  protected abstract Element abstractGetIdentityElement();

}