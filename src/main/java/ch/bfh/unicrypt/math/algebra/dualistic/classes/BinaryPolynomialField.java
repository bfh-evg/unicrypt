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
import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Represents a Polynomialfield with ZmodTwo as Primefield
 * <p>
 * @author Christian Lutz
 */
public class BinaryPolynomialField
	   extends PolynomialField<ZModTwo>
	   implements FiniteField<Polynomial<DualisticElement<ZModTwo>>> {

	private BinaryPolynomialField(PolynomialElement<ZModTwo> irreduciblePolynomial) {
		super(ZModTwo.getInstance(), irreduciblePolynomial);
	}

	/**
	 * Fermat theorem Source?!! TODO add source
	 * <p>
	 * @param element
	 * @return
	 */
	public PolynomialElement<ZModTwo> sqrtMod(PolynomialElement<ZModTwo> element) {
		BigInteger i = new BigInteger("2").pow(this.getDegree() - 1);
		return element.power(i);
	}

	/**
	 *
	 * @param s String containing 0/1 for each coefficient of binary polynomial with rightmost bit as constant term.
	 * @return
	 */
	public PolynomialElement<ZModTwo> getElementFromBitString(String s) {

		BigInteger bitString = new BigInteger(s, 2);

		// Read bits and create a BigInteger ArrayList
		ArrayList<BigInteger> arrayBigInteger = new ArrayList<BigInteger>();
		for (Character c : bitString.toString(2).toCharArray()) {
			arrayBigInteger.add(0, new BigInteger(c.toString()));

		}

		// Convert ArrayList BigInteger array and get element
		BigInteger[] coeffs = {};
		coeffs = arrayBigInteger.toArray(coeffs);

		return this.getElement(coeffs);
	}

	//
	// STATIC FACTORY METHODS
	//
	public static BinaryPolynomialField getInstance(int degree) {
		return getInstance(degree,
						   HybridRandomByteSequence.getInstance());
	}

	public static BinaryPolynomialField getInstance(int degree, RandomByteSequence randomByteSequence) {
		if (degree < 1) {
			throw new IllegalArgumentException();
		}
		PrimeField primeField = ZModTwo.getInstance();
		PolynomialRing<ZModTwo> ring = PolynomialRing.getInstance(primeField);
		PolynomialElement<ZModTwo> irreduciblePolynomial = ring
			   .findIrreduciblePolynomial(degree, randomByteSequence);
		return getInstance(irreduciblePolynomial);
	}

	public static BinaryPolynomialField getInstance(PolynomialElement<ZModTwo> irreduciblePolynomial) {
		PrimeField primeField = ZModTwo.getInstance();
		if (irreduciblePolynomial == null
			   || !irreduciblePolynomial.getSet().getSemiRing()
			   .isEquivalent(primeField)
			   || !irreduciblePolynomial.isIrreducible()) {
			throw new IllegalArgumentException();
		}

		return new BinaryPolynomialField(irreduciblePolynomial);
	}

}
