package ch.bfh.unicrypt.crypto.keygen.classes;

import java.math.BigInteger;
import java.security.Key;
import java.util.Random;

import ch.bfh.unicrypt.crypto.keygen.abstracts.KeyGeneratorAbstract;
import ch.bfh.unicrypt.crypto.keygen.interfaces.PasswordDerivedKeyGenerator;
import ch.bfh.unicrypt.crypto.utility.AESUtil;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlus;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;

/**
 * This class derives a cryptographic (secret) key for AES from a T
 * (password,salt). This key then can be used for AES encrypt / decrypt.
 * 
 * @author reto
 * 
 */
public class PasswordDerivedKeyGeneratorClass extends KeyGeneratorAbstract implements PasswordDerivedKeyGenerator {

  private final KeyGenerationFunction keyGenerationFunction;

  /**
   * Creates a default {@link PasswordDerivedKeyGenerator} where cryptographic
   * key-size = 256bit iterations during key-generation = 1024
   */
  public PasswordDerivedKeyGeneratorClass() {
    this(ZPlusMod.Factory.getInstance(BigInteger.valueOf(2).pow(256)), 1024);
  }

  /**
   * Creates a {@link PasswordDerivedKeyGenerator} with custom values. Caution:
   * In order to get a keySpace of bit-size 256 you have to create the following
   * key-space: <code>new ZPlusModClass(BigInteger.valueOf(2).pow(256))</code>
   * in contrast, <code>new ZPlusModClass(BigInteger.valueOf(2).pow(256))</code>
   * will result in a keySpace of 8bit only.
   * 
   * @param keySpace
   *          defines the cryptographic key-size
   * @param iterationAmount
   */
  public PasswordDerivedKeyGeneratorClass(ZPlusMod keySpace, int iterationAmount) {
    super(keySpace);
    this.keyGenerationFunction = new KeyGenerationFunction(iterationAmount);
  }

  @Override
  public Function getKeyGenerationFunction() {
    return this.keyGenerationFunction;
  }

  @Override
  public Element generateKey(String password, byte[] salt) {
    ExternalDataMapper mapper = new ExternalDataMapperClass();
    Element passwordElement = mapper.getEncodedElement(password);
    Element saltElement = mapper.getEncodedElement(salt);

    Element element = ((ProductGroup) this.keyGenerationFunction.getDomain()).getElement(passwordElement, saltElement);
    return this.keyGenerationFunction.apply(element);
  }

  private class KeyGenerationFunction extends AbstractFunction {
    private final int iterationAmount;

    public KeyGenerationFunction(int iterationAmount) {
      super(new ProductGroup(ZPlus.Factory.getInstance(), ZPlus.Factory.getInstance()), PasswordDerivedKeyGeneratorClass.this.getKeySpace());
      if (iterationAmount <= 0) {
        throw new IllegalArgumentException();
      }
      this.iterationAmount = iterationAmount;
    }

    @Override
    public Element apply(Element element, Random random) {
      if (!this.getDomain().contains(element))
        throw new IllegalArgumentException();
      char[] password = new String(element.getAt(0).getValue().toByteArray()).toCharArray();
      byte[] salt = element.getAt(1).getValue().toByteArray();
      Key key = null;
      try {
        int keySizeInBits = ((int) Math.ceil((this.getCoDomain().getZPlusModOrder().getModulus().bitLength() + 1) / 8)) * 8;       
        key = AESUtil.generateKey(password, salt, this.iterationAmount, keySizeInBits);
      } catch (Exception e) {
        throw new IllegalArgumentException();
      }

      //This is yet another hack, as the generated key must fit within a ZPlusMod
      if (key.getEncoded()[0] < 0)
      {
        return ((ZPlusModClass) getCoDomain()).getElement(new BigInteger(-1, key.getEncoded()));
      }
      if (key.getEncoded()[0] > 0){
        return ((ZPlusModClass) getCoDomain()).getElement(new BigInteger(1, key.getEncoded()));
      }
      return ((ZPlusModClass) getCoDomain()).getElement(new BigInteger(key.getEncoded()));
    }

  }

  // /**
  // * The following method allows to derive a cryptographic (secret)key using a
  // human created password.
  // *
  // * @param password The human created password
  // * @param salt A salt created by some random-oracle
  // * @param iterationCount (1024 is a reasonable value)
  // * @param keyLength The length of the resulting cryptographic (secret)key
  // (256 is a reasonable value)
  // * @return the cryptographic (secret)key
  // * @throws Exception
  // */
  // protected static Key generateKey(char[] password,byte[] salt, int
  // iterationCount,int keyLength) throws Exception {
  // SecretKeyFactory factory = SecretKeyFactory
  // .getInstance("PBKDF2WithHmacSHA1");
  // //KeySpec spec = new PBEKeySpec(password, salt, 1024, 256);
  // KeySpec spec = new PBEKeySpec(password, salt, iterationCount, keyLength);
  // SecretKey tmp = factory.generateSecret(spec);
  // SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
  // return secret;
  // }

}
