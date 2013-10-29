/*
 * Copyright (c) 2012 Berner Fachhochschule, Switzerland.
 * Bern University of Applied Sciences, Engineering and Information Technology,
 * Research Institute for Security in the Information Society, E-Voting Group,
 * Biel, Switzerland.
 *
 * Project Univote.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package ch.bfh.unicrypt.nizkp.classes;

import ch.bfh.unicrypt.crypto.concat.classes.ConcatSchemeClass;
import ch.bfh.unicrypt.crypto.concat.interfaces.ConcatScheme;
import ch.bfh.unicrypt.crypto.schemes.encryption.old.ElGamalEncryptionSchemeOld;
import ch.bfh.unicrypt.crypto.encryption.interfaces.ElGamalEncryption;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.SigmaProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.SigmaProofGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.GStarSave;
import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.java2unicrypt.classes.ExternalDataMapperClass;
import ch.bfh.unicrypt.math.java2unicrypt.interfaces.ExternalDataMapper;
import ch.bfh.unicrypt.math.utility.mapper.classes.CharsetXRadixYMapperClass;

import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Severin Hauser <severin.hauser@bfh.ch>
 */
public class SigmaProofGeneratorClassTest {
	
	public SigmaProofGeneratorClassTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void testRunWithOpt() {
		DDHGroup ddhGroup = new GStarSave(BigInteger.valueOf(167));
		AtomicElement generator = ddhGroup.getElement(BigInteger.valueOf(11));
		String optString = "Test1";
		
		ConcatScheme concat = new ConcatSchemeClass(ConcatenateFunction.ConcatParameter.PipeBrackets,new CharsetXRadixYMapperClass(CharsetXRadixYMapperClass.DEFAULT_CHARSET,0));
		
		ElGamalEncryption elGamalEnc = new ElGamalEncryptionSchemeOld(ddhGroup, generator);
		KeyPairGenerator keyPairGen = elGamalEnc.getKeyGenerator();
		Function proofFunction = keyPairGen.getPublicKeyGenerationFunction();
		Tuple keyPair = keyPairGen.generateKeyPair();
		Element privateKey = keyPairGen.getPrivateKey(keyPair);
		AtomicElement publicKey = (AtomicElement) keyPairGen.getPublicKey(keyPair);
		SigmaProofGenerator proofGen = new SigmaProofGenerator(proofFunction);
		ExternalDataMapper externalMapper=new ExternalDataMapperClass();
		Element optStringElement = concat.concat(externalMapper.getEncodedElement(optString));
		Tuple result = proofGen.generate(privateKey, publicKey,optStringElement);
		
		BigInteger commitment = ((AtomicElement) proofGen.getCommitment(result)).getValue();
		BigInteger response = ((AtomicElement) proofGen.getResponse(result)).getValue();
		
		Element commitmentElement = ((DDHGroup) proofGen.getCommitmentSpace()).getElement(commitment);
		Element responseElement = ((ZPlusMod) proofGen.getResponseSpace()).getElement(response);
		Tuple proofReconstructed = proofGen.getProofSpace().getElement(commitmentElement, responseElement);
		assertTrue(proofGen.verify(proofReconstructed, publicKey,optStringElement));
	}
  @Test
  public void testRunWithoutOpt() {
    DDHGroup ddhGroup = new GStarSave(BigInteger.valueOf(167));
    AtomicElement generator = ddhGroup.getElement(BigInteger.valueOf(11));

    ConcatScheme concat = new ConcatSchemeClass(ConcatenateFunction.ConcatParameter.PipeBrackets,new CharsetXRadixYMapperClass(CharsetXRadixYMapperClass.DEFAULT_CHARSET,0));

    ElGamalEncryption elGamalEnc = new ElGamalEncryptionSchemeOld(ddhGroup, generator);
    KeyPairGenerator keyPairGen = elGamalEnc.getKeyGenerator();
    Function proofFunction = keyPairGen.getPublicKeyGenerationFunction();
    Tuple keyPair = keyPairGen.generateKeyPair();
    Element privateKey = keyPairGen.getPrivateKey(keyPair);
    AtomicElement publicKey = (AtomicElement) keyPairGen.getPublicKey(keyPair);
    SigmaProofGenerator proofGen = new SigmaProofGenerator(proofFunction);

    Tuple result = proofGen.generate(privateKey, publicKey);

    BigInteger commitment = ((AtomicElement) proofGen.getCommitment(result)).getValue();
    BigInteger response = ((AtomicElement) proofGen.getResponse(result)).getValue();

    Element commitmentElement = ((DDHGroup) proofGen.getCommitmentSpace()).getElement(commitment);
    Element responseElement = ((ZPlusMod) proofGen.getResponseSpace()).getElement(response);
    Tuple proofReconstructed = proofGen.getProofSpace().getElement(commitmentElement, responseElement);
    assertTrue(proofGen.verify(proofReconstructed, publicKey));
  }




}
