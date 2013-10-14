package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import java.util.Random;

/**
 * This interface provides represents an multiplicatively written cyclic group.
 * No functionality is added to the super interfaces
 * {@link MultiplicativeSemiGroup} and {@link CyclicGroup}.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface MultiplicativeCyclicGroup extends CyclicGroup, MultiplicativeGroup {

  // The following methods are overridden from Group with an adapted return type
  @Override
  public MultiplicativeElement getDefaultGenerator();

  @Override
  public MultiplicativeElement getRandomGenerator();

  @Override
  public MultiplicativeElement getRandomGenerator(Random random);

}
