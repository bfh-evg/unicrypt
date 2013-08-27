package ch.bfh.unicrypt.math.cyclicgroup.interfaces;

import ch.bfh.unicrypt.math.cyclicgroup.interfaces.CyclicGroup;

/**
 * This is a marker interface to indicate that a cyclic group satisfies the generalized 'Discrete Logarithm' (DL) assumption.
 * 
 * @see "Handbook of Applied Cryptography, Definition 3.52"
 * @see <a href="http://en.wikipedia.org/wiki/Discrete_logarithm">http://en.wikipedia.org/wiki/Discrete_logarithm</a>
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public interface DLGroup extends CyclicGroup {

}
