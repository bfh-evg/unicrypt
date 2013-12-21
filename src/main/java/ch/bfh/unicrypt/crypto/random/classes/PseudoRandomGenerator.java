package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.abstracts.AbstractRandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

/**
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class PseudoRandomGenerator
	   extends AbstractRandomGenerator {

	public static final Element DEFAULT_SEED = Z.getInstance().getElement(0);
	public static final PseudoRandomGenerator DEFAULT = getInstance();
	private final HashMethod hashMethod;
	private final Element seed;
	private int counter;
	byte[] digestBytes;
	int digestBytesPosition;

	// Random random;
	protected PseudoRandomGenerator(HashMethod hashMethod, final Element seed) {
		this.hashMethod = hashMethod;
		this.seed = seed;
		this.setCounter(0);
	}

	private void fillRandomByteBufer() {
		if (this.digestBytes == null) {
			this.digestBytes = new byte[hashMethod.getLength()];
		}
		Pair pair = Pair.getInstance(seed, Z.getInstance().getElement(getCounter()));
		this.digestBytes = pair.getHashValue(hashMethod).getByteArray();
		this.digestBytesPosition = 0;
	}

	public HashMethod getHashMethod() {
		return hashMethod;
	}

	public Element getSeed() {
		return this.seed;
	}

	public int getCounter() {
		return this.counter;
	}

	protected void reset() {
		setCounter(0);
	}

	protected boolean isReset() {
		return counter == 0 && this.digestBytesPosition == 0;
	}

	protected void setCounter(final int counter) {
		this.counter = counter;
		fillRandomByteBufer();
	}

	@Override
	protected boolean abstractNextBoolean() {
		return nextBytes(1)[0] == 1;
	}

	/**
	 * Counter goes up after digest.length bytes, after initialization with sha256, 32bytes are ready to be read and
	 * counter is at 0 after having read 32 bytes counter jumps to 1 and another 32 bytes are ready to be read
	 * <p>
	 * @param length
	 * @return
	 */
	@Override
	protected byte[] abstractNextBytes(int length) {
		byte[] randomBytes = new byte[length];
		int randomBytesPosition = 0;
		while (randomBytesPosition < length) {
			int amount = Math.min((length - randomBytesPosition), (digestBytes.length - digestBytesPosition));
			System.arraycopy(digestBytes, digestBytesPosition, randomBytes, randomBytesPosition, amount);
			randomBytesPosition += amount;
			digestBytesPosition += amount;
			if (digestBytesPosition == digestBytes.length) {
				setCounter(getCounter() + 1);
			}
		}
		return randomBytes;
	}

	@Override
	protected int abstractNextInteger(int maxValue) {
		//This is a slow implementation.
		return nextBigInteger(BigInteger.valueOf(maxValue)).intValue();
	}

	/**
	 * MSB is always set
	 * <p>
	 * @param bitLength
	 * @return
	 */
	@Override
	protected BigInteger abstractNextBigInteger(int bitLength) {
		return internalNextBigInteger(bitLength, true);
	}

	private BigInteger internalNextBigInteger(int bitLength, boolean isMsbSet) {
		int amountOfBytes = (int) Math.ceil(bitLength / 8.0);
		byte[] bytes = nextBytes(amountOfBytes);

		int shift = 8 - (bitLength % 8);
		if (shift == 8) {
			shift = 0;
		}
		if (isMsbSet) {
			bytes[0] = (byte) (((bytes[0] & 0xFF) | 0x80) >> shift);
		} else {
			bytes[0] = (byte) ((bytes[0] & 0xFF) >> shift);

		}
		return new BigInteger(1, bytes);
	}

	@Override
	protected BigInteger abstractNextBigInteger(BigInteger maxValue) {
		BigInteger randomValue;
		int bitLength = maxValue.bitLength();
		do {
			randomValue = internalNextBigInteger(bitLength, false);
		} while (randomValue.compareTo(maxValue) > 0);
		return randomValue;
	}

	/**
	 * MSB always set
	 * <p>
	 * @param bitLength
	 * @return
	 */
	@Override
	protected BigInteger abstractNextPrime(int bitLength) {
		BigInteger bigInteger = null;
		do {
			bigInteger = internalNextBigInteger(bitLength, true);
		} while (!bigInteger.isProbablePrime(MathUtil.NUMBER_OF_PRIME_TESTS));
		return bigInteger;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + (this.hashMethod != null ? this.hashMethod.hashCode() : 0);
		hash = 17 * hash + (this.seed != null ? this.seed.hashCode() : 0);
		hash = 17 * hash + this.counter;
		hash = 17 * hash + this.digestBytesPosition;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PseudoRandomGenerator other = (PseudoRandomGenerator) obj;
		if (this.hashMethod != other.hashMethod && (this.hashMethod == null || !this.hashMethod.equals(other.hashMethod))) {
			return false;
		}
		if (this.seed != other.seed && (this.seed == null || !this.seed.equals(other.seed))) {
			return false;
		}
		if (this.counter != other.counter) {
			return false;
		}
		if (this.digestBytesPosition != other.digestBytesPosition) {
			return false;
		}
		return true;
	}

	public static PseudoRandomGenerator getInstance() {
		return PseudoRandomGenerator.getInstance(HashMethod.DEFAULT, DEFAULT_SEED);
	}

	public static PseudoRandomGenerator getInstance(HashMethod hashMethod) {
		return new PseudoRandomGenerator(hashMethod, DEFAULT_SEED);
	}

	public static PseudoRandomGenerator getInstance(Element seed) {
		return new PseudoRandomGenerator(HashMethod.DEFAULT, seed);
	}

	public static PseudoRandomGenerator getInstance(HashMethod hashMethod, Element seed) {
		if (seed == null) {
			throw new IllegalArgumentException();
		}
		if (hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomGenerator(hashMethod, seed);
	}

	/**
	 * Originally developed by Raphaël Grosbois and Diego Santa Cruz (Swiss Federal Institute of Technology-EPFL) et al.
	 * in http://www.java2s.com/Tutorial/Java/0120__Development/Calculatethefloorofthelogbase2.htm Method that
	 * calculates the floor of the log, base 2, of 'x'. The calculation is performed in integer arithmetic, therefore,
	 * it is exact.
	 * <p>
	 * @param x The value to calculate log2 on.
	 * <p>
	 * @return floor(log(x)/log(2)), calculated in an exact way.
     * */
	private static int log2Floor(int x) {
		int y, v;
		// No log of 0 or negative
		if (x <= 0) {
			throw new IllegalArgumentException("" + x + " <= 0");
		}
		// Calculate log2 (it's actually floor log2)
		v = x;
		y = -1;
		while (v > 0) {
			v >>= 1;
			y++;
		}
		return y;
	}

}
