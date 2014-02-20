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
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.helper.polynomial.GenericPolynomial;
import ch.bfh.unicrypt.math.MathUtil;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 * @param <V>
 */
public class PolynomialSemiRing<V extends Object>
	   extends AbstractSemiRing<PolynomialElement<V>, GenericPolynomial<DualisticElement<V>>> {

	private final SemiRing semiRing;

	protected PolynomialSemiRing(SemiRing semiRing) {
		super(GenericPolynomial.class);
		this.semiRing = semiRing;
	}

	public SemiRing getSemiRing() {
		return this.semiRing;
	}

	public PolynomialElement<V> getElement(BigInteger... values) {
		if (values == null) {
			throw new IllegalArgumentException();
		}
		return this.getElement(ProductSet.getInstance(this.getSemiRing(), values.length).getElementFrom(values));
	}

	public PolynomialElement<V> getElement(DualisticElement... elements) {
		if (elements == null) {
			throw new IllegalArgumentException();
		}
		return this.getElement(Tuple.getInstance(elements));
	}

	public PolynomialElement<V> getElement(Tuple coefficients) {
		Map<Integer, DualisticElement<V>> coefficientMap = new HashMap<Integer, DualisticElement<V>>();
		if (coefficients.isEmpty() || coefficients.getSet().isUniform() && coefficients.getSet().getFirst().isEquivalent(this.getSemiRing())) {
			for (int i = 0; i < coefficients.getArity(); i++) {
				coefficientMap.put(i, (DualisticElement<V>) coefficients.getAt(i));
			}
		}
		return this.getElement(coefficientMap);
	}

	public PolynomialElement<V> getRandomElement(int degree) {
		return this.getRandomElement(degree, HybridRandomByteSequence.getInstance());
	}

	public PolynomialElement<V> getRandomElement(int degree, RandomByteSequence randomByteSequence) {
		if (degree < 0 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		Map<Integer, DualisticElement<V>> coefficientMap = new HashMap<Integer, DualisticElement<V>>();
		for (int i = 0; i <= degree; i++) {
			DualisticElement<V> coefficient = this.getSemiRing().getRandomElement(randomByteSequence);
			if (!coefficient.isZero()) {
				coefficientMap.put(i, coefficient);
			}
		}
		return getElement(coefficientMap);
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	public boolean abstractEquals(final Set set) {
		final PolynomialSemiRing other = (PolynomialSemiRing) set;
		return this.getSemiRing().isEquivalent(other.getSemiRing());
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.getSemiRing().hashCode();
		return hash;
	}

	@Override
	public String defaultToStringValue() {
		return this.getSemiRing().toString();
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	protected boolean abstractContains(GenericPolynomial value) {
		// TODO
		return true;
	}

	@Override
	protected PolynomialElement<V> abstractGetElement(GenericPolynomial value) {
		return new PolynomialElement<V>(this, value);
	}

	protected PolynomialElement<V> getElement(Map<Integer, DualisticElement<V>> coefficientMap) {
		return abstractGetElement(GenericPolynomial.getInstance(coefficientMap, this.getSemiRing().getZeroElement(), this.getSemiRing().getOneElement()));
	}

	@Override
	protected PolynomialElement<V> abstractGetElementFrom(BigInteger integerValue) {
		BigInteger[] bigIntegers = MathUtil.unpairWithSize(integerValue);
		DualisticElement[] elements = new DualisticElement[bigIntegers.length];
		int i = 0;
		for (BigInteger bigInteger : bigIntegers) {
			DualisticElement<V> element = this.getSemiRing().getElementFrom(bigInteger);
			if (element == null) {
				return null; // no such elements
			}
			elements[i] = element;
			i++;
		}
		GenericPolynomial<DualisticElement<V>> polynomial = GenericPolynomial.<DualisticElement<V>>getInstance(elements, this.getSemiRing().getZeroElement(), this.getSemiRing().getOneElement());
		return new PolynomialElement<V>(this, polynomial);
	}

	@Override
	protected BigInteger abstractGetBigIntegerFrom(GenericPolynomial<DualisticElement<V>> value) {
		int degree = value.getDegree();
		BigInteger[] values = new BigInteger[degree + 1];
		for (int i = 0; i <= degree; i++) {
			values[i] = value.getCoefficient(i).getBigInteger();
		}
		return MathUtil.pairWithSize(values);
	}

	@Override
	protected PolynomialElement abstractGetElementFrom(ByteTree byteTree) {
		// TODO: See below
		throw new UnsupportedOperationException();
	}

	@Override
	protected ByteTree abstractGetByteTreeFrom(GenericPolynomial<DualisticElement<V>> value) {
		// TODO: this is not optimal (maybe it can be generalized to ImmutableArray
		int degree = value.getDegree();
		ByteTree[] byteTrees = new ByteTree[degree + 1];
		for (int i = 0; i <= degree; i++) {
			byteTrees[i] = value.getCoefficient(i).getByteTree();
		}
		return ByteTree.getInstance(byteTrees);
	}

	@Override
	protected PolynomialElement<V> abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		throw new UnsupportedOperationException("Not possible for infinite order.");
	}

	@Override
	protected PolynomialElement<V> abstractApply(PolynomialElement<V> element1, PolynomialElement<V> element2) {
		GenericPolynomial<DualisticElement<V>> polynomial1 = element1.getValue();
		GenericPolynomial<DualisticElement<V>> polynomial2 = element2.getValue();
		Map<Integer, DualisticElement<V>> coefficientMap = new HashMap<Integer, DualisticElement<V>>();
		for (Integer i : polynomial1.getIndices()) {
			coefficientMap.put(i, polynomial1.getCoefficient(i));
		}
		for (Integer i : polynomial2.getIndices()) {
			DualisticElement<V> coefficient = coefficientMap.get(i);
			if (coefficient == null) {
				coefficientMap.put(i, polynomial2.getCoefficient(i));
			} else {
				coefficientMap.put(i, coefficient.add(polynomial2.getCoefficient(i)));
			}
		}
		return getElement(coefficientMap);
	}

	@Override
	protected PolynomialElement<V> abstractGetIdentityElement() {
		return getElement(new HashMap<Integer, DualisticElement<V>>());
	}

	@Override
	protected PolynomialElement<V> abstractMultiply(PolynomialElement<V> element1, PolynomialElement<V> element2) {
		GenericPolynomial<DualisticElement<V>> polynomial1 = element1.getValue();
		GenericPolynomial<DualisticElement<V>> polynomial2 = element2.getValue();
		Map<Integer, DualisticElement<V>> coefficientMap = new HashMap<Integer, DualisticElement<V>>();
		for (Integer i : polynomial1.getIndices()) {
			for (Integer j : polynomial2.getIndices()) {
				Integer k = i + j;
				DualisticElement<V> coefficient = polynomial1.getCoefficient(i).multiply(polynomial2.getCoefficient(j));
				DualisticElement<V> newCoefficient = coefficientMap.get(k);
				if (newCoefficient == null) {
					coefficientMap.put(k, coefficient);
				} else {
					coefficientMap.put(k, newCoefficient.add(coefficient));
				}
			}
		}
		return getElement(coefficientMap);
	}

	@Override
	protected PolynomialElement abstractGetOne() {
		Map<Integer, DualisticElement<V>> coefficientMap = new HashMap<Integer, DualisticElement<V>>();
		coefficientMap.put(0, this.getSemiRing().getOneElement());
		return getElement(coefficientMap);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE_ORDER;
	}

	//
	// STATIC FACTORY METHODS
	//
	public static PolynomialSemiRing getInstance(SemiRing semiRing) {
		if (semiRing == null) {
			throw new IllegalArgumentException();
		}
		return new PolynomialSemiRing(semiRing);
	}

}
