package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ModuloFunction;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class AbstractNonInteractiveSigmaChallengeGenerator
	   extends AbstractSigmaChallengeGenerator
	   implements NonInteractiveSigmaChallengeGenerator {

	private final HashMethod hashMethod;
	private final Element proverId;

	protected AbstractNonInteractiveSigmaChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, Element proverId, HashMethod hashMethod) {
		super(publicInputSpace, commitmentSpace, challengeSpace);

		this.proverId = proverId;
		this.hashMethod = hashMethod;
	}

	@Override
	public HashMethod getHashMethod() {
		return this.hashMethod;
	}

	@Override
	public Element getProverId() {
		return this.proverId;
	}

	@Override
	protected ZModElement abstractGenerate(Pair input) {
		Tuple toHash = (this.getProverId() == null
			   ? input
			   : Pair.getInstance(input, this.getProverId()));
		FiniteByteArrayElement hashValue = toHash.getHashValue(this.getHashMethod());
		return ModuloFunction.getInstance(hashValue.getSet(), this.getChallengeSpace()).apply(hashValue);
	}

}
