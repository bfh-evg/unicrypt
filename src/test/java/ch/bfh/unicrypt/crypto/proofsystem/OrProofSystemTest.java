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

import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.FiatShamirSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.classes.OrProofSystem;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class OrProofSystemTest {

	final static int P = 167;
	final static int P2 = 23;
	final private GStarMod G_q;
	final private GStarMod G_q2;
	final private ZModPrime Z_q;
	final private StringElement proverId;

	public OrProofSystemTest() {
		this.G_q = GStarModSafePrime.getInstance(P);
		this.Z_q = ZModPrime.getInstance((P - 1) / 2);         // 83
		this.G_q2 = GStarModSafePrime.getInstance(P2);
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");

	}

	@Test
	public void TestOrProof() {
		this.testProof(2);
		this.testProof(4);
	}

	public void testProof(int index) {

		// y1 = f1(x) = g1^x
		// y2 = f2(x) = g2^x
		// y3 = f3(x) = 4^x   =>   x=3, y=64
		// y4 = f4(x) = g4^x
		// y5 = f5(x1, x2) = g^x1 * h^x2
		Function f1 = GeneratorFunction.getInstance(this.G_q.getRandomGenerator());
		Function f2 = GeneratorFunction.getInstance(this.G_q2.getRandomGenerator());
		Function f3 = GeneratorFunction.getInstance(this.G_q.getElement(4));
		Function f4 = GeneratorFunction.getInstance(this.G_q.getRandomGenerator());
		Function f5 = this.getPedersonCommitmentFunction();

		Function[] functions = new Function[]{f1, f2, f3, f4, f5};
		SigmaChallengeGenerator scg = FiatShamirSigmaChallengeGenerator.getInstance(ProductFunction.getInstance(functions), this.proverId);
		OrProofSystem pg = OrProofSystem.getInstance(scg, functions);

		// Default
		Element secret = this.Z_q.getElement(3);
		if (index == 4) {
			secret = Tuple.getInstance(this.Z_q.getElement(2), this.Z_q.getElement(1));
		}

		Pair privateInput = pg.createPrivateInput(secret, index);
		Tuple publicInput = Tuple.getInstance(
			   this.G_q.getRandomElement(),
			   this.G_q2.getRandomElement(),
			   this.G_q.getElement(64),
			   this.G_q.getRandomElement(),
			   this.G_q.getElement(96));

		// Generate
		Triple proof = pg.generate(privateInput, publicInput);

		// Verify
		boolean v = pg.verify(proof, publicInput);
		assertTrue(v);
	}

	public Function getPedersonCommitmentFunction() {
		Element g = this.G_q.getElement(4);
		Element h = this.G_q.getElement(6);

		Function f = CompositeFunction.getInstance(
			   ProductFunction.getInstance(GeneratorFunction.getInstance(g), GeneratorFunction.getInstance(h)),
			   ApplyFunction.getInstance(this.G_q));

		return f;
	}

}
