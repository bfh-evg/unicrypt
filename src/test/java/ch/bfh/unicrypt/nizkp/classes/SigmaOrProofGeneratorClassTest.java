package ch.bfh.unicrypt.nizkp.classes;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.unicrypt.encryption.classes.ElGamalEncryptionClass;
import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.classes.ProductGroupClass;
import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;

public class SigmaOrProofGeneratorClassTest {

	DDHGroup ddhGroup;
	AtomicElement generators[];
	ElGamalEncryptionClass elgamal[];
	Function functions[];
	Random random;

	private final int NUMBER_OF_FUNCTIONS = 10;

	@BeforeClass
	public static void setUpClass() throws Exception {

	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {

		ddhGroup = new GStarSaveClass(BigInteger.valueOf(167));

		generators = new AtomicElement[NUMBER_OF_FUNCTIONS];
		elgamal = new ElGamalEncryptionClass[NUMBER_OF_FUNCTIONS];
		functions = new Function[NUMBER_OF_FUNCTIONS];
		random = new SecureRandom();

		for (int i = 0; i < NUMBER_OF_FUNCTIONS; i++) {
			generators[i] = ddhGroup.createRandomGenerator(random);
			elgamal[i] = new ElGamalEncryptionClass(ddhGroup, generators[i]);
			functions[i] = elgamal[i].getEncryptionFunctionLeft();
		}

	}

	@After
	public void tearDown() {
	}

	@Test
	public void testProofOk() {

		// Create an element for which we want to generate the proof
		Element valueToProof = ddhGroup.getZPlusModOrder().createRandomElement(
				random);

		// Arbitrarily choose a random index which defines the function we'll
		// use in the proof
		int index = random.nextInt(NUMBER_OF_FUNCTIONS);

		// Apply the value to the function
		Element image = functions[index].apply(valueToProof);

		// Get an instance of the SigmaOrProofGenerator
		SigmaOrProofGeneratorClass sopg = new SigmaOrProofGeneratorClass(
				functions);

		// Assembling the public input of the proof
		Element[] publicProofInput = new Element[NUMBER_OF_FUNCTIONS];
		for (int j = 0; j < NUMBER_OF_FUNCTIONS; j++) {
			publicProofInput[j] = functions[j].getCoDomain()
					.createRandomElement();
		}
		publicProofInput[index] = image;
		Element publicProofInputElement = (ProductGroupClass
				.createTupleElement(publicProofInput));

		// generate the proof
		Element proof = sopg.generate(valueToProof, index,
				publicProofInputElement, null, random);

		// validate the proof
		assertTrue(sopg.verify((Tuple) proof, publicProofInputElement));
	}

	@Test
	public void testProofWrongIndex() {
		// Create an element for which we want to generate the proof
		Element valueToProof = ddhGroup.getZPlusModOrder().createRandomElement(
				random);

		// Arbitrarily choose a random index which defines the function we'll
		// use in the proof
		int index = random.nextInt(NUMBER_OF_FUNCTIONS);

		// Apply the value to the function
		Element image = functions[index].apply(valueToProof);

		// Get an instance of the SigmaOrProofGenerator
		SigmaOrProofGeneratorClass sopg = new SigmaOrProofGeneratorClass(
				functions);

		// Assembling the public input of the proof
		Element[] publicProofInput = new Element[NUMBER_OF_FUNCTIONS];
		for (int j = 0; j < NUMBER_OF_FUNCTIONS; j++) {
			publicProofInput[j] = functions[j].getCoDomain()
					.createRandomElement();
		}
		publicProofInput[index] = image;
		Element publicProofInputElement = (ProductGroupClass
				.createTupleElement(publicProofInput));

		// generate the proof with the wrong index
		Element proof = sopg.generate(valueToProof, (index + 1) % NUMBER_OF_FUNCTIONS,
				publicProofInputElement, null, random);

		// validate the proof
		assertFalse(sopg.verify((Tuple) proof, publicProofInputElement));
	}
	
	@Test
	public void testProofWrongSecret() {
		// Create an element for which we want to generate the proof
		Element valueToProof = ddhGroup.getZPlusModOrder().createRandomElement(
				random);

		// Arbitrarily choose a random index which defines the function we'll
		// use in the proof
		int index = random.nextInt(NUMBER_OF_FUNCTIONS);

		// Apply the value to the function
		Element image = functions[index].apply(valueToProof);

		// Get an instance of the SigmaOrProofGenerator
		SigmaOrProofGeneratorClass sopg = new SigmaOrProofGeneratorClass(
				functions);

		// Assembling the public input of the proof
		Element[] publicProofInput = new Element[NUMBER_OF_FUNCTIONS];
		for (int j = 0; j < NUMBER_OF_FUNCTIONS; j++) {
			publicProofInput[j] = functions[j].getCoDomain()
					.createRandomElement();
		}
		publicProofInput[index] = image;
		Element publicProofInputElement = (ProductGroupClass
				.createTupleElement(publicProofInput));

		// Choose a different value and make sure that it is for sure different to the originally chosen secret
		Element fakeValueToProof;
		do {
			fakeValueToProof = ddhGroup.getZPlusModOrder().createRandomElement(
					random);
		} while (ddhGroup.areEqualElements(valueToProof, fakeValueToProof));
		
		// generate the proof with the wrong index
		Element proof = sopg.generate(fakeValueToProof, index,
				publicProofInputElement, null, random);

		// validate the proof
		assertFalse(sopg.verify((Tuple) proof, publicProofInputElement));
	}
}
