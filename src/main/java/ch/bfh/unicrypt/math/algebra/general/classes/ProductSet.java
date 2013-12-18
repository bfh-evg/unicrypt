/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;
import ch.bfh.unicrypt.math.helper.CompoundIterator;
import ch.bfh.unicrypt.math.utility.ArrayUtil;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author rolfhaenni
 */
public class ProductSet
			 extends AbstractSet<Tuple>
			 implements Compound<ProductSet, Set> {

	private final Set[] sets;
	private final int arity;
	private final Class<?> setClass; // this is needed to create arrays of the actual type

	protected ProductSet(Set[] sets) {
		this.sets = sets.clone();
		this.arity = sets.length;
		this.setClass = sets.getClass().getComponentType();
	}

	protected ProductSet(Set set, int arity) {
		this.sets = new Set[]{set};
		this.arity = arity;
		this.setClass = set.getClass();
	}

	public final boolean contains(final int... values) {
		return this.contains(ArrayUtil.intToBigIntegerArray(values));
	}

	public final boolean contains(BigInteger... values) {
		int arity = this.getArity();
		if (values == null || values.length != arity) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < arity; i++) {
			if (!this.getAt(i).contains(values[i])) {
				return false;
			}
		}
		return true;
	}

	public final boolean contains(Element... elements) {
		int arity = this.getArity();
		if (elements == null || elements.length != arity) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < arity; i++) {
			if (!this.getAt(i).contains(elements[i])) {
				return false;
			}
		}
		return true;
	}

	public final Tuple getElement(final int... values) {
		return this.getElement(ArrayUtil.intToBigIntegerArray(values));
	}

	public final Tuple getElement(BigInteger[] values) {
		int arity = this.getArity();
		if (values == null || values.length != arity) {
			throw new IllegalArgumentException();
		}
		Element[] elements = new Element[arity];
		for (int i = 0; i < arity; i++) {
			elements[i] = this.getAt(i).getElement(values[i]);
		}
		return this.standardGetElement(elements);
	}

	public final Tuple getElement(final Element... elements) {
		int arity = this.getArity();
		if (elements == null || elements.length != arity) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < arity; i++) {
			if (!this.getAt(i).contains(elements[i])) {
				throw new IllegalArgumentException();
			}
		}
		return this.standardGetElement(elements);
	}

	protected Tuple standardGetElement(final Element... elements) {
		if (this.getArity() == 2) {
			return new Pair(this, elements);
		}
		if (this.getArity() == 3) {
			return new Triple(this, elements);
		}
		return new Tuple(this, elements);
	}

	@Override
	protected BigInteger standardGetOrderLowerBound() {
		if (this.isUniform()) {
			return this.getFirst().getOrderLowerBound().pow(this.getArity());
		}
		BigInteger result = BigInteger.ONE;
		for (Set set : this.makeIterable()) {
			result = result.multiply(set.getOrderLowerBound());
		}
		return result;
	}

	@Override
	protected BigInteger standardGetOrderUpperBound() {
		if (this.isUniform()) {
			return this.getFirst().getOrderUpperBound().pow(this.getArity());
		}
		BigInteger result = BigInteger.ONE;
		for (Set set : this.makeIterable()) {
			if (set.getOrderUpperBound().equals(Set.INFINITE_ORDER)) {
				return Set.INFINITE_ORDER;
			}
			result = result.multiply(set.getOrderUpperBound());
		}
		return result;
	}

	@Override
	protected BigInteger standardGetMinimalOrder() {
		if (this.isUniform()) {
			return this.getFirst().getMinimalOrder();
		}
		BigInteger result = null;
		for (Set set : this.makeIterable()) {
			if (result == null) {
				result = set.getMinimalOrder();
			} else {
				result = result.min(set.getMinimalOrder());
			}
		}
		return result;
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected BigInteger abstractGetOrder() {
		if (this.isNull()) {
			return BigInteger.ONE;
		}
		if (this.isUniform()) {
			Set first = this.getFirst();
			if (first.isFinite() && first.hasKnownOrder()) {
				return first.getOrder().pow(this.getArity());
			}
			return first.getOrder();
		}
		BigInteger result = BigInteger.ONE;
		for (Set set : this.makeIterable()) {
			if (set.isEmpty()) {
				return BigInteger.ZERO;
			}
			if (!set.isFinite() || result.equals(Set.INFINITE_ORDER)) {
				result = Set.INFINITE_ORDER;
			} else {
				if (!set.hasKnownOrder() || result.equals(Set.UNKNOWN_ORDER)) {
					result = Set.UNKNOWN_ORDER;
				} else {
					result = result.multiply(set.getOrder());
				}
			}
		}
		return result;
	}

	@Override
	protected Tuple abstractGetElement(BigInteger value) {
		BigInteger[] values = MathUtil.unpairAndUnfold(value, this.getArity());
		return this.getElement(values);
	}

	@Override
	protected Tuple abstractGetRandomElement(RandomGenerator randomGenerator) {
		int arity = this.getArity();
		final Element[] randomElements = new Element[arity];
		for (int i = 0; i < arity; i++) {
			randomElements[i] = this.getAt(i).getRandomElement(randomGenerator);
		}
		return this.standardGetElement(randomElements);
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		BigInteger[] values = MathUtil.unpairAndUnfold(value, this.getArity());
		return this.contains(values);
	}

	@Override
	public int getArity() {
		return this.arity;
	}

	@Override
	public final boolean isNull() {
		return this.getArity() == 0;
	}

	@Override
	public final boolean isUniform() {
		return this.sets.length <= 1;
	}

	@Override
	public Set getFirst() {
		return this.getAt(0);

	}

	@Override
	public Set getAt(int index
	) {
		if (index < 0 || index >= this.getArity()) {
			throw new IndexOutOfBoundsException();
		}
		if (this.isUniform()) {
			return this.sets[0];
		}
		return this.sets[index];
	}

	@Override
	public Set getAt(int... indices
	) {
		if (indices == null) {
			throw new IllegalArgumentException();
		}
		Set set = this;
		for (final int index : indices) {
			if (set.isProduct()) {
				set = ((ProductSet) set).getAt(index);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return set;
	}

	@Override
	public Set[] getAll() {
		int arity = this.getArity();
		Set[] result = (Set[]) Array.newInstance(this.setClass, arity);
		for (int i = 0; i < arity; i++) {
			result[i] = this.getAt(i);
		}
		return result;
	}

	@Override
	public ProductSet removeAt(final int index
	) {
		int arity = this.getArity();
		if (index < 0 || index >= arity) {
			throw new IndexOutOfBoundsException();
		}
		if (this.isUniform()) {
			return this.abstractRemoveAt(this.getFirst(), arity - 1);
		}
		final Set[] remaining = (Set[]) Array.newInstance(this.setClass, arity - 1);
		for (int i = 0; i < arity - 1; i++) {
			if (i < index) {
				remaining[i] = this.getAt(i);
			} else {
				remaining[i] = this.getAt(i + 1);
			}
		}
		return this.abstractRemoveAt(remaining);
	}

	protected ProductSet abstractRemoveAt(Set set, int arity) {
		return ProductSet.getInstance(set, arity);
	}

	protected ProductSet abstractRemoveAt(Set[] sets) {
		return ProductSet.getInstance(sets);
	}

	public Iterable<? extends Set> makeIterable() {
		final ProductSet productSet = this;
		return new Iterable<Set>() {
			@Override
			public Iterator<Set> iterator() {
				return new CompoundIterator<Set>(productSet);
			}
		};
	}

	@Override
	protected boolean standardIsEquivalent(Set set) {
		ProductSet other = (ProductSet) set;
		int arity = this.getArity();
		if (arity != other.getArity()) {
			return false;
		}
		for (int i = 0; i < arity; i++) {
			if (!this.getAt(i).isEquivalent(other.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean standardIsCompatible(Set set) {
		return (set instanceof ProductSet);
	}

	@Override
	protected String standardToStringContent() {
		if (this.isNull()) {
			return "";
		}
		if (this.isUniform()) {
			return this.getFirst().toString() + "^" + this.getArity();
		}
		String result = "";
		String separator = "";
		for (Set set : this.makeIterable()) {
			result = result + separator + set.toString();
			separator = " x ";
		}
		return result;
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is a static factory method to construct a composed set without calling respective constructors. The input sets
	 * are given as an array.
	 * <p>
	 * <p/>
	 * @param sets The array of input sets
	 * @return The corresponding product set
	 * @throws IllegalArgumentException if {@literal sets} is null or contains null
	 */
	public static ProductSet getInstance(final Set... sets) {
		if (sets == null) {
			throw new IllegalArgumentException();
		}
		boolean isSemiGroup = true;
		if (sets.length > 0) {
			boolean uniform = true;
			Set first = sets[0];
			for (final Set set : sets) {
				if (set == null) {
					throw new IllegalArgumentException();
				}
				if (!set.isEquivalent(first)) {
					uniform = false;
				}
				isSemiGroup = set.isSemiGroup() && isSemiGroup;
			}
			if (uniform) {
				return ProductSet.getInstance(first, sets.length);
			}
		}
		if (isSemiGroup) {
			SemiGroup[] semiGroups = Arrays.copyOf(sets, sets.length, SemiGroup[].class);
			return ProductSemiGroup.getInstance(semiGroups);
		}
		return new ProductSet(sets);
	}

	public static ProductSet getInstance(final Set set, int arity) {
		if ((set == null) || (arity < 0)) {
			throw new IllegalArgumentException();
		}
		if (set.isSemiGroup()) {
			return ProductSemiGroup.getInstance((SemiGroup) set, arity);
		}
		if (arity == 0) {
			return new ProductSet(new Set[]{});
		}
		return new ProductSet(set, arity);
	}

}
