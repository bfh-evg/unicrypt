package ch.bfh.unicrypt.crypto.schemes.hash.interfaces;

import ch.bfh.unicrypt.crypto.schemes.Scheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.HashFunction;

/**
 *
 * @author rolfhaenni
 */
public interface HashScheme
       extends Scheme {

  /**
   *
   * @return
   */
  public Set getHashSpace();

  /**
   *
   * @return
   */
  public HashFunction getHashFunction();

  /**
   *
   * @param element
   * @return
   */
  public Element hash(Element element);

  /**
   *
   * @param element
   * @param hashValue
   * @return
   */
  public BooleanElement check(Element element, Element hashValue);

}
