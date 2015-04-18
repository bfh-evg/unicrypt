package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import java.math.BigInteger;

public class ECZModElement
	   extends AbstractECElement<BigInteger, ZModElement, ECZModElement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4590767773320124829L;

	protected ECZModElement(ECZModPrime ecGroup) {
		super(ecGroup);

	}

	protected ECZModElement(ECZModPrime ecGroup, Point<ZModElement> value) {
		super(ecGroup, value);

	}

}
