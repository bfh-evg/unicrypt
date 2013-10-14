package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;

/**
 * This interface provides represents an additively written monoid. No
 * functionality is added.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface AdditiveMonoid extends Monoid, AdditiveSemiGroup {

  public AdditiveElement getZero();

  public boolean isZero(Element element);

  // The following methods are overridden from SemiGroup with an adapted return type
  @Override
  public AdditiveElement getIdentityElement();

}
