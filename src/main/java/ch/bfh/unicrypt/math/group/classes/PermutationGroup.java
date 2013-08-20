package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Permutation;
import ch.bfh.unicrypt.math.utility.MathUtil;

/**
 * An instance of this class represents the group of permutations for a given size. The elements of the group
 * are permutations, which contain the values from 0 to size-1 in a permuted order. Applying
 * the group operation to two permutation elements means to construct the combined permutation element.
 * Note that this operation is not commutative. The identity element is the permutation
 * [0, ..., size-1]. To invert an element, the inverse permutation is computed. Permutation elements
 * are considered to be atomic. This means that they can be converted into a unique integer value and
 * back. The group order is the factorial of its size.
 *
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PermutationGroup extends AbstractGroup {

  private static final long serialVersionUID = 1L;

  private final int size;

  /**
   * Returns a new instance of this class for a given {@code size >= 0}.
   * @param size The size
   * @throws IllegalArgumentException if {@code size} is negative
   */
  private PermutationGroup(final int size) {
    this.size = size;
  }

  /**
   * Returns the size of the permutation elements in this group. The smallest possible size is 0,
   * which represents the trivial case of an empty permutation.
   * @return The permutation size
   */
  public int getSize() {
    return this.size;
  }

  public Permutation getPermutation(Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    return ((PermutationElement) element).getPermutation();
  }

  /**
   * Creates and returns a group element for the given permutation (if one exists).
   * @param permutation The given permutation
   * @return The corresponding group element
   * @throws IllegalArgumentException if {@code permutation} is null or if it is not a proper permutation for the group's permutation size
   */
  public Element getElement(final Permutation permutation) {
    if (permutation == null || permutation.getSize() != this.getSize()) {
      throw new IllegalArgumentException();
    }
    return this.abstractGetElement(permutation);
  }

  protected Element abstractGetElement(Permutation permutation) {
    return new PermutationElement(this, permutation);
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //

  @Override
  public int standardHashCode() {
    return this.size;
  }

  @Override
  public String standardToString() {
    return "" + this.getSize();
  }

  //
  // The following protected methods implement the abstract methods from
  // various super-classes
  //

  @Override
  public boolean abstractEquals(final Set set) {
    final PermutationGroup other = (PermutationGroup) set;
    return this.getSize() == other.getSize();
  }

  @Override
  protected Element abstractGetRandomElement(final Random random) {
    return this.abstractGetElement(new Permutation(this.getSize(), random));
  }

  @Override
  protected boolean abstractContains(final BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getSize());
    return Permutation.isPermutationVector(MathUtil.bigIntegerToIntArray(values));
  }

  @Override
  protected Element abstractApply(final Element element1, final Element element2) {
    return this.abstractGetElement(this.getPermutation(element1).compose(this.getPermutation(element2)));
  }

  @Override
  protected Element abstractInvert(final Element element) {
    return this.abstractGetElement(this.getPermutation(element).invert());
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return MathUtil.factorial(this.getSize());
  }

  @Override
  protected Element abstractGetIdentityElement() {
    return this.abstractGetElement(new Permutation(this.getSize()));
  }

  @Override
  protected Element abstractGetElement(final BigInteger value) {
    BigInteger[] values = MathUtil.elegantUnpair(value, this.getSize());
    return abstractGetElement(new Permutation(MathUtil.bigIntegerToIntArray(values)));
  }

  // LOCAL CLASS: PERMUTATION_ELEMENT

  final private class PermutationElement extends Element {

    private static final long serialVersionUID = 1L;

    // Since a permutation element is atomic, storing both the permutation and the corresponding BigInteger value
    // is redundant, but we we keep it for convenience
    private final Permutation permutation;

    private PermutationElement(final Set set, final Permutation permutationVector) {
      super(set);
      this.permutation = permutationVector;
    }

    /**
     * Returns the corresponding permutation
     * @return The permutation
     */
    public Permutation getPermutation() {
      return this.permutation;
    }

    @Override
    protected BigInteger standardGetValue() {
      return MathUtil.elegantPair(MathUtil.intToBigIntegerArray(this.getPermutation().getPermutationVector()));
    }

    @Override
    protected boolean standardEquals(Element element) {
      return this.getPermutation().equals(((PermutationElement) element).getPermutation());
    }

    @Override
    protected int standardHashCode() {
      return this.getPermutation().hashCode();
    }

    @Override
    public String standardToString() {
      return this.getPermutation().toString();
    }

  }

  //
  // STATIC FACTORY METHODS
  //

  private static final Map<Integer,PermutationGroup> instances = new HashMap<Integer,PermutationGroup>();

  /**
   * Returns a the unique instance of this class for a given non-negative permutation size.
   * @param size The size of the permutation
   * @throws IllegalArgumentException if {@code modulus} is null, zero, or negative
   */
  public static PermutationGroup getInstance(final int size) {
    if (size < 0) {
      throw new IllegalArgumentException();
    }
    PermutationGroup instance = PermutationGroup.instances.get(Integer.valueOf(size));
    if (instance == null) {
      instance = new PermutationGroup(size);
      PermutationGroup.instances.put(Integer.valueOf(size),instance);
    }
    return instance;
  }

}
