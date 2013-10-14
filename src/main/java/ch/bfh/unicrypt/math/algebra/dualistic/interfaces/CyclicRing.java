/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveCyclicGroup;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public interface CyclicRing extends Ring, AdditiveCyclicGroup {

  // The following methods are overridden from AdditiveCyclicGroup with an adapted return type
  @Override
  public DualisticElement getDefaultGenerator();

  @Override
  public DualisticElement getRandomGenerator();

  @Override
  public DualisticElement getRandomGenerator(Random random);

}
