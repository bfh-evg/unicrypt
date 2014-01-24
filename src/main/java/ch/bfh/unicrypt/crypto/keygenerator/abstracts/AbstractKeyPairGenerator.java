/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.crypto.keygenerator.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomNumberGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.RandomFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.UniCrypt;

public abstract class AbstractKeyPairGenerator<PRS extends Set, PRE extends Element, PUS extends Set, PUE extends Element>
	   extends UniCrypt
	   implements KeyPairGenerator {

	private final PRS privateKeySpace;
	private Function privateKeyGenerationFunction;
	private Function publicKeyGenerationFunction;
	private Function keyPairGenerationFunction;

	protected AbstractKeyPairGenerator(PRS privateKeySpace) {
		this.privateKeySpace = privateKeySpace;
	}

	@Override
	public final PRS getPrivateKeySpace() {
		return privateKeySpace;
	}

	@Override
	public final Function getPrivateKeyGenerationFunction() {
		if (this.privateKeyGenerationFunction == null) {
			this.privateKeyGenerationFunction = this.standardGetPrivateKeyGenerationFunction();
		}
		return this.privateKeyGenerationFunction;
	}

	@Override
	public final PRE generatePrivateKey() {
		return this.generatePrivateKey((RandomNumberGenerator) null);
	}

	@Override
	public final PRE generatePrivateKey(RandomNumberGenerator randomGenerator) {
		return (PRE) this.getPrivateKeyGenerationFunction().apply(SingletonGroup.getInstance().getElement(), randomGenerator);
	}

	@Override
	public final PUS getPublicKeySpace() {
		return (PUS) this.getPublicKeyGenerationFunction().getCoDomain();
	}

	@Override
	public final Function getPublicKeyGenerationFunction() {
		if (this.publicKeyGenerationFunction == null) {
			this.publicKeyGenerationFunction = this.abstractGetPublicKeyGenerationFunction();
		}
		return this.publicKeyGenerationFunction;
	}

	@Override
	public final PUE generatePublicKey(Element privateKey) {
		return this.generatePublicKey(privateKey, (RandomNumberGenerator) null);
	}

	@Override
	public final PUE generatePublicKey(Element privateKey, RandomNumberGenerator randomGenerator) {
		if (privateKey == null || !this.getPrivateKeySpace().contains(privateKey)) {
			throw new IllegalArgumentException();
		}
		return (PUE) this.getPublicKeyGenerationFunction().apply(privateKey, randomGenerator);
	}

	@Override
	public ProductSet getKeyPairSpace() {
		return (ProductSet) this.getKeyPairGenerationFunction().getCoDomain();
	}

	//		this.keyPairSpace = ProductSet.getInstance(this.getPrivateKeySpace(), this.getPublicKeySpace());
	@Override
	public final Function getKeyPairGenerationFunction() {
		if (this.keyPairGenerationFunction == null) {
			this.keyPairGenerationFunction = CompositeFunction.getInstance(
				   this.getPrivateKeyGenerationFunction(),
				   MultiIdentityFunction.getInstance(this.privateKeySpace, 2),
				   ProductFunction.getInstance(IdentityFunction.getInstance(this.privateKeySpace),
											   this.getPublicKeyGenerationFunction()));
		}
		return this.keyPairGenerationFunction;
	}

	@Override
	public Pair generateKeyPair() {
		return this.generateKeyPair((RandomNumberGenerator) null);
	}

	@Override
	public Pair generateKeyPair(RandomNumberGenerator randomGenerator) {
		return (Pair) this.getKeyPairGenerationFunction().apply(SingletonGroup.getInstance().getElement(), randomGenerator);
	}

	@Override
	protected String standardToStringContent() {
		return this.getPrivateKeySpace().toString() + ", " + this.getPublicKeySpace().toString();
	}

	protected Function standardGetPrivateKeyGenerationFunction() {
		return RandomFunction.getInstance(this.getPrivateKeySpace());
	}

	protected abstract Function abstractGetPublicKeyGenerationFunction();

}
