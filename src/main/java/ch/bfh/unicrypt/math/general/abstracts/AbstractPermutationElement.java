/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.general.abstracts;

import ch.bfh.unicrypt.math.general.interfaces.Element;
import ch.bfh.unicrypt.math.general.interfaces.PermutationElement;
import ch.bfh.unicrypt.math.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.utility.MathUtil;
import ch.bfh.unicrypt.math.utility.Permutation;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractPermutationElement extends AbstractElement<PermutationElement> implements PermutationElement {

  private final Permutation permutation;

  protected AbstractPermutationElement(final PermutationGroup group, final Permutation permutationVector) {
    super(group);
    this.permutation = permutationVector;
  }

  @Override
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
