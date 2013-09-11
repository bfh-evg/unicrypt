/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encryption.classes;

import ch.bfh.unicrypt.crypto.encryption.abstracts.AbstractHomomorphicEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.classes.ElGamalKeyPairGenerator;
import ch.bfh.unicrypt.crypto.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.ApplyInverseFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.RemovalFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public class ElGamalEncryptionScheme extends AbstractHomomorphicEncryptionScheme<GStarModSafePrime, ProductGroup, ZPlusMod, GStarModElement, Tuple> {

  Function encryptionFunctionLeft;
  Function encryptionFunctionRight;

  protected ElGamalEncryptionScheme(KeyPairGenerator keyPairGenerator, Function encryptionFunctionLeft, Function encryptionFunctionRight, Function encryptionFunction, Function decryptionFunction) {
    super(keyPairGenerator, encryptionFunction, decryptionFunction);
    this.encryptionFunctionLeft = encryptionFunctionLeft;
    this.encryptionFunctionRight = encryptionFunctionRight;
  }

  public static ElGamalEncryptionScheme getInstance(GStarModSafePrime gStarMod, GStarModElement generator) {
    if (gStarMod == null || generator == null || !gStarMod.contains(generator) || !generator.isGenerator()) {
      throw new IllegalArgumentException();
    }
    return ElGamalEncryptionScheme.makeInstance(gStarMod, generator);
  }

  public static ElGamalEncryptionScheme getInstance(GStarModSafePrime gStarMod) {
    if (gStarMod == null) {
      throw new IllegalArgumentException();
    }
    return ElGamalEncryptionScheme.makeInstance(gStarMod, gStarMod.getDefaultGenerator());
  }

  public static ElGamalEncryptionScheme getInstance(GStarModElement generator) {
    if (generator == null || !(generator.getSet() instanceof GStarModSafePrime) || !generator.isGenerator()) {
      throw new IllegalArgumentException();
    }
    return ElGamalEncryptionScheme.makeInstance((GStarModSafePrime) generator.getSet(), generator);
  }

  private static ElGamalEncryptionScheme makeInstance(GStarModSafePrime gStarMod, GStarModElement generator) {
    ZPlusMod zPlusMod = gStarMod.getZPlusModOrder();

    ProductGroup encryptionSpace = ProductGroup.getInstance(gStarMod, gStarMod, zPlusMod);
    Function encryptionFunctionLeft = GeneratorFunction.getInstance(generator);
    Function encryptionFunctionRight = CompositeFunction.getInstance(
            MultiIdentityFunction.getInstance(encryptionSpace, 2),
            ProductFunction.getInstance(SelectionFunction.getInstance(encryptionSpace, 1),
                                        CompositeFunction.getInstance(RemovalFunction.getInstance(encryptionSpace, 1),
                                                                      SelfApplyFunction.getInstance(gStarMod))),
            ApplyFunction.getInstance(gStarMod));
    Function encryptionFunction = CompositeFunction.getInstance(
            MultiIdentityFunction.getInstance(encryptionSpace, 2),
            ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(encryptionSpace, 2),
                                                                      encryptionFunctionLeft),
                                        encryptionFunctionRight));

    ProductGroup decryptionSpace = ProductGroup.getInstance(zPlusMod, ProductGroup.getInstance(gStarMod, 2));
    Function decryptionFunction = CompositeFunction.getInstance(
            MultiIdentityFunction.getInstance(decryptionSpace, 2),
            ProductFunction.getInstance(SelectionFunction.getInstance(decryptionSpace, 1, 1),
                                        CompositeFunction.getInstance(MultiIdentityFunction.getInstance(decryptionSpace, 2),
                                                                      ProductFunction.getInstance(SelectionFunction.getInstance(decryptionSpace, 1, 0),
                                                                                                  SelectionFunction.getInstance(decryptionSpace, 0)),
                                                                      SelfApplyFunction.getInstance(gStarMod, zPlusMod))),
            ApplyInverseFunction.getInstance(gStarMod));

    return new ElGamalEncryptionScheme(ElGamalKeyPairGenerator.getInstance(generator), encryptionFunctionLeft, encryptionFunctionRight, encryptionFunction, decryptionFunction);
  }

  @Override
  protected String standardToStringContent() {
    return this.getPlaintextSpace().toString();
  }

}
