package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import java.util.Random;

/**
 * This interface provides represents an additively written cyclic group. No
 * functionality is added.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface AdditiveCyclicGroup extends CyclicGroup, AdditiveGroup {

  // The following methods are overridden from Group with an adapted return type
  @Override
  public AdditiveElement getDefaultGenerator();

  @Override
  public AdditiveElement getRandomGenerator();

  @Override
  public AdditiveElement getRandomGenerator(Random random);

}
