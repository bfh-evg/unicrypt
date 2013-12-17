package ch.bfh.unicrypt.crypto.schemes.encryption.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.FixedByteArrayKeyGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractSymmetricEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.FixedByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.HashMap;
import java.util.Map;

public class AESEncryptionScheme
			 extends AbstractSymmetricEncryptionScheme<ByteArrayMonoid, ByteArrayElement, ByteArrayMonoid, ByteArrayElement, FixedByteArraySet, FixedByteArrayKeyGenerator> {

	public static final int KEY_128 = 128;
	public static final int KEY_192 = 192;
	public static final int KEY_256 = 256;
	public static final int DEFAULT_KEY_LENGTH = KEY_128;
	public static final int[] SUPPORTED_KEY_LENGTHS = {KEY_128, KEY_192, KEY_256};

	public static final int AES_BLOCK_SIZE = 128;
	public static final ByteArrayMonoid AES_MESSAGE_SPACE = ByteArrayMonoid.getInstance(AES_BLOCK_SIZE / Byte.SIZE);
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
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

	}

	private class AESDecryptionFunction
				 extends AbstractFunction<ProductSet, Pair, ByteArrayMonoid, ByteArrayElement> {

		protected AESDecryptionFunction(FixedByteArraySet keySpace) {
			super(ProductSet.getInstance(keySpace, AES_MESSAGE_SPACE), AES_MESSAGE_SPACE);
		}

		@Override
		protected ByteArrayElement abstractApply(Pair element, RandomGenerator randomGenerator) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

//import java.math.BigInteger;
//import java.util.Random;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import javax.xml.bind.DatatypeConverter;
//
//import ch.bfh.unicrypt.crypto.schemes.encryption.abstracts.AbstractEncryptionScheme;
//import ch.bfh.unicrypt.crypto.keygenerator.old.PasswordBasedKeyGenerator;
//import ch.bfh.unicrypt.crypto.utility.AESUtil;
//import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
//import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
//import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
//import ch.bfh.unicrypt.math.function.interfaces.Function;
//
//public class AESEncryptionScheme extends AbstractEncryptionScheme implements DeterministicEncryptionScheme {
//  private int encryptionIterations;
//  private PasswordKeyGenerator keyGenerator;
//  private EncryptionFunction encryptionFunction;
//  private DecryptionFunction decryptionFunction;
//
//  public AESEncryptionScheme() {
//    this(1);
//  }
//
//  public AESEncryptionScheme(int encryptionIterations) {
//    this(encryptionIterations, new PasswordBasedKeyGenerator());
//  }
//
//  public AESEncryptionScheme(int encryptionIterations, ZPlusMod keySpace, int keyIterations) {
//    this(encryptionIterations, new PasswordBasedKeyGenerator(keySpace, keyIterations));
//  }
//
//  public AESEncryptionScheme(int encryptionIterations, PasswordKeyGenerator keyGenerator) {
//    if(encryptionIterations<1)
//      throw new IllegalArgumentException();
//    this.encryptionIterations = encryptionIterations;
//    if(keyGenerator==null)
//      throw new IllegalArgumentException();
//    this.keyGenerator = keyGenerator;
//    this.encryptionFunction = new EncryptionFunction(keyGenerator, encryptionIterations);
//    this.decryptionFunction = new DecryptionFunction(keyGenerator, encryptionIterations);
//
//  }
//
//  public int getEncryptionIterations() {
//    return this.encryptionIterations;
//  }
//
//  public Element encrypt(Element key, Element plaintext) {
//    return this.encryptionFunction.apply(key, plaintext);
//  }
//
//  @Override
//  public Element decrypt(Element key, Element ciphertext) {
//    return this.decryptionFunction.apply(key, ciphertext);
//  }
//
//  @Override
//  public Function getDecryptionFunction() {
//    return this.decryptionFunction;
//  }
//
//  @Override
//  public Function getEncryptionFunction() {
//    return this.encryptionFunction;
//  }
//
//  @Override
//  public Function getIdentityEncryptionFunction() {
//    throw new RuntimeException("This method does not make sense... Redesign");
//  }
//
//  public PasswordKeyGenerator getKeyGenerator() {
//    return this.keyGenerator;
//  }
//
//  public ZPlus getMessageSpace() {
//    return (ZPlus) this.decryptionFunction.getCoDomain();
//  }
//
//  public ZPlus getEncryptionSpace() {
//    return (ZPlus) this.encryptionFunction.getCoDomain();
//  }
//
//  private class EncryptionFunction extends AbstractFunction {
//    private int iterations;
//
//    public EncryptionFunction(PasswordKeyGenerator keyGenerator, int iterationAmount) {
//      super(new ProductGroup(keyGenerator.getKeySpace(), ZPlus.Factory.getInstance()), ZPlus.Factory.getInstance());
//      if (iterationAmount <= 0) {
//        throw new IllegalArgumentException();
//      }
//      this.iterations = iterationAmount;
//    }
//
//    @Override
//    public Element abstractApply(Element element, Random random) {
//      BigInteger plainText = element.getAt(1).getValue();
//      Element keyElement = element.getAt(0);
//      BigInteger key = keyElement.getValue();
//      // HACK: This hack is required in order to re-insert lost 0-bits.
//      int requiredKeySizeInBytes = ((int) Math.ceil((keyElement.getSet().getZPlusModOrder().getModulus().bitLength() + 1) / 8));
//      byte[] keyBytes = key.toByteArray(); //
//      byte[] nKey = new byte[requiredKeySizeInBytes]; //
//      System.arraycopy(keyBytes, 0, nKey, requiredKeySizeInBytes-keyBytes.length, keyBytes.length); //
//      SecretKey secret = new SecretKeySpec(nKey, "AES"); //
//      // HACK: This hack is required in order to re-insert lost 0-bits.
//      byte[] cipherText = null;
//      try {
//        cipherText = AESUtil.encrypt(plainText.toByteArray(), secret, this.iterations);
//      } catch (Exception e) {
//        e.printStackTrace();
//        throw new IllegalArgumentException();
//      }
//    //HACK: The ciphertext is stored as the byte-representation of a Base64-String
//      String base64=DatatypeConverter.printBase64Binary(cipherText);
//      return ZPlus.Factory.getInstance().getElement(new BigInteger(1,base64.getBytes()));
//    //HACK: The ciphertext is stored as the byte-representation of a Base64-String
//    }
//  }
//
//  private class DecryptionFunction extends AbstractFunction {
//    private int iterations;
//
//    public DecryptionFunction(PasswordKeyGenerator keyGenerator, int iterationAmount) {
//      super(new ProductGroup(keyGenerator.getKeySpace(), ZPlus.Factory.getInstance()), ZPlus.Factory.getInstance());
//      if (iterationAmount <= 0) {
//        throw new IllegalArgumentException();
//      }
//      this.iterations = iterationAmount;
//    }
//
//    @Override
//    public Element abstractApply(Element element, Random random) {
//      Element ciphertextElement = element.getAt(1);
//      //HACK: The ciphertext must be unwrapped from the byte-Representation of a Base64-String
//      byte[] cipherText = DatatypeConverter.parseBase64Binary(new String(ciphertextElement.getValue().toByteArray()));
//      //HACK: The ciphertext must be unwrapped from the byte-Representation of a Base64-String
//      Element keyElement = element.getAt(0);
//      BigInteger key = keyElement.getValue();
//      // HACK: This hack is required in order to re-insert lost 0-bits.
//      int requiredKeySizeInBytes = ((int) Math.ceil((keyElement.getSet().getZPlusModOrder().getModulus().bitLength() + 1) / 8));
//      byte[] keyBytes = key.toByteArray(); //
//      byte[] nKey = new byte[requiredKeySizeInBytes]; //
//      System.arraycopy(keyBytes, 0, nKey, requiredKeySizeInBytes-keyBytes.length, keyBytes.length); //
//      // HACK: This hack is required in order to re-insert lost 0-bits.
//      SecretKey secret = new SecretKeySpec(nKey, "AES");
//      byte[] plainText = null;
//      try {
//        plainText = AESUtil.decrypt(cipherText, secret, this.iterations);
//      } catch (Exception e) {
//        e.printStackTrace();
//        throw new IllegalArgumentException();
//      }
//      return ZPlus.Factory.getInstance().getElement(new BigInteger(1, plainText));
//    }
//  }
//

