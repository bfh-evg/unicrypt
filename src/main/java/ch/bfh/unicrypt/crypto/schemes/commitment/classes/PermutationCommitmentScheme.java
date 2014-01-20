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
package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Permutation;

//
//
//
// @see [Wik09] Construction 1: Pedersen Commitment
// @see [TW10]  Matrix Commitment
//
// -> Permutation commitment is equal to the columnwise commitment to a permutation matrix
//
public class PermutationCommitmentScheme
	   extends AbstractRandomizedCommitmentScheme<PermutationGroup, PermutationElement, ProductGroup, Tuple, ProductGroup> {

	private final CyclicGroup cyclicGroup;
	private final Element randomizationGenerator;
	private final Element[] messageGenerators;

	protected PermutationCommitmentScheme(CyclicGroup cyclicGroup, Element randomizationGenerator, Element[] messageGenerators) {
		this.cyclicGroup = cyclicGroup;
		this.randomizationGenerator = randomizationGenerator;
		this.messageGenerators = messageGenerators;
	}

	@Override
	protected Function abstractGetCommitmentFunction() {
		return new PermutationCommitmentFunction(this.cyclicGroup, this.randomizationGenerator, this.messageGenerators);
	}

	public CyclicGroup getCyclicGroup() {
		return this.cyclicGroup;
	}

	public int getSize() {
		return this.messageGenerators.length;
	}

	public Element getRandomizationGenerator() {
		return this.randomizationGenerator;
	}

	public Element[] getMessageGenerators() {
		return this.messageGenerators.clone();
	}

	public static PermutationCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final int size) {
		return PermutationCommitmentScheme.getInstance(cyclicGroup, size, (RandomReferenceString) null);
	}

	public static PermutationCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final int size, RandomReferenceString randomReferenceString) {
		if (randomReferenceString == null) {
			randomReferenceString = PseudoRandomReferenceString.getInstance();
		}
		Element randomizationGenerator = cyclicGroup.getIndependentGenerator(0, randomReferenceString);
		Element[] messageGenerators = cyclicGroup.getIndependentGenerators(1, size, randomReferenceString);
		return new PermutationCommitmentScheme(cyclicGroup, randomizationGenerator, messageGenerators);
	}

	private class PermutationCommitmentFunction
		   extends AbstractFunction<ProductSet, Pair, ProductGroup, Tuple> {

		private final Element randomizationGenerator;
		private final Element[] messageGenerators;

		protected PermutationCommitmentFunction(CyclicGroup cyclicGroup, Element randomizationGenerator, Element[] messageGenerators) {
			super(ProductSet.getInstance(PermutationGroup.getInstance(messageGenerators.length),
										 ProductGroup.getInstance(cyclicGroup.getZModOrder(), messageGenerators.length)),
				  ProductGroup.getInstance(cyclicGroup, messageGenerators.length));

			this.randomizationGenerator = randomizationGenerator;
			this.messageGenerators = messageGenerators;
		}

		@Override
		protected Tuple abstractApply(Pair element, RandomGenerator randomGenerator) {
			final Permutation permutation = ((PermutationElement) element.getFirst()).getValue().invert();
			final Tuple randomizations = (Tuple) element.getSecond();
			Element[] ret = new Element[this.messageGenerators.length];
			for (int i = 0; i < this.messageGenerators.length; i++) {
				ret[i] = this.randomizationGenerator
					   .selfApply(randomizations.getAt(i))
					   .apply(this.messageGenerators[permutation.permute(i)]);
			}

			return Tuple.getInstance(ret);
		}

	}

}
