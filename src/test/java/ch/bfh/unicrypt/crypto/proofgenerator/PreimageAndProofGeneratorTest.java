package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageAndProofGenerator;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PreimageAndProofGeneratorTest {

	final static int P = 167;
	final static int P2 = 23;
	final private MultiplicativeCyclicGroup G_q;
	final private MultiplicativeCyclicGroup G_q2;

	public PreimageAndProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.G_q2 = GStarModSafePrime.getInstance(P2);
	}

	@Test
	public void TestPreimageAndProof() {

		// Proof generator
		Function f1 = GeneratorFunction.getInstance(this.G_q.getElement(4));
		Function f2 = GeneratorFunction.getInstance(this.G_q.getElement(2));
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(f1, f2);
		assertTrue(pg.getProofFunctions().length == 2 && pg.getProofFunctions()[0].isEqual(f1));

		// Valid proof
		Element privateInput = Tuple.getInstance(
			   f1.getDomain().getElement(3),
			   f2.getDomain().getElement(4));
		Element publicInput = Tuple.getInstance(
			   f1.getCoDomain().getElement(64),
			   f2.getCoDomain().getElement(16));
		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

		Tuple proof = pg.generate(privateInput, publicInput, proverId);
		BooleanElement v = pg.verify(proof, publicInput, proverId);
		assertTrue(v.getBoolean());

		// Invalid proof -> One preimages is wrong
		privateInput = Tuple.getInstance(
			   f1.getDomain().getElement(3),
			   f2.getDomain().getElement(4));
		publicInput = Tuple.getInstance(
			   f1.getCoDomain().getElement(64),
			   f2.getCoDomain().getElement(32));    // Preimage = 5
		proof = pg.generate(privateInput, publicInput, proverId);
		v = pg.verify(proof, publicInput, proverId);
		assertTrue(!v.getBoolean());
	}

	@Test
	public void TestPreimageAndProof_WithArity() {

		// Proof generator
		Function f1 = GeneratorFunction.getInstance(this.G_q.getElement(2));
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(f1, 3);

		// Valid proof
		Element privateInput = Tuple.getInstance(
			   f1.getDomain().getElement(2),
			   f1.getDomain().getElement(3),
			   f1.getDomain().getElement(4));
		Element publicInput = Tuple.getInstance(
			   f1.getCoDomain().getElement(4),
			   f1.getCoDomain().getElement(8),
			   f1.getCoDomain().getElement(16));

		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

		Tuple proof = pg.generate(privateInput, publicInput, proverId);
		BooleanElement v = pg.verify(proof, publicInput, proverId);
		assertTrue(v.getBoolean());
	}

	@Test
	public void TestPreimageAndProof_SingleFunction() {

		// Proof generator
		Function f1 = GeneratorFunction.getInstance(this.G_q.getElement(2));
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(f1);

		// Valid proof
		Element privateInput = Tuple.getInstance(f1.getDomain().getElement(2));
		Element publicInput = Tuple.getInstance(f1.getCoDomain().getElement(4));

		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

		Tuple proof = pg.generate(privateInput, publicInput, proverId);
		BooleanElement v = pg.verify(proof, publicInput, proverId);
		assertTrue(v.getBoolean());

		// With arity
		pg = PreimageAndProofGenerator.getInstance(f1, 1);
		proof = pg.generate(privateInput, publicInput, proverId);
		v = pg.verify(proof, publicInput, proverId);
		assertTrue(v.getBoolean());

	}

	@Test(expected = IllegalArgumentException.class)
	public void TestPreimageAndProof_Exception() {
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance();
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestPreimageAndProof_ExceptionWithArity() {
		Function f1 = GeneratorFunction.getInstance(this.G_q.getElement(2));
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(f1, 0);
	}

}
