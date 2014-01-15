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
package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.ElGamalKeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractReEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.ApplyInverseFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.RemovalFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 * @param <MS>
 * @param <ME>
 */
public class ElGamalEncryptionScheme<MS extends CyclicGroup, ME extends Element>
	   extends AbstractReEncryptionScheme<MS, ME, ProductGroup, Pair, ZMod, ZModElement, CyclicGroup, ZMod, ElGamalKeyPairGenerator> {

	private final MS cyclicGroup;
	private final ME generator;
	private final ZMod zMod;
	private Function encryptionFunctionLeft;
	private Function encryptionFunctionRight;

	protected ElGamalEncryptionScheme(MS cyclicGroup, ME generator) {
		this.cyclicGroup = cyclicGroup;
		this.generator = generator;
		this.zMod = cyclicGroup.getZModOrder();
	}

	public final MS getCyclicGroup() {
		return this.cyclicGroup;
	}

	public final ME getGenerator() {
		return this.generator;
	}

	@Override
	protected Function abstractGetEncryptionFunction() {
		ProductGroup encryptionDomain = ProductGroup.getInstance(this.getCyclicGroup(), this.getCyclicGroup(), this.zMod);
		return CompositeFunction.getInstance(
			   MultiIdentityFunction.getInstance(encryptionDomain, 2),
			   ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(encryptionDomain, 2),
																		 this.getEncryptionFunctionLeft()),
										   this.getEncryptionFunctionRight()));
	}

	@Override
	protected Function abstractGetDecryptionFunction() {
		ProductGroup decryptionDomain = ProductGroup.getInstance(this.zMod, ProductGroup.getInstance(this.getCyclicGroup(), 2));
		return CompositeFunction.getInstance(
			   MultiIdentityFunction.getInstance(decryptionDomain, 2),
			   ProductFunction.getInstance(SelectionFunction.getInstance(decryptionDomain, 1, 1),
										   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(decryptionDomain, 2),
																		 ProductFunction.getInstance(SelectionFunction.getInstance(decryptionDomain, 1, 0),
																									 SelectionFunction.getInstance(decryptionDomain, 0)),
																		 SelfApplyFunction.getInstance(this.getCyclicGroup(), this.zMod))),
			   ApplyInverseFunction.getInstance(this.getCyclicGroup()));
	}

	@Override
	protected ElGamalKeyPairGenerator abstractGetKeyPairGenerator() {
		return ElGamalKeyPairGenerator.getInstance(this.getGenerator());
	}

	public Function getEncryptionFunctionLeft() {
		if (this.encryptionFunctionLeft == null) {
			this.encryptionFunctionLeft = GeneratorFunction.getInstance(this.getGenerator());
		}
		return this.encryptionFunctionLeft;
	}

	public Function getEncryptionFunctionRight() {
		if (this.encryptionFunctionRight == null) {
			ProductGroup encryptionDomain = ProductGroup.getInstance(this.getCyclicGroup(), this.getCyclicGroup(), this.zMod);
			this.encryptionFunctionRight = CompositeFunction.getInstance(
				   MultiIdentityFunction.getInstance(encryptionDomain, 2),
				   ProductFunction.getInstance(SelectionFunction.getInstance(encryptionDomain, 1),
											   CompositeFunction.getInstance(RemovalFunction.getInstance(encryptionDomain, 1),
																			 SelfApplyFunction.getInstance(this.getCyclicGroup()))),
				   ApplyFunction.getInstance(this.getCyclicGroup()));
		}
		return this.encryptionFunctionRight;
	}

	public static <MS extends CyclicGroup, ME extends Element> ElGamalEncryptionScheme<MS, ME> getInstance(MS cyclicGroup) {
		return ElGamalEncryptionScheme.<MS, ME>getInternalInstance(cyclicGroup);
	}

	public static ElGamalEncryptionScheme<GStarMod, GStarModElement> getInstance(GStarMod gStarMod) {
		return ElGamalEncryptionScheme.<GStarMod, GStarModElement>getInternalInstance(gStarMod);
	}

	public static <MS extends CyclicGroup, ME extends Element> ElGamalEncryptionScheme<MS, ME> getInstance(ME generator) {
		return ElGamalEncryptionScheme.<MS, ME>getInternalInstance(generator);
	}

	public static ElGamalEncryptionScheme<GStarMod, GStarModElement> getInstance(GStarModElement generator) {
		return ElGamalEncryptionScheme.<GStarMod, GStarModElement>getInternalInstance(generator);
	}

	private static <MS extends CyclicGroup, ME extends Element> ElGamalEncryptionScheme<MS, ME> getInternalInstance(ME generator) {
		if (!generator.isGenerator()) {
			throw new IllegalArgumentException();
		}
		return new ElGamalEncryptionScheme<MS, ME>((MS) generator.getSet(), generator);
	}

	public static <MS extends CyclicGroup, ME extends Element> ElGamalEncryptionScheme<MS, ME> getInternalInstance(MS cyclicGroup) {
		return new ElGamalEncryptionScheme<MS, ME>(cyclicGroup, (ME) cyclicGroup.getDefaultGenerator());
	}

}

//
//    // @Override
//    @Override
//    public Element partialDecrypt(final Element privateKey, final Element ciphertext) {
//        return (Element) this.getPartialDecryptionFunction().apply(privateKey, ((Element) ciphertext).getAt(0));
//    }
//
//    // @Override
//    @Override
//    public Element combinePartialDecryptions(final Element ciphertext, final Element... partialDecryptions) {
//        final Element element = ((Element) ciphertext).getAt(1);
//        return this.ddhGroup.apply(partialDecryptions).apply(element);
//    }
