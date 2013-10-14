/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public interface Ring extends SemiRing, AdditiveGroup {

  // The following methods are overridden from AdditiveGroup with an adapted return type
  @Override
  public DualisticElement subtract(Element element1, Element element2);

  @Override
  public DualisticElement minus(Element element);

}
