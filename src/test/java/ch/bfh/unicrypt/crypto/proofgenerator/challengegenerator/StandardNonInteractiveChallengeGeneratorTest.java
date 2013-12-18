package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.NonInteractiveMultiChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import org.junit.Test;

public class StandardNonInteractiveChallengeGeneratorTest {

	@Test
	public void testStandardNonInteractiveElementChallengeGenerator() {


		CyclicGroup cyclicGroup = GStarModSafePrime.getInstance(167);
		NonInteractiveMultiChallengeGenerator cg = NonInteractiveMultiChallengeGenerator.getInstance(cyclicGroup, cyclicGroup, 10);
		Tuple elements = cg.generate(cyclicGroup.getRandomElement());

		System.out.println(elements);

	}

}
