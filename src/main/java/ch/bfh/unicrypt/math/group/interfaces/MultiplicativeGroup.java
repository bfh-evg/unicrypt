package ch.bfh.unicrypt.math.group.interfaces;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.Element;

/**
 * This interface provides the renaming of some group operations for the case of a multiplicatively written group. No functionality is added. 
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public interface MultiplicativeGroup extends Group {

  /**
   * This method is a synonym for {@link #Group.apply(Element, Element)}.
   * @param element1 the same as in {@link #Group.apply(Element, Element)}
   * @param element2 the same as in {@link #Group.apply(Element, Element)}
   * @return the same as in {@link #Group.apply(Element, Element)}
   */
  public Element multiply(Element element1, Element element2);
  
  /**
   * This method is a synonym for {@link #Group.applyInverse(Element, Element)}.
   * @param element1 the same as in {@link #Group.applyInverse(Element, Element)}
   * @param element2 the same as in {@link #Group.applyInverse(Element, Element)}
   * @return the same as in {@link #Group.applyInverse(Element, Element)}
   */
  public Element divide(Element element1, Element element2);

  /**
   * This method is a synonym for {@link #Group.apply(Element...)}.
   * @param elements the same as in {@link #Group.apply(Element...)}
   * @return the same as in {@link #Group.apply(Element...)}
   */
  public Element multiply(Element... elements);

  /**
   * This method is a synonym for {@link #Group.selfApply(Element, BigInteger)}.
   * @param element the same as in {@link #Group.selfApply(Element, BigInteger)}
   * @param amount the same as in {@link #Group.selfApply(Element, BigInteger)}
   * @return the same as in {@link #Group.selfApply(Element, BigInteger)}
   */
  public Element power(Element element, BigInteger amount);
  
  /**
   * This method is a synonym for {@link #Group.selfApply(Element, Element)}.
   * @param element the same as in {@link #Group.selfApply(Element, Element)}
   * @param amount the same as in {@link #Group.selfApply(Element, Element)}
   * @return the same as in {@link #Group.selfApply(Element, Element)}
   */
  public Element power(Element element, Element amount);
  
  /**
   * This method is a synonym for {@link #Group.selfApply(Element, int)}.
   * @param element the same as in {@link #Group.selfApply(Element, int)}
   * @param amount the same as in {@link #Group.selfApply(Element, int)}
   * @return the same as in {@link #Group.selfApply(Element, int)}
   */
  public Element power(Element element, int amount);
  
}