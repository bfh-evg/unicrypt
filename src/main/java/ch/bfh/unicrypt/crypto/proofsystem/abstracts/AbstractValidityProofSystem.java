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
import ch.bfh.unicrypt.crypto.proofsystem.classes.OrProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.interfaces.SetMembershipProofSystem;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SharedDomainFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 * This class is an abstract base implementation for validity proof systems. A validity proof system is used to proof
 * that an encryption or commitment is indeed a valid encryption or commitment. 'Valid' means in this context, that the
 * encrypted or committed value x belongs to a set of members M. In fact, a validity proof is a set membership proof
 * implemented as a sigma proof.
 * <p>
 * The generic implementation uses internally an OR-proof. That is not very efficient (O(|M|)) but perfectly fine for
 * small sets M. The generic implementation is based on the set-membership proof function f(x,r) and the so called delta
 * function d(x,y). The resulting proof function is the composition of the two functions with fixed x:
 * <center>preimage proof function: p_x(r) = f_x(r) o d_x(y)</center>
 * The prover proves then knowledge of an r such that for one x ∈ M and for a given y p_x(r)=d_x(y) holds.
 * <p>
 * (The prover can't prove directly knowledge of an r such that f_x(r)=y as f_x(r) is not a homomorphic function.)
 * <p>
 * @see "CGS97"
 * <p>
 * @author P. Locher
 * @param <PUS> The public input space.
 * @param <PUE> The public input element.
 */
public abstract class AbstractValidityProofSystem<PUS extends SemiGroup, PUE extends Element>
	   extends AbstractSigmaProofSystem<ProductSet, Pair, PUS, PUE>
	   implements SetMembershipProofSystem {

	private final Subset members;
	private Function setMembershipProofFunction;
	private Function deltaFunction;
	private ProductFunction preimageProofFunction;
	private OrProofSystem orProofSystem;

	protected AbstractValidityProofSystem(final SigmaChallengeGenerator challengeGenerator, final Subset members) {
		super(challengeGenerator);
		this.members = members;
	}

	@Override
	public Subset getMembers() {
		return this.members;
	}

	@Override
	public Function getSetMembershipProofFunction() {
		if (this.setMembershipProofFunction == null) {
			this.setMembershipProofFunction = this.abstractGetSetMembershipFunction();
		}
		return this.setMembershipProofFunction;
	}

	public Function getDeltaFunction() {
		if (this.deltaFunction == null) {
			this.deltaFunction = this.abstractGetDeltaFunction();
		}
		return this.deltaFunction;
	}

	public final ProductFunction getPreimageProofFunction() {
		if (this.preimageProofFunction == null) {
			this.preimageProofFunction = this.createPreimageProofFunction();
		}
		return this.preimageProofFunction;
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
	protected ProductSet abstractGetPrivateInputSpace() {
		return this.getOrProofGenerator().getPrivateInputSpace();
	}

	@Override
	protected PUS abstractGetPublicInputSpace() {
		return (PUS) this.getPreimageProofFunction().getAt(0).getCoDomain();
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(
			   this.getCommitmentSpace(),
			   ProductSet.getInstance(this.getChallengeSpace(), this.members.getOrder().intValue()),
			   this.getResponseSpace());
	}

	@Override
	protected Triple abstractGenerate(Pair privateInput, PUE publicInput, RandomByteSequence randomByteSequence) {
		return this.getOrProofGenerator().generate(privateInput, this.createProofImages(publicInput),
												   randomByteSequence);
	}

	@Override
	protected boolean abstractVerify(Triple proof, PUE publicInput) {
		return this.getOrProofGenerator().verify(proof, this.createProofImages(publicInput));
	}

	public Pair createPrivateInput(Element secret, int index) {
		return this.getOrProofGenerator().createPrivateInput(secret, index);
	}

	public Pair createPrivateInput(Element secret, Element member) {
		int index = -1;
		int i = 0;
		for (Element m : this.members.getElements()) {
			if (m.equals(member)) {
				index = i;
				break;
			}
			i++;
		}
		if (index == -1) {
			throw new IllegalArgumentException();
		}
		return this.getOrProofGenerator().createPrivateInput(secret, index);
	}

	private Tuple createProofImages(PUE publicInput) {
		final Element[] images = new Element[this.members.getOrder().intValue()];
		int i = 0;
		for (Element memberElement : this.members.getElements()) {
			images[i++] = this.getDeltaFunction().apply(memberElement, publicInput);
		}
		return Tuple.getInstance(images);
	}

	private OrProofSystem getOrProofGenerator() {
		if (this.orProofSystem == null) {
			this.orProofSystem = OrProofSystem.getInstance(this.getChallengeGenerator(),
														   this.getPreimageProofFunction());
		}
		return this.orProofSystem;
	}

	private ProductFunction createPreimageProofFunction() {

		// proofFunction = composite( sharedDomainFunction(selction(0), setMembershipProofFunction), deltaFunction)
		final ProductSet setMembershipPFDomain = (ProductSet) this.getSetMembershipProofFunction().getDomain();
		final Function proofFunction
			   = CompositeFunction.getInstance(SharedDomainFunction.getInstance(
					  SelectionFunction.getInstance(setMembershipPFDomain, 0),
					  this.getSetMembershipProofFunction()), this.getDeltaFunction());

		// proofFunction_x = composite( multiIdentity(1), proofFunction.partiallyApply(x, 0))
		final Function[] proofFunctions = new Function[this.members.getOrder().intValue()];
		final Set rSet = setMembershipPFDomain.getAt(1);
		int i = 0;
		for (Element memberElement : this.members.getElements()) {
			proofFunctions[i++] = CompositeFunction.getInstance(MultiIdentityFunction.getInstance(rSet, 1),
																proofFunction.partiallyApply(memberElement, 0));
		}

		return ProductFunction.getInstance(proofFunctions);
	}

	protected abstract Function abstractGetSetMembershipFunction();

	protected abstract Function abstractGetDeltaFunction();

}
