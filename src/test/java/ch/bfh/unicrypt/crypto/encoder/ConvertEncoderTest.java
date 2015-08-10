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
package ch.bfh.unicrypt.crypto.encoder;

import ch.bfh.unicrypt.crypto.encoder.classes.ConvertEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.BigIntegerToBigInteger;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.StringToBigInteger;
import ch.bfh.unicrypt.helper.converter.classes.string.BigIntegerToString;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.N;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteStringSet;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModPrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrime;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class ConvertEncoderTest {

	@Test
	public void testConvertEncoderBigIntegerBigInteger() {

		ZMod domain1 = ZMod.getInstance(23);
		ZStarMod domain2 = ZStarMod.getInstance(23);
		ZStarModPrime domain3 = ZStarModPrime.getInstance(23);
		GStarModPrime domain4 = GStarModPrime.getInstance(31, 5);
		GStarModSafePrime domain5 = GStarModSafePrime.getInstance(23);
		Set<BigInteger>[] domains = new Set[]{domain1, domain2, domain3, domain4, domain5};

		ZMod coDomain1 = ZMod.getInstance(40);
		N coDomain2 = N.getInstance();
		Z coDomain3 = Z.getInstance();
		Set<BigInteger>[] coDomains = new Set[]{coDomain1, coDomain2, coDomain3};

		for (Set<BigInteger> domain : domains) {
			for (Set<BigInteger> coDomain : coDomains) {
				Encoder encoder1 = ConvertEncoder.getInstance(domain, coDomain);
				Encoder encoder2 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.BIG_INTEGER);
				Encoder encoder3 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.STRING);
				Encoder encoder4 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.BYTE_ARRAY);
				Encoder encoder5 = ConvertEncoder.getInstance(domain, coDomain, BigIntegerToBigInteger.getInstance(0));
				Encoder encoder6 = ConvertEncoder.getInstance(domain, coDomain, BigIntegerToBigInteger.getInstance(-5));

				Encoder[] encoders = new Encoder[]{encoder1, encoder2, encoder3, encoder4, encoder5, encoder6};

				for (Encoder encoder : encoders) {
					for (Element element : domain.getElements()) {
						assertEquals(element, encoder.decode(encoder.encode(element)));
					}
				}
			}
		}

	}

	@Test
	public void testConvertEncoderStringBigInteger() {

		StringMonoid domain1 = StringMonoid.getInstance(Alphabet.DECIMAL);
		StringMonoid domain2 = StringMonoid.getInstance(Alphabet.PRINTABLE_ASCII, 10);
		FiniteStringSet domain3 = FiniteStringSet.getInstance(Alphabet.DECIMAL, 3, 6);
		FiniteStringSet domain4 = FiniteStringSet.getInstance(Alphabet.BASE64, 3);
		Set<String>[] domains = new Set[]{domain1, domain2, domain3, domain4};

		N coDomain1 = N.getInstance();
		Z coDomain2 = Z.getInstance();
		Set<BigInteger>[] coDomains = new Set[]{coDomain1, coDomain2};

		for (Set<String> domain : domains) {
			for (Set<BigInteger> coDomain : coDomains) {
				Encoder encoder1 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.BIG_INTEGER);
				Encoder encoder2 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.BYTE_ARRAY);
				Encoder encoder3 = ConvertEncoder.getInstance(domain, coDomain, StringToBigInteger.getInstance(Alphabet.PRINTABLE_ASCII));

				Encoder[] encoders = new Encoder[]{
					encoder1,
					encoder2,
					encoder3
				};

				for (Encoder encoder : encoders) {
					for (Element element : domain.getElements().limit(1000)) {
						assertEquals(element, encoder.decode(encoder.encode(element)));
					}
				}
			}
		}

	}

	@Test
	public void testConvertEncoderBigIntegerString() {

		ZMod domain1 = ZMod.getInstance(23);
		ZStarMod domain2 = ZStarMod.getInstance(23);
		ZStarModPrime domain3 = ZStarModPrime.getInstance(23);
		GStarModPrime domain4 = GStarModPrime.getInstance(31, 5);
		GStarModSafePrime domain5 = GStarModSafePrime.getInstance(23);
		Set<BigInteger>[] domains = new Set[]{domain1, domain2, domain3, domain4, domain5};

		StringMonoid coDomain1 = StringMonoid.getInstance(Alphabet.DECIMAL);
		StringMonoid coDomain2 = StringMonoid.getInstance(Alphabet.PRINTABLE_ASCII);
		FiniteStringSet coDomain3 = FiniteStringSet.getInstance(Alphabet.DECIMAL, 0, 8);
		FiniteStringSet coDomain4 = FiniteStringSet.getInstance(Alphabet.BASE64, 10);
		Set<String>[] coDomains = new Set[]{
			coDomain1,
			coDomain2, coDomain3,
			coDomain4
		};

		for (Set<BigInteger> domain : domains) {
			for (Set<String> coDomain : coDomains) {
				Encoder encoder1 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.BIG_INTEGER);
				Encoder encoder2 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.STRING);
				Encoder encoder3 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.BYTE_ARRAY);
				Encoder encoder4 = ConvertEncoder.getInstance(domain, coDomain, BigIntegerToString.getInstance(2));
				Encoder encoder5 = ConvertEncoder.getInstance(domain, coDomain, BigIntegerToString.getInstance(10));

				Encoder[] encoders = new Encoder[]{encoder1, encoder2, encoder3, encoder4, encoder5};

				for (Encoder encoder : encoders) {
					for (Element element : domain.getElements()) {
						assertEquals(element, encoder.decode(encoder.encode(element)));
					}
				}
			}
		}

	}

	@Test
	public void testConvertEncoderTupleBigInteger() {

		FiniteStringSet domain1 = FiniteStringSet.getInstance(Alphabet.BINARY, 4);
		ZMod domain2 = ZMod.getInstance(10);
		ZStarMod domain3 = ZStarMod.getInstance(10);
		PermutationGroup domain4 = PermutationGroup.getInstance(3);
		ProductSet domain12 = ProductSet.getInstance(domain1, domain2);
		ProductSet domain13 = ProductSet.getInstance(domain1, domain3);
		ProductSet domain14 = ProductSet.getInstance(domain1, domain4);
		ProductSet domain23 = ProductSet.getInstance(domain2, domain3);
		ProductSet domain24 = ProductSet.getInstance(domain2, domain4);
		ProductSet domain34 = ProductSet.getInstance(domain3, domain4);
		ProductSet domain123 = ProductSet.getInstance(domain1, domain2, domain3);
		ProductSet domain12_3 = ProductSet.getInstance(domain12, domain3);
		ProductSet domain1_23 = ProductSet.getInstance(domain1, domain23);
		ProductSet domain2_13 = ProductSet.getInstance(domain2, domain13);

		Set[] domains = new Set[]{domain1, domain2, domain3, domain4, domain12, domain13, domain14, domain23, domain24, domain34, domain123,
			domain12_3, domain1_23, domain2_13};

		N coDomain1 = N.getInstance();
		Z coDomain2 = Z.getInstance();
		Set<BigInteger>[] coDomains = new Set[]{coDomain1, coDomain2};

		for (Set<String> domain : domains) {
			for (Set<BigInteger> coDomain : coDomains) {
				Encoder encoder1 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.BIG_INTEGER);
				Encoder encoder2 = ConvertEncoder.getInstance(domain, coDomain, ConvertEncoder.Mode.BYTE_ARRAY);

				Encoder[] encoders = new Encoder[]{
					encoder1,
					encoder2
				};

				for (Encoder encoder : encoders) {
					for (Element element : domain.getElements().limit(1000)) {
						assertEquals(element, encoder.decode(encoder.encode(element)));
					}
				}
			}
		}

	}

}
