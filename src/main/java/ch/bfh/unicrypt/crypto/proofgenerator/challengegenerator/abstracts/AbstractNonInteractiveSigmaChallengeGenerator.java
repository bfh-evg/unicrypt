package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public class AbstractNonInteractiveSigmaChallengeGenerator
			 extends AbstractSigmaChallengeGenerator
			 implements NonInteractiveSigmaChallengeGenerator {

	private final RandomOracle randomOracle;
	private final Element proverID;

	protected AbstractNonInteractiveSigmaChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, RandomOracle randomOracle, Element proverID) {
		super(publicInputSpace, commitmentSpace, challengeSpace);
		this.randomOracle = randomOracle;
		this.proverID = proverID;
	}

	@Override
	public RandomOracle getRandomOracle() {
		return this.randomOracle;
	}

	// May return null!
	@Override
	public Element getProverId() {
		return this.proverID;
	}

	@Override
	protected ZModElement abstractGenerate(Pair input) {
		Tuple query = (this.getProverId() == null
					 ? input
					 : Pair.getInstance(input, this.getProverId()));
		RandomReferenceString randomReferenceString = this.getRandomOracle().getRandomReferenceString(query);
		return this.getChallengeSpace().getRandomElement(randomReferenceString);
	}

}
