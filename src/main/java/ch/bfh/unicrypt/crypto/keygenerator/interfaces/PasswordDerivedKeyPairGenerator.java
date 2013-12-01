/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Pair;

/**
 *
 * @author rolfhaenni
 */
public interface PasswordDerivedKeyPairGenerator
			 extends KeyGenerator {

	public Pair generateKeyPair(String password);

	public Pair generateKeyPair(String password, byte[] salt);

	public Pair generatePrivateKey(String password);

	public Pair generatePrivateKey(String password, byte[] salt);

}
