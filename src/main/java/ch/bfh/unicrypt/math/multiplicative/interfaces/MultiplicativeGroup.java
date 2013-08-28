package ch.bfh.unicrypt.math.multiplicative.interfaces;

import ch.bfh.unicrypt.math.multiplicative.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.general.interfaces.Group;

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
  public MultiplicativeElement divide(Element element1, Element element2);

}
