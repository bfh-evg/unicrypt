package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageAndProofGenerator;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PreimageAndProofGeneratorTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private GStarMod G_q1;
	final private GStarMod G_q2;
	final private StringElement proverId;

	public PreimageAndProofGeneratorTest() {
		this.G_q1 = GStarModSafePrime.getInstance(P1);
		this.G_q2 = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void testPreimageAndProof() {

		GStarMod G_q = this.G_q1;

		// Proof generator
		Function f1 = GeneratorFunction.getInstance(G_q.getElement(4));
		Function f2 = GeneratorFunction.getInstance(G_q.getElement(2));
		ProductFunction f = ProductFunction.getInstance(f1, f2);
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(f, this.proverId);
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(scg, f1, f2);
		assertTrue(pg.getProofFunctions().length == 2 && pg.getProofFunctions()[0].isEqual(f1));

		// Valid proof
		Element privateInput = Tuple.getInstance(
			   f1.getDomain().getElement(3),
			   f2.getDomain().getElement(4));
		Element publicInput = Tuple.getInstance(
			   f1.getCoDomain().getElement(64),
			   f2.getCoDomain().getElement(16));

		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());

	}

	@Test
	public void testPreimageAndProof_Invalid() {

		GStarMod G_q = this.G_q2;

		// Proof generator
		Function f1 = GeneratorFunction.getInstance(G_q.getElement(4));
		Function f2 = GeneratorFunction.getInstance(G_q.getElement(2));
		ProductFunction f = ProductFunction.getInstance(f1, f2);
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(f, this.proverId);
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(scg, f1, f2);
		assertTrue(pg.getProofFunctions().length == 2 && pg.getProofFunctions()[0].isEqual(f1));


		// Invalid proof -> One preimages is wrong
		Element privateInput = Tuple.getInstance(
			   f1.getDomain().getElement(3),
			   f2.getDomain().getElement(4));
		Element publicInput = Tuple.getInstance(
			   f1.getCoDomain().getElement(64),
			   f2.getCoDomain().getElement(32));    // Preimage = 5
		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());

	}

	@Test
	public void testPreimageAndProof_WithArity() {

		// Proof generator
		Function f1 = GeneratorFunction.getInstance(this.G_q1.getElement(2));
		ProductFunction f = ProductFunction.getInstance(f1, 3);
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(f, this.proverId);
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(scg, f1, 3);

		// Valid proof
		Element privateInput = Tuple.getInstance(
			   f1.getDomain().getElement(2),
			   f1.getDomain().getElement(3),
			   f1.getDomain().getElement(4));
		Element publicInput = Tuple.getInstance(
			   f1.getCoDomain().getElement(4),
			   f1.getCoDomain().getElement(8),
			   f1.getCoDomain().getElement(16));



		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());
	}

	@Test
	public void testPreimageAndProof_SingleFunction() {

		// Proof generator
		Function f1 = GeneratorFunction.getInstance(this.G_q1.getElement(2));
		ProductFunction f = ProductFunction.getInstance(f1);
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(f, this.proverId);
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(scg, f1, 1);

		Element privateInput = Tuple.getInstance(f1.getDomain().getElement(2));
		Element publicInput = Tuple.getInstance(f1.getCoDomain().getElement(4));

		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testPreimageAndProof_Exception() {
		Function f1 = GeneratorFunction.getInstance(this.G_q1.getElement(2));
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(f1, this.proverId);
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(scg);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPreimageAndProof_ExceptionWithArity() {
		Function f1 = GeneratorFunction.getInstance(this.G_q1.getElement(2));
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(f1, this.proverId);
		PreimageAndProofGenerator pg = PreimageAndProofGenerator.getInstance(scg, f1, 0);
	}

}
