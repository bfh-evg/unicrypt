package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.math.BigInteger;

/**
 * This abstract class represents the concept an element in a mathematical group. It allows applying the group operation
 * and other methods from a {@link Group} in a convenient way. Most methods provided by {@link Element} have an
 * equivalent method in {@link Group}.
 * <p>
 * @see Group
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Element {

	public boolean isAdditive();

	public boolean isMultiplicative();

	public boolean isConcatenative();

	public boolean isDualistic();

	public boolean isTuple();

	/**
	 *
	 * @return
	 */
	public Set getSet();

	/**
	 * Returns the positive BigInteger value that corresponds the element.
	 * <p>
	 * @return The corresponding BigInteger value
	 */
	public BigInteger getValue();

	public FiniteByteArrayElement getHashValue();

	public FiniteByteArrayElement getHashValue(HashMethod hashMethod);

	/**
	 * Checks if this element is mathematically equivalent to the given element. For this, they need to belong to the same
	 * set.
	 * <p>
	 * @param element
	 * @return
	 */
	public boolean isEquivalent(Element element);

	// The following methods are equivalent to corresponding Set methods
	//
	/**
	 * @see Group#apply(Element, Element)
	 */
	public Element apply(Element element);

	/**
	 * @see Group#applyInverse(Element, Element)
	 */
	public Element applyInverse(Element element);

	/**
	 * @see Group#selfApply(Element, BigInteger)
	 */
	public Element selfApply(BigInteger amount);

	/**
	 * @see Group#selfApply(Element, Element)
	 */
	public Element selfApply(Element amount);

	/**
	 * @see Group#selfApply(Element, int)
	 */
	public Element selfApply(int amount);

	/**
	 * @see Group#selfApply(Element)
	 */
	public Element selfApply();

	/**
	 * @see Group#invert(Element)
	 */
	public Element invert();

	/**
	 * @see Group#isIdentityElement(Element)
	 */
	public boolean isIdentity();

	/**
	 * @see CyclicGroup#isGenerator(Element)
	 */
	public boolean isGenerator();

}
