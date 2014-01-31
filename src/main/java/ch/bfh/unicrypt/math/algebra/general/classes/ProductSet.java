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
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.ImmutableArray;
import ch.bfh.unicrypt.math.helper.compound.Compound;
import ch.bfh.unicrypt.math.helper.compound.RecursiveCompound;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class ProductSet
	   extends AbstractSet<Tuple, ImmutableArray<Element>>
	   implements RecursiveCompound<ProductSet, Set> {

	private final ImmutableArray<Set> sets;

	protected ProductSet(ImmutableArray<Set> sets) {
		super(ImmutableArray.class);
		this.sets = sets;
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
		if (values == null || values.length != this.getArity()) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < this.getArity(); i++) {
			if (!this.getAt(i).contains(values[i])) {
				return false;
			}
		}
		return true;
	}
//	public final Tuple getElementFrom(final int... values) {
//		return this.getElementFrom(ArrayUtil.intToBigIntegerArray(values));
//	}
//

	public final Tuple getElement(BigInteger[] values) {
		if (values == null || values.length != this.getArity()) {
			throw new IllegalArgumentException();
		}
		Element[] elements = new Element[this.getArity()];
		for (int i = 0; i < this.getArity(); i++) {
			elements[i] = this.getAt(i).getElementFrom(values[i]);
		}
		return this.abstractGetElement(ImmutableArray.getInstance(elements));
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected BigInteger abstractGetOrder() {
		if (this.isEmpty()) {
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
		for (Set set : this.sets) {
			if (!set.isFinite()) {
				return Set.INFINITE_ORDER;
			}
			if (!set.hasKnownOrder() || result.equals(Set.UNKNOWN_ORDER)) {
				result = Set.UNKNOWN_ORDER;
			} else {
				result = result.multiply(set.getOrder());
			}
		}
		return result;
	}

	@Override
	protected boolean abstractContains(BigInteger bigInteger) {
		BigInteger[] values = MathUtil.unpairAndUnfold(bigInteger, this.getArity());
		return this.contains(values);
	}

	@Override
	protected boolean abstractContains(ImmutableArray<Element> value) {
		if (value == null || value.getLength() != this.getArity()) {
			return false;
		}
		for (int i = 0; i < this.getArity(); i++) {
			if (!this.getAt(i).contains(value.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Tuple abstractGetElement(BigInteger bigInteger) {
		BigInteger[] values = MathUtil.unpairAndUnfold(bigInteger, this.getArity());
		return this.getElement(values);
	}

	@Override
	protected Tuple abstractGetElement(ImmutableArray<Element> value) {
		if (this.getArity() == 2) {
			return new Pair(this, value);
		}
		if (this.getArity() == 3) {
			return new Triple(this, value);
		}
		return new Tuple(this, value);
	}

	@Override
	protected Tuple abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		final Element[] randomElements = new Element[this.getArity()];
		for (int i = 0; i < this.getArity(); i++) {
			randomElements[i] = this.getAt(i).getRandomElement(randomByteSequence);
		}
		return this.abstractGetElement(ImmutableArray.getInstance(randomElements));
	}

	@Override
	protected boolean abstractEquals(Set set) {
		ProductSet other = (ProductSet) set;
		if (this.getArity() != other.getArity()) {
			return false;
		}
		for (int i = 0; i < this.getArity(); i++) {
			if (!this.getAt(i).equals(other.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.getArity();
		for (int i = 0; i < this.getArity(); i++) {
			hash = 47 * hash + this.getAt(i).hashCode();
		}
		return hash;
	}

	@Override
	protected boolean standardIsEquivalent(Set set) {
		ProductSet other = (ProductSet) set;
		if (this.getArity() != other.getArity()) {
			return false;
		}
		for (int i = 0; i < this.getArity(); i++) {
			if (!this.getAt(i).isEquivalent(other.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getArity() {
		return this.sets.getLength();
	}

	@Override
	public final boolean isEmpty() {
		return this.sets.isEmpty();
	}

	@Override
	public final boolean isUniform() {
		return this.sets.isUniform();
	}

	@Override
	public Set getFirst() {
		return this.sets.getFirst();
	}

	@Override
	public Set getLast() {
		return this.sets.getLast();
	}

	@Override
	public Set getAt(int index) {
		return this.sets.getAt(index);
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
		return this.sets.getAll();
	}

	@Override
	public ProductSet removeAt(final int index) {
		return ProductSet.getInstance(this.sets.removeAt(index));
	}

	@Override
	public ProductSet insertAt(int index, Set set) {
		return ProductSet.getInstance(this.sets.insertAt(index, set));
	}

	@Override
	public ProductSet replaceAt(int index, Set set) {
		return ProductSet.getInstance(this.sets.replaceAt(index, set));
	}

	@Override
	public ProductSet add(Set set) {
		return this.insertAt(this.getArity(), set);
	}

	@Override
	public ProductSet append(Compound<ProductSet, Set> compound) {
		if (compound instanceof ProductSet) {
			ProductSet other = (ProductSet) compound;
			return ProductSet.getInstance(this.sets.append(other.sets));
		}
		throw new IllegalArgumentException();
	}

	@Override
	protected BigInteger standardGetOrderLowerBound() {
		if (this.isUniform()) {
			return this.getFirst().getOrderLowerBound().pow(this.getArity());
		}
		BigInteger result = BigInteger.ONE;
		for (Set set : this.sets) {
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
		for (Set set : this.sets) {
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
		for (Set set : this.sets) {
			if (result == null) {
				result = set.getMinimalOrder();
			} else {
				result = result.min(set.getMinimalOrder());
			}
		}
		return result;
	}

	@Override
	protected String standardToStringContent() {
		if (this.isEmpty()) {
			return "";
		}
		if (this.isUniform()) {
			return this.getFirst().toString() + "^" + this.getArity();
		}
		String result = "";
		String separator = "";
		for (Set set : this.sets) {
			result = result + separator + set.toString();
			separator = " x ";
		}
		return result;
	}

	//
	// STATIC FACTORY METHODS
	//
	public static ProductSet getInstance(ImmutableArray<Set> sets) {
		if (sets == null) {
			throw new IllegalArgumentException();
		}
		boolean isSemiGroup = true;
		boolean isMonoid = true;
		boolean isGroup = true;
		boolean isCyclic = true;
		for (final Set set : sets) {
			isSemiGroup = isSemiGroup && set.isSemiGroup();
			isMonoid = isMonoid && set.isMonoid();
			isGroup = isGroup && set.isGroup();
			isCyclic = isCyclic && set.isCyclic();
		}
		if (isCyclic) {
			return new ProductCyclicGroup(sets);
		}
		if (isGroup) {
			return new ProductGroup(sets);
		}
		if (isMonoid) {
			return new ProductMonoid(sets);
		}
		if (isSemiGroup) {
			return new ProductSemiGroup(sets);
		}
		return new ProductSet(sets);
	}

	public static ProductSet getInstance(final Set... sets) {
		return ProductSet.getInstance(ImmutableArray.<Set>getInstance(sets));
	}

	public static ProductSemiGroup getInstance(final SemiGroup... semiGroups) {
		return (ProductSemiGroup) ProductSet.getInstance(ImmutableArray.<Set>getInstance(semiGroups));
	}

	public static ProductMonoid getInstance(final Monoid... monoids) {
		return (ProductMonoid) ProductSet.getInstance(ImmutableArray.<Set>getInstance(monoids));
	}

	public static ProductGroup getInstance(final Group... groups) {
		return (ProductGroup) ProductSet.getInstance(ImmutableArray.<Set>getInstance(groups));
	}

	public static ProductCyclicGroup getInstance(final CyclicGroup... cyclicGroups) {
		return (ProductCyclicGroup) ProductSet.getInstance(ImmutableArray.<Set>getInstance(cyclicGroups));
	}

	public static ProductSet getInstance(final Set set, int arity) {
		return ProductSet.getInstance(ImmutableArray.<Set>getInstance(set, arity));
	}

	public static ProductSemiGroup getInstance(final SemiGroup semiGroup, int arity) {
		return (ProductSemiGroup) ProductSet.getInstance(ImmutableArray.<Set>getInstance(semiGroup, arity));
	}

	public static ProductMonoid getInstance(final Monoid monoid, int arity) {
		return (ProductMonoid) ProductSet.getInstance(ImmutableArray.<Set>getInstance(monoid, arity));
	}

	public static ProductGroup getInstance(final Group group, int arity) {
		return (ProductGroup) ProductSet.getInstance(ImmutableArray.<Set>getInstance(group, arity));
	}

	public static ProductCyclicGroup getInstance(final CyclicGroup cyclicGroup, int arity) {
		return (ProductCyclicGroup) ProductSet.getInstance(ImmutableArray.<Set>getInstance(cyclicGroup, arity));
	}

}
