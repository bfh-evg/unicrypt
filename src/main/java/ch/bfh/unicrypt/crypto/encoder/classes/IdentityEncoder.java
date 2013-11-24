package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class IdentityEncoder
			 extends AbstractEncoder<Set, Set, Element, Element> {

	private final Set set;

	protected IdentityEncoder(Set set) {
		this.set = set;
	}

	public Set getSet() {
		return this.set;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return IdentityFunction.getInstance(this.getSet());
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return IdentityFunction.getInstance(this.getSet());
	}

	public static IdentityEncoder getInstance(Set set) {
		if (set == null) {
			throw new IllegalArgumentException();
		}
		return new IdentityEncoder(set);
	}

}
