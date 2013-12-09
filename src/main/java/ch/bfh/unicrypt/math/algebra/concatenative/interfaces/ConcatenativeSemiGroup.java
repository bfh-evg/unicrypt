package ch.bfh.unicrypt.math.algebra.concatenative.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import java.math.BigInteger;
import java.util.Random;

/**
 * This interface provides the renaming of some group operations for the case of an additively written semigroup. No
 * functionality is added.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface ConcatenativeSemiGroup
			 extends SemiGroup {

	public int getBlockLength();

	public ConcatenativeElement getRandomElement(int length);

	public ConcatenativeElement getRandomElement(int length, Random random);

	/**
	 * This method is a synonym for {@link #Group.apply(Element, Element)}.
	 * <p>
	 * @param element1 the same as in {@link #Group.apply(Element, Element)}
	 * @param element2 the same as in {@link #Group.apply(Element, Element)}
	 * @return the same as in {@link #Group.apply(Element, Element)}
	 */
	public ConcatenativeElement concatenate(Element element1, Element element2);

	/**
	 * This method is a synonym for {@link #Group.apply(Element...)}.
	 * <p>
	 * @param elements the same as in {@link #Group.apply(Element...)}
	 * @return the same as in {@link #Group.apply(Element...)}
	 */
	public ConcatenativeElement concatenate(Element... elements);

	/**
	 * This method is a synonym for {@link #Group.selfApply(Element, BigInteger)}.
	 * <p>
	 * @param element the same as in {@link #Group.selfApply(Element, BigInteger)}
	 * @param amount  the same as in {@link #Group.selfApply(Element, BigInteger)}
	 * @return the same as in {@link #Group.selfApply(Element, BigInteger)}
	 */
	public ConcatenativeElement selfConcatenate(Element element, BigInteger amount);

	/**
	 * This method is a synonym for {@link #Group.selfApply(Element, Element)}.
	 * <p>
	 * @param element the same as in {@link #Group.selfApply(Element, Element)}
	 * @param amount  the same as in {@link #Group.selfApply(Element, Element)}
	 * @return the same as in {@link #Group.selfApply(Element, Element)}
	 */
	public ConcatenativeElement selfConcatenate(Element element, Element amount);

	/**
	 * This method is a synonym for {@link #Group.selfApply(Element, int)}.
	 * <p>
	 * @param element the same as in {@link #Group.selfApply(Element, int)}
	 * @param amount  the same as in {@link #Group.selfApply(Element, int)}
	 * @return the same as in {@link #Group.selfApply(Element, int)}
	 */
	public ConcatenativeElement selfConcatenate(Element element, int amount);

	/**
	 * Applies the group operation to two instances of a given group element. This is equivalent to
	 * {@code selfApply(element, 2)}.
	 * <p>
	 * @param element A given group element
	 * @return The result of applying the group operation to the input element
	 * @throws IllegalArgumentException if {@code element} is null or does not belong to the group
	 */
	public ConcatenativeElement selfConcatenate(Element element);

	/**
	 * Applies the binary operation pair-wise sequentially to the results of computing
	 * {@link #selfApply(Element, BigInteger)} multiple times. In an additive group, this operation is sometimes called
	 * 'weighed sum', and 'product-of-powers' in a multiplicative group.
	 * <p>
	 * @param elements A given array of elements
	 * @param amounts  Corresponding amounts
	 * @return The result of this operation
	 * @throws IllegalArgumentException if {@code elements} or one of its elements is null
	 * @throws IllegalArgumentException if {@code amounts} or one of its values is null
	 * @throws IllegalArgumentException if one of the elements of {@code elements} does not belong to the group
	 * @throws IllegalArgumentException if {@code elements} and {@code amounts} have different lengths
	 */
	public ConcatenativeElement multiSelfConcatenate(Element[] elements, BigInteger[] amounts);

	public ConcatenativeElement getEmptyElement();

	public boolean isEmptyElement(Element element);

	// The following methods are overridden from Set with an adapted return type
	@Override
	public ConcatenativeElement getElement(int value);

	@Override
	public ConcatenativeElement getElement(BigInteger value);

	@Override
	public ConcatenativeElement getElement(Element element);

	@Override
	public ConcatenativeElement getRandomElement();

	@Override
	public ConcatenativeElement getRandomElement(Random random);

	// The following methods are overridden from SemiGroup with an adapted return type
	@Override
	public ConcatenativeElement apply(Element element1, Element element2);

	@Override
	public ConcatenativeElement apply(Element... elements);

	@Override
	public ConcatenativeElement selfApply(Element element, BigInteger amount);

	@Override
	public ConcatenativeElement selfApply(Element element, Element amount);

	@Override
	public ConcatenativeElement selfApply(Element element, int amount);

	@Override
	public ConcatenativeElement selfApply(Element element);

	@Override
	public ConcatenativeElement multiSelfApply(Element[] elements, BigInteger[] amounts);

}
