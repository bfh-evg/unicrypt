package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import java.math.BigInteger;

/**
 * This interface provides represents an additively written monoid. No
 * functionality is added.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface MultiplicativeMonoid extends Monoid, MultiplicativeSemiGroup {

  public MultiplicativeElement getOne();

  public boolean isOne(Element element);

  // The following methods are overridden from Monoid with an adapted return type
  @Override
  public MultiplicativeElement getIdentityElement();

}
