/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.interfaces;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public interface KeyGenerator {

  Element generateKey();

  Element generateKey(Random random);

  Element getKey(BigInteger value);

  Set getKeySpace();
}
