package ch.bfh.unicrypt.math.group.interfaces;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.Element;

/**
 * This interface provides the renaming of some group operations for the case of a additively written group. No functionality 
 * is added. 
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
//VERSION2: public interface AdditiveGroup extends CommutativeGroup, AdditiveMonoid {
public interface AdditiveGroup extends Group {

  /**
   * This method is a synonym for {@link #Group.apply(Element, Element)}.
   * @param element1 the same as in {@link #Group.apply(Element, Element)}
   * @param element2 the same as in {@link #Group.apply(Element, Element)}
   * @return the same as in {@link #Group.apply(Element, Element)}
   */
  public Element add(Element element1, Element element2);
  
  /**
   * This method is a synonym for {@link #Group.applyInverse(Element, Element)}.
   * @param element1 the same as in {@link #Group.applyInverse(Element, Element)}
   * @param element2 the same as in {@link #Group.applyInverse(Element, Element)}
   * @return the same as in {@link #Group.applyInverse(Element, Element)}
   */
  public Element subtract(Element element1, Element element2);

  /**
   * This method is a synonym for {@link #Group.apply(Element...)}.
   * @param elements the same as in {@link #Group.apply(Element...)}
   * @return the same as in {@link #Group.apply(Element...)}
   */
  public Element add(Element... elements);

  /**
   * This method is a synonym for {@link #Group.selfApply(Element, BigInteger)}.
   * @param element the same as in {@link #Group.selfApply(Element, BigInteger)}
   * @param amount the same as in {@link #Group.selfApply(Element, BigInteger)}
   * @return the same as in {@link #Group.selfApply(Element, BigInteger)}
   */
  public Element times(Element element, BigInteger amount);
  
  /**
   * This method is a synonym for {@link #Group.selfApply(Element, Element)}.
   * @param element the same as in {@link #Group.selfApply(Element, Element)}
   * @param amount the same as in {@link #Group.selfApply(Element, Element)}
   * @return the same as in {@link #Group.selfApply(Element, Element)}
   */
  public Element times(Element element, Element amount);
  
  /**
   * This method is a synonym for {@link #Group.selfApply(Element, int)}.
   * @param element the same as in {@link #Group.selfApply(Element, int)}
   * @param amount the same as in {@link #Group.selfApply(Element, int)}
   * @return the same as in {@link #Group.selfApply(Element, int)}
   */
  public Element times(Element element, int amount);
  
}