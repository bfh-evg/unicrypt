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
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.signature.abstracts.AbstractRandomizedSignatureScheme;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class SchnorrSignatureScheme
	   extends AbstractRandomizedSignatureScheme<ByteArrayMonoid, ByteArrayElement, ProductGroup, Pair, ZModPrime, ZMod, CyclicGroup, DiscreteLogarithmKeyGenerator> {

	private final CyclicGroup cyclicGroup;
	private final Element generator;
	private final HashMethod hashMethod; // HashAlgorithm?

	protected SchnorrSignatureScheme(CyclicGroup cyclicGroup, Element generator, HashMethod hashMethod) {
		this.cyclicGroup = cyclicGroup;
		this.generator = generator;
		this.hashMethod = hashMethod;
	}

	@Override
	protected KeyPairGenerator abstractGetKeyPairGenerator() {
		return DiscreteLogarithmKeyGenerator.getInstance(this.generator);
	}

	public final CyclicGroup getCyclicGroup() {
		return this.cyclicGroup;
	}

	public final Element getGenerator() {
		return this.generator;
	}

	public final HashMethod getHashMethod() {
		return this.hashMethod;
	}

	@Override
	protected Function abstractGetSignatureFunction() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Function abstractGetVerificationFunction() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
