package ch.bfh.unicrypt.crypto.keygenerator.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import java.util.Random;

public abstract class AbstractKeyPairGenerator<PRS extends Set, PRE extends Element, PUS extends Set, PUE extends Element>
			 extends UniCrypt
			 implements KeyPairGenerator {

	private ProductSet keyPairSpace;
	private KeyGenerator privateKeyGenerator;
	private Function publicKeyFunction;

	@Override
	public ProductSet getKeyPairSpace() {
		if (this.keyPairSpace == null) {
			this.keyPairSpace = ProductSet.getInstance(this.getPrivateKeySpace(), this.getPublicKeySpace());
		}
		return this.keyPairSpace;
	}

	@Override
	public Pair generateKeyPair() {
		return this.generateKeyPair(null);
	}

	@Override
	public Pair generateKeyPair(Random random) {
		Element privateKey = this.generatePrivateKey(random);
		return (Pair) this.getKeyPairSpace().getElement(privateKey, this.getPublicKey(privateKey));
	}

	@Override
	public PRS getPrivateKeySpace() {
		return (PRS) this.getPublicKeyFunction().getDomain();
	}

	@Override
	public KeyGenerator getPrivateKeyGenerator() {
		if (this.privateKeyGenerator == null) {
			this.privateKeyGenerator = this.abstractGetPrivateKeyGenerator();
		}
		return this.privateKeyGenerator;
	}

	@Override
	public PRE generatePrivateKey() {
		return this.generatePrivateKey(null);
	}

	@Override
	public PRE generatePrivateKey(Random random) {
		return (PRE) this.getPrivateKeyGenerator().generateKey(random);
	}

	@Override
	public PUS getPublicKeySpace() {
		return (PUS) this.getPublicKeyFunction().getCoDomain();
	}

	@Override
	public Function getPublicKeyFunction() {
		if (this.publicKeyFunction == null) {
			this.publicKeyFunction = this.abstractGetPublicKeyFunction();
		}
		return this.publicKeyFunction;
	}

	@Override
	public PUE getPublicKey(Element privateKey) {
		return (PUE) this.getPublicKeyFunction().apply(privateKey);
	}

	@Override
	protected String standardToStringContent() {
		return this.getPrivateKeySpace().toString() + ", " + this.getPublicKeySpace().toString();
	}

	protected abstract Function abstractGetPublicKeyFunction();

	protected abstract KeyGenerator abstractGetPrivateKeyGenerator();

}
