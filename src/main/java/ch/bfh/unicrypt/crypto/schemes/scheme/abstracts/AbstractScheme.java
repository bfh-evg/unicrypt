package ch.bfh.unicrypt.crypto.schemes.scheme.abstracts;

import ch.bfh.unicrypt.crypto.schemes.scheme.interfaces.Scheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.UniCrypt;

/**
 *
 * @author rolfhaenni
 * @param <MS>
 */
public abstract class AbstractScheme<MS extends Set>
			 extends UniCrypt
			 implements Scheme {

	private MS messageSpace;

	@Override
	public final MS getMessageSpace() {
		if (this.messageSpace == null) {
			this.messageSpace = this.abstractGetMessageSpace();
		}
		return this.messageSpace;
	}

	@Override
	protected String standardToStringContent() {
		return this.getMessageSpace().toString();
	}

	protected abstract MS abstractGetMessageSpace();

}
