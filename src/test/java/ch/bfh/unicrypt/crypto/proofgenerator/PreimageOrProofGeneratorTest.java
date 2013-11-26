package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageOrProofGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
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
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PreimageOrProofGeneratorTest {

	final static int P = 167;
	final static int Q = 83;
	final private MultiplicativeCyclicGroup G_q;
	final private MultiplicativeCyclicGroup G_q2;
	final private ZModPrime Z_q;

	public PreimageOrProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.Z_q = ZModPrime.getInstance(Q);
		this.G_q2 = GStarModSafePrime.getInstance(23);
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
		ZMod indexSet = ZMod.getInstance(functions.length);
		// Default
		Element secret = this.Z_q.getElement(3);
		if (index == 4) {
			secret = Tuple.getInstance(Tuple.getInstance(this.Z_q.getElement(2)), Tuple.getInstance(this.Z_q.getElement(1)));
		}

		Tuple privateInput = pg.createPrivateInput(secret, index);
		Tuple publicInput = Tuple.getInstance(
			   this.G_q.getRandomElement(),
			   this.G_q2.getRandomElement(),
			   this.G_q.getElement(64),
			   this.G_q.getRandomElement(),
			   this.G_q.getElement(96));

		Tuple proof = pg.generate(privateInput, publicInput);

		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());
	}

	public Function getPedersonCommitmentFunction() {
		Element g = this.G_q.getElement(4);
		Element h = this.G_q.getElement(6);

		Function f = CompositeFunction.getInstance(
			   ProductFunction.getInstance(
			   SelfApplyFunction.getInstance(this.G_q, this.Z_q).partiallyApply(g, 0),
			   SelfApplyFunction.getInstance(this.G_q, this.Z_q).partiallyApply(h, 0)),
			   ApplyFunction.getInstance(this.G_q));

		return f;
	}

	public void testFunction1() {
		Element g = this.G_q.getElement(4);
		Element h = this.G_q.getElement(6);
		Element a = this.Z_q.getElement(2);
		Element b = this.Z_q.getElement(1);

		Function f1 = SelfApplyFunction.getInstance(this.G_q, this.Z_q).partiallyApply(g, 0);
		Function f2 = SelfApplyFunction.getInstance(this.G_q, this.Z_q).partiallyApply(h, 0);

		Function f3 = ProductFunction.getInstance(f1, f2);

		Element ret = f1.apply(a);
		Element ret2 = f2.apply(b);
		Element ret3 = f3.apply(Tuple.getInstance(Tuple.getInstance(a), Tuple.getInstance(b)));
		System.out.println(ret);
		System.out.println(ret2);
		System.out.println(ret3);
	}

	public void testFunction2() {
		Element g = this.G_q.getElement(4);
		Element h = this.G_q.getElement(6);
		Element a = this.Z_q.getElement(2);
		Element b = this.Z_q.getElement(1);

		Function f1 = GeneratorFunction.getInstance(g);
		Function f2 = GeneratorFunction.getInstance(h);

		Function f3 = ProductFunction.getInstance(f1, f2);

		Element ret = f1.apply(a);
		Element ret2 = f2.apply(b);
		Element ret3 = f3.apply(Tuple.getInstance(a, b));
		System.out.println(ret);
		System.out.println(ret2);
		System.out.println(ret3);
	}

}
