package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageEqualityProofGenerator;
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

public class PreimageEqualityProofGeneratorTest {

	final static int P = 167;
	final static int P2 = 23;
	final private MultiplicativeCyclicGroup G_q;
	final private MultiplicativeCyclicGroup G_q2;

	public PreimageEqualityProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.G_q2 = GStarModSafePrime.getInstance(P2);
	}

	@Test
	public void TestPreimageEqualityProof() {

		// Proof generator
		Function f1 = GeneratorFunction.getInstance(this.G_q.getElement(4));
		Function f2 = GeneratorFunction.getInstance(this.G_q.getElement(2));
		PreimageEqualityProofGenerator pg = PreimageEqualityProofGenerator.getInstance(f1, f2);
		assertTrue(pg.getProofFunctions().length == 2 && pg.getProofFunctions()[0].isEqual(f1));

		// Valid proof
		Element privateInput = f1.getDomain().getElement(3);
		Element publicInput = Tuple.getInstance(
			   f1.getCoDomain().getElement(64),
			   f2.getCoDomain().getElement(8));
		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

		Tuple proof = pg.generate(privateInput, publicInput, proverId);

		BooleanElement v = pg.verify(proof, publicInput, proverId);
		assertTrue(v.getBoolean());

		// Invalid proof -> preimages are not equal
		privateInput = f1.getDomain().getElement(3);
		publicInput = Tuple.getInstance(
			   f1.getCoDomain().getElement(64), // Preimage = 3
			   f2.getCoDomain().getElement(16));    // Preimage = 4
		proof = pg.generate(privateInput, publicInput, proverId);
		v = pg.verify(proof, publicInput, proverId);
		assertTrue(!v.getBoolean());

	}

	@Test(expected = IllegalArgumentException.class)
	public void TestPreimageEqualityProofException() {
		Function f1 = GeneratorFunction.getInstance(this.G_q.getElement(4));
		Function f2 = GeneratorFunction.getInstance(this.G_q.getElement(2));
		Function f3 = GeneratorFunction.getInstance(this.G_q2.getElement(4));
		PreimageEqualityProofGenerator pg = PreimageEqualityProofGenerator.getInstance(f1, f2, f3);
	}

}
