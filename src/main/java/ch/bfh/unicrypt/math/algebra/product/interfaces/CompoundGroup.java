/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundGroup<CS extends CompoundGroup<CS, S>, S extends Group> extends Group, CompoundMonoid<CS, S> {
}
