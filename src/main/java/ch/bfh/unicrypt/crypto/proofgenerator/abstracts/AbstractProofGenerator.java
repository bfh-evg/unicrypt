package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.ProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

public abstract class AbstractProofGenerator<PRS extends Set, PUS extends Set, PS extends Set, E extends Element>
	   implements ProofGenerator {

	@Override
	public final E generate(final Element privateInput, final Element publicInput) {
		return this.generate(privateInput, publicInput, (Random) null);
	}

	@Override
	public final E generate(final Element privateInput, final Element publicInput, final Element proverID) {
		return this.generate(privateInput, publicInput, proverID, (Random) null);
	}

	@Override
	public final E generate(final Element privateInput, final Element publicInput, final Random random) {
		return this.generate(privateInput, publicInput, (Element) null, random);
	}

	@Override
	public final E generate(Element privateInput, Element publicInput, Element proverID, Random random) {
		if (!this.getPrivateInputSpace().contains(privateInput) || !this.getPublicInputSpace().contains(publicInput)) {
			throw new IllegalArgumentException();
		}
		return this.abstractGenerate(privateInput, publicInput, proverID, random);
	}

	@Override
	public final BooleanElement verify(Element proof, Element publicInput) {
		return this.verify(proof, publicInput, (Element) null);
	}

	@Override
	public final BooleanElement verify(Element proof, Element publicInput, Element proverID) {
		if (!this.getProofSpace().contains(proof) || !this.getPublicInputSpace().contains(publicInput)) {
			throw new IllegalArgumentException();
		}
		return this.abstractVerify(proof, publicInput, proverID);
	}

	@Override
	public final PRS getPrivateInputSpace() {
		return this.abstractGetPrivateInputSpace();
	}

	@Override
	public final PUS getPublicInputSpace() {
		return this.abstractGetPublicInputSpace();
	}

	@Override
	public final PS getProofSpace() {
		return this.abstractGetProofSpace();
	}

	protected abstract E abstractGenerate(Element secretInput, Element publicInput, Element otherInput, Random random);

	protected abstract BooleanElement abstractVerify(Element proof, Element publicInput, Element otherInput);

	protected abstract PRS abstractGetPrivateInputSpace();

	protected abstract PUS abstractGetPublicInputSpace();

	protected abstract PS abstractGetProofSpace();

}
