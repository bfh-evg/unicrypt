/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.encryption.classes;

import ch.bfh.unicrypt.crypto.schemes.encryption.old.ElGamalEncryptionSchemeOld;
import ch.bfh.unicrypt.crypto.keygen.interfaces.DDHGroupDistributedKeyPairGenerator;
import ch.bfh.unicrypt.crypto.keygen.interfaces.DDHGroupKeyPairGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.GStarSave;
import ch.bfh.unicrypt.math.group.classes.PowerGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.utility.RandomUtil;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reto
 */
public class ElGamalEncryptionClassTest {

	public ElGamalEncryptionClassTest() {
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

	/**
	 * Test of encrypt method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testEncrypt_Element_Element() {
		System.out.println("encrypt");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair = instance.getKeyGenerator().generateKey(random);
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple ciphertext1 = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), plaintext, random);
		assertFalse(plaintext.equals(ciphertext1));
		random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple ciphertext2 = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), plaintext, random);
		assertEquals(ciphertext1, ciphertext2);

	}

	/**
	 * Test of encrypt method, of class ElGamalEncryptionClass.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEncrypt_3args_1() {
		System.out.println("encrypt");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair = instance.getKeyGenerator().generateKey(random);
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), null);
		assertFalse(plaintext.equals(ciphertext));
		AtomicElement decryption = instance.decrypt(instance.getKeyGenerator().getPrivateKey(keyPair), ciphertext);
		assertEquals(plaintext, decryption);

	}

	/**
	 * Test of encrypt method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testEncrypt_3args_2() {
		System.out.println("encrypt");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair = instance.getKeyGenerator().generateKey(random);
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), plaintext);
		assertFalse(plaintext.equals(ciphertext));
		AtomicElement decryption = instance.decrypt(instance.getKeyGenerator().getPrivateKey(keyPair), ciphertext);
		assertEquals(plaintext, decryption);

	}

	/**
	 * Test of encrypt method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testEncrypt_Full() {
		System.out.println("encrypt");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());

		Set<Tuple> keyPairs = new HashSet<Tuple>();
		while (keyPairs.size() < group.getMinOrderGroup().getOrder().intValue()) {
			Tuple keyPair = instance.getKeyGenerator().generateKey(random);
			if (keyPairs.add(keyPair)) {
				Set<Element> plaintexts = new HashSet<Element>();
				while (plaintexts.size() < group.getOrder().intValue()) {
					Element plaintext = group.getRandomElement(random);
					if (plaintexts.add(plaintext)) {
						Set<Tuple> cipherTexts = new HashSet<Tuple>();
						while (cipherTexts.size() < group.getMinOrderGroup().getOrder().intValue()) {
							Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), plaintext, random);
							if (cipherTexts.add(ciphertext)) {
								AtomicElement decryption = instance.decrypt(instance.getKeyGenerator().getPrivateKey(keyPair), ciphertext);
								assertEquals(plaintext, decryption);
							}

						}

					}
				}
			}

		}


	}

	/**
	 * Test of reEncrypt method, of class ElGamalEncryptionClass.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReEncrypt_Element_Element() {
		System.out.println("reEncrypt");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair = instance.getKeyGenerator().generateKey(random);
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), plaintext, random);
		Tuple ciphertext2 = instance.reEncrypt(null, null);
	}

	/**
	 * Test of reEncrypt method, of class ElGamalEncryptionClass.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReEncrypt_3args_1() {
		System.out.println("reEncrypt");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair = instance.getKeyGenerator().generateKey(random);
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), plaintext, random);
		Tuple ciphertext2 = instance.reEncrypt(instance.getKeyGenerator().getPublicKey(keyPair), null);

	}

	/**
	 * Test of reEncrypt method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testReEncrypt_3args_2() {
		System.out.println("reEncrypt");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair = instance.getKeyGenerator().generateKey(random);
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), plaintext, random);
		Tuple ciphertext2 = instance.reEncrypt(instance.getKeyGenerator().getPublicKey(keyPair), ciphertext, random);
		System.out.println(ciphertext);
		System.out.println(ciphertext2);
		assertFalse(ciphertext.isEqual(ciphertext2));
		AtomicElement decryption = instance.decrypt(instance.getKeyGenerator().getPrivateKey(keyPair), ciphertext2);
		assertEquals(plaintext, decryption);
	}

	/**
	 * Test of decrypt method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testDecrypt() {
		System.out.println("decrypt");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair = instance.getKeyGenerator().generateKey(random);
		random = RandomUtil.getSecureRandom("123".getBytes());
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair), plaintext, random);
		AtomicElement partial1 = instance.decrypt(instance.getKeyGenerator().getPrivateKey(keyPair), ciphertext);
		assertEquals(plaintext, partial1);
	}

	/**
	 * Test of partialDecrypt method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testPartialDecrypt() {
		System.out.println("getPartialDecryptionFunction");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair1 = instance.getKeyGenerator().generateKey(random);
		random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple keyPair2 = instance.getKeyGenerator().generateKey(random);
		System.out.println(keyPair1);
		System.out.println(keyPair2);
		assertEquals(keyPair1, keyPair2);

		Element combinedPublicKey = instance.getKeyGenerator().combinePublicKeys(instance.getKeyGenerator().getPublicKey(keyPair1), instance.getKeyGenerator().getPublicKey(keyPair2));
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(combinedPublicKey, plaintext);
		AtomicElement partial1 = instance.partialDecrypt(instance.getKeyGenerator().getPrivateKey(keyPair1), ciphertext);
		AtomicElement partial2 = instance.partialDecrypt(instance.getKeyGenerator().getPrivateKey(keyPair2), ciphertext);
		assertEquals(partial1, partial2);
	}

	/**
	 * Test of combinePartialDecryptions method, of class
	 * ElGamalEncryptionClass.
	 */
	@Test
	public void testCombinePartialDecryptions() {
		System.out.println("testCombinePartialDecryptions");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Tuple keyPair1 = instance.getKeyGenerator().generateKey();
		Tuple keyPair2 = instance.getKeyGenerator().generateKey();
		Element combinedPublicKey = instance.getKeyGenerator().combinePublicKeys(instance.getKeyGenerator().getPublicKey(keyPair1), instance.getKeyGenerator().getPublicKey(keyPair2));
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(combinedPublicKey, plaintext);
		AtomicElement partial1 = instance.partialDecrypt(instance.getKeyGenerator().getPrivateKey(keyPair1), ciphertext);
		AtomicElement partial2 = instance.partialDecrypt(instance.getKeyGenerator().getPrivateKey(keyPair2), ciphertext);
		Element decrypted = instance.combinePartialDecryptions(ciphertext, partial1, partial2);
		assertEquals(plaintext, decrypted);
	}

	/**
	 * Test of getKeyGenerator method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testGetKeyGenerator() {
		System.out.println("testGetKeyGenerator");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Tuple keyPair = instance.getKeyGenerator().generateKey();
		assertNotNull(keyPair);
	}

	/**
	 * Test of getEncryptionFunction method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testGetEncryptionFunction() {
		System.out.println("getEncryptionFunction");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Tuple keyPair1 = instance.getKeyGenerator().generateKey();
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair1), plaintext, random);
		random = RandomUtil.getSecureRandom("123".getBytes());
		final Element randomization = instance.getRandomizationSpace().getRandomElement(random);
		Function function = instance.getEncryptionFunction();
		Element ciphertext2 = function.apply(instance.getKeyGenerator().getPublicKey(keyPair1), plaintext, randomization);
		assertEquals(ciphertext, ciphertext2);
	}

	/**
	 * Test of getEncryptionFunctionLeft method, of class
	 * ElGamalEncryptionClass.
	 */
	@Test
	public void testGetEncryptionFunctionLeft() {
		System.out.println("getEncryptionFunctionLeft");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Tuple keyPair1 = instance.getKeyGenerator().generateKey();
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair1), plaintext, random);
		random = RandomUtil.getSecureRandom("123".getBytes());
		final Element randomization = instance.getRandomizationSpace().getRandomElement(random);
		Function functionLeft = instance.getEncryptionFunctionLeft();
		Element leftPart = functionLeft.apply(randomization);
		assertEquals(ciphertext.getElementAt(0), leftPart);
	}

	/**
	 * Test of getEncryptionFunctionRight method, of class
	 * ElGamalEncryptionClass.
	 */
	@Test
	public void testGetEncryptionFunctionRight() {
		System.out.println("getEncryptionFunctionRight");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Tuple keyPair1 = instance.getKeyGenerator().generateKey();
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair1), plaintext, random);
		random = RandomUtil.getSecureRandom("123".getBytes());
		final Element randomization = instance.getRandomizationSpace().getRandomElement(random);
		Function functionRight = instance.getEncryptionFunctionRight();
		Element rightPart = functionRight.apply(instance.getKeyGenerator().getPublicKey(keyPair1), plaintext, randomization);
		assertEquals(ciphertext.getElementAt(1), rightPart);
	}

	/**
	 * Test of getIdentityEncryptionFunction method, of class
	 * ElGamalEncryptionClass.
	 */
	@Test
	public void testGetIdentityEncryptionFunction() {

		System.out.println("getIdentityEncryptionFunction");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Tuple keyPair1 = instance.getKeyGenerator().generateKey();
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Random random = RandomUtil.getSecureRandom("123".getBytes());
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair1), plaintext, random);
		Function result = instance.getEncryptionFunction();
		random = RandomUtil.getSecureRandom("123".getBytes());
		Element result11 = result.apply(instance.getKeyGenerator().getPublicKey(keyPair1), plaintext, instance.getRandomizationSpace().getRandomElement(random));
		assertEquals(ciphertext, result11);
	}

	/**
	 * Test of getDecryptionFunction method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testGetDecryptionFunction() {
		System.out.println("getDecryptionFunction");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Tuple keyPair1 = instance.getKeyGenerator().generateKey();
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(instance.getKeyGenerator().getPublicKey(keyPair1), plaintext);
		Element decrypted = instance.decrypt(instance.getKeyGenerator().getPrivateKey(keyPair1), ciphertext);
		Function result = instance.getDecryptionFunction();
		Element result11 = result.apply(instance.getKeyGenerator().getPrivateKey(keyPair1), ciphertext);
		assertEquals(decrypted, result11);
		assertEquals(plaintext, decrypted);
	}

	/**
	 * Test of getPartialDecryptionFunction method, of class
	 * ElGamalEncryptionClass.
	 */
	@Test
	public void testGetPartialDecryptionFunction() {
		System.out.println("getPartialDecryptionFunction");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Tuple keyPair1 = instance.getKeyGenerator().generateKey();
		Tuple keyPair2 = instance.getKeyGenerator().generateKey();
		Element combinedPublicKey = instance.getKeyGenerator().combinePublicKeys(instance.getKeyGenerator().getPublicKey(keyPair1), instance.getKeyGenerator().getPublicKey(keyPair2));
		Element plaintext = group.getElement(BigInteger.valueOf(13));
		Tuple ciphertext = instance.encrypt(combinedPublicKey, plaintext);
		AtomicElement partial1 = instance.partialDecrypt(instance.getKeyGenerator().getPrivateKey(keyPair1), ciphertext);
		AtomicElement partial2 = instance.partialDecrypt(instance.getKeyGenerator().getPrivateKey(keyPair2), ciphertext);
		Element decrypted = instance.combinePartialDecryptions(ciphertext, partial1, partial2);
		Function result = instance.getPartialDecryptionFunction();
		Element partial11 = result.apply(instance.getKeyGenerator().getPrivateKey(keyPair1), ((Tuple) ciphertext).getElementAt(0));
		assertEquals(partial1, partial11);
		assertEquals(plaintext, decrypted);

	}

	/**
	 * Test of getPlaintextSpace method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testGetPlaintextSpace() {
		System.out.println("getPlaintextSpace");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		DDHGroup expResult = group;
		DDHGroup result = instance.getPlaintextSpace();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getRandomizationSpace method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testGetRandomizationSpace() {
		System.out.println("getRandomizationSpace");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		ZPlusMod expResult = group.getMinOrderGroup();
		ZPlusMod result = instance.getRandomizationSpace();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getCiphertextSpace method, of class ElGamalEncryptionClass.
	 */
	@Test
	public void testGetCiphertextSpace() {
		System.out.println("getCiphertextSpace");
		DDHGroup group = new GStarSave(BigInteger.valueOf(23));
		ElGamalEncryptionSchemeOld instance = new ElGamalEncryptionSchemeOld(group);
		Group expResult = new PowerGroup(group, 2);
		Group result = instance.getCiphertextSpace();
		assertEquals(expResult, result);
	}
        
        /**
         * Test of createValididtyProof and verifyValidityProof methods, of class ElGamalEncryptionClass.
         * One time with a correct proof
         * One time with an incorrect proof
         */
        @Test
        public void testValidityProof1(){
            
            final GStarSave g_q = new GStarSave(BigInteger.valueOf(23), true);
            final AtomicElement generator = g_q.getElement(BigInteger.valueOf(2));
            final ElGamalEncryptionSchemeOld ecs = new ElGamalEncryptionSchemeOld(g_q, generator);
            //keys
            final DDHGroupKeyPairGenerator keyGen = ecs.getKeyGenerator();
     	    final Tuple keyPair = keyGen.generateKeyPair();

            final ZPlusMod z_q = g_q.getOrderGroup();
	    
            //Possible messages: g^0 = 1 and g^1 = 2 and g^2 = 4
	    final AtomicElement possibleMessagesList[] = { g_q.getElement(generator.getValue().modPow(BigInteger.ZERO, g_q.getModulus())), g_q.getElement(generator.getValue().modPow(BigInteger.ONE, g_q.getModulus())), g_q.getElement(generator.getValue().modPow(BigInteger.valueOf(2), g_q.getModulus())) };           
	    final AtomicElement randomization = z_q.getElement(10);
	    
	    //encrypt possibleMessagesList[0]
	    final Element ciphertext = ecs.encrypt(keyGen.getPublicKey(keyPair), possibleMessagesList[0], randomization);
	    
            //compute proof 1 with index=0
            final Tuple validityProof1 = ecs.createValididtyProof(randomization, keyGen.getPublicKey(keyPair), 0, null, possibleMessagesList);
	    
            assertTrue(ecs.verifyValidityProof(validityProof1, ciphertext, keyGen.getPublicKey(keyPair), possibleMessagesList));
            
            //compute proof 2 with index=1
            final Tuple validityProof2 = ecs.createValididtyProof(randomization, keyGen.getPublicKey(keyPair), 1, null, possibleMessagesList);
	    
            assertFalse(ecs.verifyValidityProof(validityProof2, ciphertext, keyGen.getPublicKey(keyPair), possibleMessagesList));  
        }
        
        /**
         * Test of createValididtyProof method, of class ElGamalEncryptionClass.
         * compute proof with index bigger than the possible message array => exception
         */
        @Test(expected=IllegalArgumentException.class)
        public void testValidityProof2(){
            
            final GStarSave g_q = new GStarSave(BigInteger.valueOf(23), true);
            final AtomicElement generator = g_q.getElement(BigInteger.valueOf(2));
            final ElGamalEncryptionSchemeOld ecs = new ElGamalEncryptionSchemeOld(g_q, generator);
            //keys
            final DDHGroupKeyPairGenerator keyGen = ecs.getKeyGenerator();
     	    final Tuple keyPair = keyGen.generateKeyPair();

            final ZPlusMod z_q = g_q.getOrderGroup();
	    
            //Possible messages: g^0 = 1 and g^1 = 2
	    final AtomicElement possibleMessagesList[] = { g_q.getElement(generator.getValue().modPow(BigInteger.ZERO, g_q.getModulus())), g_q.getElement(generator.getValue().modPow(BigInteger.ONE, g_q.getModulus())) };           
	    final AtomicElement randomization = z_q.getElement(10);
	    
            //compute proof with index bigger than the possible message array
            final Tuple validityProof = ecs.createValididtyProof(randomization, keyGen.getPublicKey(keyPair), 2, null, possibleMessagesList);
       }
        
         /**
         * Test of createValididtyProof method, of class ElGamalEncryptionClass.
         * compute proof with a too small possible message array => exception
         */
        @Test(expected=IllegalArgumentException.class)
        public void testValidityProof3(){
            
            final GStarSave g_q = new GStarSave(BigInteger.valueOf(23), true);
            final AtomicElement generator = g_q.getElement(BigInteger.valueOf(2));
            final ElGamalEncryptionSchemeOld ecs = new ElGamalEncryptionSchemeOld(g_q, generator);
            //keys
            final DDHGroupKeyPairGenerator keyGen = ecs.getKeyGenerator();
     	    final Tuple keyPair = keyGen.generateKeyPair();

            final ZPlusMod z_q = g_q.getOrderGroup();
	    
            //Possible messages: g^0 = 1
	    final AtomicElement possibleMessagesList[] = { g_q.getElement(generator.getValue().modPow(BigInteger.ZERO, g_q.getModulus())) };           
	    final AtomicElement randomization = z_q.getElement(10);
	    
            //compute proof with only one possible message
            final Tuple validityProof = ecs.createValididtyProof(randomization, keyGen.getPublicKey(keyPair), 0, null, possibleMessagesList);
       }
        
        /**
         * Test of verifyValidityProof method, of class ElGamalEncryptionClass.
         * verify proof with a too small possible message array => exception
         */
        @Test(expected=IllegalArgumentException.class)
        public void testValidityProof4(){
            
            final GStarSave g_q = new GStarSave(BigInteger.valueOf(23), true);
            final AtomicElement generator = g_q.getElement(BigInteger.valueOf(2));
            final ElGamalEncryptionSchemeOld ecs = new ElGamalEncryptionSchemeOld(g_q, generator);
            //keys
            final DDHGroupKeyPairGenerator keyGen = ecs.getKeyGenerator();
     	    final Tuple keyPair = keyGen.generateKeyPair();

            final ZPlusMod z_q = g_q.getOrderGroup();
	    
            //Possible messages: g^0 = 1 and g^1=2
	    AtomicElement possibleMessagesList[] = { g_q.getElement(generator.getValue().modPow(BigInteger.ZERO, g_q.getModulus())), g_q.getElement(generator.getValue().modPow(BigInteger.ONE, g_q.getModulus())) };           
	    final AtomicElement randomization = z_q.getElement(10);
	    
	    //encrypt possibleMessagesList[0]
	    final Element ciphertext = ecs.encrypt(keyGen.getPublicKey(keyPair), possibleMessagesList[0], randomization);
	    
            //compute proof 1 with index=0
            final Tuple validityProof1 = ecs.createValididtyProof(randomization, keyGen.getPublicKey(keyPair), 0, null, possibleMessagesList);
	    
            //test with a to small possibleMessagesList array
            AtomicElement possibleMessagesList2[] = { g_q.getElement(generator.getValue().modPow(BigInteger.ZERO, g_q.getModulus()))};
            ecs.verifyValidityProof(validityProof1, ciphertext, keyGen.getPublicKey(keyPair), possibleMessagesList2);
       }
        
        /**
         * Test of verifyValidityProof method, of class ElGamalEncryptionClass.
         * verify with a to small proof
         */
       @Test(expected=IllegalArgumentException.class)
        public void testValidityProof5(){
            
            final GStarSave g_q = new GStarSave(BigInteger.valueOf(23), true);
            final AtomicElement generator = g_q.getElement(BigInteger.valueOf(2));
            final ElGamalEncryptionSchemeOld ecs = new ElGamalEncryptionSchemeOld(g_q, generator);
            //keys
            final DDHGroupKeyPairGenerator keyGen = ecs.getKeyGenerator();
     	    final Tuple keyPair = keyGen.generateKeyPair();

            final ZPlusMod z_q = g_q.getOrderGroup();
	    
            //Possible messages: g^0 = 1 and g^1 = 2 and g^2 = 4
	    final AtomicElement possibleMessagesList[] = { g_q.getElement(generator.getValue().modPow(BigInteger.ZERO, g_q.getModulus())), g_q.getElement(generator.getValue().modPow(BigInteger.ONE, g_q.getModulus())), g_q.getElement(generator.getValue().modPow(BigInteger.valueOf(2), g_q.getModulus())) };           
	    final AtomicElement randomization = z_q.getElement(10);
	    
	    //encrypt possibleMessagesList[0]
	    final Element ciphertext = ecs.encrypt(keyGen.getPublicKey(keyPair), possibleMessagesList[0], randomization);
	    
            //compute proof 1 with index=0
            final Tuple validityProof1 = ecs.createValididtyProof(randomization, keyGen.getPublicKey(keyPair), 0, null, possibleMessagesList);
	    
            //verify with a to small proof
            final Tuple validityProof2 = ProductGroup.createTupleElement(validityProof1.getElementAt(0),validityProof1.getElementAt(1));
            ecs.verifyValidityProof(validityProof2, ciphertext, keyGen.getPublicKey(keyPair), possibleMessagesList);
       }
        
        
}