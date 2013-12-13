/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.helper.Permutation;
import ch.bfh.unicrypt.math.utility.ArrayUtil;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class PermutationElement
			 extends AbstractElement<PermutationGroup, PermutationElement> {

	private final Permutation permutation;

	protected PermutationElement(final PermutationGroup group, final Permutation permutationVector) {
		super(group);
		this.permutation = permutationVector;
	}

	public Permutation getPermutation() {
		return this.permutation;
	}

	@Override
	protected BigInteger standardGetValue() {
		return MathUtil.pair(ArrayUtil.intToBigIntegerArray(this.getPermutation().getPermutationVector()));
	}

	@Override
	protected boolean standardIsEqual(PermutationElement element) {
		return this.getPermutation().equals(element.getPermutation());
	}

	@Override
	public String standardToStringContent() {
		return this.getPermutation().toString();
	}

}
