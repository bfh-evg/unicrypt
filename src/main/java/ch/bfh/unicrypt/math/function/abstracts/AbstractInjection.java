/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.Injection;

/**
 *
 * @param <D>
 * @param <E>
 * @param <C>
 * @author rolfhaenni
 */
public abstract class AbstractInjection<D extends Set, C extends Set, E extends Element> extends AbstractFunction<D, C, E> implements Injection {

  protected AbstractInjection(Set domain, Set coDomain) {
    super(domain, coDomain);
  }

  @Override
  public Function invert() {
    return abstractInvert(this.getCoDomain(), this.getDomain()); // domain<->coDomain
  }

  protected abstract Function abstractInvert(Set domain, Set coDomain);

}
