/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;

/**
 *
 * @author rolfhaenni
 */
public class AESKeyGenerator
			 extends AbstractKeyGenerator<FiniteByteArraySet, FiniteByteArrayElement> {

	protected AESKeyGenerator() {
		super(FiniteByteArraySet.getInstance(16, true));
	}

	public static AESKeyGenerator getInstance() {
		return new AESKeyGenerator();
	}

}
