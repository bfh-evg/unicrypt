package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * This class represents the concept of a hash function, which maps an
 * arbitrarily long input element into an element of a given co-domain. The
 * mapping itself is defined by some cryptographic hash function such as
 * SHA-256. For complex input elements, there are two options: one in which the
 * individual elements are first recursively paired with
 * {@link MathUtil#elegantPair(java.math.BigInteger[])}, and one in which the
 * hashing itself is done recursively. The co-domain is always an instance of
 * {@link ZPlusMod}. Its order corresponds to the size of the cryptographic hash
 * function's output space (a power of 2).
 * <p>
 * @see Element#getHashValue()
 * @see Element#getRecursiveHashValue()
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class HashFunction
       extends AbstractFunction<Set, FiniteByteArraySet, FiniteByteArrayElement> {

  private MessageDigest messageDigest;
  private boolean recursiveHash;

  private HashFunction(Set domain, FiniteByteArraySet coDomain, final MessageDigest messageDigest, boolean recursiveHash) {
    super(domain, coDomain);
    this.messageDigest = messageDigest;
    this.recursiveHash = recursiveHash;
  }

  @Override
  protected boolean standardIsEqual(Function function) {
    return this.isRecursive() == ((HashFunction) function).isRecursive();
  }

  @Override
  protected FiniteByteArrayElement abstractApply(final Element element, final Random random) {
    if (this.recursiveHash) {
      return this.getCoDomain().getElement(element.getRecursiveHashValue(this.messageDigest).getByteArray());
    }
    return this.getCoDomain().getElement(element.getHashValue(this.messageDigest).getByteArray());
  }

  public MessageDigest getMessageDigest() {
    return this.messageDigest;
  }

  public boolean isRecursive() {
    return this.recursiveHash;
  }

  /**
   * This constructor generates a standard SHA-256 hash function. The order of
   * the co-domain is 2^256.
   * <p>
   * @param domain
   * @return
   */
  public static HashFunction getInstance(Set domain) {
    return HashFunction.getInstance(domain, Element.STANDARD_HASH_ALGORITHM, false);
  }

  public static HashFunction getInstance(Set domain, boolean recursiveHash) {
    return HashFunction.getInstance(domain, Element.STANDARD_HASH_ALGORITHM, recursiveHash);
  }

  /**
   * This constructor generates a standard hash function for a given hash
   * algorithm name. The co-domain is chosen accordingly.
   * <p>
   * @param domain
   * @param hashAlgorithm The name of the hash algorithm
   * @return
   * @throws IllegalArgumentException if {@literal algorithmName} is null or an
   *                                  unknown hash algorithm name
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
   * {@literal recursiveHash} defines which of the two hashing methods is used.
   * <p>
   * @param domain        The given domain
   * @param messageDigest The given cryptographic hash function
   * @param recursiveHash The boolean value
   * @return The resulting hash function
   * @throws IllegalArgumentException if {@literal domain} or
   *                                  {@literal messageDigest} is null
   */
  public static HashFunction getInstance(Set domain, final MessageDigest messageDigest, boolean recursiveHash) {
    if (domain == null || messageDigest == null) {
      throw new IllegalArgumentException();
    }
    return new HashFunction(domain, FiniteByteArraySet.getInstance(messageDigest.getDigestLength(), true), messageDigest, recursiveHash);
  }

}
