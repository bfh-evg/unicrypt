/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.interfaces.Bijection;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @param <D>
 * @param <C>
 * @param <E>
 * @author rolfhaenni
 */
public abstract class AbstractBijection<D extends Set, C extends Set, E extends Element> extends AbstractInjection<D, C, E> implements Bijection {

  protected AbstractBijection(Set domain, Set coDomain) {
    super(domain, coDomain);
  }

  @Override
  public Bijection invert() {
    return (Bijection) super.invert();
  }

}
