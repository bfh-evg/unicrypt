package ch.bfh.unicrypt.math.group.interfaces;

import ch.bfh.unicrypt.math.monoid.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;

/**
 * This interface provides represents an additively written group. It provides
 * the renaming of one group operation. No functionality is added.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface AdditiveGroup extends Group, AdditiveMonoid {

  /**
   * This method is a synonym for {@link #Group.applyInverse(Element, Element)}.
   * @param element1 the same as in {@link #Group.applyInverse(Element, Element)}
   * @param element2 the same as in {@link #Group.applyInverse(Element, Element)}
   * @return the same as in {@link #Group.applyInverse(Element, Element)}
   */
  public AdditiveElement subtract(Element element1, Element element2);

}
