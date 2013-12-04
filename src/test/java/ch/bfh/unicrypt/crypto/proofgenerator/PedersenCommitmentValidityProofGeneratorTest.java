package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PedersenCommitmentValidityProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Alphabet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PedersenCommitmentValidityProofGeneratorTest {

	final static int P = 167;
	final private GStarMod G_q;
	final private ZMod Z_q;

	public PedersenCommitmentValidityProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.Z_q = G_q.getZModOrder();
	}

	@Test
	public void testPedersenValidityProof() {

		PedersenCommitmentValidityProofGenerator pg = PedersenCommitmentValidityProofGenerator.getInstance(
			   PedersenCommitmentScheme.getInstance(G_q.getElement(4), G_q.getElement(2)),
			   new Element[]{Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5)});

		Element publicInput = G_q.getElement(128);   // 4^2*2^3 = 128
		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

		// Valid proof
		Element secret = Z_q.getElement(2);
		int index = 1;
		Tuple privateInput = pg.createPrivateInput(secret, index);

		Triple proof = pg.generate(privateInput, publicInput, proverId);
		BooleanElement v = pg.verify(proof, publicInput, proverId);
		assertTrue(v.getBoolean());

		// Invalid proof -> wrong randomndness
		secret = Z_q.getElement(7);
		index = 1;
		privateInput = pg.createPrivateInput(secret, index);

		proof = pg.generate(privateInput, publicInput);
		v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());

		// Invalid proof -> wrong index
		secret = Z_q.getElement(2);
		index = 2;
		privateInput = pg.createPrivateInput(secret, index);

		proof = pg.generate(privateInput, publicInput);
		v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());
	}

	@Test
	public void testPedersenValidityProof2() {

		PedersenCommitmentScheme pdc = PedersenCommitmentScheme.getInstance(G_q.getElement(2), G_q.getElement(4));
		Element[] members = new Element[]{Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5)};

		PedersenCommitmentValidityProofGenerator pg = PedersenCommitmentValidityProofGenerator.getInstance(pdc, members);

		int index = 3;
		Element r = Z_q.getElement(5);
		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

		Tuple privateInput = pg.createPrivateInput(r, index);
		Element publicInput = pdc.commit(members[index], r);

		Triple proof = pg.generate(privateInput, publicInput, proverId);
		BooleanElement v = pg.verify(proof, publicInput, proverId);
		assertTrue(v.getBoolean());

	}

}
