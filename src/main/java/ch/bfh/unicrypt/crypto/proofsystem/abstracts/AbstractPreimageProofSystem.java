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
package ch.bfh.unicrypt.crypto.proofsystem.abstracts;

import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.interfaces.PreimageProofSystem;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 * The abstract implementation of the {@link PreimageProofSystem}. It is a full implementation of the proof system.
 * Subclasses have to care only about the concrete instantiation, which is based on the proof function and the challenge
 * generator.
 * <p>
 * @author P. Locher
 * @param <PRS> The private input space.
 * @param <PRE> The private input element.
 * @param <PUS> The public input space.
 * @param <PUE> The public input element.
 * @param <F>   The proof function.
 */
public abstract class AbstractPreimageProofSystem<PRS extends SemiGroup, PRE extends Element, PUS extends SemiGroup, PUE extends Element, F extends Function>
	   extends AbstractSigmaProofSystem<PRS, PRE, PUS, PUE>
	   implements PreimageProofSystem {

	/**
	 * Holds the proof function.
	 */
	private final F preimageProofFunction;

	/**
	 * Constructor. A preimage proof system which is defined by its proof function and challenge generator.
	 * <p>
	 * @param challengeGenerator The challenge generator.
	 * @param function           The proof function.
	 */
	protected AbstractPreimageProofSystem(final SigmaChallengeGenerator challengeGenerator, final F function) {
		super(challengeGenerator);
		this.preimageProofFunction = function;
	}

	@Override
	public final F getPreimageProofFunction() {
		return this.preimageProofFunction;
	}

	@Override
	public final PUE getCommitment(final Triple proof) {
		return (PUE) super.getCommitment(proof);
	}

	@Override
	public final PRE getResponse(final Triple proof) {
		return (PRE) super.getResponse(proof);
	}

	@Override
	public final Set getCommitmentSpace() {
		return this.getPreimageProofFunction().getCoDomain();
	}

	@Override
	public final ZMod getChallengeSpace() {
		return ZMod.getInstance(this.getPreimageProofFunction().getDomain().getMinimalOrder());
	}

	@Override
	public final Set getResponseSpace() {
		return this.getPreimageProofFunction().getDomain();
	}

	@Override
	protected final PRS abstractGetPrivateInputSpace() {
		return (PRS) this.getPreimageProofFunction().getDomain();
	}

	@Override
	protected final PUS abstractGetPublicInputSpace() {
		return (PUS) this.getPreimageProofFunction().getCoDomain();
	}

	@Override
	protected final ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(this.getCommitmentSpace(), this.getChallengeSpace(), this.getResponseSpace());
	}

	@Override
	protected final Triple abstractGenerate(final Element secretInput, final Element publicInput,
		   final RandomByteSequence randomByteSequence) {
		final Element randomElement = this.getResponseSpace().getRandomElement(randomByteSequence);
		final Element commitment = this.getPreimageProofFunction().apply(randomElement);
		final Element challenge = this.getChallengeGenerator().generate(publicInput, commitment);
		final Element response = randomElement.apply(secretInput.selfApply(challenge));
		return (Triple) this.getProofSpace().getElement(commitment, challenge, response);
	}

	@Override
	protected final boolean abstractVerify(final Triple proof, final Element publicInput) {
		final Element challenge = this.getChallengeGenerator().generate(publicInput, this.getCommitment(proof));
		final Element left = this.getPreimageProofFunction().apply(this.getResponse(proof));
		final Element right = this.getCommitment(proof).apply(publicInput.selfApply(challenge));
		return left.isEquivalent(right);
	}

	/**
	 * Checks whether the challenge space of the challenge generator does match the domain of the proof function.
	 * <p>
	 * @param challengeGenerator The challenge generator
	 * @param proofFunction      The proof function
	 * <p>
	 * @return true if the challenge space does match otherwise false.
	 */
	protected static boolean checkChallengeSpace(final SigmaChallengeGenerator challengeGenerator,
		   final Function proofFunction) {
		return proofFunction == null || challengeGenerator == null
			   || !ZMod.getInstance(proofFunction.getDomain().getMinimalOrder()).isEquivalent(challengeGenerator
					  .getChallengeSpace());

	}

}
