/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.crypto.proofsystem.classes;

import ch.bfh.unicrypt.crypto.proofsystem.abstracts.AbstractValidityProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.FiatShamirSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SharedDomainFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

/**
 * This class implements the validity proof system for ElGamal encryptions: ZKP[(m,r) : y=enc(m,r) ∧ m ∈ M] where enc
 * creates an ElGamal encryption and M is the set of permitted plaintexts.
 * <p>
 * The class only provides instantiation functions and methods to get the setMembershipFunction and deltaFunction.
 * Everything else is implemented in the superclass {@link AbstractValidityProofSystem}.
 * <p>
 * @author P. Locher
 */
public class ElGamalEncryptionValidityProofSystem
	   extends AbstractValidityProofSystem<ProductGroup, Pair> {

	/**
	 * The ElGamal encryption scheme.
	 */
	private final ElGamalEncryptionScheme elGamalES;

	/**
	 * The public key of the ElGamal encryption.
	 */
	private final Element publicKey;

	protected ElGamalEncryptionValidityProofSystem(final SigmaChallengeGenerator challengeGenerator,
		   final ElGamalEncryptionScheme elGamalES, Element publicKey, final Subset plaintexts) {
		super(challengeGenerator, plaintexts);
		this.elGamalES = elGamalES;
		this.publicKey = publicKey;
	}

	@Override
	protected Function abstractGetSetMembershipFunction() {
		return this.elGamalES.getEncryptionFunction().partiallyApply(this.publicKey, 0);
	}

	@Override
	protected Function abstractGetDeltaFunction() {
		final CyclicGroup elGamalCyclicGroup = this.elGamalES.getCyclicGroup();
		final ProductSet deltaFunctionDomain
			   = ProductSet.getInstance(elGamalCyclicGroup, this.getSetMembershipProofFunction().getCoDomain());
		return SharedDomainFunction.getInstance(
			   SelectionFunction.getInstance(deltaFunctionDomain, 1, 0),
			   CompositeFunction.getInstance(
					  SharedDomainFunction.getInstance(
							 SelectionFunction.getInstance(deltaFunctionDomain, 1, 1),
							 CompositeFunction.getInstance(
									SelectionFunction.getInstance(deltaFunctionDomain, 0),
									InvertFunction.getInstance(elGamalCyclicGroup))),
					  ApplyFunction.getInstance(elGamalCyclicGroup)));
	}

	public static ElGamalEncryptionValidityProofSystem getInstance(final ElGamalEncryptionScheme elGamalES,
		   final Element publicKey, final Subset plaintexts) {
		return ElGamalEncryptionValidityProofSystem.getInstance((Element) null, elGamalES, publicKey, plaintexts);
	}

	public static ElGamalEncryptionValidityProofSystem getInstance(final Element proverId,
		   final ElGamalEncryptionScheme elGamalES, final Element publicKey, final Subset plaintexts) {
		SigmaChallengeGenerator challengeGenerator
			   = ElGamalEncryptionValidityProofSystem.
					  createNonInteractiveChallengeGenerator(elGamalES, plaintexts.getOrder().intValue(), proverId);
		return ElGamalEncryptionValidityProofSystem.getInstance(challengeGenerator, elGamalES, publicKey, plaintexts);
	}

	public static ElGamalEncryptionValidityProofSystem getInstance(final SigmaChallengeGenerator challengeGenerator,
		   final ElGamalEncryptionScheme elGamalES, final Element publicKey, final Subset plaintexts) {
		if (challengeGenerator == null || elGamalES == null || publicKey == null
			   || !elGamalES.getCyclicGroup().contains(publicKey)
			   || plaintexts == null || plaintexts.getOrder().intValue() < 1
			   || !ZMod.getInstance(elGamalES.getCyclicGroup().getOrder()).isEquivalent(challengeGenerator
					  .getChallengeSpace())) {
			throw new IllegalArgumentException();
		}
		return new ElGamalEncryptionValidityProofSystem(challengeGenerator, elGamalES, publicKey, plaintexts);
	}

	public static FiatShamirSigmaChallengeGenerator createNonInteractiveChallengeGenerator(
		   final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts) {
		return ElGamalEncryptionValidityProofSystem
			   .createNonInteractiveChallengeGenerator(elGamalES, numberOfPlaintexts, null);
	}

	public static FiatShamirSigmaChallengeGenerator createNonInteractiveChallengeGenerator(
		   final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final Element proverId) {
		return FiatShamirSigmaChallengeGenerator.getInstance(ZMod.getInstance(elGamalES.getCyclicGroup()
			   .getOrder()), proverId);
	}

	public static <V> FiatShamirSigmaChallengeGenerator createNonInteractiveChallengeGenerator(
		   final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final ConvertMethod<V> convertMethod,
		   final HashMethod<V> hashMethod, final Converter<ByteArray, BigInteger> converter) {
		return ElGamalEncryptionValidityProofSystem
			   .createNonInteractiveChallengeGenerator(elGamalES, numberOfPlaintexts, null, convertMethod, hashMethod, converter);
	}

	public static <V> FiatShamirSigmaChallengeGenerator createNonInteractiveChallengeGenerator(
		   final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final Element proverId,
		   final ConvertMethod<V> convertMethod, final HashMethod<V> hashMethod, final Converter<ByteArray, BigInteger> converter) {
		if (elGamalES == null || numberOfPlaintexts < 1 || convertMethod == null || hashMethod == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return FiatShamirSigmaChallengeGenerator.getInstance(ZMod.getInstance(elGamalES.getCyclicGroup()
			   .getOrder()), proverId, convertMethod, hashMethod, converter);
	}

}
