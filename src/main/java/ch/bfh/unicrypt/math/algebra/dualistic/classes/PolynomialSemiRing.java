/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
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
			 extends AbstractSemiRing<PolynomialElement> {

	private final SemiRing semiRing;

	protected PolynomialSemiRing(SemiRing semiRing) {
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
		return this.abstractGetElement(coefficientMap);
	}

	public PolynomialElement getRandomElement(int degree) {
		return this.getRandomElement(degree, null);
	}

	public PolynomialElement getRandomElement(int degree, RandomGenerator randomGenerator) {
		if (degree < 0) {
			throw new IllegalArgumentException();
		}
		Map<Integer, DualisticElement> coefficients = new HashMap<Integer, DualisticElement>();
		for (int i = 0; i <= degree; i++) {
			DualisticElement coefficient = this.getSemiRing().getRandomElement(randomGenerator);
			if (!coefficient.isZero()) {
				coefficients.put(i, coefficient);
			}
		}
		return abstractGetElement(coefficients);
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
	protected PolynomialElement abstractApply(PolynomialElement element1, PolynomialElement element2) {
		Map<Integer, DualisticElement> newCoefficients = new HashMap<Integer, DualisticElement>();
		for (Integer i : element1.getIndices()) {
			newCoefficients.put(i, element1.getCoefficient(i));
		}
		for (Integer i : element2.getIndices()) {
			DualisticElement coefficient = newCoefficients.get(i);
			if (coefficient == null) {
				newCoefficients.put(i, element1.getCoefficient(i));
			} else {
				newCoefficients.put(i, coefficient.add(element2.getCoefficient(i)));
			}
		}
		return abstractGetElement(newCoefficients);
	}

	@Override
	protected PolynomialElement abstractGetIdentityElement() {
		return abstractGetElement(new HashMap<Integer, DualisticElement>());
	}

	@Override
	protected PolynomialElement abstractMultiply(PolynomialElement element1, PolynomialElement element2) {
		Map<Integer, DualisticElement> newCoefficients = new HashMap<Integer, DualisticElement>();
		for (Integer i : element1.getIndices()) {
			for (Integer j : element2.getIndices()) {
				Integer k = i + j;
				DualisticElement coefficient = element1.getCoefficient(i).multiply(element2.getCoefficient(j));
				DualisticElement newCoefficient = newCoefficients.get(k);
				if (newCoefficient == null) {
					newCoefficients.put(k, coefficient);
				} else {
					newCoefficients.put(k, newCoefficient.add(coefficient));
				}
			}
		}
		return abstractGetElement(newCoefficients);
	}

	@Override
	protected PolynomialElement abstractGetOne() {
		Map<Integer, DualisticElement> coefficients = new HashMap<Integer, DualisticElement>();
		coefficients.put(0, this.getSemiRing().getOneElement());
		return abstractGetElement(coefficients);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Set.INFINITE_ORDER;
	}

	PolynomialElement abstractGetElement(Map<Integer, DualisticElement> coefficients) {
		return new PolynomialElement(this, coefficients);
	}

	@Override
	protected PolynomialElement abstractGetElement(BigInteger value) {
		BigInteger[] values = MathUtil.unpairWithSize(value);
		return this.getElement(values);
	}

	@Override
	protected PolynomialElement abstractGetRandomElement(RandomGenerator randomGenerator) {
		throw new UnsupportedOperationException("Not possible for infinite order.");
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		BigInteger[] values = MathUtil.unpairWithSize(value);
		for (BigInteger val : values) {
			if (!this.getSemiRing().contains(val)) {
				return false;
			}
		}
		return true;
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
