package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StandardPreimageProofGeneratorTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private GStarMod G_q1;
	final private GStarMod G_q2;
	final StringElement proverId;

	public StandardPreimageProofGeneratorTest() {
		this.G_q1 = GStarModSafePrime.getInstance(P1);
		this.G_q2 = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void testPreimageProof() {

		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q1.getElement(4));
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(
			   f.getCoDomain(), f.getCoDomain(), ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);

		PreimageProofGenerator pg = PreimageProofGenerator.getInstance(scg, f);

		// Valid proof
		Element privateInput = f.getDomain().getElement(3);
		Element publicInput = f.getCoDomain().getElement(64);

		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());
	}

	@Test
	public void testPreimageProof_Invalid() {

		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q2.getElement(4));
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(
			   f.getCoDomain(), f.getCoDomain(), ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);

		PreimageProofGenerator pg = PreimageProofGenerator.getInstance(scg, f);

		// Invalid proof -> wrong private value
		Element privateInput = f.getDomain().getElement(4);
		Element publicInput = f.getCoDomain().getElement(64);

		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());
	}

	@Test
	public void testPreimageProof2() {

		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q1.getElement(4));
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(f, this.proverId);
		PreimageProofGenerator pg = PreimageProofGenerator.getInstance(scg, f);

		// Valid proof
		Element privateInput = f.getDomain().getElement(3);
		Element publicInput = f.getCoDomain().getElement(64);

		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());
	}

	@Test
	public void testPreimageProof_ElGamal() {

		GStarMod G_q = this.G_q1;
		// f_pk(m,r) = (g^r, h^r*m)
		ElGamalEncryptionScheme elgamal = ElGamalEncryptionScheme.getInstance(G_q.getElement(4));
		Element pk = G_q.getElement(2);
		Element m = G_q.getElement(2);
		Element r = G_q.getZModOrder().getElement(2);

		Function f = elgamal.getEncryptionFunction().partiallyApply(pk, 0);

		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(
			   f.getCoDomain(), (SemiGroup) f.getCoDomain(), ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);
		PreimageProofGenerator pg = PreimageProofGenerator.getInstance(scg, f);

		// Valid proof
		Element privateInput = Tuple.getInstance(m, r);
		Element publicInput = Tuple.getInstance(G_q.getElement(16), G_q.getElement(8));


		Tuple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());
	}

	@Test
	public void testPreimageProof_ElGamalInvalid() {

		GStarMod G_q = this.G_q2;
		// f_pk(m,r) = (g^r, h^r*m)
		ElGamalEncryptionScheme elgamal = ElGamalEncryptionScheme.getInstance(G_q.getElement(4));
		Element pk = G_q.getElement(2);
		Element m = G_q.getElement(2);
		Element r = G_q.getZModOrder().getElement(2);

		Function f = elgamal.getEncryptionFunction().partiallyApply(pk, 0);

		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(
			   f.getCoDomain(), (SemiGroup) f.getCoDomain(), ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);
		PreimageProofGenerator pg = PreimageProofGenerator.getInstance(scg, f);


		// Invalid proof  => wrong r
		Element privateInput = Tuple.getInstance(m, G_q.getZModOrder().getElement(7));
		Element publicInput = Tuple.getInstance(G_q.getElement(16), G_q.getElement(8));
		Triple proof = pg.generate(privateInput, publicInput);
		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());

		// Invalid proof  => wrong m
		privateInput = Tuple.getInstance(G_q.getElement(8), r);

		proof = pg.generate(privateInput, publicInput);
		v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestPreimageProof_Exception() {
		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q1.getElement(4));
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(
			   f.getDomain(), f.getCoDomain(), ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);

		PreimageProofGenerator pg = PreimageProofGenerator.getInstance(scg, f);

	}

}
