package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.helper.Polynomial;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import java.math.BigInteger;

/**
 *
 * @author Christian Lutz
 * <p>
 */
public class ECPolynomialElement
	   extends AbstractECElement<Polynomial<? extends DualisticElement<BigInteger>>, PolynomialElement, ECPolynomialElement> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected ECPolynomialElement(ECPolynomialField ecGroup, Point<PolynomialElement> value) {
		super(ecGroup, value);
	}

	public ECPolynomialElement(ECPolynomialField ecGroup) {
		super(ecGroup);
	}

}
