package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.abstracts.AbstractRandomGenerator;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PseudoRandomGenerator
			 extends AbstractRandomGenerator {

	public static final String DEFAULT_ALGORITHM_NAME = "SHA1PRNG";
	public static final PseudoRandomGenerator DEFAULT = PseudoRandomGenerator.getInstance();

	protected Random random;

	protected PseudoRandomGenerator(Random random) {
		this.random = random;
	}

	public Random getRandom() {
		return this.random;
	}

	@Override
	protected boolean abstractNextBoolean() {
		return this.random.nextBoolean();
	}

	@Override
	protected byte[] abstractNextBytes(int length) {
		byte[] bytes = new byte[length];
		this.random.nextBytes(bytes);
		return bytes;
	}

	@Override
	protected int abstractNextInteger(int maxValue) {
		return this.random.nextInt(maxValue + 1);
	}

	@Override
	protected BigInteger abstractNextBigInteger(int bitLength) {
		return BigInteger.ONE.shiftLeft(bitLength - 1).add(new BigInteger(bitLength - 1, this.random));
	}

	@Override
	protected BigInteger abstractNextBigInteger(BigInteger maxValue) {
		BigInteger randomValue;
		int bitLength = maxValue.bitLength();
		do {
			randomValue = new BigInteger(bitLength, this.random);
		} while (randomValue.compareTo(maxValue) > 0);
		return randomValue;
	}

	@Override
	protected BigInteger abstractNextPrime(int bitLength) {
		return new BigInteger(bitLength, MathUtil.NUMBER_OF_PRIME_TESTS, this.random);
	}

	public static PseudoRandomGenerator getInstance() {
		return PseudoRandomGenerator.getInstance(DEFAULT_ALGORITHM_NAME);
	}

	public static PseudoRandomGenerator getInstance(String algorithmName) {
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance(algorithmName);
		} catch (final NoSuchAlgorithmException exception) {
			throw new IllegalArgumentException(exception);
		}
		secureRandom.nextBoolean(); // initiates the entropy gathering
		return new PseudoRandomGenerator(secureRandom);
	}

	public static PseudoRandomGenerator getInstance(Random random) {
		if (random == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomGenerator(random);
	}

}
