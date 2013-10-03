/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;

/**
 *
 * @author rolfhaenni
 */
public interface Field extends Ring {

  public MultiplicativeGroup getMultiplicativeGroup();

  public DualisticElement divide(Element element1, Element element2);

  public DualisticElement oneOver(Element element);

}
