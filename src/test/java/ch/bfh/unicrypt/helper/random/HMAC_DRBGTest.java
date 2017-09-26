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
import ch.bfh.unicrypt.helper.random.hybrid.HMAC_DRBG;
import ch.bfh.unicrypt.helper.random.nondeterministic.NonDeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.SequenceIterator;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class HMAC_DRBGTest {

	@Test
	public void hashMacSequenceTest() {
		//
		// test vector from NIST HMAC_DRBG test vector file
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
		//EntropyInput = ca851911349384bffe89de1cbdc46e6831e44d34a4fb935ee285dd14b71a7488
		//Nonce = 659ba96c601dc69fc902940805ec0ca8
		//PersonalizationString =
		//** INSTANTIATE:
		//	V   = e75855f93b971ac468d200992e211960202d53cf08852ef86772d6490bfb53f9
		//	Key = 302a4aba78412ab36940f4be7b940a0c728542b8b81d95b801a57b3797f9dd6e
		//AdditionalInput =
		//** GENERATE (FIRST CALL):
		//	V   = bfbdcf455d5c82acafc59f339ce57126ff70b67aef910fa25db7617818faeafe
		//	Key = 911bf7cbda4387a172a1a3daf6c9fa8e17c4bfef69cc7eff1341e7eef88d2811
		//AdditionalInput =
		//ReturnedBits = e528e9abf2dece54d47c7e75e5fe302149f817ea9fb4bee6f4199697d04d5b89d54fbb978a15b5c443c9ec21036d2460b6f73ebad0dc2aba6e624abf07745bc107694bb7547bb0995f70de25d6b29e2d3011bb19d27676c07162c8b5ccde0668961df86803482cb37ed6d5c0bb8d50cf1f50d476aa0458bdaba806f48be9dcb8
		//** GENERATE (SECOND CALL):
		//	V   = 6b94e773e3469353a1ca8face76b238c5919d62a150a7dfc589ffa11c30b5b94
		//	Key = 6dd2cd5b1edba4b620d195ce26ad6845b063211d11e591432de37a3ad793f66c

		final ByteArray entropyInputAndNonce = ByteArray.getInstance("ca|85|19|11|34|93|84|bf|fe|89|de|1c|bd|c4|6e|68|31|e4|4d|34|a4|fb|93|5e|e2|85|dd|14|b7|1a|74|88|65|9b|a9|6c|60|1d|c6|9f|c9|02|94|08|05|ec|0c|a8".toUpperCase());

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

		ByteArray expected = ByteArray.getInstance("e5|28|e9|ab|f2|de|ce|54|d4|7c|7e|75|e5|fe|30|21|49|f8|17|ea|9f|b4|be|e6|f4|19|96|97|d0|4d|5b|89|d5|4f|bb|97|8a|15|b5|c4|43|c9|ec|21|03|6d|24|60|b6|f7|3e|ba|d0|dc|2a|ba|6e|62|4a|bf|07|74|5b|c1|07|69|4b|b7|54|7b|b0|99|5f|70|de|25|d6|b2|9e|2d|30|11|bb|19|d2|76|76|c0|71|62|c8|b5|cc|de|06|68|96|1d|f8|68|03|48|2c|b3|7e|d6|d5|c0|bb|8d|50|cf|1f|50|d4|76|aa|04|58|bd|ab|a8|06|f4|8b|e9|dc|b8".toUpperCase());

		SequenceIterator si = HMAC_DRBG.getFactory().getInstance(ndrbs).getRandomByteSequence().iterator();
		si.next(128);
		Assert.assertEquals(expected, si.next(128));
	}

}
