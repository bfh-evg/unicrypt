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

import ch.bfh.unicrypt.crypto.proofsystem.abstracts.AbstractPreimageProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.FiatShamirSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 * This class covers the AND-composition of preimage proofs: ZKP[(x1,...,xN) : y1=f1(x1) ∧...∧ yN=fN(xN)].
 * <p>
 * @author P. Locher
 */
public class AndPreimageProofSystem
	   extends AbstractPreimageProofSystem<ProductSemiGroup, Tuple, ProductSemiGroup, Tuple, ProductFunction> {

	protected AndPreimageProofSystem(final SigmaChallengeGenerator challengeGenerator,
		   final ProductFunction proofFunction) {
		super(challengeGenerator, proofFunction);
	}

	public static AndPreimageProofSystem getInstance(final ProductFunction proofFunction) {
		return AndPreimageProofSystem.getInstance((Element) null, proofFunction);
	}

	public static AndPreimageProofSystem getInstance(final Element proverId, final ProductFunction proofFunction) {
		SigmaChallengeGenerator challengeGenerator
			   = FiatShamirSigmaChallengeGenerator.getInstance(proofFunction, proverId);
		return AndPreimageProofSystem.getInstance(challengeGenerator, proofFunction);
	}

	public static AndPreimageProofSystem getInstance(final SigmaChallengeGenerator challengeGenerator,
		   final Function... proofFunctions) {
		return AndPreimageProofSystem.getInstance(challengeGenerator, ProductFunction.getInstance(proofFunctions));
	}

	public static AndPreimageProofSystem getInstance(final SigmaChallengeGenerator challengeGenerator,
		   final Function proofFunction, int arity) {
		return AndPreimageProofSystem.getInstance(challengeGenerator,
												  ProductFunction.getInstance(proofFunction, arity));
	}

	public static AndPreimageProofSystem getInstance(final SigmaChallengeGenerator challengeGenerator,
		   final ProductFunction proofFunction) {
		if (challengeGenerator == null || proofFunction == null || proofFunction.getArity() < 1
			   || !proofFunction.getDomain().isSemiGroup() || !proofFunction.getCoDomain().isSemiGroup()) {
			throw new IllegalArgumentException();
		}
		if (AndPreimageProofSystem.checkChallengeSpace(challengeGenerator, proofFunction)) {
			throw new IllegalArgumentException("Spaces of challenge generator and proof function are inequal.");
		}
		return new AndPreimageProofSystem(challengeGenerator, proofFunction);
	}

}
