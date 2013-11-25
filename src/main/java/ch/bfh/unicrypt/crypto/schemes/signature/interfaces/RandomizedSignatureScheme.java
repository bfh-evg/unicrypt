package ch.bfh.unicrypt.crypto.schemes.signature.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

public interface RandomizedSignatureScheme extends SignatureScheme {

    public Set getRandomizationSpace();

    public Element sign(final Element privateKey, final Element message, Random random);

    public Element sign(final Element privateKey, final Element message, final Element randomization);

}
