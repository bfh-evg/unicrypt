package ch.bfh.unicrypt.crypto.schemes.sharing.abstracts;

import ch.bfh.unicrypt.crypto.schemes.sharing.interfaces.ThresholdSecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractThresholdSecretSharingScheme<MS extends Set, ME extends Element, SS extends Set, SE extends Element>
			 extends AbstractSecretSharingScheme<MS, ME, SS, SE>
			 implements ThresholdSecretSharingScheme {

	private final int threshold;

	protected AbstractThresholdSecretSharingScheme(int size, int threshold) {
		super(size);
		this.threshold = threshold;
	}

	@Override
	public final int getThreshold() {
		return this.threshold;
	}

}
