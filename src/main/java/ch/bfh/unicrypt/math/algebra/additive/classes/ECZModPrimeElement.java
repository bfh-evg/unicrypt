package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractEC;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;

public class ECZModPrimeElement extends AbstractECElement<ECZModPrimeElement, ZModElement> {

	protected ECZModPrimeElement(AbstractEC ecGroup, ZModElement x,
			ZModElement y) {
		super(ecGroup, x, y);
		// TODO Auto-generated constructor stub
	}

}
