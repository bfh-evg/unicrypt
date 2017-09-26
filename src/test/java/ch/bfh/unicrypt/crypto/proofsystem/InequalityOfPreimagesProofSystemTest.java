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
import ch.bfh.unicrypt.crypto.proofsystem.classes.InequalityOfPreimagesProofSystem;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class InequalityOfPreimagesProofSystemTest {

	final static int P = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private GStarMod G_q1;
	final private GStarMod G_q2;
	final private StringElement proverId;

	public InequalityOfPreimagesProofSystemTest() {
		this.G_q1 = GStarModSafePrime.getInstance(P);
		this.G_q2 = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void testInequlityProof2() {

		GStarMod G_q = this.G_q2;
		ZMod Z_q = G_q.getZModOrder();
		Element g = G_q.getElement(4);
		Element h = G_q.getElement(8);

		Element y = G_q.getElement(16);
		Element z = G_q.getElement(32);
		Element x = Z_q.getElement(2);

		Function f1 = GeneratorFunction.getInstance(g);
		Function f2 = GeneratorFunction.getInstance(h);

		SigmaChallengeGenerator scg = InequalityOfPreimagesProofSystem.createNonInteractiveChallengeGenerator(f1, f2, proverId);
		InequalityOfPreimagesProofSystem pg = InequalityOfPreimagesProofSystem.getInstance(scg, f1, f2);

		// Valid proof
		Pair proof = pg.generate(x, Pair.getInstance(y, z));

		boolean v = pg.verify(proof, Pair.getInstance(y, z));
		assertTrue(v);

		// Invalid proof -> wrong x
		Element xx = Z_q.getElement(3);
		proof = pg.generate(xx, Pair.getInstance(y, z));
		v = pg.verify(proof, Pair.getInstance(y, z));
		assertTrue(!v);

		// Invalid proof -> equal descrete logs
		Element zz = G_q.getElement(64);
		proof = pg.generate(x, Pair.getInstance(y, zz));
		v = pg.verify(proof, Pair.getInstance(y, zz));
		assertTrue(!v);

	}

}
