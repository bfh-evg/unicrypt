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

import ch.bfh.unicrypt.crypto.proofsystem.abstracts.AbstractSigmaProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.FiatShamirSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 * This class covers the OR-composition of preimage proofs: ZKP[(x0,...xN) : y0=f0(x0) V...V yN=fN(xN)]. Neither the
 * domain nor the codomain of the function f0,...,fN have to be equal. For example, it is possible to prove either
 * knowledge of a discrete log or of an encrypted value: ZKP[(x, (a,b)) : y0=g^x V y1=enc(a,b)].
 * <p>
 * The private input is not only the private value x_i itself but a pair consisting of the tuple (x0,...,xN) and the
 * index i. Within the tuple (x0,...,xN) only x_i must have the correct value, all other values can have any value. Use
 * the helper method {@link #createPrivateInput(Element, int)} to create the correct private input for the private value
 * x_i and the index i.
 * <p>
 * @author P. Locher
 */
public class OrProofSystem
	   extends AbstractSigmaProofSystem<ProductSet, Pair, ProductGroup, Tuple> {

	private final ProductFunction proofFunction;

	protected OrProofSystem(final SigmaChallengeGenerator challengeGenerator, final ProductFunction proofFunction) {
		super(challengeGenerator);
		this.proofFunction = proofFunction;
	}

	public static OrProofSystem getInstance(final ProductFunction proofFunction) {
		return OrProofSystem.getInstance((Element) null, proofFunction);
	}

	public static OrProofSystem getInstance(final Element proverId, final ProductFunction proofFunction) {
		SigmaChallengeGenerator challengeGenerator
			   = FiatShamirSigmaChallengeGenerator.getInstance(proofFunction, proverId);
		return OrProofSystem.getInstance(challengeGenerator, proofFunction);
	}

	public static OrProofSystem
		   getInstance(final SigmaChallengeGenerator challengeGenerator, final Function... proofFunctions) {
		return OrProofSystem.getInstance(challengeGenerator, ProductFunction.getInstance(proofFunctions));
	}

	public static OrProofSystem
		   getInstance(final SigmaChallengeGenerator challengeGenerator, final Function proofFunction, int arity) {
		return OrProofSystem.getInstance(challengeGenerator, ProductFunction.getInstance(proofFunction, arity));
	}

	public static OrProofSystem
		   getInstance(final SigmaChallengeGenerator challengeGenerator, final ProductFunction proofFunction) {
		if (challengeGenerator == null || proofFunction == null
			   || proofFunction.getArity() < 2
			   || !ZMod.getInstance(proofFunction.getDomain().getMinimalOrder())
					  .isEquivalent(challengeGenerator.getChallengeSpace())) {
			throw new IllegalArgumentException();
		}
		return new OrProofSystem(challengeGenerator, proofFunction);
	}

	public final ProductFunction getProofFunction() {
		return this.proofFunction;
	}

	@Override
	public final Set getCommitmentSpace() {
		return this.getProofFunction().getCoDomain();
	}

	@Override
	public final ZMod getChallengeSpace() {
		return ZMod.getInstance(this.getProofFunction().getDomain().getMinimalOrder());
	}

	@Override
	public final Set getResponseSpace() {
		return this.getProofFunction().getDomain();
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(this.getCommitmentSpace(),
									  ProductSet.getInstance(this.getChallengeSpace(),
															 this.getProofFunction().getArity()),
									  this.getResponseSpace());
	}

	@Override
	protected ProductSet abstractGetPrivateInputSpace() {
		return ProductSet.getInstance(this.getProofFunction().getDomain(),
									  ZMod.getInstance(this.getProofFunction().getArity()));
	}

	@Override
	protected final ProductGroup abstractGetPublicInputSpace() {
		return (ProductGroup) this.getProofFunction().getCoDomain();
	}

	public Pair createPrivateInput(Element secret, int index) {
		if (index < 0 || index >= this.getProofFunction().getArity()
			   || !this.getProofFunction().getAt(index).getDomain().contains(secret)) {
			throw new IllegalArgumentException();
		}
		Tuple domainElements = ((ProductMonoid) this.getProofFunction().getDomain()).getIdentityElement();
		domainElements = domainElements.replaceAt(index, secret);

		return (Pair) this.getPrivateInputSpace()
			   .getElement(domainElements, ZMod.getInstance(this.getProofFunction().getArity()).getElement(index));
	}

	@Override
	protected Triple abstractGenerate(Pair privateInput, Tuple publicInput, RandomByteSequence randomByteSequence) {

		// Extract secret input value and index from private input
		final int index = privateInput.getSecond().convertToBigInteger().intValue();
		final Element secret = ((Tuple) privateInput.getFirst()).getAt(index);

		final ProductFunction proofFunc = this.getProofFunction();
		final int length = proofFunc.getLength();

		// Create lists for proof elements (t, c, s)
		final Element[] commitments = new Element[length];
		final Element[] challenges = new Element[length];
		final Element[] responses = new Element[length];

		// Get challenge space and initialze the summation of the challenges
		final ZMod challengeSpace = this.getChallengeSpace();
		ZModElement sumOfChallenges = challengeSpace.getIdentityElement();
		// Create proof elements (simulate proof) for all but the known secret
		for (int i = 0; i < length; i++) {
			if (i == index) {
				continue;
			}

			// Create random challenge and response
			ZModElement c = challengeSpace.getRandomElement(randomByteSequence);
			Function f = proofFunc.getAt(i);
			Element s = f.getDomain().getRandomElement(randomByteSequence);

			sumOfChallenges = sumOfChallenges.add(c);
			challenges[i] = c;
			responses[i] = s;
			// Calculate commitment based on the the public value and the random challenge and response
			// t = f(s)/(y^c)
			commitments[i] = f.apply(s).apply(publicInput.getAt(i).selfApply(c).invert());
		}

		// Create the proof of the known secret (normal preimage-proof, but with a special challange)
		// - Create random element and calculate commitment
		final Element randomElement = proofFunc.getAt(index).getDomain().getRandomElement(randomByteSequence);
		commitments[index] = proofFunc.getAt(index).apply(randomElement);

		// - Create overall proof challenge
		final ZModElement challenge
			   = this.getChallengeGenerator().generate(publicInput, Tuple.getInstance(commitments));
		// - Calculate challenge based on the overall challenge and the chosen challenges for the simulated proofs
		challenges[index] = challenge.subtract(sumOfChallenges);
		// - finally compute response element
		responses[index] = randomElement.apply(secret.selfApply(challenges[index]));

		// Return proof
		return (Triple) this.getProofSpace().getElement(Tuple.getInstance(commitments),
														Tuple.getInstance(challenges),
														Tuple.getInstance(responses));

	}

	@Override
	protected boolean abstractVerify(Triple proof, Tuple publicInput) {

		// Extract (t, c, s)
		final Tuple commitments = (Tuple) this.getCommitment(proof);
		final Tuple challenges = (Tuple) this.getChallenge(proof);
		final Tuple responses = (Tuple) this.getResponse(proof);

		// 1. Check whether challenges sum up to the overall challenge
		final ZModElement challenge = this.getChallengeGenerator().generate(publicInput, commitments);
		ZModElement sumOfChallenges = this.getChallengeSpace().getIdentityElement();
		for (int i = 0; i < challenges.getArity(); i++) {
			sumOfChallenges = sumOfChallenges.add(challenges.getAt(i));
		}
		if (!challenge.isEquivalent(sumOfChallenges)) {
			return false;
		}

		// 2. Verify all subproofs
		for (int i = 0; i < this.getProofFunction().getArity(); i++) {
			Element a = this.getProofFunction().getAt(i).apply(responses.getAt(i));
			Element b = commitments.getAt(i).apply(publicInput.getAt(i).selfApply(challenges.getAt(i)));
			if (!a.isEquivalent(b)) {
				return false;
			}
		}

		// Proof is valid!
		return true;
	}

}
