package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.random.RandomOracle;
import java.security.SecureRandom;

public abstract class AbstractNonInteractiveChallengeGenerator<IS extends Set, IE extends Element, CS extends Set, CE extends Element>
	   extends AbstractChallengeGenerator<IS, IE, CS, CE>
	   implements NonInteractiveChallengeGenerator {

	private final RandomOracle randomOracle;

	protected AbstractNonInteractiveChallengeGenerator(final Set querySpace, final Set outputSet, final int size, final RandomOracle randomOracle) {
		super((IS) querySpace, (CS) (size < 1 ? outputSet : ProductSet.getInstance(outputSet, size)));
		this.randomOracle = randomOracle;
	}

	@Override
	public RandomOracle getRandomOracle() {
		return this.randomOracle;
	}

	@Override
	protected CE abstractGenerate(Element query) {
		SecureRandom random = randomOracle.getRandom(query.getValue().toByteArray());
		return (CE) this.getChallengeSpace().getRandomElement(random);
	}

}
