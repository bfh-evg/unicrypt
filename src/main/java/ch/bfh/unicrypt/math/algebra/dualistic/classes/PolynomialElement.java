/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractDualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import java.math.BigInteger;
import java.util.HashMap;

/**
 *
 * @author R. Haenni
 */
public class PolynomialElement
	   extends AbstractDualisticElement<PolynomialSemiRing, PolynomialElement, Polynomial<? extends DualisticElement<BigInteger>>> {

	private static final long serialVersionUID = 1L;

	protected PolynomialElement(final PolynomialSemiRing semiRing,
		   Polynomial<? extends DualisticElement<BigInteger>> polynomial) {
		super(semiRing, polynomial);
	}

	public DualisticElement<BigInteger> evaluate(DualisticElement element) {
		if (element == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (!this.getSet().getSemiRing().contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, this, element);
		}
		if (this.getSet().isBinary()) {
			SemiRing semiRing = this.getSet().getSemiRing();
			if (semiRing.getZeroElement().isEquivalent(element)) {
				return this.value.getCoefficient(0);
			} else {
				return (this.value.countCoefficients() % 2) == 0
					   ? semiRing.getZeroElement() : semiRing.getOneElement();
			}
		}
		// TBD! (n*x^2 < q*x^3 with x = log(modulus), n = order of poly and q = number of non-zero terms in poly)
		int n = this.value.getDegree();
		int q = this.value.countCoefficients();
		if (n > 0 && ((double) q / n) < 0.01) {
			DualisticElement<BigInteger> result = this.getSet().getSemiRing().getZeroElement();
			for (Integer index : this.value.getCoefficientIndices()) {
				result = result.add(this.value.getCoefficient(index).multiply(element.power(index)));
			}
			return result;
		} else {
			// Horner
			DualisticElement<BigInteger> r = this.getSet().getSemiRing().getZeroElement();
			for (int i = this.value.getDegree(); i >= 0; i--) {
				r = r.add(this.value.getCoefficient(i));
				if (i > 0) {
					r = r.multiply(element);
				}
			}
			return r;
		}
	}

	public Pair getPoint(DualisticElement element) {
		if (element == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (!this.getSet().getSemiRing().contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, this, element);
		}
		return Pair.getInstance(element, this.evaluate(element));
	}

	public boolean isIrreducible() {
		if (!this.getSet().isRing()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		return ((PolynomialRing) this.getSet()).isIrreduciblePolynomial(this);
	}

	public PolynomialElement reduce(DualisticElement<BigInteger> element) {
		if (!this.getSet().getSemiRing().isField()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		if (element == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (!this.getSet().getSemiRing().contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, this, element);
		}
		HashMap map = new HashMap();
		for (int i = 0; i <= this.value.getDegree(); i++) {
			DualisticElement<BigInteger> c = this.value.getCoefficient(i);
			if (!c.isZero()) {
				map.put(i, c.divide(element));
			}
		}
		return this.getSet().abstractGetElement(map);
	}

}
