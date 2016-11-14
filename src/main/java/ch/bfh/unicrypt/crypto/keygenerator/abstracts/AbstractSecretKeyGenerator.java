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
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.SecretKeyGenerator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.random.password.PasswordRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.RandomFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractSecretKeyGenerator<KS extends Set, KE extends Element>
	   extends UniCrypt
	   implements SecretKeyGenerator {

	private final KS secretKeySpace;
	private Function secretKeyGenerationFunction; // with singleton domain

	protected AbstractSecretKeyGenerator(final KS secretKeySpace) {
		this.secretKeySpace = secretKeySpace;
	}

	@Override
	public final KS getSecretKeySpace() {
		return this.secretKeySpace;
	}

	@Override
	public KE generateSecretKey() {
		return this.generateSecretKey(HybridRandomByteSequence.getInstance());
	}

	@Override
	public KE generateSecretKey(String password) {
		if (password == null) {
			throw new IllegalArgumentException();
		}
		return this.generateSecretKey(PasswordRandomByteSequence.getInstance(password));
	}

	@Override
	public KE generateSecretKey(String password, ByteArray salt) {
		if (password == null || salt == null) {
			throw new IllegalArgumentException();
		}
		return this.generateSecretKey(PasswordRandomByteSequence.getInstance(password, salt));
	}

	@Override
	public KE generateSecretKey(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return (KE) this.getSecretKeyGenerationFunction().apply(SingletonGroup.getInstance().getElement(),
																randomByteSequence);
	}

	@Override
	public Function getSecretKeyGenerationFunction() {
		if (this.secretKeyGenerationFunction == null) {
			this.secretKeyGenerationFunction = this.defaultGetSecretKeyGenerationFunction();
		}
		return this.secretKeyGenerationFunction;
	}

	protected Function defaultGetSecretKeyGenerationFunction() {
		return RandomFunction.getInstance(this.getSecretKeySpace());
	}

	@Override
	protected String defaultToStringContent() {
		return this.getSecretKeySpace().toString();
	}

}
