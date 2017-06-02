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
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author R. Haenni
 */
public class PolynomialRing
	   extends PolynomialSemiRing
	   implements Ring<Polynomial<? extends DualisticElement<BigInteger>>> {

	private static final long serialVersionUID = 1L;

	protected PolynomialRing(Ring ring) {
		super(ring);
	}

	public Ring getRing() {
		return (Ring) super.getSemiRing();
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	public PolynomialElement invert(Element element) {
		if (this.isBinary()) {
			return (PolynomialElement) element;
		}

		Map<Integer, DualisticElement<BigInteger>> coefficientMap
			   = new HashMap<>();
		Polynomial<? extends DualisticElement<BigInteger>> polynomial = ((PolynomialElement) element).getValue();
		for (Integer i : polynomial.getCoefficientIndices()) {
			coefficientMap.put(i, polynomial.getCoefficient(i).negate());
		}
		return this.abstractGetElement(coefficientMap);
	}

	@Override
	public PolynomialElement applyInverse(Element element1, Element element2) {
		return this.apply(element1, this.invert(element2));
	}

	@Override
	public final PolynomialElement invertSelfApply(Element element, long amount) {
		return this.invertSelfApply(element, BigInteger.valueOf(amount));
	}

	@Override
	public final PolynomialElement invertSelfApply(Element element, Element<BigInteger> amount) {
		if (amount == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.invertSelfApply(element, amount.getValue());
	}

	@Override
	public final PolynomialElement invertSelfApply(Element element, BigInteger amount) {
		if (amount == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (amount.signum() == 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ARGUMENT, this, amount);
		}
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		if (!this.isFinite() || !this.hasKnownOrder()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		boolean positiveAmount = amount.signum() > 0;
		amount = MathUtil.modInv(amount.abs().mod(this.getOrder()), this.getOrder());
		PolynomialElement result = this.defaultSelfApplyAlgorithm((PolynomialElement) element, amount);
		if (positiveAmount) {
			return result;
		}
		return this.invert(result);
	}

	@Override
	public final PolynomialElement invertSelfApply(Element element) {
		return this.invertSelfApply(element, MathUtil.TWO);
	}

	@Override
	public PolynomialElement subtract(Element element1, Element element2) {
		return this.applyInverse(element1, element2);
	}

	@Override
	public PolynomialElement negate(Element element) {
		return this.invert(element);
	}

	@Override
	public final PolynomialElement divide(Element element, long amount) {
		return this.invertSelfApply(element, amount);
	}

	@Override
	public final PolynomialElement divide(Element element, BigInteger amount) {
		return this.invertSelfApply(element, amount);
	}

	@Override
	public final PolynomialElement halve(Element element) {
		return this.invertSelfApply(element);
	}

	/**
	 * GCD. d(x) = gcd(g(x),h(x)). The unique monic gcd is returned.
	 * <p>
	 * Z_p must be a field.
	 * <p>
	 * See Algorithm 2.218 Euclidean algorithm for Z_p[x]
	 * <p>
	 * @param g g(x) in Z_p[x]
	 * @param h h(x) in Z_p[x]
	 * @return d(x) in Z_p[x]
	 */
	public PolynomialElement euclidean(PolynomialElement g, PolynomialElement h) {
		if (!this.getSemiRing().isField()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		if (!this.contains(g) || !this.contains(h)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, g, h);
		}
		final PolynomialRing ring
			   = PolynomialRing.getInstance((Ring<Polynomial<? extends DualisticElement<BigInteger>>>) this.
					  getSemiRing());

		while (!h.isEquivalent(ring.getZeroElement())) {
			Pair div = longDivision(g, h);
			g = h;
			h = (PolynomialElement) div.getSecond();
		}

		// Reduce g to be monic
		if (!g.getValue().isMonic()) {
			DualisticElement<BigInteger> a = g.getValue().getCoefficient(g.getValue().getDegree());
			g = g.reduce(a);
		}
		return g;
	}

	/**
	 * GCD. d(x) = gcd(g(x),h(x)) and d(x) = s(x)g(x) + t(x)h(x). The unique monic gcd is returned.
	 * <p>
	 * Z_p must be a field, so p must be prime.
	 * <p>
	 * See Algorithm 2.221 Extended Euclidean algorithm for Z_p[x]
	 * <p>
	 * @param g g(x) in Z_p[x]
	 * @param h h(x) in Z_p[x]
	 * @return (d(x), s(x), t(x)) with d(x), s(x), r(x) in Z_p[x]
	 */
	public Triple extendedEuclidean(PolynomialElement g, PolynomialElement h) {
		if (!this.getSemiRing().isField()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		if (!this.contains(g) || !this.contains(h)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, g, h);
		}
		final PolynomialRing ring
			   = PolynomialRing.getInstance((Ring<Polynomial<? extends DualisticElement<BigInteger>>>) this.
					  getSemiRing());
		final PolynomialElement zero = ring.getZeroElement();
		final PolynomialElement one = ring.getOneElement();

		// 1.
		if (h.isEquivalent(zero)) {
			return Triple.getInstance(ring.getElement(g.getValue()), one, zero);
		}

		// 2.
		PolynomialElement s2 = one, s1 = zero, t2 = zero, t1 = one;
		PolynomialElement q, r, s, t;

		// 3.
		while (!h.isEquivalent(zero)) {
			// 3.1
			Pair div = longDivision(g, h);
			q = (PolynomialElement) div.getFirst();
			r = (PolynomialElement) div.getSecond();
			// 3.2
			s = s2.subtract(q.multiply(s1));
			t = t2.subtract(q.multiply(t1));
			// 3.3
			g = h;
			h = r;
			// 3.4
			s2 = s1;
			s1 = s;
			t2 = t1;
			t1 = t;
		}
		// 4./5.
		// Reduce gcd to be monic
		if (!g.getValue().isMonic()) {
			DualisticElement<BigInteger> a = g.getValue().getCoefficient(g.getValue().getDegree());
			g = g.reduce(a);
			s2 = s2.reduce(a);
			t2 = t2.reduce(a);
		}
		return Triple.getInstance(g, s2, t2);
	}

	/**
	 * Polynomial long division. g(x) = h(x)q(x) + r(x)
	 * <p>
	 * Z_p must be a field, so p must be prime.
	 * <p>
	 * @param g g(x)in Z_p[x]
	 * @param h h(x)in Z_p[x]
	 * @return (q(x), r(x)) with q(x), r(x) in Z_p[x]
	 */
	public Pair longDivision(PolynomialElement g, PolynomialElement h) {
		if (!this.getSemiRing().isField()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		if (!this.contains(g) || !this.contains(h) || this.isZeroElement(h)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, g, h);
		}

		// Create explicitly a ring to work in (the instance might be a field).
		final PolynomialRing ring
			   = PolynomialRing.getInstance((Ring<Polynomial<? extends DualisticElement<BigInteger>>>) this.
					  getSemiRing());
		final PolynomialElement zero = ring.getZeroElement();

		PolynomialElement q = zero;
		PolynomialElement r = ring.getElement(g.getValue());
		PolynomialElement t;
		while (!r.isEquivalent(zero) && r.getValue().getDegree() >= h.getValue().getDegree()) {
			DualisticElement<BigInteger> c
				   = r.getValue().getCoefficient(r.getValue().getDegree()).divide(h.getValue()
						  .getCoefficient(h.getValue().getDegree()));
			int i = r.getValue().getDegree() - h.getValue().getDegree();
			HashMap map = new HashMap(1);
			map.put(i, c);
			t = ring.abstractGetElement(map);
			q = t.add(q);
			r = r.subtract(t.multiply(h));
		}
		return Pair.getInstance(q, r);
	}

	/**
	 * isIrreduciblePolynomial. Tests monic polynomial f(x) in Z_p[x] for irreducibility. Z_p must be a prime field.
	 * <p>
	 * See Algorithm 4.69 Testing a polynomial for irreducibility
	 * <p>
	 * @param f f(x) in Z_p[x] and monic
	 * @return true if f(x) is irreducible over Z_p
	 */
	public boolean isIrreduciblePolynomial(PolynomialElement f) {
		if (f == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (!this.contains(f)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, f);
		}
		if (!this.getSemiRing().isField() || !f.getValue().isMonic()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		final PolynomialRing ring
			   = PolynomialRing.getInstance((Ring<Polynomial<? extends DualisticElement<BigInteger>>>) this.
					  getSemiRing());
		PolynomialElement x = ring.getElement(ring.getSemiRing().getZeroElement(), ring.getSemiRing().getOneElement());
		PolynomialElement u = x;
		PolynomialElement d;
		int m = f.getValue().getDegree();
		BigInteger p = this.getSemiRing().getOrder();
		for (int i = 1; i <= (m / 2); i++) {
			u = squareAndMultiply(u, p, f);
			d = euclidean(f, u.subtract(x));
			if (!d.isEquivalent(ring.getOneElement())) {
				return false;
			}
		}
		return true;
	}

	public PolynomialElement findIrreduciblePolynomial(int degree) {
		return this.findIrreduciblePolynomial(degree, HybridRandomByteSequence.getInstance());
	}

	// See Algorithm 4.70 Generating a random monic irreducible polynomial over Z_p, see Fact 4.75
	// TODO Generalize to getRandomElements by replacing HybridRandomByteSequence by RandomByteSequence
	public PolynomialElement findIrreduciblePolynomial(int degree, HybridRandomByteSequence randomByteSequence) {
		if (!this.getSemiRing().isField()) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (degree < 1) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_DEGREE, this, degree);
		}
		// TODO Search for irreducible trinomials if set is binary -> see Fact 4.75
		PolynomialElement f;
		do {
			f = this.getRandomMonicElement(degree, true, randomByteSequence);
		} while (!f.isIrreducible());
		return f;
	}

	// Returns g(x)^k mod f(x) where g(x), f(x) in Z_p[x].
	// See Algorithm 2.227 Repeated square-and-multiply algorithm for exponentiation
	private PolynomialElement squareAndMultiply(PolynomialElement g, BigInteger k, PolynomialElement f) {
		if (k.signum() < 0) {
			throw new UniCryptRuntimeException(ErrorCode.NEGATIVE_VALUE, k);
		}
		final PolynomialRing ring
			   = PolynomialRing.getInstance((Ring<Polynomial<? extends DualisticElement<BigInteger>>>) this.
					  getSemiRing());
		PolynomialElement s = ring.getOneElement();

		if (k.signum() == 0) {
			return s;
		}
		PolynomialElement t = ring.getElement(g.getValue());
		if (k.testBit(0)) {
			s = t;
		}
		for (int i = 1; i <= k.bitLength(); i++) {
			t = (PolynomialElement) this.longDivision(t.square(), f).getSecond();
			if (k.testBit(i)) {
				s = (PolynomialElement) this.longDivision(t.multiply(s), f).getSecond();
			}
		}
		return s;
	}

	public static PolynomialRing getInstance(Ring ring) {
		if (ring == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		return new PolynomialRing(ring);
	}

}
