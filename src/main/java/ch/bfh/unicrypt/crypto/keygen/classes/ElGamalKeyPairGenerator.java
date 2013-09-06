/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygen.classes;

import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class ElGamalKeyPairGenerator extends KeyPairGenerator {

  protected ElGamalKeyPairGenerator(Function publicKeyFunction) {
    super(publicKeyFunction);
  }

  public static ElGamalKeyPairGenerator getInstance(GStarModSafePrime publicKeySpace) {
    return ElGamalKeyPairGenerator.getInstance(publicKeySpace, publicKeySpace.getDefaultGenerator());
  }

  public static ElGamalKeyPairGenerator getInstance(GStarModSafePrime publicKeySpace, Element generator) {
    if (publicKeySpace == null || generator == null || !generator.isGenerator()) {
      throw new IllegalArgumentException();
    }
    ZPlusMod privateKeySpace = publicKeySpace.getZPlusModOrder();
    Function function = SelfApplyFunction.getInstance(publicKeySpace, privateKeySpace).partiallyApply(generator, 0);
    return new ElGamalKeyPairGenerator(CompositeFunction.getInstance(MultiIdentityFunction.getInstance(privateKeySpace), function));
  }

}
