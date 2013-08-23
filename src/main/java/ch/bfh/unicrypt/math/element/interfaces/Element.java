package ch.bfh.unicrypt.math.element.interfaces;

import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ProductMonoid;
import ch.bfh.unicrypt.math.group.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;
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
   *
   * @return
   */
  public AdditiveSemiGroup getAdditiveSemiGroup();

  /**
   *
   * @return
   */
  public AdditiveMonoid getAdditiveMonoid();

  /**
   *
   * @return
   */
  public AdditiveGroup getAdditiveGroup();

  /**
   *
   * @return
   */
  public AdditiveCyclicGroup getAdditiveCyclicGroup();

  /**
   *
   * @return
   */
  public MultiplicativeSemiGroup getMultiplicativeSemiGroup();

  /**
   *
   * @return
   */
  public MultiplicativeMonoid getMultiplicativeMonoid();

  /**
   *
   * @return
   */
  public MultiplicativeGroup getMultiplicativeGroup();

  /**
   *
   * @return
   */
  public MultiplicativeCyclicGroup getMultiplicativeCyclicGroup();

  /**
   *
   * @return
   */
  public ProductSet getProductSet();

  /**
   *
   * @return
   */
  public ProductSemiGroup getProductSemiGroup();

  /**
   *
   * @return
   */
  public ProductMonoid getProductMonoid();

  /**
   *
   * @return
   */
  public ProductGroup getProductGroup();

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
   * @see Group#apply(Element, Element)
   */
  public Element add(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public Element subtract(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public Element times(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public Element times(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public Element times(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public Element timesTwo();

  /**
   * @see Group#apply(Element, Element)
   */
  public Element multiply(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public Element divide(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public Element power(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public Element power(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public Element power(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public Element square();

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