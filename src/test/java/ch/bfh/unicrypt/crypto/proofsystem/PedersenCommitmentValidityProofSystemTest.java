/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.classes.PedersenCommitmentValidityProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PedersenCommitmentValidityProofSystemTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private GStarMod G_q1;
	final private GStarMod G_q2;
	final private StringElement proverId;

	public PedersenCommitmentValidityProofSystemTest() {
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

		SigmaChallengeGenerator scg = PedersenCommitmentValidityProofSystem.createNonInteractiveChallengeGenerator(pedersenCS, proverId);
		PedersenCommitmentValidityProofSystem pg = PedersenCommitmentValidityProofSystem.getInstance(scg, pedersenCS, messages);

		Element publicInput = G_q.getElement(128);   // 4^2*2^3 = 128

		// Valid proof
		Element secret = Z_q.getElement(2);
		int index = 1;
		Tuple privateInput = pg.createPrivateInput(secret, index);

		Triple proof = pg.generate(privateInput, publicInput);
		boolean v = pg.verify(proof, publicInput);
		assertTrue(v);
	}

	@Test
	public void testPedersenValidityProof2() {

		GStarMod G_q = this.G_q1;
		ZMod Z_q = G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q.getElement(2), G_q.getElement(4));
		ZModElement message0 = Z_q.getElement(2);
		ZModElement message1 = Z_q.getElement(3);
		ZModElement message2 = Z_q.getElement(4);
		ZModElement message3 = Z_q.getElement(5);
		Subset messages = Subset.getInstance(Z_q, message0, message1, message2, message3);

		SigmaChallengeGenerator scg = PedersenCommitmentValidityProofSystem.createNonInteractiveChallengeGenerator(pedersenCS, proverId);
		PedersenCommitmentValidityProofSystem pg = PedersenCommitmentValidityProofSystem.getInstance(scg, pedersenCS, messages);

		int index = 3;
		Element r = Z_q.getElement(5);

		Tuple privateInput = pg.createPrivateInput(r, index);
		Element publicInput = pedersenCS.commit(message3, r);

		Triple proof = pg.generate(privateInput, publicInput);
		boolean v = pg.verify(proof, publicInput);
		assertTrue(v);

	}

	@Test
	public void testPedersenValidityProof_Invalid() {

		for (int i = 0; i < 100; i++) {
			GStarMod G_q = this.G_q2;
			ZMod Z_q = G_q.getZModOrder();

			PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q.getElement(4), G_q.getElement(2));
			Subset messages = Subset.getInstance(Z_q, new Element[]{Z_q.getElement(2), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5)});

			SigmaChallengeGenerator scg = PedersenCommitmentValidityProofSystem.createNonInteractiveChallengeGenerator(pedersenCS, proverId);
			PedersenCommitmentValidityProofSystem pg = PedersenCommitmentValidityProofSystem.getInstance(scg, pedersenCS, messages);

			Element publicInput = G_q.getElement(128);   // 4^2*2^3 = 128

			// Invalid proof-> wrong randomndness
			Element secret = Z_q.getElement(7);
			int index = 1;
			Tuple privateInput = pg.createPrivateInput(secret, index);

			Triple proof = pg.generate(privateInput, publicInput);
			boolean v = pg.verify(proof, publicInput);
			assertTrue(!v);

			// Invalid proof -> wrong index
			secret = Z_q.getElement(2);
			index = 2;
			privateInput = pg.createPrivateInput(secret, index);

			proof = pg.generate(privateInput, publicInput);
			v = pg.verify(proof, publicInput);
			assertTrue(!v);
		}
	}

}
