package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface MultiChallengeGenerator
	   extends ChallengeGenerator {

	@Override
	public ProductSet getChallengeSpace();

	@Override
	public Tuple generate(Element input);

}
