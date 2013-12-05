package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.ElGamalEncryptionValidityProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Alphabet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ElGamalValidityProofGeneratorTest {

	final static int P = 167;
	final private CyclicGroup G_q;
	final private StringElement proverId;

	public ElGamalValidityProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void TestElGamalValidityProof() {

		ElGamalEncryptionScheme elGamalES = ElGamalEncryptionScheme.getInstance(G_q.getElement(2));
		Element[] plaintexts = new Element[]{G_q.getElement(4), G_q.getElement(2), G_q.getElement(8), G_q.getElement(16)};
		Element publicKey = G_q.getElement(4);

		SigmaChallengeGenerator scg = ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, plaintexts.length, proverId);
		ElGamalEncryptionValidityProofGenerator pg = ElGamalEncryptionValidityProofGenerator.getInstance(scg, elGamalES, publicKey, plaintexts);

		Pair publicInput = Pair.getInstance(G_q.getElement(8), G_q.getElement(128));   // (2^3, 4^3*2)


		// Valid proof
		Element secret = G_q.getZModOrder().getElement(3);
		int index = 1;
		Pair privateInput = pg.createPrivateInput(secret, index);

		Triple proof = pg.generate(privateInput, publicInput);
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
