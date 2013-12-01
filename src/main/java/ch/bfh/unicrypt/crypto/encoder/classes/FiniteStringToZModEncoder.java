/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteStringElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteStringSet;
import ch.bfh.unicrypt.math.function.classes.ConvertFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class FiniteStringToZModEncoder
			 extends AbstractEncoder<FiniteStringSet, FiniteStringElement, ZMod, ZModElement> {

	private final FiniteStringSet finiteStringSet;
	private final ZMod zMod;

	protected FiniteStringToZModEncoder(FiniteStringSet stringMonoid, ZMod zMod) {
		this.finiteStringSet = stringMonoid;
		this.zMod = zMod;
	}

	public FiniteStringSet getFiniteStringSet() {
		return this.finiteStringSet;
	}

	public ZMod getZMod() {
		return this.zMod;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return ConvertFunction.getInstance(this.getFiniteStringSet(), this.getZMod());
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return ConvertFunction.getInstance(this.getZMod(), this.getFiniteStringSet());
	}

	public static FiniteStringToZModEncoder getInstance(FiniteStringSet finiteStringSet) {
		if (finiteStringSet == null) {
			throw new IllegalArgumentException();
		}
		return new FiniteStringToZModEncoder(finiteStringSet, ZMod.getInstance(finiteStringSet.getOrder()));
	}

	public static FiniteStringToZModEncoder getInstance(ZMod zMod, Alphabet alphabet) {
		return FiniteStringToZModEncoder.getInstance(zMod, alphabet, false);
	}

	public static FiniteStringToZModEncoder getInstance(ZMod zMod, Alphabet alphabet, boolean equalLength) {
		if (zMod == null || alphabet == null) {
			throw new IllegalArgumentException();
		}
		return new FiniteStringToZModEncoder(FiniteStringSet.getInstance(alphabet, zMod.getOrder(), equalLength), zMod);
	}

}
