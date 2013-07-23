package ch.bfh.unicrypt.math.group.interfaces;

/**
 * This is a marker interface to indicate that a cyclic group satisfies the generalized 'Decisional Diffie-Hellman' (DDH) assumption.
 * A group satisfying this assumption also satisfies the generalized discrete logarithm assumption (see {@link DLGroup}).
 * 
 * @see "Handbook of Applied Cryptography, Definition 3.75"
 * @see <a href="http://en.wikipedia.org/wiki/Decisional_Diffie–Hellman_assumption">http://en.wikipedia.org/wiki/Decisional_Diffie–Hellman_assumption</a>
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public interface DDHGroup extends DLGroup {

}
