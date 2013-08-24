/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractMultiplicativeElement<T extends MultiplicativeElement> extends AbstractElement<T> implements MultiplicativeElement {

  protected AbstractMultiplicativeElement(final Set set) {
    super(set);
  }

  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final T multiply(final Element element) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.multiply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final T divide(final Element element) {
    MultiplicativeGroup group = this.getMultiplicativeGroup();
    return (T) group.divide(this, element);
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  @Override
  public final T power(final BigInteger amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final T power(final Element amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final T power(final int amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final T square() {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.square(this);
  }

}
