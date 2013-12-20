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
	public static final PseudoRandomGenerator DEFAULT = PseudoRandomGenerator.getInstance(DEFAULT_SEED);
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

	public Element getSeed() {
		return this.seed;
	}

	public int getCounter() {
		return this.counter;
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
		return 0;
	}

	@Override
	protected BigInteger abstractNextBigInteger(int bitLength) {
		int amountOfBytes = (bitLength / 8) + 1;
		byte[] bytes = nextBytes(amountOfBytes);

		int shift = 8 - (bitLength % 8);
		if (shift == 8) {
			shift = 0;
		}
		bytes[bytes.length - 1] = (byte) (((bytes[bytes.length - 1] & 0xFF) | 0x70) >> shift);
		return new BigInteger(bytes);
	}

	@Override
	protected BigInteger abstractNextBigInteger(BigInteger maxValue) {
		BigInteger randomValue;
		int bitLength = maxValue.bitLength();
		do {
			randomValue = nextBigInteger(bitLength);
		} while (randomValue.compareTo(maxValue) > 0);
		return randomValue;
	}

	@Override
	protected BigInteger abstractNextPrime(int bitLength) {
		BigInteger bigInteger = null;
		do {
			bigInteger = nextBigInteger(bitLength);
		} while (!bigInteger.isProbablePrime(MathUtil.NUMBER_OF_PRIME_TESTS));
		return bigInteger;
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

}
