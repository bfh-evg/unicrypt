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

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractDualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rolfhaenni
 */
public class PolynomialElement
			 extends AbstractDualisticElement<PolynomialSemiRing, PolynomialElement> {

	private Map<Integer, DualisticElement> coefficients;
	private int degree;

	protected PolynomialElement(final PolynomialSemiRing semiRing, final Map<Integer, DualisticElement> coefficients) {
		super(semiRing);
		this.coefficients = coefficients;
		this.degree = 0;
		for (Integer i : this.getIndices()) {
			this.degree = Math.max(this.degree, i);
		}
	}

	public final Set<Integer> getIndices() {
		return this.coefficients.keySet();
	}

	public DualisticElement getCoefficient(int index) {
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		DualisticElement coefficient = this.coefficients.get(index);
		if (coefficient == null) {
			return this.getSet().getSemiRing().getZeroElement();
		}
		return coefficient;
	}

	public DualisticElement evaluate(DualisticElement element) {
		if (element == null || !this.getSet().getSemiRing().contains(element)) {
			throw new IllegalArgumentException();
		}
		DualisticElement result = this.getSet().getSemiRing().getZeroElement();
		for (Integer index : this.getIndices()) {
			result = result.add(this.getCoefficient(index).multiply(element.power(index)));
		}
		return result;
	}

	public Pair getPoint(DualisticElement element) {
		if (element == null || !this.getSet().getSemiRing().contains(element)) {
			throw new IllegalArgumentException();
		}
		return Pair.getInstance(element, this.evaluate(element));
	}

	public int getDegree() {
		return this.degree;
	}

	@Override
	protected BigInteger standardGetValue() {
		BigInteger values[] = new BigInteger[this.getDegree() + 1];
		for (int i = 0; i <= this.getDegree(); i++) {
			DualisticElement element = this.coefficients.get(i);
			if (element == null) {
				values[i] = this.getSet().getSemiRing().getZeroElement().getValue();
			} else {
				values[i] = element.getValue();
			}
		}
		return MathUtil.pairWithSize(values);
	}

	@Override
	protected boolean standardIsEquivalent(PolynomialElement element) {
		return this.coefficients.equals(element.coefficients);
	}

	@Override
	public String standardToStringContent() {
		String result = "f(x)=";
		if (this.getDegree() == 0) {
			return result + this.getSet().getSemiRing().getZeroElement().getValue();
		}
		String separator = "";
		for (Integer index : this.getIndices()) {
			DualisticElement coefficient = this.coefficients.get(index);
			if (!coefficient.isZero() || this.getDegree() == 0) {
				result = result + separator;
				if (!coefficient.isOne() || index == 0) {
					result = result + coefficient.getValue().toString();
				}
				if (index == 1) {
					result = result + "X";
				}
				if (index > 1) {
					result = result + "X^" + index;
				}
				separator = "+";
			}
		}
		return result;
	}

}
