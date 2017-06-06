/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.crypto.keygenerator.classes.ByteArrayKeyGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractSymmetricEncryptionScheme;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FixedByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptionScheme
	   extends AbstractSymmetricEncryptionScheme<ByteArrayMonoid, ByteArrayElement, ByteArrayMonoid, ByteArrayElement, FixedByteArraySet, FiniteByteArrayElement, ByteArrayKeyGenerator> {

	private static final long serialVersionUID = 1L;

	public enum KeyLength {

		KEY128(128), KEY192(192), KEY256(256); // bits
		private final int length;

		private KeyLength(int length) {
			this.length = length;
		}

		public int getLenght() {
			return this.length;
		}

	}

	public enum Mode {

		CBC, ECB

	};

	private static final String ALGORITHM_NAME = "AES";
	public static final KeyLength DEFAULT_KEY_LENGTH = KeyLength.KEY128;
	public static final Mode DEFAULT_MODE = Mode.CBC;

	public static final int AES_BLOCK_SIZE = 128; // bits
	public static final ByteArrayMonoid AES_SPACE = ByteArrayMonoid.getInstance(AES_BLOCK_SIZE / Byte.SIZE);
	public static final ByteArray DEFAULT_IV = ByteArray.getInstance(false, AES_BLOCK_SIZE / Byte.SIZE);

	private final KeyLength keyLength;
	private final Mode mode;
	private final ByteArray initializationVector;
	private Cipher cipher;

	protected AESEncryptionScheme(KeyLength keyLength, Mode mode, ByteArray initializationVector) {
		super(AES_SPACE, AES_SPACE);
		this.keyLength = keyLength;
		this.mode = mode;
		this.initializationVector = initializationVector;
		try {
			this.cipher = Cipher.getInstance(ALGORITHM_NAME + "/" + mode + "/NoPadding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException exception) {
			throw new UniCryptRuntimeException(ErrorCode.JAVA_AES_FAILURE, exception);
		}
	}

	public KeyLength getKeyLength() {
		return this.keyLength;
	}

	public Mode getMode() {
		return this.mode;
	}

	public ByteArray getInitializationVector() {
		return this.initializationVector;
	}

	@Override
	protected Function abstractGetEncryptionFunction() {
		return new AESEncryptionFunction();
	}

	@Override
	protected Function abstractGetDecryptionFunction() {
		return new AESDecryptionFunction();
	}

	@Override
	protected ByteArrayKeyGenerator abstractGetKeyGenerator() {
		return ByteArrayKeyGenerator.getInstance(this.getKeyLength().getLenght() / Byte.SIZE);
	}

	private class AESEncryptionFunction
		   extends AbstractFunction<AESEncryptionFunction, ProductSet, Pair, ByteArrayMonoid, ByteArrayElement> {

		protected AESEncryptionFunction() {
			super(ProductSet.getInstance(getEncryptionKeySpace(), messageSpace), encryptionSpace);
		}

		@Override
		protected ByteArrayElement abstractApply(Pair element, RandomByteSequence randomByteSequence) {
			FiniteByteArrayElement key = (FiniteByteArrayElement) element.getFirst();
			ByteArrayElement message = (ByteArrayElement) element.getSecond();
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getValue().getBytes(), ALGORITHM_NAME);
			byte[] encryptedBytes;
			try {
				if (mode == Mode.CBC) {
					cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,
								new IvParameterSpec(initializationVector.getBytes()));
				} else {
					cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				}
				encryptedBytes = cipher.doFinal(message.getValue().getBytes());
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				   | InvalidAlgorithmParameterException exception) {
				throw new UniCryptRuntimeException(ErrorCode.JAVA_AES_FAILURE, exception);
			}
			return this.getCoDomain().getElement(encryptedBytes);
		}

	}

	private class AESDecryptionFunction
		   extends AbstractFunction<AESDecryptionFunction, ProductSet, Pair, ByteArrayMonoid, ByteArrayElement> {

		protected AESDecryptionFunction() {
			super(ProductSet.getInstance(getDecryptionKeySpace(), encryptionSpace), messageSpace);
		}

		@Override
		protected ByteArrayElement abstractApply(Pair element, RandomByteSequence randomByteSequence) {
			FiniteByteArrayElement key = (FiniteByteArrayElement) element.getFirst();
			ByteArrayElement encryption = (ByteArrayElement) element.getSecond();
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getValue().getBytes(), ALGORITHM_NAME);
			byte[] message;
			try {
				if (mode == Mode.CBC) {
					cipher.init(Cipher.DECRYPT_MODE, secretKeySpec,
								new IvParameterSpec(initializationVector.getBytes()));
				} else {
					cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
				}
				message = cipher.doFinal(encryption.getValue().getBytes());
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				   | InvalidAlgorithmParameterException exception) {
				throw new UniCryptRuntimeException(ErrorCode.JAVA_AES_FAILURE, exception);
			}
			return this.getCoDomain().getElement(message);
		}

	}

	/**
	 * Convenience method in order to easily get a secret AES-key given a password
	 * <p>
	 * @param password
	 * @return Returns an AES-key
	 */
	public FiniteByteArrayElement getPasswordBasedKey(String password) {
		return super.getSecretKeyGenerator().generateSecretKey(password);
	}

	/**
	 * Convenience method in order to easily get a secret AES-key given a password and a salt
	 * <p>
	 * @param password
	 * @param salt
	 * @return Returns an AES-key
	 */
	public FiniteByteArrayElement getPasswordBasedKey(String password, ByteArray salt) {
		return super.getSecretKeyGenerator().generateSecretKey(password, salt);
	}

	public static AESEncryptionScheme getInstance() {
		return AESEncryptionScheme.getInstance(AESEncryptionScheme.DEFAULT_KEY_LENGTH, AESEncryptionScheme.DEFAULT_MODE,
											   AESEncryptionScheme.DEFAULT_IV);
	}

	public static AESEncryptionScheme getInstance(KeyLength keyLength, Mode mode, ByteArray initializationVector) {
		if (keyLength == null || mode == null || initializationVector == null
			   || initializationVector.getLength() != AES_BLOCK_SIZE / Byte.SIZE) {
			throw new IllegalArgumentException();
		}
		return new AESEncryptionScheme(keyLength, mode, initializationVector);
	}

}
