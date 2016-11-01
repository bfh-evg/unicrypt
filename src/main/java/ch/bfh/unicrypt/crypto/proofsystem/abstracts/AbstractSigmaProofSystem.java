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
import ch.bfh.unicrypt.crypto.proofsystem.interfaces.SigmaProofSystem;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 * The abstract implementation of the {@link SigmaProofSystem}. It fixes the proof space to a {@link ProductSet} and the
 * proof element to a {@link Triple} and holds an instance of the {@link SigmaChallengeGenerator}.
 * <p>
 * @author P. Locher
 * @param <PRS> The private input space.
 * @param <PRE> The private input element.
 * @param <PUS> The public input space.
 * @param <PUE> The public input element.
 */
public abstract class AbstractSigmaProofSystem<PRS extends Set, PRE extends Element, PUS extends Set, PUE extends Element>
	   extends AbstractProofSystem<PRS, PRE, PUS, PUE, ProductSet, Triple>
	   implements SigmaProofSystem {

	private final SigmaChallengeGenerator challengeGenerator;

	protected AbstractSigmaProofSystem(final SigmaChallengeGenerator challengeGenerator) {
		this.challengeGenerator = challengeGenerator;
	}

	@Override
	public final SigmaChallengeGenerator getChallengeGenerator() {
		return this.challengeGenerator;
	}

	@Override
	public Element getCommitment(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return proof.getFirst();
	}

	@Override
	public Element getChallenge(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return proof.getSecond();
	}

	@Override
	public Element getResponse(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return proof.getThird();
	}

}
