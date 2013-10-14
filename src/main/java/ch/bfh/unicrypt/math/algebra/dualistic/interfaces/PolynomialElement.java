/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import java.util.Set;

/**
 *
 * @author rolfhaenni
 */
public interface PolynomialElement extends DualisticElement {

  public Set<Integer> getIndices();

  public DualisticElement getCoefficient(int index);

}
