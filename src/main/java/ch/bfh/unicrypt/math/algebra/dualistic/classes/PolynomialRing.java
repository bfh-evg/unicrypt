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
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rolfhaenni
 * @param <V>
 */
public class PolynomialRing<V>
	   extends PolynomialSemiRing<V>
	   implements Ring<Polynomial<DualisticElement<V>>> {

	protected PolynomialRing(Ring ring) {
		super(ring);
	}

	public Ring<V> getRing() {
		return (Ring<V>) super.getSemiRing();
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	public PolynomialElement<V> invert(Element element) {
		if (this.isBinray()) {
			return (PolynomialElement<V>) element;
		}

		Map<Integer, DualisticElement<V>> coefficientMap = new HashMap<Integer, DualisticElement<V>>();
		Polynomial<DualisticElement<V>> polynomial = ((PolynomialElement<V>) element).getValue();
		for (Integer i : polynomial.getIndices()) {
			coefficientMap.put(i, polynomial.getCoefficient(i).negate());
		}
		return this.getElement(coefficientMap);
	}

	@Override
	public PolynomialElement<V> applyInverse(Element element1, Element element2) {
		return this.apply(element1, this.invert(element2));
	}

	@Override
	public PolynomialElement<V> subtract(Element element1, Element element2) {
		return this.applyInverse(element1, element2);
	}

	@Override
	public PolynomialElement<V> negate(Element element) {
		return this.invert(element);
	}

	/**
	 * GCD.
	 * <p>
	 * See Algorithm 2.218 Euclidean algorithm for Z_p[x]
	 * <p>
	 * @param g
	 * @param h
	 * @return
	 */
	public PolynomialElement<V> euclidean(PolynomialElement<V> g, PolynomialElement<V> h) {
		return null;
	}

	/**
	 * GCD. d(x) = gcd(g(x),h(x)) and d(x) = s(x)g(x) + t(x)h(x)
	 * <p>
	 * Z_p must be a field, so p must be prime.
	 * <p>
	 * See Algorithm 2.221 Extended Euclidean algorithm for Z_p[x]
	 * <p>
	 * @param g g(x) in Z_p[x]
	 * @param h h(x) in Z_p[x]
	 * @return (d(x), s(x), t(x)) with d(x), s(x), r(x) in Z_p[x]
	 */
	public Triple extendedEuclidean(PolynomialElement<V> g, PolynomialElement<V> h) {
		if (!this.getSemiRing().isField()) {
			throw new UnsupportedOperationException();
		}
		if (!this.contains(g) || !this.contains(h)) {
			throw new IllegalArgumentException();
		}
		final PolynomialRing<V> ring = PolynomialRing.getInstance((Ring<V>) this.getSemiRing());
		final PolynomialElement<V> zero = ring.getZeroElement();
		final PolynomialElement<V> one = ring.getOneElement();

		// 1.
		if (h.isEquivalent(zero)) {
			return Triple.getInstance(ring.getElement(g.getValue()), one, zero);
		}
		// 2.
		PolynomialElement<V> s2 = one;
		PolynomialElement<V> s1 = zero;
		PolynomialElement<V> t2 = zero;
		PolynomialElement<V> t1 = one;
		PolynomialElement<V> q, r, s, t;
		// 3.
		while (!h.isEquivalent(zero)) {

			// 3.1
			Pair div = longDivision(g, h);
			q = (PolynomialElement<V>) div.getFirst();
			r = (PolynomialElement<V>) div.getSecond();
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
		return Triple.getInstance(g, s2, t2);
	}

	/**
	 * Polynomial long devision. g(x) = h(x)q(x) + r(x)
	 * <p>
	 * Z_p must be a field, so p must be prime.
	 * <p>
	 * @param g g(x)in Z_p[x]
	 * @param h h(x)in Z_p[x]
	 * @return (q(x), r(x)) with q(x), r(x) in Z_p[x]
	 */
	public Pair longDivision(PolynomialElement<V> g, PolynomialElement<V> h) {
		if (!this.getSemiRing().isField()) {
			throw new UnsupportedOperationException();
		}
		if (!this.contains(g) || !this.contains(h) || h.isEquivalent(this.getZeroElement())) {
			throw new IllegalArgumentException();
		}

		// Don't use 'this' as 'this' might be a PolynomialField
		// -> getElement() in t = ring.getElement(map) would result in an endless loop
		final PolynomialRing<V> ring = PolynomialRing.getInstance((Ring<V>) this.getSemiRing());
		final PolynomialElement<V> zero = ring.getZeroElement();

		PolynomialElement<V> q = zero;
		PolynomialElement<V> r = ring.getElement(g.getValue());
		PolynomialElement<V> t;
		while (!r.isEquivalent(zero) && r.getValue().getDegree() >= h.getValue().getDegree()) {
			DualisticElement<V> c = r.getValue().getCoefficient(r.getValue().getDegree()).divide(h.getValue().getCoefficient(h.getValue().getDegree()));
			int i = r.getValue().getDegree() - h.getValue().getDegree();
			HashMap map = new HashMap(1);
			map.put(i, c);
			t = ring.getElement(map);
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
	public boolean isIrreduciblePolynomial(PolynomialElement<V> f) {
		if (!this.getSemiRing().isField() || f != null && !f.getValue().isMonic()) {
			throw new UnsupportedOperationException();
		}
		if (!this.contains(f)) {
			throw new IllegalArgumentException();
		}
		final PolynomialRing<V> ring = PolynomialRing.getInstance((Ring<V>) this.getSemiRing());
		PolynomialElement<V> x = ring.getElement(BigInteger.ZERO, BigInteger.ONE);
		PolynomialElement<V> u = x;
		PolynomialElement<V> d;
		int m = f.getValue().getDegree();
		BigInteger p = this.getSemiRing().getOrder();
		for (int i = 1; i < m / 2; i++) {
			u = squareAndMultiply(u, p, f);
			d = euclidean(f, u.subtract(x));
			if (!d.isEquivalent(ring.getOneElement())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * See Algorithm 2.227 Repeated square-and-multiply algorithm for exponentiation
	 * <p>
	 * @param g
	 * @param k
	 * @param f
	 * @return
	 */
	private PolynomialElement<V> squareAndMultiply(PolynomialElement<V> g, BigInteger k, PolynomialElement<V> f) {
		return null;
	}

	//
	// STATIC FACTORY METHODS
	//
	public static PolynomialRing getInstance(Ring ring) {
		if (ring == null) {
			throw new IllegalArgumentException();
		}
		return new PolynomialRing(ring);
	}

}
