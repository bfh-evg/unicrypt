package ch.bfh.unicrypt.crypto.schemes.hash.classes;

import ch.bfh.unicrypt.crypto.schemes.hash.abstracts.AbstractHashScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class StandardHashScheme
			 extends AbstractHashScheme<ByteArrayMonoid, FiniteByteArraySet, FiniteByteArrayElement> {

	private final HashMethod hashMethod;

	protected StandardHashScheme(HashMethod hashMethod) {
		this.hashMethod = hashMethod;
	}

	public HashMethod getHashMethod() {
		return this.hashMethod;
	}

	@Override
	protected Function abstractGetHashFunction() {
		return HashFunction.getInstance(ByteArrayMonoid.getInstance(), this.getHashMethod());
	}

	public static StandardHashScheme getInstance(HashMethod hashMethod) {
		if (hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new StandardHashScheme(hashMethod);
	}

}
