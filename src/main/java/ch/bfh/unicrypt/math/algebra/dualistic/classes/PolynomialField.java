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

import ch.bfh.unicrypt.helper.Polynomial;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PrimeField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 * @param <V>
 */
public class PolynomialField<V extends Object>
	   extends PolynomialRing<V>
	   implements FiniteField<Polynomial<DualisticElement<V>>> {

	private PolynomialElement<V> irreduciblePolynomial;
	private int degree;

	protected PolynomialField(PrimeField primeField) {
		super(primeField);
	}

	public PrimeField<V> getPrimeField() {
		return (PrimeField<V>) super.getRing();
	}

	public int getDegree() {
		return this.degree;
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	public BigInteger getCharacteristic() {
		return this.getPrimeField().getOrder();
	}

	@Override
	public MultiplicativeGroup<Polynomial<DualisticElement<V>>> getMultiplicativeGroup() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public PolynomialElement<V> divide(Element element1, Element element2) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public PolynomialElement<V> oneOver(Element element) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	//
	// STATIC FACTORY METHODS
	//
	public static <V extends Object> PolynomialField getInstance(PrimeField primeField, PolynomialElement<V> irreduciblePolynomialElement) {
		if (primeField == null || irreduciblePolynomialElement == null || !irreduciblePolynomialElement.getSet().getSemiRing().isEquivalent(primeField)) {
			throw new IllegalArgumentException();
		}
		return new PolynomialField(primeField);
	}

}
