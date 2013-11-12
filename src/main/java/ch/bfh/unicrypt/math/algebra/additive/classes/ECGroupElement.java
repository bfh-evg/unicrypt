package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

public class ECGroupElement
       extends AbstractAdditiveElement<ECGroup, ECGroupElement> {

  private final DualisticElement x, y;

  protected ECGroupElement(ECGroup ecGroup, DualisticElement x, DualisticElement y) {
    super(ecGroup);
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
      return MathUtil.pair(this.getX().getValue(), this.getY().getValue());
    }
  }

  @Override
  public String standardToStringContent() {
    if (this.getX() == null && this.getY() == null) {
      return "(-1,-1)";
    } else {
      return "(" + this.getX().getValue() + "," + this.getY().getValue() + ")";
    }
  }

}
