/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public interface KeyPairGenerator {

	Element generatePrivateKey();

	Element generatePrivateKey(RandomGenerator randomGenerator);

	Element generatePublicKey(Element privateKey);

	Element generatePublicKey(Element privateKey, RandomGenerator randomGenerator);

	Pair generateKeyPair();

	Pair generateKeyPair(RandomGenerator randomGenerator);

	ProductSet getKeyPairSpace();

	Set getPrivateKeySpace();

	Set getPublicKeySpace();

	Function getKeyPairGenerationFunction();

	Function getPrivateKeyGenerationFunction();

	Function getPublicKeyGenerationFunction();

}
