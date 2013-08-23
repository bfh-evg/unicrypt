package ch.bfh.unicrypt.math.group.interfaces;

import ch.bfh.unicrypt.math.element.interfaces.Element;

/**
 * This interface provides represents an additively written group. It provides
 * the renaming of one group operation. No functionality is added.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface MultiplicativeGroup extends Group, MultiplicativeMonoid {

  /**
   * This method is a synonym for {@link #Group.applyInverse(Element, Element)}.
   * @param element1 the same as in {@link #Group.applyInverse(Element, Element)}
   * @param element2 the same as in {@link #Group.applyInverse(Element, Element)}
   * @return the same as in {@link #Group.applyInverse(Element, Element)}
   */
  public Element divide(Element element1, Element element2);

}
