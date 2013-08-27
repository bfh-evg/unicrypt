/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.function.interfaces.Bijection;
import ch.bfh.unicrypt.math.function.interfaces.Injection;
import ch.bfh.unicrypt.math.set.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractBijection extends AbstractInjection implements Bijection {

  protected AbstractBijection(Set domain, Set coDomain) {
    super(domain, coDomain);
  }

  public Bijection invert() {
    return (Bijection) super.invert();
  }

}
