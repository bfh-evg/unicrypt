package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.MultiChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractNonInteractiveMultiChallengeGenerator<IS extends Set, IE extends Element, CS extends ProductSet>
			 extends AbstractNonInteractiveChallengeGenerator<IS, IE, CS, Tuple>
			 implements MultiChallengeGenerator {

	protected AbstractNonInteractiveMultiChallengeGenerator(final IS inputSpace, final CS challengeSpace, final RandomOracle randomOracle, Element proverID) {
		super(inputSpace, challengeSpace, randomOracle, proverID);
	}

}
