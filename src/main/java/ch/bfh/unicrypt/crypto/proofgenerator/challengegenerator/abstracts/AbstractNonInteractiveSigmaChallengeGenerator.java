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
package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.random.classes.ReferenceRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomOracle;

public class AbstractNonInteractiveSigmaChallengeGenerator
	   extends AbstractSigmaChallengeGenerator
	   implements NonInteractiveSigmaChallengeGenerator {

	private final RandomOracle randomOracle;
	private final Element proverId;

	protected AbstractNonInteractiveSigmaChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, Z challengeSpace, RandomOracle randomOracle, Element proverId) {
		super(publicInputSpace, commitmentSpace, challengeSpace);
		this.randomOracle = randomOracle;
		this.proverId = proverId;
	}

	@Override
	public RandomOracle getRandomOracle() {
		return this.randomOracle;
	}

	// May return null!
	@Override
	public Element getProverId() {
		return this.proverId;
	}

	@Override
	protected ZElement abstractGenerate(Pair input) {
		Tuple query = (this.getProverId() == null
			   ? input
			   : Pair.getInstance(input, this.getProverId()));
		ReferenceRandomByteSequence randomReferenceString = this.getRandomOracle().getReferenceRandomByteSequence(query.getByteTree().getByteArray());
		return this.getChallengeSpace().getRandomElement(randomReferenceString);
	}

}
