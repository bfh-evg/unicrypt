/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class ProductSet
	   extends AbstractSet<Tuple>
	   implements Compound<ProductSet, Set>, Set {

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
		return this.contains(MathUtil.intToBigIntegerArray(values));
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
		return this.getElement(MathUtil.intToBigIntegerArray(values));
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
		return new Tuple(this, elements);
	}

	@Override
	protected BigInteger standardGetMinOrder() {
		if (this.isUniform()) {
			return this.getFirst().getMinOrder().pow(this.getArity());
		}
		BigInteger result = BigInteger.ONE;
		for (Set set : this) {
			result = result.multiply(set.getMinOrder());
		}
		return result;
	}

	@Override
	protected BigInteger standardGetMaxOrder() {
		if (this.isUniform()) {
			return this.getFirst().getMaxOrder().pow(this.getArity());
		}
		BigInteger result = BigInteger.ONE;
		for (Set set : this) {
			if (set.getMaxOrder().equals(Set.INFINITE_ORDER)) {
				return Set.INFINITE_ORDER;
			}
			result = result.multiply(set.getMaxOrder());
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
		for (Set set : this) {
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
	protected Tuple abstractGetRandomElement(Random random) {
		int arity = this.getArity();
		final Element[] randomElements = new Element[arity];
		for (int i = 0; i < arity; i++) {
			randomElements[i] = this.getAt(i).getRandomElement(random);
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
	public Set getAt(int index) {
		if (index < 0 || index >= this.getArity()) {
			throw new IndexOutOfBoundsException();
		}
		if (this.isUniform()) {
			return this.sets[0];
		}
		return this.sets[index];
	}

	@Override
	public Set getAt(int... indices) {
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
	public ProductSet removeAt(final int index) {
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

	@Override
	public Iterator<Set> iterator() {
		final Compound<ProductSet, Set> compoundSet = this;
		return new Iterator<Set>() {
			int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return this.currentIndex < compoundSet.getArity();
			}

			@Override
			public Set next() {
				if (this.hasNext()) {
					return compoundSet.getAt(this.currentIndex++);
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};
	}

	@Override
	protected boolean standardIsEqual(Set set) {
		ProductSet other = (ProductSet) set;
		int arity = this.getArity();
		if (arity != other.getArity()) {
			return false;
		}
		for (int i = 0; i < arity; i++) {
			if (!this.getAt(i).isEqual(other.getAt(i))) {
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
		for (Set set : this) {
			result = result + separator + set.toString();
			separator = " x ";
		}
		return result;
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is a static factory method to construct a composed set without
	 * calling respective constructors. The input sets are given as an array.
	 * <p>
	 * <p/>
	 * @param sets The array of input sets
	 * @return The corresponding product set
	 * @throws IllegalArgumentException if {@literal sets} is null or contains
	 *                                  null
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
				if (!set.isEqual(first)) {
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
