package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.InequalityOfPreimagesProofGenerator;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class InequalityOfPreimagesProofGeneratorTest {

	final static int P = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private CyclicGroup G_q1;
	final private CyclicGroup G_q2;
	final private StringElement proverId;

	public InequalityOfPreimagesProofGeneratorTest() {
		this.G_q1 = GStarModSafePrime.getInstance(P);
		this.G_q2 = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void testInequlityProof2() {

		CyclicGroup G_q = this.G_q2;
		ZMod Z_q = G_q.getZModOrder();
		Element g = G_q.getElement(4);
		Element h = G_q.getElement(8);

		Element y = G_q.getElement(16);
		Element z = G_q.getElement(32);
		Element x = Z_q.getElement(2);

		Function f1 = GeneratorFunction.getInstance(g);
		Function f2 = GeneratorFunction.getInstance(h);

		SigmaChallengeGenerator scg = InequalityOfPreimagesProofGenerator.createNonInteractiveChallengeGenerator(f1, f2, proverId);
		InequalityOfPreimagesProofGenerator pg = InequalityOfPreimagesProofGenerator.getInstance(scg, f1, f2);

		// Valid proof
		Pair proof = pg.generate(x, Pair.getInstance(y, z));

		BooleanElement v = pg.verify(proof, Pair.getInstance(y, z));
		assertTrue(v.getBoolean());

		// Invalid proof -> wrong x
		Element xx = Z_q.getElement(3);
		proof = pg.generate(xx, Pair.getInstance(y, z));
		v = pg.verify(proof, Pair.getInstance(y, z));
		assertTrue(!v.getBoolean());

		// Invalid proof -> equal descrete logs
		Element zz = G_q.getElement(64);
		proof = pg.generate(x, Pair.getInstance(y, zz));
		v = pg.verify(proof, Pair.getInstance(y, zz));
		assertTrue(!v.getBoolean());

	}

}
