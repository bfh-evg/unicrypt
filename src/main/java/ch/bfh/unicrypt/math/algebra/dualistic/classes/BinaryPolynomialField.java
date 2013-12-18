/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractFiniteField;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class BinaryPolynomialField
			 extends AbstractFiniteField<BinaryPolynomialElement, MultiplicativeGroup> {

	private PolynomialElement irreduciblePolynomial;

	protected BinaryPolynomialField(PolynomialElement irreduciblePolynomial) {
		this.irreduciblePolynomial = irreduciblePolynomial;
	}

	public ZModTwo getPrimeField() {
		return ZModTwo.getInstance();
	}

	public int getDegree() {
		return this.irreduciblePolynomial.getDegree() - 1;
	}

	//
	// The following protected methods override the standard implementation from
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
	protected BinaryPolynomialElement abstractGetElement(BigInteger value) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected BinaryPolynomialElement abstractGetRandomElement(RandomGenerator randomGenerator) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	//
	// STATIC FACTORY METHODS
	//
	public static BinaryPolynomialField getInstance(PolynomialElement irreduciblePolynomial) {
		if (irreduciblePolynomial == null || !irreduciblePolynomial.getSet().getSemiRing().isEquivalent(ZModTwo.getInstance())) {
			throw new IllegalArgumentException();
		}
		return new BinaryPolynomialField(irreduciblePolynomial);
	}

}
