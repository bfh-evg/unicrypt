package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Alphabet;
import org.junit.Test;

public class InequalityOfDescreteLogarithmsProofGeneratorTest {

	final static int P = 167;
	final private CyclicGroup G_q;
	final private ZMod Z_q;

	public InequalityOfDescreteLogarithmsProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.Z_q = this.G_q.getZModOrder();
	}

	@Test
	public void testInequlityOfDescretLogarithmsProof() {

		Element g = this.G_q.getElement(4);
		Element h = this.G_q.getElement(8);

		Element y = this.G_q.getElement(16);
		Element z = this.G_q.getElement(32);
		Element x = this.Z_q.getElement(2);

		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

//		InequalityOfDescreteLogarithmsProofGenerator pg = InequalityOfDescreteLogarithmsProofGenerator.getInstance(g, h);
//
//		// Valid proof
//		Pair proof = pg.generate(x, Pair.getInstance(y, z), proverId);
//
//		BooleanElement v = pg.verify(proof, Pair.getInstance(y, z), proverId);
//		assertTrue(v.getBoolean());
//
//		// Valid proof without proverId
//		proof = pg.generate(x, Pair.getInstance(y, z));
//		v = pg.verify(proof, Pair.getInstance(y, z));
//		assertTrue(v.getBoolean());
//
//		// Invalid proof -> wrong x
//		Element xx = this.Z_q.getElement(3);
//		proof = pg.generate(xx, Pair.getInstance(y, z), proverId);
//		v = pg.verify(proof, Pair.getInstance(y, z), proverId);
//		assertTrue(!v.getBoolean());
//
//		// Invalid proof -> equal descrete logs
//		Element zz = this.G_q.getElement(64);
//		proof = pg.generate(x, Pair.getInstance(y, zz), proverId);
//		v = pg.verify(proof, Pair.getInstance(y, zz), proverId);
//		assertTrue(!v.getBoolean());
//
//		// Invalid proof -> verification without proverId
//		proof = pg.generate(x, Pair.getInstance(y, z), proverId);
//		v = pg.verify(proof, Pair.getInstance(y, z));
//		assertTrue(!v.getBoolean());

	}

}
