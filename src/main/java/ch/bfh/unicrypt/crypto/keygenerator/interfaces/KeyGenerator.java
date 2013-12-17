/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 */
public interface KeyGenerator {

	Element generateKey();

	Element generateKey(RandomGenerator randomGenerator);

	Function getKeyGenerationFunction();

	Set getKeySpace();

}
