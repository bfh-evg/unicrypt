package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

public abstract class AbstractECElement<E extends AbstractECElement, D extends DualisticElement>
       extends AbstractAdditiveElement<AbstractEC, E> {

  private final D x, y;

  protected AbstractECElement(AbstractEC ecGroup, D x, D y) {
    super(ecGroup);
    this.x = x;
    this.y = y;
  }

  public D getX() {
    return this.x;
  }

  public D getY() {
    return this.y;
  }

  @Override
  protected BigInteger standardGetValue() {
    if (null==x && null==y) {
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
