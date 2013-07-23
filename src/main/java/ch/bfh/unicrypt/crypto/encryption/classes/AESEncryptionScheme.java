package ch.bfh.unicrypt.crypto.encryption.classes;

import java.math.BigInteger;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import ch.bfh.unicrypt.crypto.encryption.abstracts.AbstractEncryptionScheme;
import ch.bfh.unicrypt.crypto.encryption.interfaces.DeterministicEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.classes.PasswordDerivedKeyGeneratorClass;
import ch.bfh.unicrypt.crypto.keygen.interfaces.PasswordDerivedKeyGenerator;
import ch.bfh.unicrypt.crypto.utility.AESUtil;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlus;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;

public class AESEncryptionScheme extends AbstractEncryptionScheme implements DeterministicEncryptionScheme {
  private int encryptionIterations;
  private PasswordDerivedKeyGenerator keyGenerator;
  private EncryptionFunction encryptionFunction;
  private DecryptionFunction decryptionFunction;

  public AESEncryptionScheme() {
    this(1);
  }

  public AESEncryptionScheme(int encryptionIterations) {
    this(encryptionIterations, new PasswordDerivedKeyGeneratorClass());
  }

  public AESEncryptionScheme(int encryptionIterations, ZPlusMod keySpace, int keyIterations) {
    this(encryptionIterations, new PasswordDerivedKeyGeneratorClass(keySpace, keyIterations));
  }

  public AESEncryptionScheme(int encryptionIterations, PasswordDerivedKeyGenerator keyGenerator) {
    if(encryptionIterations<1)
      throw new IllegalArgumentException();
    this.encryptionIterations = encryptionIterations;
    if(keyGenerator==null)
      throw new IllegalArgumentException();
    this.keyGenerator = keyGenerator;
    this.encryptionFunction = new EncryptionFunction(keyGenerator, encryptionIterations);
    this.decryptionFunction = new DecryptionFunction(keyGenerator, encryptionIterations);

  }

  public int getEncryptionIterations() {
    return this.encryptionIterations;
  }

  public Element encrypt(Element key, Element plaintext) {
    return this.encryptionFunction.apply(key, plaintext);
  }

  @Override
  public Element decrypt(Element key, Element ciphertext) {
    return this.decryptionFunction.apply(key, ciphertext);
  }

  @Override
  public Function getDecryptionFunction() {
    return this.decryptionFunction;
  }

  @Override
  public Function getEncryptionFunction() {
    return this.encryptionFunction;
  }

  @Override
  public Function getIdentityEncryptionFunction() {
    throw new RuntimeException("This method does not make sense... Redesign");
  }

  public PasswordDerivedKeyGenerator getKeyGenerator() {
    return this.keyGenerator;
  }

  public ZPlus getPlaintextSpace() {
    return (ZPlus) this.decryptionFunction.getCoDomain();
  }

  public ZPlus getCiphertextSpace() {
    return (ZPlus) this.encryptionFunction.getCoDomain();
  }

  private class EncryptionFunction extends AbstractFunction {
    private int iterations;

    public EncryptionFunction(PasswordDerivedKeyGenerator keyGenerator, int iterationAmount) {
      super(new ProductGroup(keyGenerator.getKeySpace(), ZPlus.Factory.getInstance()), ZPlus.Factory.getInstance());
      if (iterationAmount <= 0) {
        throw new IllegalArgumentException();
      }
      this.iterations = iterationAmount;
    }

    @Override
    public Element abstractApply(Element element, Random random) {
      BigInteger plainText = element.getElementAt(1).getValue();
      Element keyElement = element.getElementAt(0);     
      BigInteger key = keyElement.getValue();
      // HACK: This hack is required in order to re-insert lost 0-bits.
      int requiredKeySizeInBytes = ((int) Math.ceil((keyElement.getGroup().getOrderGroup().getModulus().bitLength() + 1) / 8));       
      byte[] keyBytes = key.toByteArray(); //
      byte[] nKey = new byte[requiredKeySizeInBytes]; //
      System.arraycopy(keyBytes, 0, nKey, requiredKeySizeInBytes-keyBytes.length, keyBytes.length); //
      SecretKey secret = new SecretKeySpec(nKey, "AES"); //
      // HACK: This hack is required in order to re-insert lost 0-bits.
      byte[] cipherText = null;
      try {
        cipherText = AESUtil.encrypt(plainText.toByteArray(), secret, this.iterations);                   
      } catch (Exception e) {
        e.printStackTrace();
        throw new IllegalArgumentException();
      }
    //HACK: The ciphertext is stored as the byte-representation of a Base64-String
      String base64=DatatypeConverter.printBase64Binary(cipherText);
      return ZPlus.Factory.getInstance().getElement(new BigInteger(1,base64.getBytes()));
    //HACK: The ciphertext is stored as the byte-representation of a Base64-String
    }
  }

  private class DecryptionFunction extends AbstractFunction {
    private int iterations;

    public DecryptionFunction(PasswordDerivedKeyGenerator keyGenerator, int iterationAmount) {
      super(new ProductGroup(keyGenerator.getKeySpace(), ZPlus.Factory.getInstance()), ZPlus.Factory.getInstance());
      if (iterationAmount <= 0) {
        throw new IllegalArgumentException();
      }
      this.iterations = iterationAmount;
    }

    @Override
    public Element abstractApply(Element element, Random random) {
      Element ciphertextElement = element.getElementAt(1);
      //HACK: The ciphertext must be unwrapped from the byte-Representation of a Base64-String
      byte[] cipherText = DatatypeConverter.parseBase64Binary(new String(ciphertextElement.getValue().toByteArray()));      
      //HACK: The ciphertext must be unwrapped from the byte-Representation of a Base64-String      
      Element keyElement = element.getElementAt(0);     
      BigInteger key = keyElement.getValue();
      // HACK: This hack is required in order to re-insert lost 0-bits.
      int requiredKeySizeInBytes = ((int) Math.ceil((keyElement.getGroup().getOrderGroup().getModulus().bitLength() + 1) / 8));       
      byte[] keyBytes = key.toByteArray(); //
      byte[] nKey = new byte[requiredKeySizeInBytes]; //
      System.arraycopy(keyBytes, 0, nKey, requiredKeySizeInBytes-keyBytes.length, keyBytes.length); //
      // HACK: This hack is required in order to re-insert lost 0-bits.
      SecretKey secret = new SecretKeySpec(nKey, "AES");
      byte[] plainText = null;
      try {        
        plainText = AESUtil.decrypt(cipherText, secret, this.iterations);
      } catch (Exception e) {
        e.printStackTrace();        
        throw new IllegalArgumentException();
      }
      return ZPlus.Factory.getInstance().getElement(new BigInteger(1, plainText));
    }
  }

}
