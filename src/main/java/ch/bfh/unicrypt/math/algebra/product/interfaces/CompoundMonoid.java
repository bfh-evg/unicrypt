/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundMonoid<CS extends CompoundMonoid<CS, S>, S extends Monoid> extends Monoid, CompoundSemiGroup<CS, S> {
}
