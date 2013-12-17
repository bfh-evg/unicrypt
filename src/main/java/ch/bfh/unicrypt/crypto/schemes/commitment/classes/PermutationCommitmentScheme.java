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
			final Permutation permutation = ((PermutationElement) element.getFirst()).getPermutation().invert();
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
