/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.utility;

import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class SafePrime extends Prime {

  public SafePrime(BigInteger safePrime) {
    super(safePrime);
    if (!MathUtil.isSavePrime(safePrime)) {
      throw new IllegalArgumentException();
    }
  }

}
