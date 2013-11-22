/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.sharing.classes;

import ch.bfh.unicrypt.crypto.schemes.sharing.abstracts.AbstractShamirSecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ShamirSecretSharingScheme
			 extends AbstractShamirSecretSharingScheme<ZModPrime> {

	protected ShamirSecretSharingScheme(ZModPrime zModPrime, int size, int threshold) {
		super(zModPrime, size, threshold);
	}

	public static ShamirSecretSharingScheme getInstance(ZModPrime zModPrime, int size, int threshold) {
		if (zModPrime == null || size < 1 || threshold < 1 || threshold > size) {
			throw new IllegalArgumentException();
		}
		return new ShamirSecretSharingScheme(zModPrime, size, threshold);
	}

}
