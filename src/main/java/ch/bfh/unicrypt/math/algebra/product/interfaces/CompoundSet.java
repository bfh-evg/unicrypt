/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundSet<CS extends CompoundSet<CS, S>, S extends Set> extends Set, Compound<CS, S> {
}
