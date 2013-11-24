/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.sharing.classes;

import ch.bfh.unicrypt.crypto.schemes.sharing.abstracts.AbstractThresholdSecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductCyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.util.Random;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ShamirSecretSharingScheme
			 extends AbstractThresholdSecretSharingScheme<ZModPrime, ZModElement, ProductCyclicGroup, Tuple> {

	private final ZModPrime zModPrime;

	protected ShamirSecretSharingScheme(ZModPrime zModPrime, int size, int threshold) {
		super(size, threshold);
		this.zModPrime = zModPrime;
	}

	@Override
	protected ZModPrime abstractGetMessageSpace() {
		return this.zModPrime;
	}

	@Override
	protected ProductCyclicGroup abstractGetShareSpace() {
		return ProductCyclicGroup.getInstance(this.zModPrime, ZMod.getInstance(this.getSize()));
	}

	@Override
	protected Tuple[] abstractShare(Element message, Random random) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected ZModElement abstractRecover(Element[] shares) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public static ShamirSecretSharingScheme getInstance(ZModPrime zModPrime, int size, int threshold) {
		if (zModPrime == null || size < 1 || threshold < 1 || threshold > size) {
			throw new IllegalArgumentException();
		}
		return new ShamirSecretSharingScheme(zModPrime, size, threshold);
	}

}
