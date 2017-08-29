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

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.crypto.proofsystem.abstracts.AbstractSigmaProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.FiatShamirSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.interfaces.SetMembershipProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @see "[BG13] Zero-Knowledge Argument for Polynomial Evaluation with Application to Blacklists"
 * <p>
 * @author philipp
 */
public class PolynomialMembershipProofSystem
	   extends AbstractSigmaProofSystem<ProductGroup, Pair, Group, Element>
	   implements SetMembershipProofSystem {

	final private Subset members;
	final private PolynomialEvaluationProofSystem pepsi;

	private PolynomialMembershipProofSystem(final SigmaChallengeGenerator challengeGenerator, final Subset members,
		   final PedersenCommitmentScheme pedersenCS) {
		super(challengeGenerator);

		this.members = members;
		final Element[] roots = new Element[this.members.getOrder().intValue()];
		int i = 0;
		for (Element member : this.members.getElements()) {
			roots[i++] = member;
		}

		PolynomialElement polynomial
			   = PolynomialSemiRing.getInstance((SemiRing) members.getSuperset())
					  .getElementByRoots(Tuple.getInstance(roots));
		this.pepsi = PolynomialEvaluationProofSystem.getInstance(challengeGenerator, polynomial, pedersenCS);
	}

	private PolynomialMembershipProofSystem(final SigmaChallengeGenerator challengeGenerator,
		   final PolynomialElement polynomial, final PedersenCommitmentScheme pedersenCS) {
		super(challengeGenerator);
		this.members = null;
		this.pepsi = PolynomialEvaluationProofSystem.getInstance(challengeGenerator, polynomial, pedersenCS);
	}

	public static final PolynomialMembershipProofSystem getInstance(final Subset members,
		   final PedersenCommitmentScheme pedersenCS) {
		SigmaChallengeGenerator challengeGenerator
			   = FiatShamirSigmaChallengeGenerator.getInstance(pedersenCS.getMessageSpace());
		return PolynomialMembershipProofSystem.getInstance(challengeGenerator, members, pedersenCS);
	}

	public static final PolynomialMembershipProofSystem getInstance(final SigmaChallengeGenerator challengeGenerator,
		   final Subset members, final PedersenCommitmentScheme pedersenCS) {

		if (challengeGenerator == null || members == null || pedersenCS == null) {
			throw new IllegalArgumentException();
		}
		if (!(members.getSuperset() instanceof ZModPrime)
			   || pedersenCS.getCyclicGroup().getOrder() != members.getSuperset().getOrder()) {
			throw new IllegalArgumentException();
		}
		if (!challengeGenerator.getChallengeSpace().isEquivalent(pedersenCS.getMessageSpace())) {
			throw new IllegalArgumentException();
		}

		return new PolynomialMembershipProofSystem(challengeGenerator, members, pedersenCS);
	}

	public static final PolynomialMembershipProofSystem getInstance(final PolynomialElement polynomial,
		   final PedersenCommitmentScheme pedersenCS) {
		SigmaChallengeGenerator challengeGenerator
			   = FiatShamirSigmaChallengeGenerator.getInstance(pedersenCS.getMessageSpace());
		return PolynomialMembershipProofSystem.getInstance(challengeGenerator, polynomial, pedersenCS);
	}

	public static final PolynomialMembershipProofSystem
		   getInstance(final SigmaChallengeGenerator challengeGenerator, final PolynomialElement polynomial,
				  final PedersenCommitmentScheme pedersenCS) {

		if (challengeGenerator == null || polynomial == null || pedersenCS == null) {
			throw new IllegalArgumentException();
		}
		if (!(polynomial.getSet().getSemiRing() instanceof ZModPrime)
			   || pedersenCS.getCyclicGroup().getOrder() != polynomial.getSet().getSemiRing().getOrder()) {
			throw new IllegalArgumentException();
		}
		if (!challengeGenerator.getChallengeSpace().isEquivalent(pedersenCS.getMessageSpace())) {
			throw new IllegalArgumentException();
		}

		return new PolynomialMembershipProofSystem(challengeGenerator, polynomial, pedersenCS);
	}

	@Override
	protected ProductGroup abstractGetPrivateInputSpace() {
		// u, r_0\in Z_p
		return ProductGroup.getInstance(this.pepsi.getZModPrime(), 2);
	}

	@Override
	protected Group abstractGetPublicInputSpace() {
		// c_u \in G
		return this.pepsi.getCyclicGroup();
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return this.pepsi.getProofSpace();
	}

	@Override
	public Set getCommitmentSpace() {
		return this.pepsi.getCommitmentSpace();
	}

	@Override
	public ZMod getChallengeSpace() {
		return this.pepsi.getChallengeSpace();
	}

	@Override
	public Set getResponseSpace() {
		return this.pepsi.getResponseSpace();
	}

	@Override
	public Subset getMembers() {
		// In case, the proof system has been created directly by the representing polynomial
		// -> May comupte the roots from the polynomial...?
		if (this.members == null) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		return this.members;
	}

	@Override
	public Function getSetMembershipProofFunction() {
		return this.pepsi.getPedersenCommitmentScheme().getCommitmentFunction();
	}

	@Override
	protected Triple abstractGenerate(Pair secretInput, Element publicInput, RandomByteSequence randomByteSequence) {
		Element v = this.pepsi.getZModPrime().getZeroElement();
		Element t = this.pepsi.getZModPrime().getZeroElement();
		Element cv = this.pepsi.getCyclicGroup().getIdentityElement();

		return this.pepsi.generate(
			   Tuple.getInstance(secretInput.getFirst(), v, secretInput.getSecond(), t),
			   Pair.getInstance(publicInput, cv), randomByteSequence);
	}

	@Override
	protected boolean abstractVerify(Triple proof, Element publicInput) {
		Element cv = this.pepsi.getCyclicGroup().getIdentityElement();
		return this.pepsi.verify(proof, Pair.getInstance(publicInput, cv));
	}

}
