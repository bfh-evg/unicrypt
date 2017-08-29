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
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.classes.biginteger.ByteArrayToBigInteger;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SharedDomainFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

/**
 * This class implements the validity proof system for Pedersen commitments: ZKP[(m,r) : y=com(m,r) ∧ m ∈ M] where com
 * creates a Pedersen commitment and M is the set of permitted messages.
 * <p>
 * The class only provides instantiation functions and methods to get the setMembershipFunction and deltaFunction.
 * Everything else is implemented in the superclass {@link AbstractValidityProofSystem}.
 * <p>
 * @author P. Locher
 */
public class PedersenCommitmentValidityProofSystem
	   extends AbstractValidityProofSystem<CyclicGroup, Element> {

	/**
	 * The Pedersen commitment scheme.
	 */
	private final PedersenCommitmentScheme pedersenCS;

	protected PedersenCommitmentValidityProofSystem(final SigmaChallengeGenerator challengeGenerator,
		   final PedersenCommitmentScheme pedersenCS, final Subset messages) {
		super(challengeGenerator, messages);
		this.pedersenCS = pedersenCS;
	}

	@Override
	protected Function abstractGetSetMembershipFunction() {
		return this.pedersenCS.getCommitmentFunction();
	}

	@Override
	protected Function abstractGetDeltaFunction() {
		final ProductSet deltaFunctionDomain = ProductSet.getInstance(this.pedersenCS.getMessageSpace(),
																	  this.getSetMembershipProofFunction()
																			 .getCoDomain());
		final Function deltaFunction = CompositeFunction.getInstance(
			   SharedDomainFunction.getInstance(
					  SelectionFunction.getInstance(deltaFunctionDomain, 1),
					  CompositeFunction.getInstance(
							 SelectionFunction.getInstance(deltaFunctionDomain, 0),
							 GeneratorFunction.getInstance(this.pedersenCS.getMessageGenerator()),
							 InvertFunction.getInstance(this.pedersenCS.getCyclicGroup()))),
			   ApplyFunction.getInstance(this.pedersenCS.getCyclicGroup()));
		return deltaFunction;
	}

	public static PedersenCommitmentValidityProofSystem getInstance(final PedersenCommitmentScheme pedersenCS,
		   final Subset messages) {
		return PedersenCommitmentValidityProofSystem.getInstance(pedersenCS, messages, null);
	}

	public static PedersenCommitmentValidityProofSystem getInstance(final PedersenCommitmentScheme pedersenCS,
		   final Subset messages, final Element proverId) {
		SigmaChallengeGenerator challengeGenerator
			   = PedersenCommitmentValidityProofSystem.
					  createNonInteractiveChallengeGenerator(pedersenCS, proverId);
		return PedersenCommitmentValidityProofSystem.getInstance(challengeGenerator, pedersenCS, messages);
	}

	public static PedersenCommitmentValidityProofSystem
		   getInstance(final SigmaChallengeGenerator challengeGenerator, final PedersenCommitmentScheme pedersenCS,
				  final Subset messages) {
		if (challengeGenerator == null || pedersenCS == null || messages == null
			   || messages.getOrder().intValue() < 1) {
			throw new IllegalArgumentException();
		}

		if (!ZMod.getInstance(pedersenCS.getCyclicGroup().getOrder())
			   .isEquivalent(challengeGenerator.getChallengeSpace())) {
			throw new IllegalArgumentException("Spaces of challenge generator don't match!");
		}
		return new PedersenCommitmentValidityProofSystem(challengeGenerator, pedersenCS, messages);
	}

	public static FiatShamirSigmaChallengeGenerator createNonInteractiveChallengeGenerator(
		   final PedersenCommitmentScheme pedersenCS) {
		return PedersenCommitmentValidityProofSystem.createNonInteractiveChallengeGenerator(pedersenCS,
																							(Element) null);
	}

	public static FiatShamirSigmaChallengeGenerator createNonInteractiveChallengeGenerator(
		   final PedersenCommitmentScheme pedersenCS, final Element proverId) {
		ConvertMethod<ByteArray> convertMethod = ConvertMethod.getInstance();
		HashMethod<ByteArray> hashMethod = HashMethod.getInstance();
		int hashLength = hashMethod.getHashAlgorithm().getByteLength();
		Converter<ByteArray, BigInteger> converter = ByteArrayToBigInteger.getInstance(hashLength);
		return PedersenCommitmentValidityProofSystem.createNonInteractiveChallengeGenerator(pedersenCS,
																							proverId, convertMethod, hashMethod, converter);
	}

	public static <V> FiatShamirSigmaChallengeGenerator createNonInteractiveChallengeGenerator(
		   final PedersenCommitmentScheme pedersenCS, final Element proverId,
		   final ConvertMethod<V> convertMethod, final HashMethod<V> hashMethod, final Converter<ByteArray, BigInteger> converter) {
		if (pedersenCS == null || convertMethod == null || hashMethod == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return FiatShamirSigmaChallengeGenerator.getInstance(ZMod.getInstance(pedersenCS.getCyclicGroup().getOrder()),
															 proverId, convertMethod, hashMethod, converter);

	}

}
