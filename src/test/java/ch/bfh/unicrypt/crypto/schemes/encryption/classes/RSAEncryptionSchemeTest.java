/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrimePair;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class RSAEncryptionSchemeTest {

	@Test
	public void EncryptionDecryptionTest() {
		RSAEncryptionScheme instance = RSAEncryptionScheme.getInstance(ZModPrimePair.getInstance(3, 5));
		Element prKey = instance.abstractGetKeyPairGenerator().generatePrivateKey();
		Element puKey = instance.abstractGetKeyPairGenerator().generatePublicKey(prKey);
		Element message = instance.getMessageSpace().getElement(5);
		Element encryption = instance.encrypt(puKey, message);
		Element decryption = instance.decrypt(prKey, message);
		//	Assert.assertTrue(encryption.isEquivalent(decryption));
		System.out.println(Tuple.getInstance(prKey, puKey, message, encryption, decryption));
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

}
