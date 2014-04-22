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
package ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.abstracts.AbstractNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.helper.distribution.UniformDistribution;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.random.interfaces.RandomOracle;

public class StandardNonInteractiveSigmaChallengeGenerator
	   extends AbstractNonInteractiveSigmaChallengeGenerator {

	protected StandardNonInteractiveSigmaChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, Z challengeSpace, RandomOracle randomOracle, Element proverId) {
		super(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, proverId);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, Z challengeSpace) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (RandomOracle) null, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, Z challengeSpace, Element proverId) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (RandomOracle) null, (Element) proverId);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, Z challengeSpace, RandomOracle randomOracle) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, Z challengeSpace, RandomOracle randomOracle, Element proverId) {
		if (publicInputSpace == null || commitmentSpace == null || challengeSpace == null) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return new StandardNonInteractiveSigmaChallengeGenerator(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, proverId);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, (RandomOracle) null, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, RandomOracle randomOracle) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, randomOracle, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, Element proverId) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, (RandomOracle) null, proverId);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, RandomOracle randomOracle, Element proverId) {
		if (function == null || !function.getCoDomain().isSemiGroup()) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return new StandardNonInteractiveSigmaChallengeGenerator(
			   function.getCoDomain(), (SemiGroup) function.getCoDomain(), Z.getInstance(UniformDistribution.getInstance(function.getDomain().getMinimalOrder())), randomOracle, proverId);

	}

}
