package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public interface ChallengeGenerator {

	public Set getInputSpace();

	public Set getChallengeSpace();

	public Element generate(Element input);

}
