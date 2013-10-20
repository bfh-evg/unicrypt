/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygen.classes;

import ch.bfh.unicrypt.crypto.keygen.abstracts.AbstractKeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModPrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class SchnorrSignatureKeyPairGenerator extends AbstractKeyPairGenerator<ZMod, GStarModPrime, ZModElement, GStarModElement> {

  protected SchnorrSignatureKeyPairGenerator(Function publicKeyFunction) {
    super(publicKeyFunction);
  }

  public static SchnorrSignatureKeyPairGenerator getInstance(GStarModSafePrime publicKeySpace) {
    return new SchnorrSignatureKeyPairGenerator(GeneratorFunction.getInstance(publicKeySpace));
  }

  public static SchnorrSignatureKeyPairGenerator getInstance(Element generator) {
    return new SchnorrSignatureKeyPairGenerator(GeneratorFunction.getInstance(generator));
  }

}
