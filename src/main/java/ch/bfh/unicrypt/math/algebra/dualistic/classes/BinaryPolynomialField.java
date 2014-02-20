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

import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractFiniteField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.helper.array.ByteArray;
import ch.bfh.unicrypt.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.helper.bytetree.ByteTreeLeaf;
import ch.bfh.unicrypt.helper.polynomial.BinaryPolynomial;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class BinaryPolynomialField
	   extends AbstractFiniteField<BinaryPolynomialElement, MultiplicativeGroup, BinaryPolynomial> {

	private final BinaryPolynomialElement irreduciblePolynomialElement;

	protected BinaryPolynomialField(BinaryPolynomialElement irreduciblePolynomial) {
		super(BinaryPolynomial.class);
		this.irreduciblePolynomialElement = irreduciblePolynomial;
	}

	public ZModTwo getPrimeField() {
		return ZModTwo.getInstance();
	}

	public int getDegree() {
		return this.irreduciblePolynomialElement.getValue().getDegree() - 1;
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	public MultiplicativeGroup getMultiplicativeGroup() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected BigInteger abstractGetCharacteristic() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BinaryPolynomialElement abstractOneOver(BinaryPolynomialElement element) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected MultiplicativeGroup abstractGetMultiplicativeGroup() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BinaryPolynomialElement abstractInvert(BinaryPolynomialElement element) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BinaryPolynomialElement abstractMultiply(BinaryPolynomialElement element1, BinaryPolynomialElement element2) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BinaryPolynomialElement abstractGetOne() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BinaryPolynomialElement abstractGetIdentityElement() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BinaryPolynomialElement abstractApply(BinaryPolynomialElement element1, BinaryPolynomialElement element2) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BigInteger abstractGetOrder() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected boolean abstractContains(BinaryPolynomial value) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected BinaryPolynomialElement abstractGetElement(BinaryPolynomial value) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected BinaryPolynomialElement abstractGetElementFrom(BigInteger bigInteger) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BigInteger abstractGetBigIntegerFrom(BinaryPolynomial value) {
		return new BigInteger(value.getCoefficients().getAll());
	}

	@Override
	protected BinaryPolynomialElement abstractGetElementFrom(ByteTree byteTree) {
		if (byteTree.isLeaf()) {
			ByteArray byteArray = ((ByteTreeLeaf) byteTree).convertToByteArray();
			if (this.contains(byteArray)) {
				return this.abstractGetElement(BinaryPolynomial.getInstance(byteArray));
			}
		}
		// no such element
		return null;
	}

	@Override
	protected ByteTree abstractGetByteTreeFrom(BinaryPolynomial value
	) {
		return ByteTree.getInstance(value.getCoefficients());
	}

	@Override
	protected BinaryPolynomialElement abstractGetRandomElement(RandomByteSequence randomByteSequence
	) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected boolean abstractEquals(Set set
	) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected int abstractHashCode() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	//
	// STATIC FACTORY METHODS
	//
	public static BinaryPolynomialField getInstance(BinaryPolynomialElement irreduciblePolynomialElement) {
		if (irreduciblePolynomialElement == null || !irreduciblePolynomialElement.getSet().getPrimeField().isEquivalent(ZModTwo.getInstance())) {
			throw new IllegalArgumentException();
		}
		return new BinaryPolynomialField(irreduciblePolynomialElement);
	}

}
