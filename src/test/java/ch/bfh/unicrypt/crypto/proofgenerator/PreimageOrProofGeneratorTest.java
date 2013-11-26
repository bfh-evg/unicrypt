package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageOrProofGenerator;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PreimageOrProofGeneratorTest {

	final static int P = 167;
	final static int P2 = 23;
	final private MultiplicativeCyclicGroup G_q;
	final private MultiplicativeCyclicGroup G_q2;
	final private ZModPrime Z_q;

	public PreimageOrProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.Z_q = ZModPrime.getInstance((P - 1) / 2);         // 83
		this.G_q2 = GStarModSafePrime.getInstance(P2);
	}

	@Test
	public void TestOrProof() {
		this.testProof(2);
		this.testProof(4);
	}

	public void testProof(int index) {

		// y1 = f1(x) = g1^x
		// y2 = f2(x) = g2^x
		// y3 = f3(x) = 4^x   =>   x=3, y=64
		// y4 = f4(x) = g4^x
		// y5 = f5(x1, x2) = g^x1 * h^x2

		Function f1 = GeneratorFunction.getInstance(this.G_q.getRandomGenerator());
		Function f2 = GeneratorFunction.getInstance(this.G_q2.getRandomGenerator());
		Function f3 = GeneratorFunction.getInstance(this.G_q.getElement(4));
		Function f4 = GeneratorFunction.getInstance(this.G_q.getRandomGenerator());
		Function f5 = this.getPedersonCommitmentFunction();

		Function[] functions = new Function[]{f1, f2, f3, f4, f5};
		PreimageOrProofGenerator pg = PreimageOrProofGenerator.getInstance(functions);

		// Default
		Element secret = this.Z_q.getElement(3);
		if (index == 4) {
			secret = Tuple.getInstance(this.Z_q.getElement(2), this.Z_q.getElement(1));
		}

		Tuple privateInput = pg.createPrivateInput(secret, index);
		Tuple publicInput = Tuple.getInstance(
			   this.G_q.getRandomElement(),
			   this.G_q2.getRandomElement(),
			   this.G_q.getElement(64),
			   this.G_q.getRandomElement(),
			   this.G_q.getElement(96));
		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

		// Generate
		Tuple proof = pg.generate(privateInput, publicInput, proverId);

		// Verify
		BooleanElement v = pg.verify(proof, publicInput, proverId);
		assertTrue(v.getBoolean());
	}

	public Function getPedersonCommitmentFunction() {
		Element g = this.G_q.getElement(4);
		Element h = this.G_q.getElement(6);

		Function f = CompositeFunction.getInstance(
			   ProductFunction.getInstance(GeneratorFunction.getInstance(g), GeneratorFunction.getInstance(h)),
			   ApplyFunction.getInstance(this.G_q));

		return f;
	}

}
