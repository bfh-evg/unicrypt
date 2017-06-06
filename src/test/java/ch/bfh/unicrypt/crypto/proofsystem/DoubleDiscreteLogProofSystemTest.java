/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.crypto.proofsystem;

import ch.bfh.unicrypt.crypto.proofsystem.classes.DoubleDiscreteLogProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModPrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractCompoundFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class DoubleDiscreteLogProofSystemTest {

	final static int Q1 = 83;     // Q1               (prime)
	final static int P1 = 167;    // P1 =  2*Q1+1     (prime)
	final static int O1 = 2339;   // O1 = 15*P1+1     (prime)

	//                               Q2               (prime)
	final static String Q2 = "44029592011280554637067270297569196376551445501032604370128853948420151648611";
	//                               P1 =  2*Q1+1     (prime, 256 bit)
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	//                               O2 =603*P2+1     (prime)
	final static String O2 = "53011628781581787783028993438273312437367940383243255661635140153897862584928247";

	@Test
	public void testDoubleDiscreteLogProofSystem() {
		final CyclicGroup G_p = GStarModPrime.getInstance(O1, P1);
		final ZModPrime Z_p = (ZModPrime) G_p.getZModOrder();
		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		final Element g = G_p.getElement(BigInteger.valueOf(912));
		final Element g0 = G_p.getElement(BigInteger.valueOf(310));

		final Element h = G_q.getElement(BigInteger.valueOf(24));
		final Element h1 = G_q.getElement(BigInteger.valueOf(19));
		final Element h2 = G_q.getElement(BigInteger.valueOf(22));

		PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(g, g0);
		GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(h, Tuple.getInstance(h1, h2));

		DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(pcs, gpcs, 3);

		Element m1 = Z_q.getElement(7);
		Element m2 = Z_q.getElement(13);

		Element x = Z_p.getElement(h1.selfApply(m1).apply(h2.selfApply(m2)).convertToBigInteger());

		Element r = Z_p.getElement(BigInteger.valueOf(38));
		Element s = Z_q.getElement(BigInteger.valueOf(45));

		Tuple secretInput = Tuple.getInstance(x, r, s, Tuple.getInstance(m1, m2));

		Element C = pcs.commit(x, r);
		Element D = gpcs.commit(Tuple.getInstance(m1, m2), s);

		Pair publicInput = Pair.getInstance(C, D);

		Triple proof = ddlps.generate(secretInput, publicInput);
		boolean verify = ddlps.verify(proof, publicInput);

		assertTrue(verify);
	}

	@Test
	public void testDoubleDiscreteLogProofSystem2() {
		final CyclicGroup G_p = GStarModPrime.getInstance(new BigInteger(O2, 10), new BigInteger(P2, 10));
		final ZModPrime Z_p = (ZModPrime) G_p.getZModOrder();
		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		int size = 10;

		PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(G_p);
		GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(G_q, size);

		DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(pcs, gpcs, 40);

		Tuple m = gpcs.getMessageSpace().getRandomElement();

		Element x = Z_p.getElement(((AbstractCompoundFunction<ProductFunction, ProductSet, Tuple, ProductSet, Tuple>) ((ImmutableArray<Function>) gpcs.getCommitmentFunction()).getAt(0)).getAt(0).apply(m).convertToBigInteger());

		Element r = Z_p.getRandomElement();
		Element s = Z_q.getRandomElement();

		Tuple secretInput = Tuple.getInstance(x, r, s, m);

		Element C = pcs.commit(x, r);
		Element D = gpcs.commit(m, s);

		Pair publicInput = Pair.getInstance(C, D);

		Triple proof = ddlps.generate(secretInput, publicInput);
		boolean verify = ddlps.verify(proof, publicInput);

		assertTrue(verify);
	}

	@Test
	public void testDoubleDiscreteLogProofSystem3() {

		final RandomByteSequence randomByteSequence = DeterministicRandomByteSequence.getInstance();

		final CyclicGroup G_p = GStarModPrime.getInstance(O1, P1);
		final ZModPrime Z_p = (ZModPrime) G_p.getZModOrder();
		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		final Element g = G_p.getElement(BigInteger.valueOf(912));
		final Element g0 = G_p.getElement(BigInteger.valueOf(310));

		final Element h = G_q.getElement(BigInteger.valueOf(24));
		final Element h1 = G_q.getElement(BigInteger.valueOf(19));
		final Element h2 = G_q.getElement(BigInteger.valueOf(22));

		PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(g, g0);
		GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(h, Tuple.getInstance(h1, h2));

		DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(pcs, gpcs, 4);

		Element m1 = Z_q.getElement(7);
		Element m2 = Z_q.getElement(13);

		Element x = Z_p.getElement(h1.selfApply(m1).apply(h2.selfApply(m2)).convertToBigInteger());

		Element r = Z_p.getElement(BigInteger.valueOf(38));
		Element s = Z_q.getElement(BigInteger.valueOf(45));

		Element C = pcs.commit(x, r);
		Element D = gpcs.commit(Tuple.getInstance(m1, m2), s);

		Pair publicInput = Pair.getInstance(C, D);

		Tuple secretInputInvalid = Tuple.getInstance(x, r, s, Tuple.getInstance(Z_q.getElement(10), m2));
		Triple proofInvalid = ddlps.generate(secretInputInvalid, publicInput, randomByteSequence);
		boolean verify = ddlps.verify(proofInvalid, publicInput);
		assertFalse(verify);

		secretInputInvalid = Tuple.getInstance(x, r, s, Tuple.getInstance(m1, Z_q.getElement(12)));
		proofInvalid = ddlps.generate(secretInputInvalid, publicInput, randomByteSequence);
		verify = ddlps.verify(proofInvalid, publicInput);
		assertFalse(verify);

		secretInputInvalid = Tuple.getInstance(x, r, Z_q.getElement(5), Tuple.getInstance(m1, m2));
		proofInvalid = ddlps.generate(secretInputInvalid, publicInput, randomByteSequence);
		verify = ddlps.verify(proofInvalid, publicInput);
		assertFalse(verify);

		secretInputInvalid = Tuple.getInstance(x, Z_p.getElement(5), s, Tuple.getInstance(m1, m2));
		proofInvalid = ddlps.generate(secretInputInvalid, publicInput, randomByteSequence);
		verify = ddlps.verify(proofInvalid, publicInput);
		assertFalse(verify);
	}

}
