/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.concatenative.abstracts;

import ch.bfh.unicrypt.math.concatenative.interfaces.ConcatenativeElement;
import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.concatenative.interfaces.ConcatenativeMonoid;
import ch.bfh.unicrypt.math.concatenative.interfaces.ConcatenativeSemiGroup;
import ch.bfh.unicrypt.math.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.general.interfaces.Group;
import ch.bfh.unicrypt.math.general.interfaces.SemiGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractConcatenativeElement<E extends ConcatenativeElement> extends AbstractElement<E> implements ConcatenativeElement {

  protected AbstractConcatenativeElement(final SemiGroup semiGroup) {
    super(semiGroup);
  }

  protected AbstractConcatenativeElement(final SemiGroup semiGroup, final BigInteger value) {
    super(semiGroup);
    if (!semiGroup.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

  /**
   *
   * @return
   */
  @Override
  public final ConcatenativeSemiGroup getConcatenativeSemiGroup() {
    if (this.getSet() instanceof ConcatenativeSemiGroup) {
      return (ConcatenativeSemiGroup) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final ConcatenativeMonoid getConcatenativeMonoid() {
    if (this.getSet() instanceof ConcatenativeMonoid) {
      return (ConcatenativeMonoid) this.getSet();
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final AbstractConcatenativeElement concatenate(final Element element) {
    ConcatenativeSemiGroup semiGroup = this.getConcatenativeSemiGroup();
    return (AbstractConcatenativeElement) semiGroup.concatenate(this, element);
  }

  /**
   * @see Group#T(Element, BigInteger)
   */
  @Override
  public final AbstractConcatenativeElement selfConcatenate(final BigInteger amount) {
    ConcatenativeSemiGroup semiGroup = this.getConcatenativeSemiGroup();
    return (AbstractConcatenativeElement) semiGroup.selfConcatenate(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final AbstractConcatenativeElement selfConcatenate(final Element amount) {
    ConcatenativeSemiGroup semiGroup = this.getConcatenativeSemiGroup();
    return (AbstractConcatenativeElement) semiGroup.selfConcatenate(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final AbstractConcatenativeElement selfConcatenate(final int amount) {
    ConcatenativeSemiGroup semiGroup = this.getConcatenativeSemiGroup();
    return (AbstractConcatenativeElement) semiGroup.selfConcatenate(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final AbstractConcatenativeElement selfConcatenate() {
    ConcatenativeSemiGroup semiGroup = this.getConcatenativeSemiGroup();
    return (AbstractConcatenativeElement) semiGroup.selfConcatenate(this);
  }

}
