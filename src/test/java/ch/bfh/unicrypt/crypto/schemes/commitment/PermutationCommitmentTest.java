package ch.bfh.unicrypt.crypto.schemes.commitment;

import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PermutationCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Permutation;
import ch.bfh.unicrypt.math.random.RandomOracle;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PermutationCommitmentTest {

	final static int P = 23;
	final private CyclicGroup G_q;
	final private ZMod Z_q;
	final private RandomOracle ro;

	public PermutationCommitmentTest() {
		this.G_q = GStarModSafePrime.getInstance(this.P);
		this.Z_q = G_q.getZModOrder();
		this.ro = RandomOracle.DEFAULT;

//		System.out.println("g0: " + this.G_q.getRandomGenerator(ro.getRandom(0)));   //  6
//		System.out.println("g1: " + this.G_q.getRandomGenerator(ro.getRandom(1)));   // 13
//		System.out.println("g2: " + this.G_q.getRandomGenerator(ro.getRandom(2)));   //  2
//		System.out.println("g3: " + this.G_q.getRandomGenerator(ro.getRandom(3)));   //  2
//		System.out.println("g4: " + this.G_q.getRandomGenerator(ro.getRandom(4)));   // 18

	}

	@Test
	public void testPermuationCommitment1() {

		Permutation pi = new Permutation(new int[]{0});

		PermutationElement permutation = PermutationGroup.getInstance(pi.getSize()).getElement(pi);
		Tuple randomizations = Tuple.getInstance(this.Z_q.getElement(2));

		PermutationCommitmentScheme cp = PermutationCommitmentScheme.getInstance(this.G_q, pi.getSize(), this.ro);
		Tuple commitment = cp.commit(permutation, randomizations);
		assertTrue(commitment.getAt(0).isEqual(this.G_q.getElement(8)));   // 6^2 *13 = 8
	}

	@Test
	public void testPermuationCommitment3() {

		Permutation pi = new Permutation(new int[]{2, 0, 1});   // invert: [1, 2, 0]
		assertTrue(pi.permute(1) == 0);
		assertTrue(pi.permute(0) == 2);

		PermutationElement permutation = PermutationGroup.getInstance(pi.getSize()).getElement(pi);
		Tuple randomizations = Tuple.getInstance(this.Z_q.getElement(1), this.Z_q.getElement(2), this.Z_q.getElement(3));

		PermutationCommitmentScheme cp = PermutationCommitmentScheme.getInstance(this.G_q, pi.getSize(), this.ro);
		Tuple commitment = cp.commit(permutation, randomizations);
		assertTrue(commitment.getAt(0).isEqual(this.G_q.getElement(12)));  // 6^1 * 2 = 12
		assertTrue(commitment.getAt(1).isEqual(this.G_q.getElement(3)));   // 6^2 * 2 =  3
		assertTrue(commitment.getAt(2).isEqual(this.G_q.getElement(2)));   // 6^3 *13 =  2
	}

	@Test
	public void testPermuationCommitment4() {

		Permutation pi = new Permutation(new int[]{2, 3, 1, 0});  // invert: [3, 2, 0, 1]

		PermutationElement permutation = PermutationGroup.getInstance(pi.getSize()).getElement(pi);
		Tuple randomizations = Tuple.getInstance(this.Z_q.getElement(1), this.Z_q.getElement(2), this.Z_q.getElement(3), this.Z_q.getElement(4));

		PermutationCommitmentScheme cp = PermutationCommitmentScheme.getInstance(this.G_q, pi.getSize(), this.ro);
		Tuple commitment = cp.commit(permutation, randomizations);
		assertTrue(commitment.getAt(0).isEqual(this.G_q.getElement(16)));  // 6^1 *18 = 16
		assertTrue(commitment.getAt(1).isEqual(this.G_q.getElement(3)));   // 6^2 * 2 =  3
		assertTrue(commitment.getAt(2).isEqual(this.G_q.getElement(2)));   // 6^3 *13 =  2
		assertTrue(commitment.getAt(3).isEqual(this.G_q.getElement(16)));  // 6^4 * 2 = 16
	}

}
