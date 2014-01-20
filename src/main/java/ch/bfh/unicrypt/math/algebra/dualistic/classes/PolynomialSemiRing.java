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

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.polynomial.GenericPolynomial;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class PolynomialSemiRing
	   extends AbstractSemiRing<PolynomialElement, GenericPolynomial<DualisticElement>> {

	private final SemiRing semiRing;

	protected PolynomialSemiRing(SemiRing semiRing) {
		super(GenericPolynomial.class);
		this.semiRing = semiRing;
	}

	public SemiRing getSemiRing() {
		return this.semiRing;
	}

	public PolynomialElement getElement(BigInteger[] values) {
		if (values == null) {
			throw new IllegalArgumentException();
		}
		return this.getElement(ProductSet.getInstance(this.getSemiRing(), values.length).getElement(values));
	}

	public PolynomialElement getElement(DualisticElement... elements) {
		if (elements == null) {
			throw new IllegalArgumentException();
		}
		return this.getElement(Tuple.getInstance(elements));
	}

	public PolynomialElement getElement(Tuple coefficients) {
		Map<Integer, DualisticElement> coefficientMap = new HashMap<Integer, DualisticElement>();
		if (coefficients.isNull() || coefficients.getSet().isUniform() && coefficients.getSet().getFirst().isEquivalent(this.getSemiRing())) {
			for (int i = 0; i < coefficients.getArity(); i++) {
				coefficientMap.put(i, (DualisticElement) coefficients.getAt(i));
			}
		}
		return this.getElement(coefficientMap);
	}

	public PolynomialElement getRandomElement(int degree) {
		return this.getRandomElement(degree, null);
	}

	public PolynomialElement getRandomElement(int degree, RandomGenerator randomGenerator) {
		if (degree < 0) {
			throw new IllegalArgumentException();
		}
		Map<Integer, DualisticElement> coefficientMap = new HashMap<Integer, DualisticElement>();
		for (int i = 0; i <= degree; i++) {
			DualisticElement coefficient = this.getSemiRing().getRandomElement(randomGenerator);
			if (!coefficient.isZero()) {
				coefficientMap.put(i, coefficient);
			}
		}
		return getElement(coefficientMap);
	}

	//
	// The following protected methods override the standard implementation from
	// various super-classes
	//
	@Override
	public boolean standardIsEquivalent(final Set set) {
		final PolynomialSemiRing other = (PolynomialSemiRing) set;
		return this.getSemiRing().isEquivalent(other.getSemiRing());
	}

	@Override
	public String standardToStringContent() {
		return this.getSemiRing().toString();
	}

	//
	// The following protected methods override the standard implementation from
	// various super-classes
	//
	@Override
	protected boolean abstractContains(BigInteger integerValue) {
		BigInteger[] integerValues = MathUtil.unpairWithSize(integerValue);
		for (BigInteger value : integerValues) {
			if (!this.getSemiRing().contains(value)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean abstractContains(GenericPolynomial value) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected PolynomialElement abstractGetElement(BigInteger value) {
		BigInteger[] values = MathUtil.unpairWithSize(value);
		return this.getElement(values);
	}

	@Override
	protected PolynomialElement abstractGetElement(GenericPolynomial value) {
		return new PolynomialElement(this, value);
	}

	protected PolynomialElement getElement(Map<Integer, DualisticElement> coefficientMap) {
		return abstractGetElement(GenericPolynomial.getInstance(coefficientMap, this.getSemiRing().getZeroElement(), this.getSemiRing().getOneElement()));
	}

	@Override
	protected PolynomialElement abstractGetRandomElement(RandomGenerator randomGenerator) {
		throw new UnsupportedOperationException("Not possible for infinite order.");
	}

	@Override
	protected PolynomialElement abstractApply(PolynomialElement element1, PolynomialElement element2) {
		GenericPolynomial<DualisticElement> polynomial1 = element1.getValue();
		GenericPolynomial<DualisticElement> polynomial2 = element2.getValue();
		Map<Integer, DualisticElement> coefficientMap = new HashMap<Integer, DualisticElement>();
		for (Integer i : polynomial1.getIndices()) {
			coefficientMap.put(i, polynomial1.getCoefficient(i));
		}
		for (Integer i : polynomial2.getIndices()) {
			DualisticElement coefficient = coefficientMap.get(i);
			if (coefficient == null) {
				coefficientMap.put(i, polynomial2.getCoefficient(i));
			} else {
				coefficientMap.put(i, coefficient.add(polynomial2.getCoefficient(i)));
			}
		}
		return getElement(coefficientMap);
	}

	@Override
	protected PolynomialElement abstractGetIdentityElement() {
		return getElement(new HashMap<Integer, DualisticElement>());
	}

	@Override
	protected PolynomialElement abstractMultiply(PolynomialElement element1, PolynomialElement element2) {
		GenericPolynomial<DualisticElement> polynomial1 = element1.getValue();
		GenericPolynomial<DualisticElement> polynomial2 = element2.getValue();
		Map<Integer, DualisticElement> coefficientMap = new HashMap<Integer, DualisticElement>();
		for (Integer i : polynomial1.getIndices()) {
			for (Integer j : polynomial2.getIndices()) {
				Integer k = i + j;
				DualisticElement coefficient = polynomial1.getCoefficient(i).multiply(polynomial2.getCoefficient(j));
				DualisticElement newCoefficient = coefficientMap.get(k);
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
		Map<Integer, DualisticElement> coefficientMap = new HashMap<Integer, DualisticElement>();
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
