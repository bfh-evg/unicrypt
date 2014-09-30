package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;

public class ECZModElement extends AbstractECElement<BigInteger,ZModElement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ECZModElement(ECZModPrime ecGroup) {
		super(ecGroup);

	}

	protected ECZModElement(ECZModPrime ecGroup, Point<ZModElement> value) {
		super(ecGroup, value);

	}

}
