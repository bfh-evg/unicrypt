package ch.bfh.unicrypt.crypto.keygenerator.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.RandomFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import java.util.Random;

public abstract class AbstractKeyGenerator<KS extends Set, KE extends Element>
			 extends UniCrypt
			 implements KeyGenerator {

	private final KS keySpace;
	private Function keyGenerationFunction; // with singleton domain

	protected AbstractKeyGenerator(final KS keySpace) {
		this.keySpace = keySpace;
	}

	@Override
	public final KS getKeySpace() {
		return this.keySpace;
	}

	@Override
	public KE generateKey() {
		return this.generateKey((Random) null);
	}

	@Override
	public KE generateKey(Random random) {
		return (KE) this.getKeyGenerationFunction().apply(SingletonGroup.getInstance().getElement(), random);
	}

	@Override
	public Function getKeyGenerationFunction() {
		if (this.keyGenerationFunction == null) {
			this.keyGenerationFunction = this.standardGetKeyGenerationFunction();
		}
		return this.keyGenerationFunction;
	}

	protected Function standardGetKeyGenerationFunction() {
		return RandomFunction.getInstance(this.getKeySpace());
	}

	@Override
	protected String standardToStringContent() {
		return this.getKeySpace().toString();
	}

}
