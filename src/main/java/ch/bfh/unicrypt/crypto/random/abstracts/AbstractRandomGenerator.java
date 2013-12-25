package ch.bfh.unicrypt.crypto.random.abstracts;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

/**
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public abstract class AbstractRandomGenerator
	   extends UniCrypt
	   implements RandomGenerator {

	@Override
	public final boolean nextBoolean() {
		return this.abstractNextBoolean();
	}

	@Override
	public final byte nextByte() {
		return this.nextBytes(1)[0];
	}

	@Override
	public final byte[] nextBytes(int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return this.abstractNextBytes(length);
	}

	@Override
	public final int nextInteger() {
		return this.nextInteger(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public final int nextInteger(int maxValue) {
		if (maxValue < 0) {
			throw new IllegalArgumentException();
		}
		return this.abstractNextInteger(maxValue);
	}

	@Override
	public final int nextInteger(int minValue, int maxValue) {
		return this.nextInteger(maxValue - minValue) + minValue;
	}

	@Override
	public final BigInteger nextBigInteger(int bitLength) {
		if (bitLength < 0) {
			throw new IllegalArgumentException();
		}
		if (bitLength == 0) {
			return BigInteger.ZERO;
		}
		return this.abstractNextBigInteger(bitLength);
	}

	@Override
	public final BigInteger nextBigInteger(BigInteger maxValue) {
		if (maxValue == null || maxValue.signum() < 0) {
			throw new IllegalArgumentException();
		}
		return this.abstractNextBigInteger(maxValue);
	}

	@Override
	public final BigInteger nextBigInteger(BigInteger minValue, BigInteger maxValue) {
		if (minValue == null || maxValue == null) {
			throw new IllegalArgumentException();
		}
		return this.nextBigInteger(maxValue.subtract(minValue)).add(minValue);
	}

	@Override
	public final BigInteger nextPrime(int bitLength) {
		if (bitLength < 2) {
			throw new IllegalArgumentException();
		}
		return this.abstractNextPrime(bitLength);
	}

	@Override
	public final BigInteger nextSavePrime(int bitLength) {
		BigInteger prime;
		BigInteger savePrime;
		do {
			prime = this.nextPrime(bitLength - 1);
			savePrime = prime.shiftLeft(1).add(BigInteger.ONE);
		} while (!MathUtil.isPrime(savePrime));
		return savePrime;
	}

	@Override
	public final BigInteger[] nextPrimePair(int bitLength1, int bitLength2) {
		if (bitLength1 <= bitLength2 || bitLength2 < 2) {
			throw new IllegalArgumentException();
		}
		BigInteger k;
		BigInteger prime1, prime2;
		BigInteger minValue, maxValue;
		do {
			prime2 = this.nextPrime(bitLength2);
			minValue = BigInteger.ONE.shiftLeft(bitLength1 - 1);
			maxValue = BigInteger.ONE.shiftLeft(bitLength1).subtract(BigInteger.ONE);
			k = this.nextBigInteger(minValue.divide(prime2).add(BigInteger.ONE), maxValue.divide(prime2));
			prime1 = prime2.multiply(k).add(BigInteger.ONE);
		} while (!MathUtil.isPrime(prime1));
		return new BigInteger[]{prime1, prime2};
	}

	protected abstract boolean abstractNextBoolean();

	protected abstract byte[] abstractNextBytes(int length);

	protected abstract int abstractNextInteger(int maxValue);

	protected abstract BigInteger abstractNextBigInteger(int bitLength);

	protected abstract BigInteger abstractNextBigInteger(BigInteger maxValue);

	protected abstract BigInteger abstractNextPrime(int bitLength);

}
