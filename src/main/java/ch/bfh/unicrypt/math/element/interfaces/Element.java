package ch.bfh.unicrypt.math.element.interfaces;

import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.monoid.classes.ProductMonoid;
import ch.bfh.unicrypt.math.semigroup.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.set.classes.ProductSet;
import ch.bfh.unicrypt.math.cyclicgroup.interfaces.AdditiveCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.monoid.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.semigroup.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.cyclicgroup.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.monoid.interfaces.Monoid;
import ch.bfh.unicrypt.math.cyclicgroup.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.monoid.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.math.semigroup.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.math.semigroup.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.set.interfaces.Set;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * This abstract class represents the concept an element in a mathematical
 * group. It allows applying the group operation and other methods from a
 * {@link Group} in a convenient way. Most methods provided by {@link Element}
 * have an equivalent method in {@link Group}.
 *
 * @see Group
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Element {

  public static final String STANDARD_HASH_ALGORITHM = "SHA-256";

  /**
   *
   * @return
   */
  public Set getSet();

  /**
   *
   * @return
   */
  public SemiGroup getSemiGroup();

  /**
   *
   * @return
   */
  public Monoid getMonoid();

  /**
   *
   * @return
   */
  public Group getGroup();

  /**
   *
   * @return
   */
  public CyclicGroup getCyclicGroup();

  /**
   * Returns the positive BigInteger value that corresponds the element.
   *
   * @return The corresponding BigInteger value
   */
  public BigInteger getValue();

  public byte[] getHashValue();

  public byte[] getHashValue(String hashAlgorithm);

  public byte[] getHashValue(MessageDigest messageDigest);

  public byte[] getRecursiveHashValue();

  public byte[] getRecursiveHashValue(String hashAlgorithm);

  public byte[] getRecursiveHashValue(MessageDigest messageDigest);

  //
  // The following methods are equivalent to corresponding Set methods
  //

  /**
   * @see Group#apply(Element, Element)
   */
  public Element apply(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public Element applyInverse(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public Element selfApply(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public Element selfApply(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public Element selfApply(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public Element selfApply();

  /**
   * @see Group#invert(Element)
   */
  public Element invert();

  /**
   * @see Group#isIdentityElement(Element)
   */
  public boolean isIdentity();

  /**
   * @see CyclicGroup#isGenerator(Element)
   */
  public boolean isGenerator();

  //
  // The standard implementations of the following three methods are
  // insufficient for elements.
  //

  @Override
  public boolean equals(Object object);

  @Override
  public int hashCode();

  @Override
  public String toString();

}