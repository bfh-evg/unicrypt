package ch.bfh.unicrypt.crypto.schemes.hash.interfaces;

import ch.bfh.unicrypt.crypto.schemes.scheme.interfaces.Scheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

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
  public Function getHashFunction();

  /**
   *
   * @return
   */
  public Function getCheckFunction();

  /**
   *
   * @param message
   * @return
   */
  public Element hash(Element message);

  /**
   *
   * @param message
   * @param hashValue
   * @return
   */
  public BooleanElement check(Element message, Element hashValue);

}
