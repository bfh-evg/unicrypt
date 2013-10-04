/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygen.classes;

import ch.bfh.unicrypt.crypto.keygen.abstracts.AbstractKeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZPlusTimesElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZPlusTimesModTwoPrimes;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class RSAKeyPairGenerator extends AbstractKeyPairGenerator<ZPlusTimesModTwoPrimes, ZPlusTimesModTwoPrimes, ZPlusTimesElement, ZPlusTimesElement> {

  protected RSAKeyPairGenerator(Function publicKeyFunction) {
    super(publicKeyFunction);
  }

  public static RSAKeyPairGenerator getInstance(ZPlusTimesModTwoPrimes zTimesModTwoPrimes) {
    return new RSAKeyPairGenerator(InvertFunction.getInstance(zTimesModTwoPrimes.getZStarModOrder()));
  }

}
