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
import ch.bfh.unicrypt.crypto.proofsystem.classes.ElGamalEncryptionValidityProofSystem;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ElGamalValidityProofSystemTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private GStarMod G_q1;
	final private GStarMod G_q2;
	final private StringElement proverId;

	public ElGamalValidityProofSystemTest() {
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

		SigmaChallengeGenerator scg = ElGamalEncryptionValidityProofSystem.createNonInteractiveChallengeGenerator(elGamalES, plaintexts.getOrder().intValue(), proverId);
		ElGamalEncryptionValidityProofSystem pg = ElGamalEncryptionValidityProofSystem.getInstance(scg, elGamalES, publicKey, plaintexts);

		Pair publicInput = Pair.getInstance(G_q.getElement(8), G_q.getElement(128));   // (2^3, 4^3*2)

		// Valid proof
		Element secret = G_q.getZModOrder().getElement(3);
		int index = 1;
		Pair privateInput = pg.createPrivateInput(secret, index);

		Triple proof = pg.generate(privateInput, publicInput);
		boolean v = pg.verify(proof, publicInput);
		assertTrue(v);

	}

	@Test
	public void TestElGamalValidityProof_Invalid() {

		GStarMod G_q = this.G_q2;
		for (int i = 0; i < 10; i++) {
			ElGamalEncryptionScheme elGamalES = ElGamalEncryptionScheme.getInstance(G_q.getElement(2));
			Subset plaintexts = Subset.getInstance(G_q, new Element[]{G_q.getElement(4), G_q.getElement(2), G_q.getElement(8), G_q.getElement(16)});
			Element publicKey = G_q.getElement(4);

			SigmaChallengeGenerator scg = ElGamalEncryptionValidityProofSystem.createNonInteractiveChallengeGenerator(elGamalES, plaintexts.getOrder().intValue(), proverId);
			ElGamalEncryptionValidityProofSystem pg = ElGamalEncryptionValidityProofSystem.getInstance(scg, elGamalES, publicKey, plaintexts);

			Pair publicInput = Pair.getInstance(G_q.getElement(8), G_q.getElement(128));   // (2^3, 4^3*2)

			// Invalid proof -> wrong randomndness
			Element secret = G_q.getZModOrder().getElement(7);
			int index = 1;
			Pair privateInput = pg.createPrivateInput(secret, index);

			Triple proof = pg.generate(privateInput, publicInput);
			boolean v = pg.verify(proof, publicInput);
			assertTrue(!v);

			// Invalid proof -> wrong index
			secret = G_q.getZModOrder().getElement(3);
			index = 2;
			privateInput = pg.createPrivateInput(secret, index);

			proof = pg.generate(privateInput, publicInput);
			v = pg.verify(proof, publicInput);
			assertTrue(!v);
		}
	}

}
