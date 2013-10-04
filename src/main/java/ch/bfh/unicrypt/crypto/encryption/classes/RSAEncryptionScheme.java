/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encryption.classes;

import ch.bfh.unicrypt.crypto.encryption.abstracts.AbstractAsymmetricEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZTimesMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZTimesModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZPlusTimesModTwoPrimes;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class RSAEncryptionScheme extends AbstractAsymmetricEncryptionScheme<ZTimesMod, ZTimesMod, ZTimesModElement, ZTimesModElement> {

  protected RSAEncryptionScheme(KeyPairGenerator keyPairGenerator, Function encryptionFunction, Function decryptionFunction) {
    super(keyPairGenerator, encryptionFunction, decryptionFunction);
  }

  public static RSAEncryptionScheme getInstance(ZPlusTimesModTwoPrimes zTimesMod) {
    if (zTimesMod == null) {
      throw new IllegalArgumentException();
    }
    Function encryptionFunction
    return new RSAEncryptionScheme

  }

  @Override
  protected String standardToStringContent() {
    return this.getPlaintextSpace().toString();
  }

}
