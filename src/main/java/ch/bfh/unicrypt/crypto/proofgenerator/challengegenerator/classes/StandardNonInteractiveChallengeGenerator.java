package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts.AbstractNonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.random.RandomOracle;

public class StandardNonInteractiveChallengeGenerator<IS extends Set, IE extends Element, CS extends Set, CE extends Element>
	   extends AbstractNonInteractiveChallengeGenerator<IS, IE, CS, CE> {

	protected StandardNonInteractiveChallengeGenerator(final Set inputSpace,
		   final Set outputSet, final RandomOracle randomOracle) {
		super(inputSpace, outputSet, 0, randomOracle);
	}

	protected StandardNonInteractiveChallengeGenerator(final Set inputSpace,
		   final Set outputSet, final int size, final RandomOracle randomOracle) {
		super(inputSpace, outputSet, size, randomOracle);
	}

	public static StandardNonInteractiveChallengeGenerator<Set, Element, Set, Element> getInstance(final Set inputSpace, final Set outputSet) {
		return StandardNonInteractiveChallengeGenerator.getInstance(inputSpace, outputSet, RandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveChallengeGenerator<Set, Element, Set, Element> getInstance(final Set inputSpace, final Set outputSet, final RandomOracle randomOracle) {
		if (inputSpace == null || outputSet == null || randomOracle == null) {
			throw new IllegalArgumentException();
		}
		return new StandardNonInteractiveChallengeGenerator<Set, Element, Set, Element>(inputSpace, outputSet, randomOracle);
	}

	public static StandardNonInteractiveChallengeGenerator<Set, Element, ProductSet, Tuple> getInstance(final Set inputSpace, final Set outputSet, final int size) {
		return StandardNonInteractiveChallengeGenerator.getInstance(inputSpace, outputSet, size, RandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveChallengeGenerator<Set, Element, ProductSet, Tuple> getInstance(final Set inputSpace, final Set outputSet, final int size, final RandomOracle randomOracle) {
		if (inputSpace == null || outputSet == null || size < 1 || randomOracle == null) {
			throw new IllegalArgumentException();
		}
		return new StandardNonInteractiveChallengeGenerator<Set, Element, ProductSet, Tuple>(inputSpace, outputSet, size, randomOracle);
	}

}
