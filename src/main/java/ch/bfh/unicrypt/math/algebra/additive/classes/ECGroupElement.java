package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.utility.MathUtil;

public class ECGroupElement extends
		AbstractAdditiveElement<ECGroup, ECGroupElement> {

	private DualisticElement x, y;

	protected ECGroupElement(ECGroup semiGroup, DualisticElement x,
			DualisticElement y) {
		super(semiGroup);
		this.x = x;
		this.y = y;
	}

	public DualisticElement getX() {
		return this.x;
	}

	public DualisticElement getY() {
		return this.y;
	}

	@Override
	protected BigInteger standardGetValue() {
		if (this.isZero()) {
			return new BigInteger("-1");
		} else {
			return MathUtil.elegantPair(this.x.getValue(), this.y.getValue());
		}
	}

	@Override
	public String toString() {
		if (this.x == null && this.y == null) {
			return "(-1,-1)";
		} else {
			return "(" + this.x.getValue() + "," + this.y.getValue() + ")";
		}
	}

}
