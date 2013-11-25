/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.sharing.classes;

import ch.bfh.unicrypt.crypto.schemes.sharing.abstracts.AbstractThresholdSecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ShamirSecretSharingScheme
			 extends AbstractThresholdSecretSharingScheme<ZModPrime, ZModElement, ProductGroup, Tuple> {

	private final ZModPrime zModPrime;
	private final PolynomialRing polynomialRing;

	protected ShamirSecretSharingScheme(ZModPrime zModPrime, int size, int threshold) {
		super(size, threshold);
		this.zModPrime = zModPrime;
		this.polynomialRing = PolynomialRing.getInstance(zModPrime);
	}

	public ZModPrime getZModPrime() {
		return this.zModPrime;
	}

	public PolynomialRing getPolynomialRing() {
		return this.polynomialRing;
	}

	@Override
	protected ZModPrime abstractGetMessageSpace() {
		return this.zModPrime;
	}

	@Override
	protected ProductGroup abstractGetShareSpace() {
		//return ProductCyclicGroup.getInstance(this.zModPrime, ZMod.getInstance(this.getSize()));
		return ProductGroup.getInstance(this.zModPrime, 2);
	}

	@Override
	protected Tuple[] abstractShare(Element message, Random random) {

		// create an array of coefficients with size threshold
		// the coefficient of degree 0 is fixed (message)
		// all other coefficients are random
		DualisticElement[] coefficients = new DualisticElement[getThreshold()];
		coefficients[0] = (DualisticElement) message;
		for (int i = 1; i < getThreshold(); i++) {
			coefficients[i] = this.getZModPrime().getRandomElement(random);
		}

		// create a polynomial out of the coefficients
		final PolynomialElement polynomial = this.getPolynomialRing().getElement(coefficients);

		// create a tuple which stores the shares
		Tuple[] shares = new Tuple[getSize()];
		DualisticElement xVal;

		// populate the tuple array with tuples of x and y values
		for (int i = 0; i < getSize(); i++) {
			xVal = this.getZModPrime().getElement(BigInteger.valueOf(i + 1));
			shares[i] = ProductSet.getTuple(xVal, polynomial.evaluate(xVal));
		}

		return shares;
	}

	@Override
	protected ZModElement abstractRecover(Element[] shares) {

		// make sure that we have a tuple array
		Tuple[] points = (Tuple[]) shares;
		int length = points.length;

		// Calculating the lagrange coefficients for each point we got
		DualisticElement product;
		DualisticElement[] lagrangeCoefficients = new DualisticElement[length];
		for (int j = 0; j < length; j++) {
			product = null;
			DualisticElement elementJ = (DualisticElement) points[j].getAt(0);
			for (int l = 0; l < length; l++) {
				DualisticElement elementL = (DualisticElement) points[l].getAt(0);
				if (!elementJ.equals(elementL)) {
					if (product == null) {
						product = elementL.divide(elementL.subtract(elementJ));
					} else {
						product = product.multiply(elementL.divide(elementL.subtract(elementJ)));
					}
				}
			}
			lagrangeCoefficients[j] = product;
		}

		// multiply the y-value of the point with the lagrange coefficient and sum everything up
		ZModElement result = this.getZModPrime().getIdentityElement();
		for (int j = 0; j < length; j++) {
			DualisticElement value = (DualisticElement) points[j].getAt(1);
			result = result.add(value.multiply(lagrangeCoefficients[j]));
		}

		return result;
	}

	public static ShamirSecretSharingScheme getInstance(ZModPrime zModPrime, int size, int threshold) {
		if (zModPrime == null || size < 1 || threshold < 1 || threshold > size) {
			throw new IllegalArgumentException();
		}
		return new ShamirSecretSharingScheme(zModPrime, size, threshold);
	}

}
