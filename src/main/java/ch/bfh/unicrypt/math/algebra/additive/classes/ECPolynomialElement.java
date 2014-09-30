package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.helper.Polynomial;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModTwo;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;

/**
 * 
 * @author Christian Lutz
 *
 */
public class ECPolynomialElement extends AbstractECElement<Polynomial<DualisticElement<ZModTwo>>, PolynomialElement<ZModTwo>> {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ECPolynomialElement(
			ECPolynomialField ecGroup,
			Point<PolynomialElement<ZModTwo>> value) {
		super(ecGroup, value);
		// TODO Auto-generated constructor stub
	}

	public ECPolynomialElement(
			ECPolynomialField ecGroup) {
		super(ecGroup);
		// TODO Auto-generated constructor stub
	}


	



}
