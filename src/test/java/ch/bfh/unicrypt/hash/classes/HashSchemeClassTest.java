package ch.bfh.unicrypt.hash.classes;

import java.math.BigInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.crypto.concat.classes.ConcatSchemeClass;
import ch.bfh.unicrypt.crypto.concat.interfaces.ConcatScheme;
import ch.bfh.unicrypt.crypto.encryption.classes.AESEncryptionScheme;
import ch.bfh.unicrypt.crypto.hash.classes.HashSchemeClass;
import ch.bfh.unicrypt.crypto.hash.interfaces.HashScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.classes.HashFunction.HashAlgorithm;
import ch.bfh.unicrypt.math.additive.classes.ZPlus;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.java2unicrypt.classes.ExternalDataMapperClass;
import ch.bfh.unicrypt.math.utility.mapper.classes.CharsetXRadixYMapperClass;

@SuppressWarnings("static-method")
public class HashSchemeClassTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashSchemeClassStringConcatSchemeNullstringNullConcat() {
		new HashSchemeClass((String) null, (ConcatScheme) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashSchemeClassStringConcatSchemeNullstringConcat() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		new HashSchemeClass((String) null, concat);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashSchemeClassStringConcatSchemeStringNullConcat() {
		new HashSchemeClass(HashAlgorithm.SHA1.getAlgorithmName(), (ConcatScheme) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashSchemeClassStringConcatSchemeWrongStringConcat() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		new HashSchemeClass("No--", concat);
	}

	@Test
	public void testHashSchemeClassStringConcatSchemeStringConcat() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		new HashSchemeClass(HashAlgorithm.SHA1.getAlgorithmName(), concat);
	}

	@Test
	public void testHashSchemeClassHashAlgorithmConcatScheme() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		new HashSchemeClass(HashAlgorithm.SHA1, concat);
	}

	@Test
	public void testHashSchemeClassStringConcatSchemeZPlusMod() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		HashScheme scheme = new HashSchemeClass(HashAlgorithm.SHA1.getAlgorithmName(), concat, HashAlgorithm.SHA1.getCoDomain());
		scheme.getHashFunction().getCoDomain().equals(HashAlgorithm.SHA1.getCoDomain());
	}

	@Test
	public void testHashSchemeClassStringConcatSchemeZPlusMod6() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		ZPlusMod coDomain = new ZPlusModClass(BigInteger.valueOf(6));
		HashScheme scheme = new HashSchemeClass(HashAlgorithm.SHA1.getAlgorithmName(), concat, coDomain);
		scheme.getHashFunction().getCoDomain().equals(coDomain);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashSchemeClassHashAlgorithmConcatSchemeZPlusMod() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		new HashSchemeClass(HashAlgorithm.SHA1.getAlgorithmName(), concat, null);
	}

	@Test
	public void testHash() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		HashScheme scheme = new HashSchemeClass(HashAlgorithm.SHA1.getAlgorithmName(), concat, HashAlgorithm.SHA1.getCoDomain());
		scheme.getHashFunction().getCoDomain().equals(HashAlgorithm.SHA1.getCoDomain());
		AtomicElement element = scheme.hash(ZPlus.getInstance().createEncodedElement(new BigInteger("abcde".getBytes())));
		Assert.assertEquals(new BigInteger("03de6c570bfe24bfc328ccd7ca46b76eadaf4334", 16), element.getValue());
	}

	@Test
	public void testHashTwice() {
		String password = "test";
		String saltString = "1234";

		String hashString1 = null;
		String hashString2 = null;
		{
			AESEncryptionScheme aes = new AESEncryptionScheme();
			byte[] salt =
					(new BigInteger(saltString, 10)).toByteArray();
			Element key = aes.getKeyGenerator().generateKey(password, salt);
			String keyString = ((AtomicElement) key).getValue().toString(10);
			String tmp = keyString + saltString;
			ExternalDataMapperClass mapper = new ExternalDataMapperClass();
			Element elementToHash = mapper.getEncodedElement(tmp);
			/////////////////////////////////
			//Create Hash
			ConcatScheme concat = new ConcatSchemeClass(ConcatenateFunction.ConcatParameter.Plain,
					new CharsetXRadixYMapperClass());
			HashScheme sha256 = new HashSchemeClass(HashFunction.SHA256, concat);
			AtomicElement hashResult = sha256.hash(elementToHash);
			hashString1 = hashResult.getValue().toString(10);
		}
		{
			AESEncryptionScheme aes = new AESEncryptionScheme();
			byte[] salt =
					(new BigInteger(saltString, 10)).toByteArray();
			Element key = aes.getKeyGenerator().generateKey(password, salt);
			String keyString = ((AtomicElement) key).getValue().toString(10);
			String tmp = keyString + saltString;
			ExternalDataMapperClass mapper = new ExternalDataMapperClass();
			Element elementToHash = mapper.getEncodedElement(tmp);
			/////////////////////////////////
			//Create Hash
			ConcatScheme concat = new ConcatSchemeClass(ConcatenateFunction.ConcatParameter.Plain,
					new CharsetXRadixYMapperClass());
			HashScheme sha256 = new HashSchemeClass(HashFunction.SHA256, concat);
			AtomicElement hashResult = sha256.hash(elementToHash);
			hashString2 = hashResult.getValue().toString(10);
		}
		
		Assert.assertEquals(hashString1, hashString2);
	}

	@Test
	public void testGetHashFunction() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		HashScheme scheme = new HashSchemeClass(HashAlgorithm.SHA1.getAlgorithmName(), concat, HashAlgorithm.SHA1.getCoDomain());
		Assert.assertNotNull(scheme.getHashFunction());
		//TODO: Warum kann aus der HashFunction nicht der Hash-Algorithmus ausgelesen werden? Methode fehlt!

	}

	@Test
	public void testGetConcatScheme() {
		final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
		HashScheme scheme = new HashSchemeClass(HashAlgorithm.SHA1.getAlgorithmName(), concat, HashAlgorithm.SHA1.getCoDomain());
		Assert.assertEquals(concat, scheme.getConcatScheme());

	}
}
