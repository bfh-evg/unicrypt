package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;

/**
 * This interface provides represents an multiplicatively written cyclic group.
 * No functionality is added to the super interfaces
 * {@link MultiplicativeSemiGroup} and {@link CyclicGroup}.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface MultiplicativeCyclicGroup<E extends MultiplicativeElement> extends CyclicGroup<E>, MultiplicativeGroup<E> {
}
