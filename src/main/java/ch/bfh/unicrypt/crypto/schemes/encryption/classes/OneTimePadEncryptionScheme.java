/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.FixedByteArrayKeyGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractSymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FixedByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.ByteArray;

/**
 *
 * @author rolfhaenni
 */
public class OneTimePadEncryptionScheme
	   extends AbstractSymmetricEncryptionScheme<FixedByteArraySet, FiniteByteArrayElement, FixedByteArraySet, FiniteByteArrayElement, FixedByteArraySet, FixedByteArrayKeyGenerator> {

	private final FixedByteArraySet fixedByteArraySet;

	protected OneTimePadEncryptionScheme(FixedByteArraySet fixedByteArraySet) {
		this.fixedByteArraySet = fixedByteArraySet;
	}

	public final FixedByteArraySet getFixedByteArraySet() {
		return this.fixedByteArraySet;
	}

	@Override
	protected Function abstractGetEncryptionFunction() {
		return new OneTimePadFunction(this.getFixedByteArraySet());
	}

	@Override
	protected Function abstractGetDecryptionFunction() {
		return new OneTimePadFunction(this.getFixedByteArraySet());
	}

	@Override
	protected FixedByteArrayKeyGenerator abstractGetKeyGenerator() {
		return FixedByteArrayKeyGenerator.getInstance(this.getFixedByteArraySet());
	}

	public static OneTimePadEncryptionScheme getInstance(int length) {
		return new OneTimePadEncryptionScheme(FixedByteArraySet.getInstance(length));
	}

	private class OneTimePadFunction
		   extends AbstractFunction<ProductSet, Pair, FixedByteArraySet, FiniteByteArrayElement> {

		protected OneTimePadFunction(FixedByteArraySet fixedByteArraySet) {
			super(ProductSet.getInstance(fixedByteArraySet, 2), fixedByteArraySet);
		}

		@Override
		protected FiniteByteArrayElement abstractApply(Pair element, RandomGenerator randomGenerator) {
			ByteArray byteArray1 = ((FiniteByteArrayElement) element.getFirst()).getByteArray();
			ByteArray byteArray2 = ((FiniteByteArrayElement) element.getSecond()).getByteArray();
			return this.getCoDomain().getElement(byteArray1.xor(byteArray2));
		}
		
	}

}
