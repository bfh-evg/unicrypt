package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.util.Random;

/**
 * This class represents the concept of a hash function, which maps an arbitrarily long input element into an element of
 * a given co-domain. The mapping itself is defined by some cryptographic hash function such as SHA-256. For complex
 * input elements, there are two options: one in which the individual elements are first recursively paired with
 * {@link MathUtil#elegantPair(java.math.BigInteger[])}, and one in which the hashing itself is done recursively. The
 * co-domain is always an instance of {@link ZPlusMod}. Its order corresponds to the size of the cryptographic hash
 * function's output space (a power of 2).
 * <p>
 * @see Element#getHashValue()
 * @see Element#getRecursiveHashValue()
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class HashFunction
			 extends AbstractFunction<Set, FiniteByteArraySet, FiniteByteArrayElement> {

	private HashMethod hashMethod;

	private HashFunction(Set domain, FiniteByteArraySet coDomain, HashMethod hashMethod) {
		super(domain, coDomain);
		this.hashMethod = hashMethod;
	}

	@Override
	protected boolean abstractIsEqual(Function function) {
		return this.getHashMethod().equals(((HashFunction) function).getHashMethod());
	}

	@Override
	protected FiniteByteArrayElement abstractApply(final Element element, final Random random) {
		return this.getCoDomain().getElement(element.getHashValue(this.hashMethod).getByteArray());
	}

	public HashMethod getHashMethod() {
		return this.hashMethod;
	}

	/**
	 * This constructor generates a standard SHA-256 hash function. The order of the co-domain is 2^256.
	 * <p>
	 * @param domain
	 * @return
	 */
	public static HashFunction getInstance(Set domain) {
		return HashFunction.getInstance(domain, HashMethod.DEFAULT);
	}

	/**
	 * This constructor generates a standard hash function for a given hash algorithm name. The co-domain is chosen
	 * accordingly.
	 * <p>
	 * @param domain
	 * @param hashMethod The name of the hash algorithm
	 * @return
	 * @throws IllegalArgumentException if {@literal algorithmName} is null or an unknown hash algorithm name
	 */
	public static HashFunction getInstance(Set domain, final HashMethod hashMethod) {
		if (domain == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new HashFunction(domain, FiniteByteArraySet.getInstance(hashMethod.getLength()), hashMethod);
	}

}
