package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ElementChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractElementChallengeGenerator
	   extends AbstractChallengeGenerator<Set, Element, ProductSet, Tuple>
	   implements ElementChallengeGenerator {

	protected AbstractElementChallengeGenerator(final Set inputSpace, final Set outputSet, final int size) {
		super(inputSpace, ProductSet.getInstance(outputSet, size));
	}

}
