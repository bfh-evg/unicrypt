/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public interface KeyPairGenerator {

	Pair generateKeyPair();

	Pair generateKeyPair(Random random);

	Element generatePrivateKey();

	Element generatePrivateKey(Random random);

	Element getPublicKey(Element privateKey);

	ProductSet getKeyPairSpace();

	Set getPrivateKeySpace();

	Set getPublicKeySpace();

	KeyGenerator getPrivateKeyGenerator();

	Function getPublicKeyFunction();

}
