package ch.bfh.unicrypt.crypto.schemes.signature.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.crypto.schemes.signature.interfaces.SignatureScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractSignatureScheme<MS extends Set, ME extends Element, SS extends Set, SE extends Element>
			 extends AbstractScheme<MS>
			 implements SignatureScheme {

	private Function signatureFunction;
	private Function verificationFunction;
	private KeyPairGenerator keyPairGenerator;

	@Override
	public SE sign(final Element privateKey, final Element message) {
		return (SE) this.getSignatureFunction().apply(privateKey, message);
	}

	@Override
	public BooleanElement verify(final Element publicKey, final Element message, final Element signature) {
		return BooleanSet.getInstance().getElement(this.getVerificationFunction().apply(publicKey, message, signature).isEquivalent(BooleanSet.TRUE));
	}

	@Override
	public SS getSignatureSpace() {
		return (SS) this.getSignatureFunction().getCoDomain();
	}

	@Override
	public final KeyPairGenerator getKeyPairGenerator() {
		if (this.keyPairGenerator == null) {
			this.keyPairGenerator = this.abstractGetKeyPairGenerator();
		}
		return this.keyPairGenerator;
	}

	@Override
	public final Function getSignatureFunction() {
		if (this.signatureFunction == null) {
			this.signatureFunction = this.abstractGetSignatureFunction();
		}
		return this.signatureFunction;
	}

	@Override
	public final Function getVerificationFunction() {
		if (this.verificationFunction == null) {
			this.verificationFunction = this.abstractGetVerificationFunction();
		}
		return this.verificationFunction;
	}

	@Override
	protected MS abstractGetMessageSpace() {
		return (MS) this.getSignatureFunction().getDomain();
	}

	protected abstract KeyPairGenerator abstractGetKeyPairGenerator();

	protected abstract Function abstractGetSignatureFunction();

	protected abstract Function abstractGetVerificationFunction();

}
