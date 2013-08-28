/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.Injection;
import ch.bfh.unicrypt.math.set.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractInjection extends AbstractFunction implements Injection {

  protected  AbstractInjection(Set domain, Set coDomain) {
    super(domain, coDomain);
  }

  public Function invert() {
    return abstractInvert(this.getCoDomain(), this.getDomain()); // domain<->coDomain
  }

  protected abstract Function abstractInvert(Set domain, Set coDomain);

}
