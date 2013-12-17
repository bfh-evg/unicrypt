package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;

/**
 * This interface represents the concept a cyclic atomic group. Every element of a cyclic group can be written as a
 * power of some particular element in multiplicative notation, or as a multiple of the element in additive notation.
 * Such an element is called generator of the group. For every positive integer there is exactly one cyclic group (up to
 * isomorphism) with that order, and there is exactly one infinite cyclic group. This interface extends extends
 * {@link Group} with additional methods for dealing with generators. Each implementing class must provide a default
 * generator.
 * <p>
 * @see "Handbook of Applied Cryptography, Definition 2.167"
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface CyclicGroup
			 extends Group {

	/**
	 * Returns a default generator of this group.
	 * <p>
	 * @return The default generator
	 */
	public Element getDefaultGenerator();

	/**
	 * Returns a randomly selected generator of this group using the default random generator.
	 * <p>
	 * @return The randomly selected generator
	 */
	public Element getRandomGenerator();

	/**
	 * Returns a randomly selected generator of this group using a given random generator.
	 * <p>
	 * @param randomGenerator The given random generator
	 * @return The randomly selected generator
	 */
	public Element getRandomGenerator(RandomGenerator randomGenerator);

	public Element getIndependentGenerator(int index);

	public Element getIndependentGenerator(int index, RandomReferenceString randomReferenceString);

	public Element[] getIndependentGenerators(int maxIndex);

	public Element[] getIndependentGenerators(int maxIndex, RandomReferenceString randomReferenceString);

	public Element[] getIndependentGenerators(int minIndex, int maxIndex);

	public Element[] getIndependentGenerators(int minIndex, int maxIndex, RandomReferenceString randomReferenceString);

	/**
	 * Checks if a given element is a generator of the group.
	 * <p>
	 * @param element The given element
	 * @return {@code true} if the element is a generator, {@code false} otherwise
	 * @throws IllegalArgumentException if {@code} is null
	 */
	public boolean isGenerator(Element element);

}
