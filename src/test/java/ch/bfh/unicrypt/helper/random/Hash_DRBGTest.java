/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
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
package ch.bfh.unicrypt.helper.random;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.random.hybrid.Hash_DRBG;
import ch.bfh.unicrypt.helper.random.nondeterministic.NonDeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.SequenceIterator;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class Hash_DRBGTest {

	@Test
	public void hashMacSequenceTest() {
		//
		// test vector from NIST HASH_DRBG test vector file
		//
		//[SHA-256]
		//[PredictionResistance = False]
		//[EntropyInputLen = 256]
		//[NonceLen = 128]
		//[PersonalizationStringLen = 0]
		//[AdditionalInputLen = 0]
		//[ReturnedBitsLen = 1024]
		//
		//COUNT = 0
		//EntropyInput = a65ad0f345db4e0effe875c3a2e71f42c7129d620ff5c119a9ef55f05185e0fb
		//Nonce = 8581f9317517276e06e9607ddbcbcc2e
		//PersonalizationString =
		//** INSTANTIATE:
		//	V = 6e2f8fe3cdcd8942bc19890b70e89dd37ef46dfbdc17c209941b1b236417b3704ae2e5bbbf289500068fd45b6b40b69c78944f611255cf
		//	C = 665fee50d6d7c604f96d68192ebfaf508ea88a193c7b9ccd04a47034ee7de5a9549a709b7201b38b307fdfe842ff1be8f7fcbce6c82a28
		//	reseed counter = 1
		//AdditionalInput =
		//** GENERATE (FIRST CALL):
		//	V = d48f7e34a4a54f47b586f1249fa84d240d9cf81518935f09c40e32fe2b70cefff9715765773a401b85809bbceb0e8a12ff6e7d4e25281f
		//	C = 665fee50d6d7c604f96d68192ebfaf508ea88a193c7b9ccd04a47034ee7de5a9549a709b7201b38b307fdfe842ff1be8f7fcbce6c82a28
		//	reseed counter = 2
		//AdditionalInput =
		//ReturnedBits = d3e160c35b99f340b2628264d1751060e0045da383ff57a57d73a673d2b8d80daaf6a6c35a91bb4579d73fd0c8fed111b0391306828adfed528f018121b3febdc343e797b87dbb63db1333ded9d1ece177cfa6b71fe8ab1da46624ed6415e51ccde2c7ca86e283990eeaeb91120415528b2295910281b02dd431f4c9f70427df
		//** GENERATE (SECOND CALL):
		//	V = 3aef6c857b7d154caef4593dce67fc749c45822e550efc1498b6cc482dce1492a6d442e2af5d76013490f5019bbb01f94476c212ea2eae
		//	C = 665fee50d6d7c604f96d68192ebfaf508ea88a193c7b9ccd04a47034ee7de5a9549a709b7201b38b307fdfe842ff1be8f7fcbce6c82a28
		//	reseed counter = 3

		final ByteArray entropyInputAndNonce = ByteArray.getInstance("a6|5a|d0|f3|45|db|4e|0e|ff|e8|75|c3|a2|e7|1f|42|c7|12|9d|62|0f|f5|c1|19|a9|ef|55|f0|51|85|e0|fb|85|81|f9|31|75|17|27|6e|06|e9|60|7d|db|cb|cc|2e".toUpperCase());

		// used to simulate the entropy source
		RandomByteSequenceIterator iterator = new RandomByteSequenceIterator() {

			private int i = 0;

			@Override
			protected Byte abstractNext() {
				Byte next = entropyInputAndNonce.getAt(i);
				this.i = (this.i + 1) % entropyInputAndNonce.getLength();
				return next;
			}

		};

		NonDeterministicRandomByteSequence ndrbs = new NonDeterministicRandomByteSequence(iterator) {
		};

		ByteArray expected = ByteArray.getInstance("d3|e1|60|c3|5b|99|f3|40|b2|62|82|64|d1|75|10|60|e0|04|5d|a3|83|ff|57|a5|7d|73|a6|73|d2|b8|d8|0d|aa|f6|a6|c3|5a|91|bb|45|79|d7|3f|d0|c8|fe|d1|11|b0|39|13|06|82|8a|df|ed|52|8f|01|81|21|b3|fe|bd|c3|43|e7|97|b8|7d|bb|63|db|13|33|de|d9|d1|ec|e1|77|cf|a6|b7|1f|e8|ab|1d|a4|66|24|ed|64|15|e5|1c|cd|e2|c7|ca|86|e2|83|99|0e|ea|eb|91|12|04|15|52|8b|22|95|91|02|81|b0|2d|d4|31|f4|c9|f7|04|27|df".toUpperCase());

		SequenceIterator si = Hash_DRBG.getFactory().getInstance(ndrbs).getRandomByteSequence().iterator();
		si.next(128);
		Assert.assertEquals(expected, si.next(128));
	}

}
