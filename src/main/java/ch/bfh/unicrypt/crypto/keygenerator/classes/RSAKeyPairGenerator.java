/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.IntegerElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrimes;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class RSAKeyPairGenerator extends AbstractKeyPairGenerator<ZModPrimes, ZModPrimes, IntegerElement, IntegerElement> {

  protected RSAKeyPairGenerator(Function publicKeyFunction) {
    super(publicKeyFunction);
  }

  public static RSAKeyPairGenerator getInstance(ZModPrimes zTimesModTwoPrimes) {
    return new RSAKeyPairGenerator(InvertFunction.getInstance(zTimesModTwoPrimes.getZStarModOrder()));
  }

}
