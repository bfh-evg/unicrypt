package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.ElGamalValidityProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ElGamalValidityProofGeneratorTest {

	final static int P = 167;
	final private CyclicGroup G_q;

	public ElGamalValidityProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
	}

	@Test
	public void TestElGamalValidityProof() {

		ElGamalValidityProofGenerator pg = ElGamalValidityProofGenerator.getInstance(
			   ElGamalEncryptionScheme.getInstance(G_q.getElement(2)),
			   G_q.getElement(4),
			   Tuple.getInstance(G_q.getElement(4), G_q.getElement(2), G_q.getElement(8), G_q.getElement(16)));

		Tuple publicInput = Tuple.getInstance(G_q.getElement(8), G_q.getElement(128));   // (2^3, 4^3*2)

		// Valid proof
		Element secret = G_q.getZModOrder().getElement(3);
		int index = 1;
		Tuple privateInput = pg.createPrivateInput(secret, index);

		Tuple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());

		// Invalid proof -> wrong randomndness
		secret = G_q.getZModOrder().getElement(7);
		index = 1;
		privateInput = pg.createPrivateInput(secret, index);

		proof = pg.generate(privateInput, publicInput);
		v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());

		// Invalid proof -> wrong index
		secret = G_q.getZModOrder().getElement(3);
		index = 2;
		privateInput = pg.createPrivateInput(secret, index);

		proof = pg.generate(privateInput, publicInput);
		v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());
	}

}
