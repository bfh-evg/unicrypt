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
public interface CompoundElement<CE extends CompoundElement<CE, E>, E extends Element> extends Element, Compound<CE, E> {
}
