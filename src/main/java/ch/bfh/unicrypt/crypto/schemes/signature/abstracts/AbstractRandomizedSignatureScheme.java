package ch.bfh.unicrypt.crypto.schemes.signature.abstracts;

import ch.bfh.unicrypt.crypto.schemes.signature.interfaces.RandomizedSignatureScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

public abstract class AbstractRandomizedSignatureScheme<RS extends Set, M extends Set, S extends Set, SE extends Element, E extends Element> extends AbstractSignatureScheme<M, S, E> implements RandomizedSignatureScheme {

    @Override
    public RS getRandomizationSpace() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SE sign(Element privateKey, Element message, Random random) {
	return this.sign(privateKey, message, getRandomizationSpace().getRandomElement(random));
    }

    @Override
    public SE sign(Element privateKey, Element message, Element randomization) {
	if (!this.getSignatureSpace().contains(privateKey) || !this.getMessageSpace().contains(message) || !this.getRandomizationSpace().contains(randomization)) {
	    throw new IllegalArgumentException();
	}
	return (SE) this.getSignatureFunction().apply(privateKey, message, randomization);
    }
}
