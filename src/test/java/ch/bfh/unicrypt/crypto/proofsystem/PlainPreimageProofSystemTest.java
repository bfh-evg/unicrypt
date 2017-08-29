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
import ch.bfh.unicrypt.crypto.proofsystem.classes.PlainPreimageProofSystem;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PlainPreimageProofSystemTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";
	final private GStarMod G_q1;
	final private GStarMod G_q2;
	final StringElement proverId;

	public PlainPreimageProofSystemTest() {
		this.G_q1 = GStarModSafePrime.getInstance(P1);
		this.G_q2 = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		this.proverId = StringMonoid.getInstance(Alphabet.BASE64).getElement("Prover1");
	}

	@Test
	public void testPreimageProof() {

		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q1.getElement(4));
		SigmaChallengeGenerator scg = FiatShamirSigmaChallengeGenerator.getInstance(
			   ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);

		PlainPreimageProofSystem pg = PlainPreimageProofSystem.getInstance(scg, f);

		// Valid proof
		Element privateInput = this.G_q1.getZModOrder().getElement(3);
		Element publicInput = this.G_q1.getElement(64);

		Triple proof = pg.generate(privateInput, publicInput);
		boolean v = pg.verify(proof, publicInput);
		assertTrue(v);
	}

	@Test
	public void testPreimageProof_Invalid() {

		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q2.getElement(4));
		SigmaChallengeGenerator scg = FiatShamirSigmaChallengeGenerator.getInstance(
			   ZMod.getInstance(f.getDomain().getMinimalOrder()), this.proverId);

		PlainPreimageProofSystem pg = PlainPreimageProofSystem.getInstance(scg, f);

		// Invalid proof -> wrong private value
		Element privateInput = this.G_q2.getZModOrder().getElement(4);
		Element publicInput = this.G_q2.getElement(64);

		Triple proof = pg.generate(privateInput, publicInput);
		boolean v = pg.verify(proof, publicInput);
		assertTrue(!v);
	}

	@Test
	public void testPreimageProof2() {

		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q1.getElement(4));
		SigmaChallengeGenerator scg = FiatShamirSigmaChallengeGenerator.getInstance(f, this.proverId);
		PlainPreimageProofSystem pg = PlainPreimageProofSystem.getInstance(scg, f);

		// Valid proof
		Element privateInput = this.G_q1.getZModOrder().getElement(3);
		Element publicInput = this.G_q1.getElement(64);

		Triple proof = pg.generate(privateInput, publicInput);
		boolean v = pg.verify(proof, publicInput);
		assertTrue(v);
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

		PlainPreimageProofSystem pg = PlainPreimageProofSystem.getInstance(this.proverId, f);

		// Valid proof
		Element privateInput = Tuple.getInstance(m, r);
		Element publicInput = Tuple.getInstance(G_q.getElement(16), G_q.getElement(8));
		// System.out.println("" + privateInput + "," + publicInput);

		Tuple proof = pg.generate(privateInput, publicInput);
		// System.out.println("" + proof);
		boolean v = pg.verify(proof, publicInput);
		assertTrue(v);
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
		PlainPreimageProofSystem pg = PlainPreimageProofSystem.getInstance(this.proverId, f);

		// Invalid proof  => wrong r
		Element privateInput = Tuple.getInstance(m, G_q.getZModOrder().getElement(7));
		Element publicInput = Tuple.getInstance(G_q.getElement(16), G_q.getElement(8));
		Triple proof = pg.generate(privateInput, publicInput);
		boolean v = pg.verify(proof, publicInput);
		assertTrue(!v);

		// Invalid proof  => wrong m
		privateInput = Tuple.getInstance(G_q.getElement(8), r);

		proof = pg.generate(privateInput, publicInput);
		v = pg.verify(proof, publicInput);
		assertTrue(!v);
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestPreimageProof_Exception() {
		// Proof generator
		GeneratorFunction f = GeneratorFunction.getInstance(this.G_q1.getElement(4));
		SigmaChallengeGenerator scg = FiatShamirSigmaChallengeGenerator.getInstance(
			   this.G_q2.getZModOrder(), this.proverId);

		PlainPreimageProofSystem pg = PlainPreimageProofSystem.getInstance(scg, f);

	}

}
