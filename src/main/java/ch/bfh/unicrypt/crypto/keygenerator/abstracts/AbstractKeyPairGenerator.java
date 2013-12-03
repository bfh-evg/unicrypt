package ch.bfh.unicrypt.crypto.keygenerator.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.RandomFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import java.util.Random;

public abstract class AbstractKeyPairGenerator<PRS extends Set, PRE extends Element, PUS extends Set, PUE extends Element>
			 extends UniCrypt
			 implements KeyPairGenerator {

	private final PRS privateKeySpace;
	private Function privateKeyGenerationFunction;
	private Function publicKeyGenerationFunction;
	private Function keyPairGenerationFunction;

	protected AbstractKeyPairGenerator(PRS privateKeySpace) {
		this.privateKeySpace = privateKeySpace;
	}

	@Override
	public final PRS getPrivateKeySpace() {
		return privateKeySpace;
	}

	@Override
	public final Function getPrivateKeyGenerationFunction() {
		if (this.privateKeyGenerationFunction == null) {
			this.privateKeyGenerationFunction = this.standardGetPrivateKeyGenerationFunction();
		}
		return this.privateKeyGenerationFunction;
	}

	@Override
	public final PRE generatePrivateKey() {
		return this.generatePrivateKey((Random) null);
	}

	@Override
	public final PRE generatePrivateKey(Random random) {
		return (PRE) this.getPrivateKeyGenerationFunction().apply(SingletonGroup.getInstance().getElement(), random);
	}

	@Override
	public final PUS getPublicKeySpace() {
		return (PUS) this.getPublicKeyGenerationFunction().getCoDomain();
	}

	@Override
	public final Function getPublicKeyGenerationFunction() {
		if (this.publicKeyGenerationFunction == null) {
			this.publicKeyGenerationFunction = this.abstractGetPublicKeyGenerationFunction();
		}
		return this.publicKeyGenerationFunction;
	}

	@Override
	public final PUE generatePublicKey(Element privateKey) {
		return this.generatePublicKey(privateKey, (Random) null);
	}

	@Override
	public final PUE generatePublicKey(Element privateKey, Random random) {
		if (privateKey == null || !this.getPrivateKeySpace().contains(privateKey)) {
			throw new IllegalArgumentException();
		}
		return (PUE) this.getPublicKeyGenerationFunction().apply(privateKey, random);
	}

	@Override
	public ProductSet getKeyPairSpace() {
		return (ProductSet) this.getKeyPairGenerationFunction().getCoDomain();
	}

	//		this.keyPairSpace = ProductSet.getInstance(this.getPrivateKeySpace(), this.getPublicKeySpace());
	@Override
	public final Function getKeyPairGenerationFunction() {
		if (this.keyPairGenerationFunction == null) {
			this.keyPairGenerationFunction = CompositeFunction.getInstance(
						 this.getPrivateKeyGenerationFunction(),
						 MultiIdentityFunction.getInstance(this.privateKeySpace, 2),
						 ProductFunction.getInstance(IdentityFunction.getInstance(this.privateKeySpace),
																				 this.getPublicKeyGenerationFunction()));
		}
		return this.keyPairGenerationFunction;
	}

	@Override
	public Pair generateKeyPair() {
		return this.generateKeyPair((Random) null);
	}

	@Override
	public Pair generateKeyPair(Random random) {
		return (Pair) this.getKeyPairGenerationFunction().apply(random);
	}

	@Override
	protected String standardToStringContent() {
		return this.getPrivateKeySpace().toString() + ", " + this.getPublicKeySpace().toString();
	}

	protected Function standardGetPrivateKeyGenerationFunction() {
		return RandomFunction.getInstance(this.getPrivateKeySpace());
	}

	protected abstract Function abstractGetPublicKeyGenerationFunction();

}
