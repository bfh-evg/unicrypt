package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractEC;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

public class ECZModPrime
			 extends AbstractEC<ECZModPrimeElement, ZModPrime, ZModElement> {

	protected ECZModPrime(ZModPrime finiteField, ZModElement a,
				 ZModElement b, ZModElement gx, ZModElement gy,
				 BigInteger order, BigInteger h) {
		super(finiteField, a, b, gx, gy, order, h);
	}

	protected ECZModPrime(ZModPrime finiteField, ZModElement a,
				 ZModElement b, BigInteger order, BigInteger h) {
		super(finiteField, a, b, order, h);
	}

	@Override
	protected ECZModPrimeElement abstractApply(ECZModPrimeElement element1, ECZModPrimeElement element2) {
		ZModElement s, rx, ry, px, py, qx, qy;
		px = element1.getX();
		py = element1.getY();
		qx = element2.getX();
		qy = element2.getY();

		if (element1.isIdentity()) {
			return element2;
		} else {
			if (element2.isIdentity()) {
				return element1;
			} else {
				if (element1.isEqual(element2.invert())) {
					return this.getIdentityElement();
				} else {
					if (element1.isEqual(element2)) {
						ZModElement three = (ZModElement) this.getFiniteField().getElement(3);
						ZModElement two = (ZModElement) this.getFiniteField().getElement(2);
						s = ((px.power(2).multiply(three)).apply(this.getA())).divide(py.multiply(two));
						rx = s.power(2).apply(px.multiply(two).invert());
						ry = s.multiply(px.subtract(rx)).apply(py.invert());
						return this.getElement(rx, ry);
					} else {
						s = py.apply(qy.invert()).divide(px.apply(qx.invert()));
						rx = (s.power(2).apply(px.invert()).apply(qx.invert()));
						ry = py.invert().add(s.multiply(px.apply(rx.invert())));
						return this.getElement(rx, ry);
					}
				}
			}
		}

	}

	@Override
	protected ECZModPrimeElement abstractInvert(ECZModPrimeElement element) {
		if (element.isZero()) {
			return this.getIdentityElement();
		}
		return new ECZModPrimeElement(this, element.getX(), element.getY().invert());
	}

	@Override
	public Boolean contains(ZModElement x, ZModElement y) {
		y = y.power(2);
		x = x.power(3).add(x.multiply(this.getA())).add(this.getB());

		return y.isEqual(x);
	}

	@Override
	protected ECZModPrimeElement getRandomElementWithoutGenerator(RandomGenerator randomGenerator) {
		BigInteger p = this.getFiniteField().getModulus();
		ZModElement x = (ZModElement) this.getFiniteField().getRandomElement(randomGenerator);
		ZModElement y = x.power(3).add(this.getA().multiply(x)).add(this.getB());
		boolean neg = x.getValue().mod(new BigInteger("2")).equals(BigInteger.ONE);

		while (!MathUtil.hasSqrtModPrime(y.getValue(), p)) {
			x = (ZModElement) this.getFiniteField().getRandomElement(randomGenerator);
			y = x.power(3).add(this.getA().multiply(x)).add(this.getB());
		}

		//if neg is true return solution 2(p-sqrt) of sqrtModPrime else solution 1
		if (neg) {
			y = (ZModElement) this.getFiniteField().getElement(p.subtract(MathUtil.sqrtModPrime(y.getValue(), p)));
		} else {
			y = (ZModElement) this.getFiniteField().getElement(MathUtil.sqrtModPrime(y.getValue(), p));
		}

		return this.getElement(x, y);
	}

	@Override
	protected boolean isValid() {
		boolean c1, c2, c3, c4, c5, c61, c62;

		ZModElement i4 = (ZModElement) getFiniteField().getElement(4);
		ZModElement i27 = (ZModElement) getFiniteField().getElement(27);
		c1 = !getA().power(3).multiply(i4).add(i27.multiply(getB().power(2))).equals(BigInteger.ZERO);

		c2 = contains(this.getDefaultGenerator());

		c3 = MathUtil.arePrime(getOrder());

		c4 = 0 >= getH().compareTo(new BigInteger("4"));

		c5 = getIdentityElement().equals(getDefaultGenerator().selfApply(getOrder()));

		c61 = true; //_> Must be corrected!
		for (int i = 1; i < 20; i++) {

		}

		c62 = !getOrder().multiply(getH()).equals(getP());

		return c1 && c2 && c3 && c4 && c5 && c61 && c62;
	}

	/**
	 * Return modulus of the finite field ZModPrime
	 * <p>
	 * @return
	 */
	protected BigInteger getP() {
		return this.getFiniteField().getModulus();
	}

	/**
	 * Returns an elliptic curve over Fp y²=x³+ax+b
	 * <p>
	 * @param f     Finite field of type ZModPrime
	 * @param a     Element of Fp respresnting a in the curve equation
	 * @param b     Element of Fp respresnting b in the curve equation
	 * @param order Order of the the used subgroup
	 * @param h     Co-factor h*order= N -> total order of the group
	 * @return
	 */
	public static ECZModPrime getInstance(ZModPrime f, ZModElement a, ZModElement b, BigInteger order, BigInteger h) {
		return new ECZModPrime(f, a, b, order, h);
	}

	/**
	 * Returns an elliptic curve over Fp y²=x³+ax+b
	 * <p>
	 * @param f     Finite field of type ZModPrime
	 * @param a     Element of Fp respresnting a in the curve equation
	 * @param b     Element of Fp respresnting b in the curve equation
	 * @param gx    x-coordinate of the generator
	 * @param gy    y-coordinate of the generator
	 * @param order Order of the the used subgroup
	 * @param h     Co-factor h*order= N -> total order of the group
	 * @return
	 */
	public static ECZModPrime getInstance(ZModPrime f, ZModElement a, ZModElement b, ZModElement gx, ZModElement gy, BigInteger order, BigInteger h) {
		return new ECZModPrime(f, a, b, gx, gy, order, h);
	}

	@Override
	protected ECZModPrimeElement abstractGetElement(ZModElement x, ZModElement y) {
		if (contains(x, y)) {
			return new ECZModPrimeElement(this, x, y);
		} else {
			throw new IllegalArgumentException(x + " and " + y + " are not valid coordinates");
		}

	}

	@Override
	protected ECZModPrimeElement abstractGetIdentityElement() {
		return new ECZModPrimeElement(this, zero, zero);
	}

	@Override
	public Boolean contains(ZModElement x) {
		BigInteger p = this.getP();
		ZModElement right = x.power(3).add(getA().multiply(x)).add(getB());

		if (MathUtil.hasSqrtModPrime(right.getValue(), p)) {
			BigInteger y1 = MathUtil.sqrtModPrime(right.getValue(), p);
			ZModElement y = this.getFiniteField().getElement(y1);
			return contains(x, y);
		} else {
			return false;
		}
	}

}
