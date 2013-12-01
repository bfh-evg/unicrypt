/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteStringElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteStringSet;
import ch.bfh.unicrypt.math.function.classes.ConvertFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class FiniteStringToFiniteByteArrayEncoder
			 extends AbstractEncoder<FiniteStringSet, FiniteStringElement, FiniteByteArraySet, FiniteByteArrayElement> {

	private final FiniteStringSet finiteStringSet;
	private final FiniteByteArraySet finiteByteArraySet;

	protected FiniteStringToFiniteByteArrayEncoder(FiniteStringSet finiteStringSet, FiniteByteArraySet finiteByteArraySet) {
		this.finiteStringSet = finiteStringSet;
		this.finiteByteArraySet = finiteByteArraySet;
	}

	public FiniteStringSet getFiniteStringSet() {
		return this.finiteStringSet;
	}

	public FiniteByteArraySet getFiniteByteArraySet() {
		return this.finiteByteArraySet;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return ConvertFunction.getInstance(this.getFiniteStringSet(), this.getFiniteByteArraySet());
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return ConvertFunction.getInstance(this.getFiniteByteArraySet(), this.getFiniteStringSet());
	}

	public static FiniteStringToFiniteByteArrayEncoder getInstance(FiniteStringSet finiteStringSet) {
		return FiniteStringToFiniteByteArrayEncoder.getInstance(finiteStringSet, false);
	}

	public static FiniteStringToFiniteByteArrayEncoder getInstance(FiniteStringSet finiteStringSet, boolean equalLength) {
		if (finiteStringSet == null) {
			throw new IllegalArgumentException();
		}
		BigInteger minOrder = finiteStringSet.getOrder();
		return new FiniteStringToFiniteByteArrayEncoder(finiteStringSet, FiniteByteArraySet.getInstance(minOrder, equalLength));
	}

	public static FiniteStringToFiniteByteArrayEncoder getInstance(FiniteByteArraySet finiteByteArraySet, Alphabet alphabet) {
		return FiniteStringToFiniteByteArrayEncoder.getInstance(finiteByteArraySet, alphabet, false);
	}

	public static FiniteStringToFiniteByteArrayEncoder getInstance(FiniteByteArraySet finiteByteArraySet, Alphabet alphabet, boolean equalLength) {
		if (finiteByteArraySet == null || alphabet == null) {
			throw new IllegalArgumentException();
		}
		BigInteger minOrder = finiteByteArraySet.getOrder();
		return new FiniteStringToFiniteByteArrayEncoder(FiniteStringSet.getInstance(alphabet, minOrder, equalLength), finiteByteArraySet);
	}

}
