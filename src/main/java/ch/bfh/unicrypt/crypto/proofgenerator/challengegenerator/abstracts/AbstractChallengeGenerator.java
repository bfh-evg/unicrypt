package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractChallengeGenerator<IS extends Set, IE extends Element, CS extends Set, CE extends Element>
	   implements ChallengeGenerator {

	private final IS inputSpace;
	private final CS challengeSpace;

	protected AbstractChallengeGenerator(IS inputSpace, CS challengeSpace) {
		this.inputSpace = inputSpace;
		this.challengeSpace = challengeSpace;
	}

	@Override
	public final Set getInputSpace() {
		return this.inputSpace;
	}

	@Override
	public final CS getChallengeSpace() {
		return this.challengeSpace;
	}

	@Override
	public final CE generate(Element input) {
		if (!this.getInputSpace().contains(input)) {
			throw new IllegalArgumentException();
		}
		return this.abstractGenerate((IE) input);
	}

	protected abstract CE abstractGenerate(IE input);

}
