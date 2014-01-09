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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptionScheme
	   extends AbstractSymmetricEncryptionScheme<ByteArrayMonoid, ByteArrayElement, ByteArrayMonoid, ByteArrayElement, FixedByteArraySet, FixedByteArrayKeyGenerator> {

	public enum KeyLength {

		KEY128(128), KEY192(192), KEY256(226);
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

	public enum Padding {

		NONE("NoPadding"), PKCS5("PKCS5Padding");
		private final String name;

		private Padding(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

	};

	public static final String NAME = "AES";
	public static final KeyLength DEFAULT_KEY_LENGTH = KeyLength.KEY128;
	public static final Mode DEFAULT_MODE = Mode.ECB;
	public static final Padding DEFAULT_PADDING = Padding.NONE;

	public static final int AES_BLOCK_SIZE = 128;
	public static final ByteArrayMonoid AES_MESSAGE_SPACE = ByteArrayMonoid.getInstance(AES_BLOCK_SIZE / Byte.SIZE);
	public static final ByteArrayMonoid AES_ENCRYPTION_SPACE = ByteArrayMonoid.getInstance(AES_BLOCK_SIZE / Byte.SIZE);
	public static final byte[] DEFAULT_IV = new byte[AES_BLOCK_SIZE / Byte.SIZE];

	private final KeyLength keyLength;
	private final Mode mode;
	private final Padding padding;
	private byte[] initializationVector;
	private Cipher cipher;

	protected AESEncryptionScheme(KeyLength keyLength, Mode mode, Padding padding, byte[] initializationVector) {
		this.keyLength = keyLength;
		this.mode = mode;
		this.padding = padding;
		this.initializationVector = initializationVector;
		try {
			this.cipher = Cipher.getInstance(NAME + "/" + mode + "/" + padding);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException();
		} catch (NoSuchPaddingException ex) {
			throw new RuntimeException();
		}
	}

	public KeyLength getKeyLength() {
		return this.keyLength;
	}

	public Mode getMode() {
		return this.mode;
	}

	public Padding getPadding() {
		return this.padding;
	}

	public byte[] getInitializationVector() {
		return this.initializationVector;
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
		return FixedByteArrayKeyGenerator.getInstance(this.getKeyLength().getLenght() / Byte.SIZE);
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
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getByteArray(), NAME);
			byte[] encryptedBytes;
			try {
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				encryptedBytes = cipher.doFinal(message.getByteArray());
			} catch (InvalidKeyException ex) {
				throw new RuntimeException();
			} catch (IllegalBlockSizeException ex) {
				throw new RuntimeException();
			} catch (BadPaddingException ex) {
				throw new RuntimeException();
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
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getByteArray(), NAME);
			byte[] message;
			try {
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
				message = cipher.doFinal(encryption.getByteArray());
			} catch (InvalidKeyException e) {
				throw new RuntimeException();
			} catch (IllegalBlockSizeException e) {
				throw new RuntimeException();
			} catch (BadPaddingException e) {
				throw new RuntimeException();
			}
			return AES_MESSAGE_SPACE.getElement(message);
		}

	}

	public static AESEncryptionScheme getInstance() {
		return AESEncryptionScheme.getInstance(AESEncryptionScheme.DEFAULT_KEY_LENGTH, AESEncryptionScheme.DEFAULT_MODE, AESEncryptionScheme.DEFAULT_PADDING, AESEncryptionScheme.DEFAULT_IV);
	}

	public static AESEncryptionScheme getInstance(KeyLength keyLength, Mode mode, Padding padding, byte[] initializationVector) {
		if (keyLength == null || mode == null || padding == null || initializationVector == null || initializationVector.length != AES_BLOCK_SIZE / Byte.SIZE) {
			throw new IllegalArgumentException();
		}
		return new AESEncryptionScheme(keyLength, mode, padding, initializationVector);
	}

}
