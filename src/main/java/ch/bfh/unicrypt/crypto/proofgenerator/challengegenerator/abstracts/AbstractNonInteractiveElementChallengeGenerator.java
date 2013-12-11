package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveElementChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.random.RandomOracle;
import java.security.SecureRandom;

public abstract class AbstractNonInteractiveElementChallengeGenerator
	   extends AbstractElementChallengeGenerator
	   implements NonInteractiveElementChallengeGenerator {

	private final RandomOracle randomOracle;
	private final HashMethod hashMethod;

	protected AbstractNonInteractiveElementChallengeGenerator(final Set querySpace, final Set outputSet, final int size, final RandomOracle randomOracle, final HashMethod hashMethod) {
		super(querySpace, outputSet, size);
		this.randomOracle = randomOracle;
		this.hashMethod = hashMethod;
	}

	@Override
	public RandomOracle getRandomOracle() {
		return this.randomOracle;
	}

	@Override
	public HashMethod getHashMethod() {
		return this.hashMethod;
	}

	@Override
	protected Tuple abstractGenerate(Element query) {
		SecureRandom random = randomOracle.getRandom(query.getHashValue(this.hashMethod).getByteArray());
		return this.getChallengeSpace().getRandomElement(random);
	}

}
