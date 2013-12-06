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
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StandardPreimageProofGeneratorTest {

	final static int P = 167;
	final private CyclicGroup G_q;
	final StringElement proverId;

	public StandardPreimageProofGeneratorTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void testPreimageProof() {

		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q.getElement(4));
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(
			   f.getCoDomain(), f.getCoDomain(), ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);

		PreimageProofGenerator pg = PreimageProofGenerator.getInstance(scg, f);

		// Valid proof
		Element privateInput = f.getDomain().getElement(3);
		Element publicInput = f.getCoDomain().getElement(64);


		Triple proof = pg.generate(privateInput, publicInput);

		BooleanElement v = pg.verify(proof, publicInput);
		assertTrue(v.getBoolean());

		// Invalid proof -> wrong private value
		privateInput = f.getDomain().getElement(4);
		proof = pg.generate(privateInput, publicInput);
		v = pg.verify(proof, publicInput);
		assertTrue(!v.getBoolean());
	}

	@Test
	public void testPreimageProof2() {

		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q.getElement(4));
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
	public void testElGamalPreimage() {
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

		// Invalid proof  => wrong r
		privateInput = Tuple.getInstance(m, G_q.getZModOrder().getElement(7));

		proof = pg.generate(privateInput, publicInput);
		v = pg.verify(proof, publicInput);
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
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q.getElement(4));
		SigmaChallengeGenerator scg = StandardNonInteractiveSigmaChallengeGenerator.getInstance(
			   f.getDomain(), f.getCoDomain(), ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);

		PreimageProofGenerator pg = PreimageProofGenerator.getInstance(scg, f);

	}

}
