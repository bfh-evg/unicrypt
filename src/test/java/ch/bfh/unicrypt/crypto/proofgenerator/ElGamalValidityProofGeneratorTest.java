package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.ElGamalEncryptionValidityProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ElGamalValidityProofGeneratorTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private GStarMod G_q1;
	final private GStarMod G_q2;
	final private StringElement proverId;

	public ElGamalValidityProofGeneratorTest() {
		this.G_q1 = GStarModSafePrime.getInstance(P1);
		this.G_q2 = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void TestElGamalValidityProof() {

		GStarMod G_q = this.G_q1;
		ElGamalEncryptionScheme elGamalES = ElGamalEncryptionScheme.getInstance(G_q.getElement(2));
		Subset plaintexts = Subset.getInstance(G_q, new Element[]{G_q.getElement(4), G_q.getElement(2), G_q.getElement(8), G_q.getElement(16)});
		Element publicKey = G_q.getElement(4);

		SigmaChallengeGenerator scg = ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, plaintexts.getOrder().intValue(), proverId);
		ElGamalEncryptionValidityProofGenerator pg = ElGamalEncryptionValidityProofGenerator.getInstance(scg, elGamalES, publicKey, plaintexts);

		Pair publicInput = Pair.getInstance(G_q.getElement(8), G_q.getElement(128));   // (2^3, 4^3*2)

		// Valid proof
		Element secret = G_q.getZModOrder().getElement(3);
		int index = 1;
		Pair privateInput = pg.createPrivateInput(secret, index);

		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());

	}

	@Test
	public void TestElGamalValidityProof_Invalid() {

		GStarMod G_q = this.G_q2;
		for (int i = 0; i < 10; i++) {
			ElGamalEncryptionScheme elGamalES = ElGamalEncryptionScheme.getInstance(G_q.getElement(2));
			Subset plaintexts = Subset.getInstance(G_q, new Element[]{G_q.getElement(4), G_q.getElement(2), G_q.getElement(8), G_q.getElement(16)});
			Element publicKey = G_q.getElement(4);

			SigmaChallengeGenerator scg = ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, plaintexts.getOrder().intValue(), proverId);
			ElGamalEncryptionValidityProofGenerator pg = ElGamalEncryptionValidityProofGenerator.getInstance(scg, elGamalES, publicKey, plaintexts);

			Pair publicInput = Pair.getInstance(G_q.getElement(8), G_q.getElement(128));   // (2^3, 4^3*2)

			// Invalid proof -> wrong randomndness
			Element secret = G_q.getZModOrder().getElement(7);
			int index = 1;
			Pair privateInput = pg.createPrivateInput(secret, index);

			Triple proof = pg.generate(privateInput, publicInput);
			BooleanElement v = pg.verify(proof, publicInput);
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

}
