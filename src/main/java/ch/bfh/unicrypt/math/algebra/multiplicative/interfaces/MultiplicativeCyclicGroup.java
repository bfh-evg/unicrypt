package ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;

/**
 * This interface provides represents an multiplicatively written cyclic group. No functionality is added to the super
 * interfaces {@link MultiplicativeSemiGroup} and {@link CyclicGroup}.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface MultiplicativeCyclicGroup
			 extends CyclicGroup, MultiplicativeGroup {

	// The following methods are overridden from Group with an adapted return type
	@Override
	public MultiplicativeElement getDefaultGenerator();

	@Override
	public MultiplicativeElement getRandomGenerator();

	@Override
	public MultiplicativeElement getRandomGenerator(RandomGenerator randomGenerator);

	@Override
	public MultiplicativeElement[] getIndependentGenerators(int amount);

	@Override
	public MultiplicativeElement[] getIndependentGenerators(int amount, RandomReferenceString randomReferenceString);

}
