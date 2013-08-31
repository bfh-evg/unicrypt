package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;

/**
 * This interface provides represents an additively written monoid. No
 * functionality is added.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface MultiplicativeMonoid<E extends MultiplicativeElement> extends Monoid<E>, MultiplicativeSemiGroup<E> {
}
