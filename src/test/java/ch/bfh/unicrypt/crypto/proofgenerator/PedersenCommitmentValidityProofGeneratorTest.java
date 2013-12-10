package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.PedersenCommitmentValidityProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PedersenCommitmentValidityProofGeneratorTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private GStarMod G_q1;
	final private GStarMod G_q2;
	final private StringElement proverId;

	public PedersenCommitmentValidityProofGeneratorTest() {
		this.G_q1 = GStarModSafePrime.getInstance(P1);
		this.G_q2 = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void testPedersenValidityProof() {

		GStarMod G_q = this.G_q1;
		ZMod Z_q = G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q.getElement(4), G_q.getElement(2));
		Subset messages = Subset.getInstance(Z_q, new Element[]{Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5)});

		SigmaChallengeGenerator scg = PedersenCommitmentValidityProofGenerator.createNonInteractiveChallengeGenerator(pedersenCS, messages.getOrder().intValue(), proverId);
		PedersenCommitmentValidityProofGenerator pg = PedersenCommitmentValidityProofGenerator.getInstance(scg, pedersenCS, messages);

		Element publicInput = G_q.getElement(128);   // 4^2*2^3 = 128


		// Valid proof
		Element secret = Z_q.getElement(2);
		int index = 1;
		Tuple privateInput = pg.createPrivateInput(secret, index);

		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());
	}

	@Test
	public void testPedersenValidityProof2() {

		GStarMod G_q = this.G_q1;
		ZMod Z_q = G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q.getElement(2), G_q.getElement(4));
		Subset messages = Subset.getInstance(Z_q, new Element[]{Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5)});

		SigmaChallengeGenerator scg = PedersenCommitmentValidityProofGenerator.createNonInteractiveChallengeGenerator(pedersenCS, messages.getOrder().intValue(), proverId);
		PedersenCommitmentValidityProofGenerator pg = PedersenCommitmentValidityProofGenerator.getInstance(scg, pedersenCS, messages);

		int index = 3;
		Element r = Z_q.getElement(5);

		Tuple privateInput = pg.createPrivateInput(r, index);
		Element publicInput = pedersenCS.commit(messages.getElements()[index], r);

		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());

	}

	@Test
	public void testPedersenValidityProof_Invalid() {

		for (int i = 0; i < 100; i++) {
			GStarMod G_q = this.G_q2;
			ZMod Z_q = G_q.getZModOrder();

			PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q.getElement(4), G_q.getElement(2));
			Subset messages = Subset.getInstance(Z_q, new Element[]{Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5)});

			SigmaChallengeGenerator scg = PedersenCommitmentValidityProofGenerator.createNonInteractiveChallengeGenerator(pedersenCS, messages.getOrder().intValue(), proverId);
			PedersenCommitmentValidityProofGenerator pg = PedersenCommitmentValidityProofGenerator.getInstance(scg, pedersenCS, messages);

			Element publicInput = G_q.getElement(128);   // 4^2*2^3 = 128


			// Invalid proof-> wrong randomndness
			Element secret = Z_q.getElement(7);
			int index = 1;
			Tuple privateInput = pg.createPrivateInput(secret, index);

			Triple proof = pg.generate(privateInput, publicInput);
			BooleanElement v = pg.verify(proof, publicInput);
			assertTrue(!v.getBoolean());

			// Invalid proof -> wrong index
			secret = Z_q.getElement(2);
			index = 2;
			privateInput = pg.createPrivateInput(secret, index);

			proof = pg.generate(privateInput, publicInput);
			v = pg.verify(proof, publicInput);
			assertTrue(!v.getBoolean());
		}
	}

}
