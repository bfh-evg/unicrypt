package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.general.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * This class represents the concept of a hash function, which maps an
 * arbitrarily long input element into an element of a given co-domain. The
 * mapping itself is defined by some cryptographic hash function such as
 * SHA-256. For complex input elements, there are two options: one in which
 * the individual elements are first recursively paired with
 * {@link MathUtil#elegantPair(java.math.BigInteger[])}, and one in which the
 * hashing itself is done recursively. The co-domain is always an instance of
 * {@link ZPlusMod}. Its order corresponds to the size of the cryptographic
 * hash function's output space (a power of 2).
 *
 * @see Element#getHashValue()
 * @see Element#getRecursiveHashValue()
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class HashFunction extends AbstractFunction<Set, ZPlusMod, AdditiveElement> {

  private MessageDigest messageDigest;
  private boolean recursiveHash;

  private HashFunction(Set domain, ZPlusMod coDomain, final MessageDigest messageDigest, boolean recursiveHash) {
    super(domain, coDomain);
    this.messageDigest = messageDigest;
    this.recursiveHash = recursiveHash;
  }

  @Override
  protected boolean standardEquals(Function function) {
    return this.isRecursive() == ((HashFunction) function).isRecursive();
  }

  @Override
  protected int standardHashCode() {
    return (this.isRecursive()) ? 0 : 1;
  }

  @Override
  protected AdditiveElement abstractApply(final Element element, final Random random) {
    BigInteger value;
    if (this.recursiveHash) {
      value = new BigInteger(element.getRecursiveHashValue(this.messageDigest));
    } else {
      value = new BigInteger(element.getHashValue(this.messageDigest));
    }
    return this.getCoDomain().getElement(value);
  }

  public MessageDigest getMessageDigest() {
    return this.messageDigest;
  }

  public boolean isRecursive() {
    return this.recursiveHash;
  }

  /**
   * This constructor generates a standard SHA-256 hash function. The order of the co-domain is 2^256.
   */
  public static HashFunction getInstance(Set domain) {
    return HashFunction.getInstance(domain, Element.STANDARD_HASH_ALGORITHM, false);
  }

  public static HashFunction getInstance(Set domain, boolean recursiveHash) {
    return HashFunction.getInstance(domain, Element.STANDARD_HASH_ALGORITHM, recursiveHash);
  }

  /**
   * This constructor generates a standard hash function for a given hash algorithm name. The co-domain
   * is chosen accordingly.
   * @param hashAlgorithm The name of the hash algorithm
   * @throws IllegalArgumentException if {@code algorithmName} is null or an unknown hash algorithm name
   */
  public static HashFunction getInstance(Set domain, final String hashAlgorithm) {
    return HashFunction.getInstance(domain, hashAlgorithm, false);
  }

  public static HashFunction getInstance(Set domain, final String hashAlgorithm, boolean recursiveHash) {
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

  public static HashFunction getInstance(Set domain, final MessageDigest messageDigest) {
    return HashFunction.getInstance(domain, messageDigest, false);
  }

  /**
   * This is the general factory method of this class. If generates a new hash
   * function for a given domain. The cryptographic hash function is defined by
   * a given instance of type {@link MessageDigest}. A boolean value
   * {@code recursiveHash} defines which of the two hashing methods is used.
   * @param domain The given domain
   * @param messageDigest The given cryptographic hash function
   * @param recursiveHash The boolean value
   * @return The resulting hash function
   * @throws IllegalArgumentException if {@code domain} or {@code messageDigest} is null
   */
  public static HashFunction getInstance(Set domain, final MessageDigest messageDigest, boolean recursiveHash) {
    if (domain == null || messageDigest == null) {
      throw new IllegalArgumentException();
    }
    BigInteger modulus = BigInteger.valueOf(2).pow(8*messageDigest.getDigestLength());
    return new HashFunction(domain, ZPlusMod.getInstance(modulus), messageDigest, recursiveHash);
  }

}
