package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialElement;

public class ECBinaryPolynomialFieldElement
			 extends AbstractECElement<ECBinaryPolynomialFieldElement, BinaryPolynomialElement> {

	protected ECBinaryPolynomialFieldElement(ECBinaryPolynomialField ecGroup,
				 BinaryPolynomialElement x, BinaryPolynomialElement y) {
		super(ecGroup, x, y);
		// TODO Auto-generated constructor stub
	}

}
