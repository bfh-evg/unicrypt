/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.interfaces;

import ch.bfh.unicrypt.math.helper.Compound;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundFunction<CF extends CompoundFunction<CF, F>, F extends Function> extends Function, Compound<CF, F> {
}
