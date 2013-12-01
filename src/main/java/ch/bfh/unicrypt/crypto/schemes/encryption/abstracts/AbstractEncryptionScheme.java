package ch.bfh.unicrypt.crypto.schemes.encryption.abstracts;

import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.EncryptionScheme;
import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractEncryptionScheme<MS extends Set, ME extends Element, ES extends Set, EE extends Element>
			 extends AbstractScheme<MS>
			 implements EncryptionScheme {

	private Function encryptionFunction;
	private Function decryptionFunction;

	@Override
	public final ES getEncryptionSpace() {
		return (ES) this.getEncryptionFunction().getCoDomain();
	}

	@Override
	public final Function getEncryptionFunction() {
		if (this.encryptionFunction == null) {
			this.encryptionFunction = this.abstractGetEncryptionFunction();
		}
		return this.encryptionFunction;
	}

	@Override
	public final Function getDecryptionFunction() {
		if (this.decryptionFunction == null) {
			this.decryptionFunction = this.abstractGetDecryptionFunction();
		}
		return this.decryptionFunction;
	}

	@Override
	public EE encrypt(Element encryptionKey, Element message) {
		if (!this.getEncryptionKeySpace().contains(encryptionKey) || !this.getMessageSpace().contains(message)) {
			throw new IllegalArgumentException();
		}
		return (EE) this.getEncryptionFunction().apply(encryptionKey, message);
	}

	@Override
	public final ME decrypt(Element decryptionKey, Element encryption) {
		if (!this.getDecryptionKeySpace().contains(decryptionKey) || !this.getEncryptionSpace().contains(encryption)) {
			throw new IllegalArgumentException();
		}
		return (ME) this.getDecryptionFunction().apply(decryptionKey, encryption);
	}

	@Override
	protected final MS abstractGetMessageSpace() {
		return (MS) this.getDecryptionFunction().getCoDomain();
	}

	protected abstract Function abstractGetEncryptionFunction();

	protected abstract Function abstractGetDecryptionFunction();

}
