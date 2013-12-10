package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveElementChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.random.RandomOracle;
import java.security.SecureRandom;

public class AbstractNonInteractiveElementChallengeGenerator
	   extends AbstractElementChallengeGenerator
	   implements NonInteractiveElementChallengeGenerator {

	private final RandomOracle randomOracle;

	protected AbstractNonInteractiveElementChallengeGenerator(final Set querySpace, final Set outputSet, final int size, final RandomOracle randomOracle) {
		super(querySpace, outputSet, size);
		this.randomOracle = randomOracle;
	}

	@Override
	public RandomOracle getRandomOracle() {
		return this.randomOracle;
	}

	@Override
	protected Tuple abstractGenerate(Element query) {
		SecureRandom random = randomOracle.getRandom(query.getValue().longValue());
		return this.getChallengeSpace().getRandomElement(random);
	}

}
