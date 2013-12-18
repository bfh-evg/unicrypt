package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Permutation;
import ch.bfh.unicrypt.math.utility.ArrayUtil;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * An instance of this class represents the group of permutations for a given size. The elements of the group are
 * permutations, which contain the values from 0 to size-1 in a permuted order. Applying the group operation to two
 * permutation elements means to construct the combined permutation element. Note that this operation is not
 * commutative. The identity element is the permutation [0, ..., size-1]. To invert an element, the inverse permutation
 * is computed. Permutation elements are considered to be atomic. This means that they can be converted into a unique
 * integer value and back. The group order is the factorial of its size.
 * <p>
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PermutationGroup
			 extends AbstractGroup<PermutationElement> {

	private final int size;

	/**
	 * Returns a new instance of this class for a given {@literal size >= 0}.
	 * <p>
	 * @param size The size
	 * @throws IllegalArgumentException if {@literal size} is negative
	 */
	private PermutationGroup(final int size) {
		this.size = size;
	}

	/**
	 * Returns the size of the permutation elements in this group. The smallest possible size is 0, which represents the
	 * trivial case of an empty permutation.
	 * <p>
	 * @return The permutation size
	 */
	public final int getSize() {
		return this.size;
	}

	/**
	 * Creates and returns a group element for the given permutation (if one exists).
	 * <p>
	 * @param permutation The given permutation
	 * @return The corresponding group element
	 * @throws IllegalArgumentException if {@literal permutation} is null or if it is not a proper permutation for the
	 *                                  group's permutation size
	 */
	public PermutationElement getElement(final Permutation permutation) {
		if (permutation == null || permutation.getSize() != this.getSize()) {
			throw new IllegalArgumentException();
		}
		return this.standardGetElement(permutation);
	}

	protected PermutationElement standardGetElement(Permutation permutation) {
		return new PermutationElement(this, permutation);
	}

	//
	// The following protected methods override the standard implementation from
	// various super-classes
	//
	@Override
	public boolean standardIsEquivalent(final Set set) {
		final PermutationGroup other = (PermutationGroup) set;
		return this.getSize() == other.getSize();
	}

	@Override
	public String standardToStringContent() {
		return "" + this.getSize();
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected PermutationElement abstractGetRandomElement(final RandomGenerator randomGenerator) {
		return this.standardGetElement(Permutation.getRandomInstance(this.getSize(), randomGenerator));
	}

	@Override
	protected boolean abstractContains(final BigInteger value) {
		BigInteger[] values = MathUtil.unpair(value, this.getSize());
		return Permutation.isPermutationVector(ArrayUtil.bigIntegerToIntArray(values));
	}

	@Override
	protected PermutationElement abstractApply(final PermutationElement element1, final PermutationElement element2) {
		return this.standardGetElement(element1.getPermutation().compose(element2.getPermutation()));
	}

	@Override
	protected PermutationElement abstractInvert(final PermutationElement element) {
		return this.standardGetElement(element.getPermutation().invert());
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return MathUtil.factorial(this.getSize());
	}

	@Override
	protected PermutationElement abstractGetIdentityElement() {
		return this.standardGetElement(Permutation.getInstance(this.getSize()));
	}

	@Override
	protected PermutationElement abstractGetElement(final BigInteger value) {
		BigInteger[] values = MathUtil.unpair(value, this.getSize());
		return standardGetElement(new Permutation(ArrayUtil.bigIntegerToIntArray(values)));
	}
	//
	// STATIC FACTORY METHODS
	//
	private static final Map<Integer, PermutationGroup> instances = new HashMap<Integer, PermutationGroup>();

	/**
	 * Returns a the unique instance of this class for a given non-negative permutation size.
	 * <p>
	 * @param size The size of the permutation
	 * @throws IllegalArgumentException if {@literal modulus} is null, zero, or negative
	 */
	public static PermutationGroup getInstance(final int size) {
		if (size < 0) {
			throw new IllegalArgumentException();
		}
		PermutationGroup instance = PermutationGroup.instances.get(Integer.valueOf(size));
		if (instance == null) {
			instance = new PermutationGroup(size);
			PermutationGroup.instances.put(Integer.valueOf(size), instance);
		}
		return instance;
	}

}
