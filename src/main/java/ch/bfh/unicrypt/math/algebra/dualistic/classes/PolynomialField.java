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
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import java.math.BigInteger;
import java.util.HashMap;

/**
 *
 * @author rolfhaenni
 * @param <V>
 */
public class PolynomialField<V>
	   extends PolynomialRing<V>
	   implements FiniteField<Polynomial<DualisticElement<V>>> {

	private final PolynomialElement<V> irreduciblePolynomial;

	protected PolynomialField(PrimeField primeField, PolynomialElement<V> irreduciblePolynomial) {
		super(primeField);
		this.irreduciblePolynomial = irreduciblePolynomial;
	}

	public PrimeField<V> getPrimeField() {
		return (PrimeField<V>) super.getRing();
	}

	public PolynomialElement<V> getIrreduciblePolynomial() {
		return this.irreduciblePolynomial;
	}

	public int getDegree() {
		return this.irreduciblePolynomial.getValue().getDegree();
	}

	//
	// The following protected methods override the default implementation from
	// various super-classes
	//
	@Override
	protected BigInteger abstractGetOrder() {
		// p^m
		return this.getCharacteristic().pow(this.getDegree());
	}

	@Override
	protected PolynomialElement<V> abstractGetElement(Polynomial value) {
		PolynomialElement<V> e = super.abstractGetElement(value);
		return new PolynomialElement<V>(this, this.mod(e).getValue());
	}

	@Override
	public BigInteger getCharacteristic() {
		return this.getPrimeField().getOrder();
	}

	@Override
	public MultiplicativeGroup<Polynomial<DualisticElement<V>>> getMultiplicativeGroup() {
		// TODO Create muliplicative.classes.FStar (Definition 2.228, Fact 2.229/2.230)
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected PolynomialElement<V> abstractMultiply(PolynomialElement<V> element1, PolynomialElement<V> element2) {
		PolynomialElement<V> poly = super.abstractMultiply(element1, element2);
		return this.getElement(poly.getValue());
	}

	@Override
	public PolynomialElement<V> divide(Element element1, Element element2) {
		return this.multiply(element1, this.oneOver(element2));
	}

	/**
	 *
	 * TODO Compute using extended Euclidean algorithm for polynomial (Algorithm 2.226)
	 * <p>
	 * @param element
	 * @return
	 */
	@Override
	public PolynomialElement<V> oneOver(Element element) {

		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}

		if (element.isEquivalent(this.getZeroElement())) {
			throw new UnsupportedOperationException();
		}

		Triple euclid = PolynomialField.<V>extendedEuclidean((PolynomialElement<V>) element, this.irreduciblePolynomial);
		return (PolynomialElement<V>) euclid.getSecond();

	}

	private PolynomialElement<V> mod(PolynomialElement<V> poly) {
		if (poly.getValue().getDegree() < this.getDegree()) {
			return poly;
		}
		Pair longDiv = PolynomialField.<V>longDivision(poly, this.irreduciblePolynomial);
		return (PolynomialElement<V>) longDiv.getSecond();
	}

	//
	// STATIC FACTORY METHODS
	//
	public static <V> PolynomialElement<V> findIrreduciblePolynomial(PrimeField primeField, int degree) {
		if (primeField == null || degree < 1) {
			throw new IllegalArgumentException();
		}
		// TODO Find irreducible polynomial (Fact 2.224, §4.5.1)
		return null;
	}

	public static <V> boolean isIrreduciblePolynomial(PolynomialElement<V> poly) {
		if (poly == null) {
			throw new IllegalArgumentException();
		}
		// TODO Find irreducible polynomial (Fact 2.224, §4.5.1)
		return true;
	}

	/**
	 * GCD. d(x) = gcd(g(x),h(x)) and d(x) = s(x)g(x) + t(x)h(x)
	 * <p>
	 * See Algorithm 2.221 Extended Euclidean for polynomials
	 * <p>
	 * @param <V>
	 * @param g   g(x)
	 * @param h   h(x)
	 * @return (d(x), s(x), t(x))
	 */
	public static <V> Triple extendedEuclidean(PolynomialElement<V> g, PolynomialElement<V> h) {
		if (g == null || h == null || !g.getSet().getSemiRing().isField() || !g.getSet().getSemiRing().isEquivalent(h.getSet().getSemiRing())) {
			throw new IllegalArgumentException();
		}
		final PolynomialElement<V> zero = g.getSet().getZeroElement();
		final PolynomialElement<V> one = g.getSet().getOneElement();

		// 1.
		if (h.isEquivalent(zero)) {
			return Triple.getInstance(g, one, zero);
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
			Pair div = PolynomialField.<V>longDivision(g, h);
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
	 * @param <V>
	 * @param g   g(x)
	 * @param h   h(x)
	 * @return (q(x), r(x))
	 */
	public static <V> Pair longDivision(PolynomialElement<V> g, PolynomialElement<V> h) {
		if (g == null || h == null || !g.getSet().getSemiRing().isField() || !g.getSet().getSemiRing().isEquivalent(h.getSet().getSemiRing())
			   || h.isEquivalent(h.getSet().getZeroElement())) {
			throw new IllegalArgumentException();
		}

		final PolynomialRing<V> ring = PolynomialRing.getInstance((Ring<V>) g.getSet().getSemiRing());
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

	public static <V> PolynomialField getInstance(PrimeField primeField, int degree) {
		if (primeField == null || degree < 1) {
			throw new IllegalArgumentException();
		}

		PolynomialElement<V> irreduciblePolynomialElement = PolynomialField.<V>findIrreduciblePolynomial(primeField, degree);
		return new PolynomialField(primeField, irreduciblePolynomialElement);
	}

	public static <V> PolynomialField getInstance(PrimeField primeField, PolynomialElement<V> irreduciblePolynomialElement) {
		if (primeField == null || irreduciblePolynomialElement == null || !irreduciblePolynomialElement.getSet().getSemiRing().isEquivalent(primeField)) {
			throw new IllegalArgumentException();
		}
		// TODO Check whether irreduciblePolynomial is really a irreducible polynomial!
		return new PolynomialField(primeField, irreduciblePolynomialElement);
	}

}
