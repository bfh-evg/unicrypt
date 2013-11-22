/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.sharing.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <S>
 */
public abstract class AbstractShamirSecretSharingScheme<S extends SemiRing>
			 extends AbstractThresholdSecretSharingScheme<S, DualisticElement, S, DualisticElement> {

	private final S semiRing;

	protected AbstractShamirSecretSharingScheme(S semiRing, int size, int threshold) {
		super(size, threshold);
		this.semiRing = semiRing;
	}

}
