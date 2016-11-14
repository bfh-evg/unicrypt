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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Instances of this class represent hash algorithms such as SHA-1 or SHA-256. This class is a wrapper class for
 * {@link MessageDigest} with method names adjusted to the conventions of UniCrypt. A hash algorithm maps arbitrary Java
 * byte arrays into fixed-length byte arrays. The resulting byte array is called hash value of the input.
 * <p>
 * There are also methods to compute keyed hash values based on HMAC as defined in RFC 2104 "HMAC: Keyed-Hashing for
 * Message Authentication".
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class HashAlgorithm
	   extends UniCrypt {

	/**
	 * The SHA-1 hash algorithm.
	 */
	public static final HashAlgorithm SHA1 = new HashAlgorithm("SHA-1");

	/**
	 * The SHA-224 hash algorithm.
	 */
	public static final HashAlgorithm SHA224 = new HashAlgorithm("SHA-224");

	/**
	 * The SHA-256 hash algorithm.
	 */
	public static final HashAlgorithm SHA256 = new HashAlgorithm("SHA-256");

	/**
	 * The SHA-384 hash algorithm.
	 */
	public static final HashAlgorithm SHA384 = new HashAlgorithm("SHA-384");

	/**
	 * The SHA-512 hash algorithm.
	 */
	public static final HashAlgorithm SHA512 = new HashAlgorithm("SHA-512");

	private static final long serialVersionUID = 1L;

	// The name of the algorithm
	private final String algorithmName;

	// An instance of MessageDigest to execute the algorithm
	private final MessageDigest messageDigest;

	// The block length of the algorithm (used in HMAC)
	private final int blockLength;

	private HashAlgorithm(String algorithmName) {
		this.algorithmName = algorithmName;
		try {
			this.messageDigest = MessageDigest.getInstance(algorithmName);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException();
		}
		switch (algorithmName) {
			case "SHA-1":
				this.blockLength = 64;
				break;
			case "SHA-224":
				this.blockLength = 64;
				break;
			case "SHA-256":
				this.blockLength = 64;
				break;
			case "SHA-384":
				this.blockLength = 128;
				break;
			case "SHA-512":
				this.blockLength = 128;
				break;
			default:
				this.blockLength = 0; // impossible case
		}
	}

	/**
	 * Returns the name of the hash algorithm.
	 * <p>
	 * @return The name of the hash algorithm
	 */
	public String getAlgorithmName() {
		return this.algorithmName;
	}

	/**
	 * Returns the hash value of a given Java byte array.
	 * <p>
	 * This method must be synchronized in order to work properly in multi-threaded environment. The underlying
	 * messageDigest is not thread safe
	 * <p>
	 * @param message The given Java byte array
	 * @return The hash value
	 */
	public synchronized byte[] getHashValue(byte[] message) {
		if (message == null) {
			throw new IllegalArgumentException();
		}
		return this.messageDigest.digest(message);

	}

	/**
	 * Returns the hash value of a given byte array.
	 * <p>
	 * @param message The given byte array
	 * @return The hash value
	 */
	public ByteArray getHashValue(ByteArray message) {
		if (message == null) {
			throw new IllegalArgumentException();
		}
		// this call gives acces to the internal byte[] representation of ByteArray
		return message.getHashValue(this);
	}

	/**
	 * Returns the hash value of a given byte array truncated to a certain number of bytes.
	 * <p>
	 * @param message    The given byte array
	 * @param hashLength The length of the hash value
	 * @return The hash value
	 */
	public ByteArray getHashValue(ByteArray message, int hashLength) {
		return this.getHashValue(message).extractPrefix(hashLength);
	}

	/**
	 * Returns the HMAC value (RFC 2104) for a given byte array and key.
	 * <p>
	 * @param message The given byte array
	 * @param key     The given key
	 * @return The keyed hash value
	 */
	public ByteArray getHashValue(ByteArray message, ByteArray key) {
		if (message == null || key == null) {
			throw new IllegalArgumentException();
		}
		if (key.getLength() > this.blockLength) {
			key = key.getHashValue(this);
		}
		if (key.getLength() < this.blockLength) {
			key = key.addSuffix(this.blockLength - key.getLength());
		}
		ByteArray ipad = ByteArray.getInstance(MathUtil.getByte(0x36), this.blockLength);
		ByteArray byteArray1 = key.xor(ipad).append(message).getHashValue(this);
		ByteArray opad = ByteArray.getInstance(MathUtil.getByte(0x5C), this.blockLength);
		ByteArray byteArray2 = key.xor(opad);
		// according to RFC 2104
		return byteArray2.append(byteArray1).getHashValue(this);
		// according to FIPS 198
		//return byteArray2.append(byteArray1).append(message).getHashValue(this);
	}

	/**
	 * Returns the HMAC value (RFC 2104) for a given byte array truncated to a certain number of bytes.
	 * <p>
	 * @param message    The given byte array
	 * @param key        The given key
	 * @param hashLength The length of the hash value
	 * @return The keyed hash value
	 */
	public ByteArray getHashValue(ByteArray message, ByteArray key, int hashLength) {
		return this.getHashValue(message, key).extractPrefix(hashLength);
	}

	/**
	 * Returns the byte length (number of bytes) of the hash values returned by this hash algorithm.
	 * <p>
	 * @return The byte length
	 */
	public int getByteLength() {
		return this.messageDigest.getDigestLength();
	}

	/**
	 * Returns the bit length (number of bits) of the hash values returned by this hash algorithm.
	 * <p>
	 * @return The bit length
	 */
	public int getBitLength() {
		return this.getByteLength() * Byte.SIZE;
	}

	/**
	 * Returns the library's default hash algorithm SHA-256.
	 * <p>
	 * @return The default hash algorithm
	 */
	public static HashAlgorithm getInstance() {
		return HashAlgorithm.SHA256;
	}

	/**
	 * Returns the hash algorithm that corresponds to the algorithm name specified as a string. The supported algorithm
	 * names are: {@code "SHA-1"}, {@code "SHA-224"}, {@code "SHA-256"}, {@code "SHA-384"}, and {@code "SHA-512"}.
	 * <p>
	 * @param algorithmName The name of the hash algorithm.
	 * @return The new hash method
	 */
	public static HashAlgorithm getInstance(String algorithmName) {
		switch (algorithmName) {
			case "SHA-1":
				return HashAlgorithm.SHA1;
			case "SHA-224":
				return HashAlgorithm.SHA224;
			case "SHA-256":
				return HashAlgorithm.SHA256;
			case "SHA-384":
				return HashAlgorithm.SHA384;
			case "SHA-512":
				return HashAlgorithm.SHA512;
		}
		throw new IllegalArgumentException();
	}

	@Override
	protected String defaultToStringContent() {
		return this.algorithmName;
	}

	@Override
	public int hashCode() {
		return 7 * this.algorithmName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HashAlgorithm other = (HashAlgorithm) obj;
		return this.algorithmName.equals(other.algorithmName);
	}

}
