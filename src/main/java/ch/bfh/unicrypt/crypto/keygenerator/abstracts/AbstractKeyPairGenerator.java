/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.random.password.PasswordRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.classes.RandomFunction;
import ch.bfh.unicrypt.math.function.classes.SharedDomainFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author R. Haenni
 * @param <PRS> Private key space
 * @param <PRE> Private key element
 * @param <PUS> Public key space
 * @param <PUE> Public key element
 */
public abstract class AbstractKeyPairGenerator<PRS extends Set, PRE extends Element, PUS extends Set, PUE extends Element>
	   extends UniCrypt
	   implements KeyPairGenerator {

	private final PRS privateKeySpace;
	private final PUS publicKeySpace;

	private Function privateKeyGenerationFunction;
	private Function publicKeyGenerationFunction;
	private Function keyPairGenerationFunction;

	/**
	 *
	 * @param privateKeySpace
	 * @param publicKeySpace
	 */
	protected AbstractKeyPairGenerator(PRS privateKeySpace, PUS publicKeySpace) {
		this.privateKeySpace = privateKeySpace;
		this.publicKeySpace = publicKeySpace;
	}

	@Override
	public final PRS getPrivateKeySpace() {
		return this.privateKeySpace;
	}

	@Override
	public final PUS getPublicKeySpace() {
		return this.publicKeySpace;
	}

	@Override
	public final Function getPrivateKeyGenerationFunction() {
		if (this.privateKeyGenerationFunction == null) {
			this.privateKeyGenerationFunction = this.defaultGetPrivateKeyGenerationFunction();
		}
		return this.privateKeyGenerationFunction;
	}

	@Override
	public final PRE generatePrivateKey() {
		return this.generatePrivateKey(HybridRandomByteSequence.getInstance());
	}

	/**
	 *
	 * @param password
	 * @return Returns the private key
	 */
	@Override
	public PRE generatePrivateKey(String password) {
		if (password == null) {
			throw new IllegalArgumentException();
		}
		return this.generatePrivateKey(PasswordRandomByteSequence.getInstance(password));
	}

	/**
	 *
	 * @param password
	 * @param salt
	 * @return Returns the private key
	 */
	@Override
	public PRE generatePrivateKey(String password, ByteArray salt) {
		if (password == null || salt == null) {
			throw new IllegalArgumentException();
		}
		return this.generatePrivateKey(PasswordRandomByteSequence.getInstance(password, salt));
	}

	/**
	 *
	 * @param randomByteSequence
	 * @return Returns the private key
	 */
	@Override
	public final PRE generatePrivateKey(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return (PRE) this.getPrivateKeyGenerationFunction().apply(SingletonGroup.getInstance().getElement(),
																  randomByteSequence);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public final Function getPublicKeyGenerationFunction() {
		if (this.publicKeyGenerationFunction == null) {
			this.publicKeyGenerationFunction = this.abstractGetPublicKeyGenerationFunction();
		}
		return this.publicKeyGenerationFunction;
	}

	/**
	 *
	 * @param privateKey
	 * @return Returns the public key
	 */
	@Override
	public final PUE generatePublicKey(Element privateKey) {
		return this.generatePublicKey(privateKey, HybridRandomByteSequence.getInstance());
	}

	/**
	 *
	 * @param privateKey
	 * @param randomByteSequence
	 * @return Returns the public key
	 */
	@Override
	public final PUE generatePublicKey(Element privateKey, RandomByteSequence randomByteSequence) {
		if (privateKey == null || !this.getPrivateKeySpace().contains(privateKey)) {
			throw new IllegalArgumentException();
		}
		return (PUE) this.getPublicKeyGenerationFunction().apply(privateKey, randomByteSequence);
	}

	/**
	 *
	 * @return Returns the keypair space
	 */
	@Override
	public ProductSet getKeyPairSpace() {
		return (ProductSet) this.getKeyPairGenerationFunction().getCoDomain();
	}

	//		this.keyPairSpace = ProductSet.getInstance(this.getPrivateKeySpace(), this.getPublicKeySpace());
	/**
	 *
	 * @return Returns the function used to generate keypairs
	 */
	@Override
	public final Function getKeyPairGenerationFunction() {
		if (this.keyPairGenerationFunction == null) {
			this.keyPairGenerationFunction = CompositeFunction.getInstance(
				   this.getPrivateKeyGenerationFunction(),
				   SharedDomainFunction.getInstance(IdentityFunction.getInstance(this.privateKeySpace),
													this.getPublicKeyGenerationFunction()));
		}
		return this.keyPairGenerationFunction;
	}

	/**
	 *
	 * @return Returns a keypair
	 */
	@Override
	public Pair generateKeyPair() {
		return this.generateKeyPair(HybridRandomByteSequence.getInstance());
	}

	/**
	 *
	 * @param randomByteSequence
	 * @return Returns the keypair based on the provided sequence
	 */
	@Override
	public Pair generateKeyPair(RandomByteSequence randomByteSequence) {
		return (Pair) this.getKeyPairGenerationFunction().apply(SingletonGroup.getInstance().getElement(),
																randomByteSequence);
	}

	/**
	 *
	 * @param password
	 * @return Returns the keypair based on the provided password
	 */
	@Override
	public Pair generateKeyPair(String password) {
		return this.generateKeyPair(password, ByteArray.getInstance());
	}

	/**
	 *
	 * @param password
	 * @param salt
	 * @return Returns the keypair based on the provided password and salt
	 */
	@Override
	public Pair generateKeyPair(String password, ByteArray salt) {
		if (password == null || salt == null) {
			throw new IllegalArgumentException();
		}
		return this.generateKeyPair(PasswordRandomByteSequence.getInstance(password, salt));
	}

	/**
	 *
	 * @return Returns a string representing this object
	 */
	@Override
	protected String defaultToStringContent() {
		return this.getPrivateKeySpace().toString() + ", " + this.getPublicKeySpace().toString();
	}

	/**
	 *
	 * @return Returns the default function to generate a private key
	 */
	protected Function defaultGetPrivateKeyGenerationFunction() {
		return RandomFunction.getInstance(this.getPrivateKeySpace());
	}

	/**
	 *
	 * @return Returns the function to generate a public key
	 */
	protected abstract Function abstractGetPublicKeyGenerationFunction();

}
