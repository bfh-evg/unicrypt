package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public class ECGroupF2m extends ECGroup {

	public ECGroupF2m(ZModPrime Finitefiled, DualisticElement a,
			DualisticElement b, DualisticElement gx, DualisticElement gy,
			BigInteger order, BigInteger h) {
		super(Finitefiled, a, b, gx, gy, order, h);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ECGroupElement abstractApply(Element element1, Element element2) {
		// TODO Auto-generated method stub
		return null;
	}

}
