package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractSigmaChallengeGenerator
	   extends AbstractChallengeGenerator<ProductSet, Pair, ZMod, ZModElement>
	   implements SigmaChallengeGenerator {

	private final Set publicInputSpace;
	private final SemiGroup commitmentSpace;

	protected AbstractSigmaChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace) {
		super(ProductSet.getInstance(publicInputSpace, commitmentSpace), challengeSpace);
		this.publicInputSpace = publicInputSpace;
		this.commitmentSpace = commitmentSpace;
	}

	@Override
	public Set getPublicInputSpace() {
		return this.publicInputSpace;
	}

	@Override
	public SemiGroup getCommitmentSpace() {
		return this.commitmentSpace;
	}

	@Override
	public ZModElement generate(Element publicInput, Element commitment) {
		return this.generate(Pair.getInstance(publicInput, commitment));
	}

}
