package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

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
import ch.bfh.unicrypt.math.random.RandomOracle;
import java.lang.reflect.Array;
import java.util.Random;

// RS: ProductZModPrime does not exist, so ProductGroup
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
		return new PermutationCommitmentFunction();
	}

	public int getSize() {
		return this.messageGenerators.length;
	}

	public static PermutationCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final int size) {
		return PermutationCommitmentScheme.getInstance(cyclicGroup, size, (RandomOracle) null);
	}

	public static PermutationCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final int size, RandomOracle randomOracle) {
		if (randomOracle == null) {
			randomOracle = RandomOracle.DEFAULT;
		}
		final Element randomizationGenerator = cyclicGroup.getIndependentGenerator(0, randomOracle);
		final Element[] messageGenerators = (Element[]) Array.newInstance(Element.class, size);
		for (int i = 0; i < size; i++) {
			messageGenerators[i] = (Element) cyclicGroup.getIndependentGenerator(i + 1, randomOracle);
		}

		return new PermutationCommitmentScheme(cyclicGroup, randomizationGenerator, messageGenerators);
	}

	private class PermutationCommitmentFunction
		   extends AbstractFunction<ProductSet, Pair, ProductGroup, Tuple> {

		protected PermutationCommitmentFunction() {
			super(ProductSet.getInstance(PermutationGroup.getInstance(getSize()),
										 ProductGroup.getInstance(cyclicGroup.getZModOrder(), getSize())),
				  ProductGroup.getInstance(cyclicGroup, getSize()));
		}

		@Override
		protected Tuple abstractApply(Pair element, Random random) {
			final Permutation permutation = ((PermutationElement) element.getFirst()).getPermutation();
			final Tuple randomizations = (Tuple) element.getSecond();
			Element[] ret = new Element[getSize()];
			for (int i = 0; i < getSize(); i++) {
				ret[i] = randomizationGenerator
					   .selfApply(randomizations.getAt(i))
					   .apply(messageGenerators[permutation.permute(i)]);
			}

			return Tuple.getInstance(ret);
		}

	}

}
