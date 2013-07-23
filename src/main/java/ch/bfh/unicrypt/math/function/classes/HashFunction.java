package ch.bfh.unicrypt.math.function.classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.Group;

/**
 * This class represents the concept of a hash function, which maps an arbitrarily long input
 * elements of a given group into an output element of a fixed length (an element of type 
 * {@link ZPlusMod}).  
 * 
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class HashFunction extends AbstractFunction {

  private MessageDigest messageDigest;
  private boolean recursiveHash;

  private HashFunction(Group domain, Group coDomain, final MessageDigest messageDigest, boolean recursiveHash) {
    super(domain, coDomain);
    this.messageDigest = messageDigest;
    this.recursiveHash = recursiveHash;
  }
  
  public MessageDigest getMessageDigest() {
    return this.messageDigest;
  }
    
  public boolean isRecursive() {
    return this.recursiveHash;
  }

  //
  // The following protected method implements the abstract method from {@code AbstractFunction}
  //

  @Override
  protected Element abstractApply(final Element element, final Random random) {
    BigInteger value;
    if (this.recursiveHash) {
      value = new BigInteger(element.recursiveHashValue(this.messageDigest));
    } else {
      value = new BigInteger(element.hashValue(this.messageDigest));      
    }
    return this.getCoDomain().getElement(value);
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This constructor generates a standard SHA-256 hash function. The order of the co-domain is 2^256.
   */
  public static HashFunction getInstance(Group domain) {
    return HashFunction.getInstance(domain, false);
  }

  public static HashFunction getInstance(Group domain, boolean recursiveHash) {
    return HashFunction.getInstance(domain, Element.STANDARD_HASH_ALGORITHM, recursiveHash);
  }

  /**
   * This constructor generates a standard hash function for a given hash algorithm name. The co-domain 
   * is chosen accordingly.
   * @param hashAlgorithm The name of the hash algorithm
   * @throws IllegalArgumentException if {@code algorithmName} is null or an unknown hash algorithm name
   */
  public static HashFunction getInstance(Group domain, final String hashAlgorithm) {
    return HashFunction.getInstance(domain, hashAlgorithm, false);
  }

  public static HashFunction getInstance(Group domain, final String hashAlgorithm, boolean recursiveHash) {
    if (hashAlgorithm == null) {
      throw new IllegalArgumentException();
    }
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance(hashAlgorithm);
    } catch (final NoSuchAlgorithmException e) {
      throw new IllegalArgumentException();
    }
    return HashFunction.getInstance(domain, messageDigest, recursiveHash);
  }
  
  public static HashFunction getInstance(Group domain, final MessageDigest messageDigest) {
    return HashFunction.getInstance(domain, messageDigest, false);
  }
  
  public static HashFunction getInstance(Group domain, final MessageDigest messageDigest, boolean recursiveHash) {
    if (domain == null || messageDigest == null) {
      throw new IllegalArgumentException();
    }
    BigInteger modulus = BigInteger.valueOf(2).pow(8*messageDigest.getDigestLength());
    return new HashFunction(domain, ZPlusMod.getInstance(modulus), messageDigest, recursiveHash);
  }
  
}
