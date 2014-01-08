package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.FixedByteArrayKeyGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractSymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FixedByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptionScheme
	   extends AbstractSymmetricEncryptionScheme<ByteArrayMonoid, ByteArrayElement, ByteArrayMonoid, ByteArrayElement, FixedByteArraySet, FixedByteArrayKeyGenerator> {

	public static final int KEY_128 = 128;
	public static final int KEY_192 = 192;
	public static final int KEY_256 = 256;
	public static final int DEFAULT_KEY_LENGTH = KEY_128;
	public static final int[] SUPPORTED_KEY_LENGTHS = {KEY_128, KEY_192, KEY_256};

	public static final int AES_BLOCK_SIZE = 128;
	public static final ByteArrayMonoid AES_MESSAGE_SPACE = ByteArrayMonoid.getInstance(AES_BLOCK_SIZE / Byte.SIZE);
	public static final ByteArrayMonoid AES_ENCRYPTION_SPACE = ByteArrayMonoid.getInstance(AES_BLOCK_SIZE / Byte.SIZE);

	private final int keyLength;

	protected AESEncryptionScheme(int keyLength) {
		this.keyLength = keyLength;
	}

	public int getKeyLength() {
		return this.keyLength;
	}

	@Override
	protected Function abstractGetEncryptionFunction() {
		return new AESEncryptionFunction(this.getKeyGenerator().getKeySpace());
	}

	@Override
	protected Function abstractGetDecryptionFunction() {
		return new AESDecryptionFunction(this.getKeyGenerator().getKeySpace());
	}

	@Override
	protected FixedByteArrayKeyGenerator abstractGetKeyGenerator() {
		return FixedByteArrayKeyGenerator.getInstance(this.getKeyLength() / Byte.SIZE);
	}

	private class AESEncryptionFunction
		   extends AbstractFunction<ProductSet, Pair, ByteArrayMonoid, ByteArrayElement> {

		protected AESEncryptionFunction(FixedByteArraySet keySpace) {
			super(ProductSet.getInstance(keySpace, AES_MESSAGE_SPACE), AES_MESSAGE_SPACE);
		}

		@Override
		protected ByteArrayElement abstractApply(Pair element, RandomGenerator randomGenerator) {
			FiniteByteArrayElement key = (FiniteByteArrayElement) element.getFirst();
			ByteArrayElement message = (ByteArrayElement) element.getSecond();
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getByteArray(), "AES");
			byte[] encryptedBytes;
			try {
				Cipher cipher;
				cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				encryptedBytes = cipher.doFinal(message.getByteArray());
			} catch (Exception e) {
				throw new UnknownError();
			}
			return AES_ENCRYPTION_SPACE.getElement(encryptedBytes);
		}

	}

	private class AESDecryptionFunction
		   extends AbstractFunction<ProductSet, Pair, ByteArrayMonoid, ByteArrayElement> {

		protected AESDecryptionFunction(FixedByteArraySet keySpace) {
			super(ProductSet.getInstance(keySpace, AES_MESSAGE_SPACE), AES_MESSAGE_SPACE);
		}

		@Override
		protected ByteArrayElement abstractApply(Pair element, RandomGenerator randomGenerator) {
			FiniteByteArrayElement key = (FiniteByteArrayElement) element.getFirst();
			ByteArrayElement encryption = (ByteArrayElement) element.getSecond();
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getByteArray(), "AES");
			byte[] message;
			try {
				Cipher cipher;
				cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
				message = cipher.doFinal(encryption.getByteArray());
			} catch (Exception e) {
				throw new UnknownError();
			}
			return AES_MESSAGE_SPACE.getElement(message);
		}

	}

	private static final Map<Integer, AESEncryptionScheme> instances = new HashMap<Integer, AESEncryptionScheme>();

	public static AESEncryptionScheme getInstance() {
		return AESEncryptionScheme.getInstance(AESEncryptionScheme.DEFAULT_KEY_LENGTH);
	}

	public static AESEncryptionScheme getInstance(int keyLength) {
		for (int length : AESEncryptionScheme.SUPPORTED_KEY_LENGTHS) {
			if (keyLength == length) {
				AESEncryptionScheme instance = AESEncryptionScheme.instances.get(keyLength);
				if (instance == null) {
					instance = new AESEncryptionScheme(keyLength);
					AESEncryptionScheme.instances.put(keyLength, instance);
				}
				return instance;
			}
		}
		throw new IllegalArgumentException();
	}

}
