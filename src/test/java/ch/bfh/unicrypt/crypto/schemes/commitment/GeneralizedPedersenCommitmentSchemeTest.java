package ch.bfh.unicrypt.crypto.schemes.commitment;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class GeneralizedPedersenCommitmentSchemeTest {

	final static int P = 23;
	final private CyclicGroup G_q;
	final private ZMod Z_q;

	public GeneralizedPedersenCommitmentSchemeTest() {
		this.G_q = GStarModSafePrime.getInstance(this.P);
		this.Z_q = G_q.getZModOrder();
	}

	@Test
	public void testGeneralizedPedersenCommitment() {

		RandomReferenceString rrs = PseudoRandomReferenceString.getInstance();

//		System.out.println("g0: " + this.G_q.getRandomGenerator(ro.getRandom(0)));   //  6
//		System.out.println("g1: " + this.G_q.getRandomGenerator(ro.getRandom(1)));   // 13
//		System.out.println("g2: " + this.G_q.getRandomGenerator(ro.getRandom(2)));   //  2
//		System.out.println("g3: " + this.G_q.getRandomGenerator(ro.getRandom(3)));   //  2
//		System.out.println("g4: " + this.G_q.getRandomGenerator(ro.getRandom(4)));   // 18
		GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(G_q, 2, rrs);
		Tuple messages = Tuple.getInstance(Z_q.getElement(1), Z_q.getElement(3));
		Element r = Z_q.getElement(2);
		Element c = gpcs.commit(messages, r);   // c = g1^m1 * g2^m2 * g0^r = 13^1 * 2^3 * 6^2 = 13 * 8 * 13 = 18
		assertTrue(c.isEqual(this.G_q.getElement(18)));

		gpcs = GeneralizedPedersenCommitmentScheme.getInstance(G_q, 4, rrs);
		messages = Tuple.getInstance(Z_q.getElement(1), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5));
		r = Z_q.getElement(3);
		c = gpcs.commit(messages, r);   // c = 13^1 * 2^3 * 2^4 * 18^5 * 6^3 = 13 * 8 * 16 * 3 * 9 = 9
		assertTrue(c.isEqual(this.G_q.getElement(9)));

//		System.out.println(c);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGeneralizedPedersenCommitment_Exception() {
		// Commitment size does not match with number of messages
		GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(G_q, 3);
		Tuple messages = Tuple.getInstance(Z_q.getElement(1), Z_q.getElement(3));
		Element r = Z_q.getElement(2);
		Element c = gpcs.commit(messages, r);

	}

}
