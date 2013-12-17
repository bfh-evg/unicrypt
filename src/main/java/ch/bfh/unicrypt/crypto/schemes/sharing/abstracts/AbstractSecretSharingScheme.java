package ch.bfh.unicrypt.crypto.schemes.sharing.abstracts;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.crypto.schemes.sharing.interfaces.SecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractSecretSharingScheme<MS extends Set, ME extends Element, SS extends Set, SE extends Element>
			 extends AbstractScheme<MS>
			 implements SecretSharingScheme {

	private final int size;

	protected AbstractSecretSharingScheme(int size) {
		this.size = size;
	}

	@Override
	public final SS getShareSpace() {
		return this.abstractGetShareSpace();
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final SE[] share(Element message) {
		return this.share(message, (RandomGenerator) null);
	}

	@Override
	public final SE[] share(Element message, RandomGenerator randomGenerator) {
		if (message == null || !this.getMessageSpace().contains(message)) {
			throw new IllegalArgumentException();
		}
		return this.abstractShare(message, randomGenerator);
	}

	@Override
	public final ME recover(Element... shares) {
		if (shares == null || shares.length < this.getThreshold() || shares.length > this.getSize()) {
			throw new IllegalArgumentException();
		}
		for (Element share : shares) {
			if (share == null || !this.getShareSpace().contains(share)) {
				throw new IllegalArgumentException();
			}
		}
		return this.abstractRecover(shares);
	}

	protected int getThreshold() { // this method is not really needed here, but it simplifies the method recover
		return this.getSize();
	}

	protected abstract SS abstractGetShareSpace();

	protected abstract SE[] abstractShare(Element message, RandomGenerator randomGenerator);

	protected abstract ME abstractRecover(Element[] shares);

}
