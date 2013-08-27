package ch.bfh.unicrypt.math.cyclicgroup.interfaces;

import ch.bfh.unicrypt.math.cyclicgroup.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeGroup;

/**
 * This interface provides represents an multiplicatively written cyclic group. No functionality is added to the super interfaces
 * {@link MultiplicativeSemiGroup} and {@link CyclicGroup}.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface MultiplicativeCyclicGroup extends CyclicGroup, MultiplicativeGroup {

}
