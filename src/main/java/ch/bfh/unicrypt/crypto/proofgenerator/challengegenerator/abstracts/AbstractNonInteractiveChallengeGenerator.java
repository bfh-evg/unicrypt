package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractNonInteractiveChallengeGenerator<IS extends Set, IE extends Element, CS extends Set, CE extends Element>
			 extends AbstractChallengeGenerator<IS, IE, CS, CE>
			 implements NonInteractiveChallengeGenerator {

	private final RandomOracle randomOracle;
	private final Element proverID;

	protected AbstractNonInteractiveChallengeGenerator(IS inputSpace, CS challengeSpace, RandomOracle randomOracle, Element proverID) {
		super(inputSpace, challengeSpace);
		this.randomOracle = randomOracle;
		this.proverID = proverID;
	}

	@Override
	public RandomOracle getRandomOracle() {
		return this.randomOracle;
	}

	// May return null!
	@Override
	public Element getProverID() {
		return this.proverID;
	}

	@Override
	protected CE abstractGenerate(IE input) {
		Element query = (this.getProverID() == null
					 ? input
					 : Pair.getInstance(input, this.getProverID()));
		RandomReferenceString randomReferenceString = this.getRandomOracle().getRandomReferenceString(query);
		return (CE) this.getChallengeSpace().getRandomElement(randomReferenceString);
	}

}
