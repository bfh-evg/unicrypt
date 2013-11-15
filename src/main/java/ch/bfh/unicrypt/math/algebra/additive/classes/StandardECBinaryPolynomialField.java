package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;

public class StandardECBinaryPolynomialField extends ECBinaryPolynomialField {

	public StandardECBinaryPolynomialField(ZModPrime Finitefiled, DualisticElement a,
			DualisticElement b, DualisticElement gx, DualisticElement gy,
			BigInteger order, BigInteger h) {
		super(Finitefiled, a, b, gx, gy, order, h);
	}

}
