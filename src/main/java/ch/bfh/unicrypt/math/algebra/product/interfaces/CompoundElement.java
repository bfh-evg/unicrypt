/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;

/**
 *
 * @author rolfhaenni
 */
public interface CompoundElement<CS extends CompoundSet<CS, S>, CE extends CompoundElement<CS, CE, S, E>, S extends Set, E extends Element<S>> extends Element<CS>, Compound<CE, E> {
}
