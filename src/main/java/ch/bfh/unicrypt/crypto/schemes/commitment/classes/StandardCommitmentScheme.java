package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractDeterministicCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class StandardCommitmentScheme<CS extends CyclicGroup, CE extends Element>
	extends AbstractDeterministicCommitmentScheme<ZMod, CS, CE> {

    private final CS cyclicGroup;
    private final CE generator;

    protected StandardCommitmentScheme(CS cyclicGroup, CE generator) {
	this.cyclicGroup = cyclicGroup;
	this.generator = generator;
    }

    public final CS getCyclicGroup() {
	return this.cyclicGroup;
    }

    public final CE getGenerator() {
	return this.generator;
    }

    @Override
    protected Function abstractGetCommitmentFunction() {
	return GeneratorFunction.getInstance(this.getCyclicGroup().getDefaultGenerator());
    }

    public static <CS extends CyclicGroup, CE extends Element> StandardCommitmentScheme<CS, CE> getInstance(CS cyclicGroup) {
	return new StandardCommitmentScheme<CS, CE>(cyclicGroup, (CE) cyclicGroup.getDefaultGenerator());
    }

    public static <CS extends CyclicGroup, CE extends Element> StandardCommitmentScheme<CS, CE> getInstance(CE generator) {
	return StandardCommitmentScheme.<CS, CE>getInternalInstance(generator);
    }

    public static StandardCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarMod gStarMod) {
	return StandardCommitmentScheme.<GStarMod, GStarModElement>getInstance(gStarMod.getDefaultGenerator());
    }

    public static StandardCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarModElement generator) {
	return StandardCommitmentScheme.<GStarMod, GStarModElement>getInternalInstance(generator);
    }

    private static <CS extends CyclicGroup, CE extends Element> StandardCommitmentScheme<CS, CE> getInternalInstance(CE generator) {
	if (!generator.isGenerator()) {
	    throw new IllegalArgumentException();
	}
	return new StandardCommitmentScheme<CS, CE>((CS) generator.getSet(), generator);
    }

}
