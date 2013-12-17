/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.sharing.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.schemes.sharing.abstracts.AbstractSecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class SimpleSecretSharingScheme
			 extends AbstractSecretSharingScheme<Group, Element, Group, Element> {

	private final Group group;

	protected SimpleSecretSharingScheme(Group group, int size) {
		super(size);
		this.group = group;
	}

	@Override
	protected Group abstractGetMessageSpace() {
		return this.group;
	}

	@Override
	protected Group abstractGetShareSpace() {
		return this.group;
	}

	@Override
	protected Element[] abstractShare(Element message, RandomGenerator randomGenerator) {
		Element[] shares = new Element[this.getSize()];
		Element total = this.group.getIdentityElement();
		for (int i = 1; i < shares.length; i++) {
			shares[i] = this.group.getRandomElement(randomGenerator);
			total = total.apply(shares[i]);
		}
		shares[0] = message.applyInverse(total);
		return shares;
	}

	@Override
	protected Element abstractRecover(Element[] shares) {
		return this.group.apply(shares);
	}

	public static SimpleSecretSharingScheme getInstance(Group group, int size) {
		if (group == null || size < 1) {
			throw new IllegalArgumentException();
		}
		return new SimpleSecretSharingScheme(group, size);
	}

}
