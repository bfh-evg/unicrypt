package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.MultiChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractMultiChallengeGenerator<IS extends Set, IE extends Element, CS extends ProductSet>
			 extends AbstractChallengeGenerator<IS, IE, CS, Tuple>
			 implements MultiChallengeGenerator {

	protected AbstractMultiChallengeGenerator(final IS inputSpace, final CS challengeSpace) {
		super(inputSpace, challengeSpace);
	}

}
