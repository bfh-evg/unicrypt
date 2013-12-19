package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractNonInteractiveChallengeGenerator<IS extends Set, IE extends Element, CS extends Set, CE extends Element>
			 extends AbstractChallengeGenerator<IS, IE, CS, CE>
			 implements NonInteractiveChallengeGenerator {

	private final RandomOracle randomOracle;

	protected AbstractNonInteractiveChallengeGenerator(IS inputSpace, CS challengeSpace, RandomOracle randomOracle) {
		super(inputSpace, challengeSpace);
		this.randomOracle = randomOracle;
	}

	@Override
	public RandomOracle getRandomOracle() {
		return this.randomOracle;
	}

	@Override
	protected CE abstractGenerate(IE input) {
		RandomReferenceString randomReferenceString = this.getRandomOracle().getRandomReferenceString(input);
		return (CE) this.getChallengeSpace().getRandomElement(randomReferenceString);
	}

}
