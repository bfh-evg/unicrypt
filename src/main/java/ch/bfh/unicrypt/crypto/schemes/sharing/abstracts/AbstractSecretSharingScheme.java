package ch.bfh.unicrypt.crypto.schemes.sharing.abstracts;

import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.crypto.schemes.sharing.interfaces.SecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractSecretSharingScheme<MS extends Set, ME extends Element, SS extends Set, SE extends Element>
			 extends AbstractScheme
			 implements SecretSharingScheme {

	private final int size;

	protected AbstractSecretSharingScheme(int size) {
		this.size = size;
	}

	@Override
	public final MS getMessageSpace() {
		return null;
	}

	@Override
	public final SS getShareSpace() {
		return null;
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final Tuple share(Element message) {
		return null;
	}

	@Override
	public final ME recover(Tuple shares) {
		return null;

	}

}
