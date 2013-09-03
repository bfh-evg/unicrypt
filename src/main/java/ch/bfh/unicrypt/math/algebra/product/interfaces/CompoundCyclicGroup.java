/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundCyclicGroup<CS extends CompoundCyclicGroup<CS, S>, S extends CyclicGroup> extends CyclicGroup, CompoundGroup<CS, S> {
}
