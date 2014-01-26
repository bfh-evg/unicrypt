/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.ImmutableArray;
import ch.bfh.unicrypt.math.helper.compound.Compound;
import ch.bfh.unicrypt.math.helper.compound.CompoundIterator;
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
	   extends AbstractSet<Tuple, ImmutableArray<Element>>
	   implements Compound<ProductSet, Set> {

	private final Set[] sets;
	private final int arity;
	private final Class<? extends Set> setClass; // this is needed to create arrays of the actual type

	protected ProductSet(Set[] sets) {
		super(ImmutableArray.class);
		this.sets = sets.clone();
		this.arity = sets.length;
		this.setClass = (Class<Set>) sets.getClass().getComponentType();
	}

	protected ProductSet(Set set, int arity) {
		super(ImmutableArray.class);
		this.sets = new Set[]{set};
		this.arity = arity;
		this.setClass = set.getClass();
	}

	public final boolean contains(Element... elements) {
		return this.contains(ImmutableArray.getInstance(elements));
	}

	public final Tuple getElement(final Element... elements) {
		return this.getElement(ImmutableArray.getInstance(elements));
	}

//	public final boolean contains(final int... values) {
//		return this.contains(ArrayUtil.intToBigIntegerArray(values));
//	}
	public final boolean contains(BigInteger... values) {
		if (values == null || values.length != this.arity) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < this.arity; i++) {
			if (!this.getAt(i).contains(values[i])) {
				return false;
			}
		}
		return true;
	}
//	public final Tuple getElement(final int... values) {
//		return this.getElement(ArrayUtil.intToBigIntegerArray(values));
//	}
//

	public final Tuple getElement(BigInteger[] values) {
		if (values == null || values.length != this.arity) {
			throw new IllegalArgumentException();
		}
		Element[] elements = new Element[this.arity];
		for (int i = 0; i < this.arity; i++) {
			elements[i] = this.getAt(i).getElement(values[i]);
		}
		return this.abstractGetElement(ImmutableArray.getInstance(elements));
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
				return first.getOrder().pow(this.arity);
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
	protected boolean abstractContains(BigInteger bigInteger) {
		BigInteger[] values = MathUtil.unpairAndUnfold(bigInteger, this.arity);
		return this.contains(values);
	}

	@Override
	protected boolean abstractContains(ImmutableArray<Element> value) {
		if (value == null || value.getLength() != this.arity) {
			return false;
		}
		for (int i = 0; i < this.arity; i++) {
			if (!this.getAt(i).contains(value.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Tuple abstractGetElement(BigInteger bigInteger) {
		BigInteger[] values = MathUtil.unpairAndUnfold(bigInteger, this.arity);
		return this.getElement(values);
	}

	@Override
	protected Tuple abstractGetElement(ImmutableArray<Element> value) {
		if (value.getLength() == 2) {
			return new Pair(this, value);
		}
		if (value.getLength() == 3) {
			return new Triple(this, value);
		}
		return new Tuple(this, value);
	}

	@Override
	protected Tuple abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		final Element[] randomElements = new Element[this.arity];
		for (int i = 0; i < this.arity; i++) {
			randomElements[i] = this.getAt(i).getRandomElement(randomByteSequence);
		}
		return this.abstractGetElement(ImmutableArray.getInstance(randomElements));
	}

	@Override
	public int getArity() {
		return this.arity;
	}

	@Override
	public final boolean isNull() {
		return this.arity == 0;
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
		if (index < 0 || index >= this.arity) {
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
		Set[] result = (Set[]) Array.newInstance(this.setClass, this.arity);
		for (int i = 0; i < this.arity; i++) {
			result[i] = this.getAt(i);
		}
		return result;
	}

	@Override
	public ProductSet removeAt(final int index) {
		if (index < 0 || index >= this.arity) {
			throw new IndexOutOfBoundsException();
		}
		Set[] remainingSets = new Set[this.arity - 1];
		for (int i = 0; i < this.arity - 1; i++) {
			if (i < index) {
				remainingSets[i] = this.getAt(i);
			} else {
				remainingSets[i] = this.getAt(i + 1);
			}
		}
		return ProductSet.getInstance(remainingSets);
	}

	@Override
	public ProductSet insertAt(int index, Set set) {
		if (index < 0 || index > this.arity) {
			throw new IndexOutOfBoundsException();
		}
		if (set == null) {
			throw new IllegalArgumentException();
		}
		Set[] newSets = new Set[this.arity + 1];
		for (int i = 0; i < this.arity + 1; i++) {
			if (i < index) {
				newSets[i] = this.getAt(i);
			} else if (i == index) {
				newSets[i] = set;
			} else {
				newSets[i] = this.getAt(i - 1);
			}
		}
		return ProductSet.getInstance(newSets);
	}

	@Override
	public ProductSet add(Set set) {
		return this.insertAt(this.arity, set);
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
	protected BigInteger standardGetOrderLowerBound() {
		if (this.isUniform()) {
			return this.getFirst().getOrderLowerBound().pow(this.arity);
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
			return this.getFirst().getOrderUpperBound().pow(this.arity);
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

	@Override
	protected boolean standardIsEquivalent(Set set) {
		ProductSet other = (ProductSet) set;
		if (this.arity != other.arity) {
			return false;
		}
		for (int i = 0; i < this.arity; i++) {
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
			return this.getFirst().toString() + "^" + this.arity;
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
	 * This is a static factory method to construct a composed set without calling respective constructors. The input
	 * sets are given as an array.
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
