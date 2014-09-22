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
package ch.bfh.unicrypt.crypto.schemes.signature.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.DiscreteLogarithmKeyGenerator;
import ch.bfh.unicrypt.crypto.schemes.signature.abstracts.AbstractRandomizedSignatureScheme;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.FiniteByteArrayToBigInteger;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.StringToByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.BigIntegerConverter;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.N;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.AdapterFunction;
import ch.bfh.unicrypt.math.function.classes.AdditionFunction;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.ConvertFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.ModuloFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.classes.TimesFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class SchnorrSignatureScheme<MS extends Set>
	   extends AbstractRandomizedSignatureScheme<MS, Element, ProductGroup, Pair, ZModPrime, ZMod, CyclicGroup, DiscreteLogarithmKeyGenerator> {

	private final CyclicGroup cyclicGroup;
	private final Element generator;

	protected SchnorrSignatureScheme(MS messageSpace, CyclicGroup cyclicGroup, Element generator, HashMethod hashMethod) {
		super(messageSpace, ProductSet.getInstance(cyclicGroup.getZModOrder(), 2), (ZModPrime) cyclicGroup.getZModOrder(), hashMethod);
		this.cyclicGroup = cyclicGroup;
		this.generator = generator;
	}

	@Override
	protected DiscreteLogarithmKeyGenerator abstractGetKeyPairGenerator(StringToByteArray converter) {
		return DiscreteLogarithmKeyGenerator.getInstance(this.generator, converter);
	}

	public final CyclicGroup getCyclicGroup() {
		return this.cyclicGroup;
	}

	public final Element getGenerator() {
		return this.generator;
	}

	@Override
	protected Function abstractGetSignatureFunction() {
		ZMod zMod = this.cyclicGroup.getZModOrder();
		ProductSet inputSpace = ProductSet.getInstance(zMod, this.messageSpace, zMod);
		ProductSet middleSpace = ProductSet.getInstance(zMod, 3);

		HashFunction hashFunction = HashFunction.getInstance(ProductSet.getInstance(this.messageSpace, this.cyclicGroup), this.hashMethod);
		BigIntegerConverter<ByteArray> converter = FiniteByteArrayToBigInteger.getInstance(this.hashMethod.getHashAlgorithm().getHashLength());
		ConvertFunction convertFunction = ConvertFunction.getInstance(hashFunction.getCoDomain(), N.getInstance(), converter);

		return CompositeFunction.getInstance(
			   MultiIdentityFunction.getInstance(inputSpace, 3),
			   ProductFunction.getInstance(
					  SelectionFunction.getInstance(inputSpace, 0),
					  CompositeFunction.getInstance(
							 AdapterFunction.getInstance(inputSpace, 1, 2),
							 ProductFunction.getInstance(
									IdentityFunction.getInstance(this.messageSpace),
									GeneratorFunction.getInstance(this.generator)),
							 hashFunction,
							 convertFunction,
							 ModuloFunction.getInstance(N.getInstance(), zMod)),
					  SelectionFunction.getInstance(inputSpace, 2)),
			   MultiIdentityFunction.getInstance(middleSpace, 2),
			   ProductFunction.getInstance(
					  SelectionFunction.getInstance(middleSpace, 1),
					  CompositeFunction.getInstance(
							 MultiIdentityFunction.getInstance(middleSpace, 2),
							 ProductFunction.getInstance(
									CompositeFunction.getInstance(
										   AdapterFunction.getInstance(middleSpace, 0, 1),
										   TimesFunction.getInstance(zMod, zMod)),
									SelectionFunction.getInstance(middleSpace, 2)),
							 AdditionFunction.getInstance(zMod))));
	}

	@Override
	protected Function abstractGetVerificationFunction() {
		ZMod zMod = this.cyclicGroup.getZModOrder();
		ProductSet inputSpace = ProductSet.getInstance(this.cyclicGroup, this.messageSpace, this.signatureSpace);

		HashFunction hashFunction = HashFunction.getInstance(ProductSet.getInstance(this.messageSpace, this.cyclicGroup), this.hashMethod);
		BigIntegerConverter<ByteArray> converter = FiniteByteArrayToBigInteger.getInstance(this.hashMethod.getHashAlgorithm().getHashLength());
		ConvertFunction convertFunction = ConvertFunction.getInstance(hashFunction.getCoDomain(), N.getInstance(), converter);

		return CompositeFunction.getInstance(
			   MultiIdentityFunction.getInstance(inputSpace, 2),
			   ProductFunction.getInstance(
					  CompositeFunction.getInstance(
							 MultiIdentityFunction.getInstance(inputSpace, 2),
							 ProductFunction.getInstance(
									SelectionFunction.getInstance(inputSpace, 1),
									CompositeFunction.getInstance(
										   MultiIdentityFunction.getInstance(inputSpace, 2),
										   ProductFunction.getInstance(
												  CompositeFunction.getInstance(
														 SelectionFunction.getInstance(inputSpace, 2, 1),
														 GeneratorFunction.getInstance(this.generator)),
												  CompositeFunction.getInstance(
														 MultiIdentityFunction.getInstance(inputSpace, 2),
														 ProductFunction.getInstance(
																SelectionFunction.getInstance(inputSpace, 0),
																SelectionFunction.getInstance(inputSpace, 2, 0)),
														 SelfApplyFunction.getInstance(this.cyclicGroup, zMod),
														 InvertFunction.getInstance(this.cyclicGroup))),
										   ApplyFunction.getInstance(this.cyclicGroup, 2))),
							 hashFunction,
							 convertFunction,
							 ModuloFunction.getInstance(N.getInstance(), zMod)),
					  SelectionFunction.getInstance(inputSpace, 2, 0)),
			   EqualityFunction.getInstance(zMod, 2));
	}

	public static <MS extends Set> SchnorrSignatureScheme getInstance(MS messageSpace, Element generator) {
		return SchnorrSignatureScheme.getInstance(messageSpace, generator, HashMethod.getInstance());
	}

	public static <MS extends Set> SchnorrSignatureScheme getInstance(MS messageSpace, Element generator, HashMethod hashMethod) {
		if (messageSpace == null || generator == null || !generator.getSet().isCyclic() || !generator.isGenerator() || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new SchnorrSignatureScheme(messageSpace, (CyclicGroup) generator.getSet(), generator, hashMethod);
	}

}

//	@Override
//	public Function abstractGetSignatureFunction() {
//
//		// TODO: not correct
//		ZMod zMod = this.cyclicGroup.getZModOrder();
//		ProductSet domain = ProductSet.getInstance(zMod, this.byteArrayMonoid, zMod);    // (prvateKeky,message,randomization)
//		Function privateKeySelector = SelectionFunction.getInstance(domain, 0);
//		Function messageSelector = SelectionFunction.getInstance(domain, 1);
//		Function randomizationSelector = SelectionFunction.getInstance(domain, 2);
//
//		// r = g^{randomization}
//		Function r = GeneratorFunction.getInstance(this.generator);
//
//		// e = h(message||r)
//		Function e = HashFunction.getInstance(ProductSet.getInstance(this.byteArrayMonoid, r.getCoDomain()));
//
//		ProductSet sDomain = ProductSet.getInstance(zMod, 3);
//
//		// s = randomization + e*privateKey
//		Function f3 = CompositeFunction.getInstance(SharedDomainFunction.getInstance(SelectionFunction.getInstance(sDomain, 0),
//																					 CompositeFunction.getInstance(AdapterFunction.getInstance(sDomain, 1, 2),
//																												   SelfApplyFunction.getInstance((SemiGroup) zMod))),
//													ApplyFunction.getInstance(zMod));
//
//		//result = randomization + privateKey*f2
//		Function s = CompositeFunction.getInstance(SharedDomainFunction.getInstance(randomizationSelector, privateKeySelector,
//																					CompositeFunction.getInstance(SharedDomainFunction.getInstance(messageSelector, r),
//																												  CompositeFunction.getInstance(e, ModuloFunction.getInstance(e.getCoDomain(), zMod)))),
//												   SharedDomainFunction.getInstance(SelectionFunction.getInstance(sDomain, 2), f3));
//
//		return s;
//		//Not yet finished... Must return Tuple (e,s) not only s
//	}
//
//	@Override
//	public Function abstractGetVerificationFunction() {
//		//r_=g^s * y^{-e}
//		//e_=H(m||r_)
//		//E_ ?=? E
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//	}
