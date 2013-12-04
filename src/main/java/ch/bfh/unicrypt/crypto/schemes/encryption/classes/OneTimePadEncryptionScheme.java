/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.OneTimePadKeyGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractSymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class OneTimePadEncryptionScheme
			 extends AbstractSymmetricEncryptionScheme<FiniteByteArraySet, FiniteByteArrayElement, FiniteByteArraySet, FiniteByteArrayElement, FiniteByteArraySet> {

	private final FiniteByteArraySet finiteByteArraySet;

//  protected ElGamalEncryptionScheme(Function encryptionFunction, Function decryptionFunction, ElGamalKeyPairGenerator keyPairGenerator, Function encryptionFunctionLeft, Function encryptionFunctionRight) {
	protected OneTimePadEncryptionScheme(FiniteByteArraySet finiteByteArraySet) {
		this.finiteByteArraySet = finiteByteArraySet;
	}

	public final FiniteByteArraySet getFiniteByteArraySet() {
		return this.finiteByteArraySet;
	}

	@Override
	protected Function abstractGetEncryptionFunction() {
		return new OneTimePadFunction(this.getFiniteByteArraySet());
	}

	@Override
	protected Function abstractGetDecryptionFunction() {
		return new OneTimePadFunction(this.getFiniteByteArraySet());
	}

	@Override
	protected KeyGenerator abstractGetKeyGenerator() {
		return OneTimePadKeyGenerator.getInstance(this.getFiniteByteArraySet());
	}

	public static OneTimePadEncryptionScheme getInstance(int length) {
		return new OneTimePadEncryptionScheme(FiniteByteArraySet.getInstance(length, true));
	}

	private class OneTimePadFunction
				 extends AbstractFunction<ProductSet, Pair, FiniteByteArraySet, FiniteByteArrayElement> {

		protected OneTimePadFunction(FiniteByteArraySet finiteByteArraySet) {
			super(ProductSet.getInstance(finiteByteArraySet, 2), finiteByteArraySet);
		}

		@Override
		protected FiniteByteArrayElement abstractApply(Pair element, Random random) {
			int length = this.getCoDomain().getLength();
			byte[] bytes1 = ((FiniteByteArrayElement) element.getFirst()).getByteArray();
			byte[] bytes2 = ((FiniteByteArrayElement) element.getSecond()).getByteArray();
			byte[] result = new byte[length];
			for (int i = 0; i < length; i++) {
				result[i] = (byte) (0xff & ((int) bytes1[i]) ^ ((int) bytes2[i]));;
			}
			return this.getCoDomain().getElement(result);
		}

	}

}
