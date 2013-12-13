/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FixedByteArraySet;

/**
 *
 * @author rolfhaenni
 */
public class FixedByteArrayKeyGenerator
			 extends AbstractKeyGenerator<FixedByteArraySet, FiniteByteArrayElement> {

	protected FixedByteArrayKeyGenerator(FixedByteArraySet fixedByteArraySet) {
		super(fixedByteArraySet);
	}

	public static FixedByteArrayKeyGenerator getInstance(int keyLength) {
		if (keyLength < 0) {
			throw new IllegalArgumentException();
		}
		return new FixedByteArrayKeyGenerator(FixedByteArraySet.getInstance(keyLength));
	}

	public static FixedByteArrayKeyGenerator getInstance(FixedByteArraySet fixedByteArraySet) {
		if (fixedByteArraySet == null) {
			throw new IllegalArgumentException();
		}
		return new FixedByteArrayKeyGenerator(fixedByteArraySet);
	}

}
