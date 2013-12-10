package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveElementChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import org.junit.Test;

public class StandardNonInteractiveElementChallengeGeneratorTest {

	@Test
	public void testStandardNonInteractiveElementChallengeGenerator() {
		CyclicGroup cyclicGroup = GStarModSafePrime.getInstance(167);
		StandardNonInteractiveElementChallengeGenerator cg = StandardNonInteractiveElementChallengeGenerator.getInstance(cyclicGroup, 10);
		Tuple elements = cg.generate(FiniteByteArraySet.getInstance(64).getElement("test".getBytes()));

		System.out.println(elements);
	}

}
