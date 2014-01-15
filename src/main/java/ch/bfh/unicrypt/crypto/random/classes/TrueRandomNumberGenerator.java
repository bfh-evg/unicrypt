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
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.abstracts.AbstractRandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.DistributionSampler;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

/**
 * This class allows the generation of ephemeral keys. Hence it provides (backward-)security and forward-security to the
 * generated random strings. Its security is based on the quality of the DistributionSampler and on the feedback of the
 * PseudoRandomGenerator. The injection of new random bits into the randomization process allows (backward-)security,
 * whilst The feedback (in this case internally requesting a byte[] which is only used for reseeding) allows
 * forward-security.
 * <p>
 * <p>
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class TrueRandomNumberGenerator
	   extends AbstractRandomGenerator {

	private final PseudoRandomGenerator pseudoRandomGenerator;
	private final DistributionSampler distributionSampler;
	private final ByteArrayMonoid byteArrayMonoid;

	protected TrueRandomNumberGenerator(DistributionSampler distributionSampler, PseudoRandomGenerator pseudoRandomGenerator) {
		this.pseudoRandomGenerator = pseudoRandomGenerator;
		this.distributionSampler = distributionSampler;
		this.byteArrayMonoid = ByteArrayMonoid.getInstance(this.pseudoRandomGenerator.getHashMethod().getLength());

	}

	public void reseed() {

		byte[] pseudoFeedback = this.pseudoRandomGenerator.nextBytes(byteArrayMonoid.getBlockLength());

		byte[] freshSeed = distributionSampler.getDistributionSample(byteArrayMonoid.getBlockLength());
		ByteArrayElement freshState = this.byteArrayMonoid.getElement(MathUtil.xor(pseudoFeedback, freshSeed));
		this.pseudoRandomGenerator.setSeed(freshState);

	}

	@Override
	protected boolean abstractNextBoolean() {
		reseed();
		return pseudoRandomGenerator.nextBoolean();
	}

	@Override
	protected byte[] abstractNextBytes(int length) {
		reseed();
		return pseudoRandomGenerator.nextBytes(length);
	}

	@Override
	protected int abstractNextInteger(int maxValue) {
		reseed();
		return pseudoRandomGenerator.nextInteger(maxValue);
	}

	@Override
	protected BigInteger abstractNextBigInteger(int bitLength) {
		reseed();
		return pseudoRandomGenerator.nextBigInteger(bitLength);
	}

	@Override
	protected BigInteger abstractNextBigInteger(BigInteger maxValue) {
		reseed();
		return pseudoRandomGenerator.nextBigInteger(maxValue);
	}

	@Override
	protected BigInteger abstractNextPrime(int bitLength) {
		reseed();
		return pseudoRandomGenerator.nextPrime(bitLength);
	}

	public static TrueRandomNumberGenerator getInstance() {
		DistributionSampler distributionSampler = DistributionSamplerSecureRandom.getInstance();
		return getInstance(distributionSampler, PseudoRandomGenerator.getInstance());
	}

	public static TrueRandomNumberGenerator getInstance(DistributionSampler distributionSampler, PseudoRandomGenerator pseudoRandomGenerator) {
		if (distributionSampler == null || pseudoRandomGenerator == null) {
			throw new IllegalArgumentException();
		}
		return new TrueRandomNumberGenerator(distributionSampler, pseudoRandomGenerator);
	}

}
