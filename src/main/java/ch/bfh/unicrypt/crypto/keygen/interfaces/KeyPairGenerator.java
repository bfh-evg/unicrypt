/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.crypto.keygen.abstracts.AbstractKeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public interface KeyPairGenerator {

  Tuple generateKeyPair();

  Tuple generateKeyPair(Random random);

  Element generatePrivateKey();

  Element generatePrivateKey(Random random);

  Tuple getKeyPair(BigInteger value);

  ProductSet getKeyPairSpace();

  Element getPrivateKey(BigInteger value);

  KeyGenerator getPrivateKeyGenerator();

  Set getPrivateKeySpace();

  Element getPublicKey(BigInteger value);

  Element getPublicKey(Element privateKey);

  Function getPublicKeyFunction();

  Set getPublicKeySpace();

}
