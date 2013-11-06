/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.crypto.encoder.classes.IdentityEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.keygenerator.classes.ElGamalKeyPairGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractHomomorphicEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
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
 * @param <M>
 * @param <ME>
 */
public class ElGamalEncryptionScheme<M extends Set, ME extends Element>
       extends AbstractHomomorphicEncryptionScheme<M, CyclicGroup, ProductGroup, ME, Tuple, CyclicGroup, ZModPrime, ZModPrime> {

  Function encryptionFunctionLeft;
  Function encryptionFunctionRight;

  protected ElGamalEncryptionScheme(M messageSpace, Encoder encoder, Function encryptionFunction, Function decryptionFunction, KeyPairGenerator keyPairGenerator, Function encryptionFunctionLeft, Function encryptionFunctionRight) {
    super(messageSpace, encoder, encryptionFunction, decryptionFunction, keyPairGenerator);
    this.encryptionFunctionLeft = encryptionFunctionLeft;
    this.encryptionFunctionRight = encryptionFunctionRight;
  }

  public static ElGamalEncryptionScheme getInstance(CyclicGroup cyclicGroup) {
    return ElGamalEncryptionScheme.getInstance(null, null, cyclicGroup, null);
  }

  public static ElGamalEncryptionScheme getInstance(Set messageSpace, CyclicGroup cyclicGroup) {
    return ElGamalEncryptionScheme.getInstance(messageSpace, null, cyclicGroup, null);
  }

  public static ElGamalEncryptionScheme getInstance(Encoder encoder, CyclicGroup cyclicGroup) {
    return ElGamalEncryptionScheme.getInstance(null, encoder, cyclicGroup, null);
  }

  public static ElGamalEncryptionScheme getInstance(Set messageSpace, Encoder encoder, CyclicGroup cyclicGroup) {
    return ElGamalEncryptionScheme.getInstance(messageSpace, encoder, cyclicGroup, null);
  }

  public static ElGamalEncryptionScheme getInstance(Element generator) {
    return ElGamalEncryptionScheme.getInstance(null, null, null, generator);
  }

  public static ElGamalEncryptionScheme getInstance(Set messageSpace, Element generator) {
    return ElGamalEncryptionScheme.getInstance(messageSpace, null, null, generator);
  }

  public static ElGamalEncryptionScheme getInstance(Encoder encoder, Element generator) {
    return ElGamalEncryptionScheme.getInstance(null, encoder, null, generator);
  }

  public static ElGamalEncryptionScheme getInstance(Set messageSpace, Encoder encoder, Element generator) {
    return ElGamalEncryptionScheme.getInstance(messageSpace, encoder, null, generator);
  }

  private static ElGamalEncryptionScheme getInstance(Set messageSpace, Encoder encoder, CyclicGroup cyclicGroup, Element generator) {
    if (generator == null) {
      generator = cyclicGroup.getDefaultGenerator();
    } else {
      if (generator.getSet().isCyclic() && generator.isGenerator()) {
        cyclicGroup = (CyclicGroup) generator.getSet();
      } else {
        throw new IllegalArgumentException();
      }
    }
    if (encoder == null) {
      encoder = IdentityEncoder.getInstance(cyclicGroup);
    } else {
      if (!encoder.getCoDomain().equals(cyclicGroup)) {
        throw new IllegalArgumentException();
      }
    }
    if (messageSpace == null) {
      messageSpace = encoder.getDomain();
    }

    ZMod zPlusTimesMod = cyclicGroup.getZModOrder();

    ProductGroup encryptionSpace = ProductGroup.getInstance(cyclicGroup, cyclicGroup, zPlusTimesMod);

    Function encryptionFunctionLeft = GeneratorFunction.getInstance(generator);

    Function encryptionFunctionRight = CompositeFunction.getInstance(
           MultiIdentityFunction.getInstance(encryptionSpace, 2),
           ProductFunction.getInstance(SelectionFunction.getInstance(encryptionSpace, 1),
                                       CompositeFunction.getInstance(RemovalFunction.getInstance(encryptionSpace, 1),
                                                                     SelfApplyFunction.getInstance(cyclicGroup))),
           ApplyFunction.getInstance(cyclicGroup));

    Function encryptionFunction = CompositeFunction.getInstance(
           MultiIdentityFunction.getInstance(encryptionSpace, 2),
           ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(encryptionSpace, 2),
                                                                     encryptionFunctionLeft),
                                       encryptionFunctionRight));

    ProductGroup decryptionSpace = ProductGroup.getInstance(zPlusTimesMod, ProductGroup.getInstance(cyclicGroup, 2));

    Function decryptionFunction = CompositeFunction.getInstance(
           MultiIdentityFunction.getInstance(decryptionSpace, 2),
           ProductFunction.getInstance(SelectionFunction.getInstance(decryptionSpace, 1, 1),
                                       CompositeFunction.getInstance(MultiIdentityFunction.getInstance(decryptionSpace, 2),
                                                                     ProductFunction.getInstance(SelectionFunction.getInstance(decryptionSpace, 1, 0),
                                                                                                 SelectionFunction.getInstance(decryptionSpace, 0)),
                                                                     SelfApplyFunction.getInstance(cyclicGroup, zPlusTimesMod))),
           ApplyInverseFunction.getInstance(cyclicGroup));

    return new ElGamalEncryptionScheme(messageSpace, encoder, encryptionFunction, decryptionFunction, ElGamalKeyPairGenerator.getInstance(generator), encryptionFunctionLeft, encryptionFunctionRight);
  }

  @Override
  protected String standardToStringContent() {
    return this.getMessageSpace().toString();
  }

}
