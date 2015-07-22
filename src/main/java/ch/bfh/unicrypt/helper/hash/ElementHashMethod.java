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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.helper.UniCrypt;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;

/**
 * The purpose of this class is to extend the applicability of hash algorithms from byte array input objects to
 * arbitrary input objects. This adds complexity in two ways.
 * <p>
 * First, input objects may have different types, for example {@link String}, {@link BigInteger}, {@link Permutation},
 * etc. To compute hash values of such general input objects, they need to be converted to byte arrays first. Each
 * instance of {@link ElementHashMethod} provides therefore its own {@link ConvertMethod} object of type
 * {@link ByteArray}, which handles the conversion. Let {@code b=bytes(x)} denote the conversion of an input {@code x}
 * into a byte array {@code b}.
 * <p>
 * Second, input objects may have an internal structure such as a list or a tree. In this class, we consider general
 * tree-shaped structures, which can be handled in different ways when it comes to computing hash values. Three
 * different modes are supported:
 * <ul>
 * <li> {@link Mode#RECURSIVE}: Hash values are computed recursively, for example {@code h(h(b1)|h(h(b2)|h(b3))|h(b4))}
 * for input {@code (x1,(x2,x3),x4)} and {@code bi=bytes(xi)}. This is the default mode of this class.
 * <li> {@link Mode#BYTETREE}: Hash values are computed from the byte tree representation provided by the class
 * {@link ByteTree}, for example {@code h(bytes(byteTree(x1,(x2,x3),x4)))} for input {@code (x1,(x2,x3),x4)}.
 * <li> {@link Mode#BYTEARRAY}: Hash values are computed by first converting the entire input to a byte array, for
 * example {@code h(bytes(x1,(x2,x3),x4))} for input {@code (x1,(x2,x3),x4)}. It is assumed that such a conversion
 * exists, without specifying this further.
 * </ul>
 * The default mode of this class is {@link Mode#RECURSIVE}.
 * <p>
 * Note that instances of this class only provide a specification of the hash value computation method, but not the
 * actual functionality of computing hash values.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class ElementHashMethod
	   extends UniCrypt {

	/**
	 * This enum type lists the three supported modes of computing the hash values of general tree-shaped inputs.
	 */
	public enum Mode {

		RECURSIVE, BYTETREE, BYTEARRAY;

	};

	private final HashAlgorithm hashAlgorithm;
	private final ConvertMethod<ByteArray> convertMethod;
	private final Mode mode;

	protected ElementHashMethod(HashAlgorithm hashAlgorithm, ConvertMethod<ByteArray> convertMethod, Mode mode) {
		this.hashAlgorithm = hashAlgorithm;
		this.convertMethod = convertMethod;
		this.mode = mode;
	}

	/**
	 * Returns the hash algorithm of the hash method.
	 * <p>
	 * @return The hash algorithm
	 */
	public HashAlgorithm getHashAlgorithm() {
		return this.hashAlgorithm;
	}

	/**
	 * Returns the convert method of the hash method, which handles the conversion to byte arrays.
	 * <p>
	 * @return The convert method
	 */
	public ConvertMethod<ByteArray> getConvertMethod() {
		return this.convertMethod;
	}

	/**
	 * Returns the mode of the hash value computation for general tree-shaped inputs.
	 * <p>
	 * @return The mode
	 */
	public Mode getMode() {
		return this.mode;
	}

	@Override
	protected String defaultToStringContent() {
		return this.hashAlgorithm.toString() + "," + this.convertMethod.toString() + "," + this.mode.toString();
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + this.hashAlgorithm.hashCode();
		hash = 97 * hash + this.convertMethod.hashCode();
		hash = 97 * hash + this.mode.hashCode();
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
		final ElementHashMethod other = (ElementHashMethod) obj;
		return this.hashAlgorithm.equals(other.hashAlgorithm) && this.convertMethod.equals(other.convertMethod)
			   && this.mode.equals(other.mode);
	}

	/**
	 * Returns a new hash method for the default hash algorithm, the default convert method, and the default mode.
	 * <p>
	 * @return The new hash method
	 */
	public static ElementHashMethod getInstance() {
		return ElementHashMethod.getInstance(HashAlgorithm.getInstance(), ConvertMethod.getInstance(ByteArray.class),
											 Mode.RECURSIVE);
	}

	/**
	 * Returns a new hash method for a given hash algorithm, the default convert method, and the default mode.
	 * <p>
	 * @param hashAlgorithm The hash algorithm
	 * @return The new hash method
	 */
	public static ElementHashMethod getInstance(HashAlgorithm hashAlgorithm) {
		return ElementHashMethod.getInstance(hashAlgorithm, ConvertMethod.getInstance(ByteArray.class), Mode.RECURSIVE);
	}

	/**
	 * Returns a new hash method for the default hash algorithm, a given convert method, and the default mode.
	 * <p>
	 * @param convertMethod The convert method
	 * @return The new hash method
	 */
	public static ElementHashMethod getInstance(ConvertMethod<ByteArray> convertMethod) {
		return ElementHashMethod.getInstance(HashAlgorithm.getInstance(), convertMethod, Mode.RECURSIVE);
	}

	/**
	 * Returns a new hash method for the default hash algorithm, the default convert method, and a given mode.
	 * <p>
	 * @param mode The mode
	 * @return The new hash method
	 */
	public static ElementHashMethod getInstance(Mode mode) {
		return ElementHashMethod.getInstance(HashAlgorithm.getInstance(), ConvertMethod.getInstance(ByteArray.class), mode);
	}

	/**
	 * Returns a new hash method for a given hash algorithm, a given convert method, and the default mode.
	 * <p>
	 * @param hashAlgorithm The hash algorithm
	 * @param convertMethod The convert method
	 * @return The new hash method
	 */
	public static ElementHashMethod getInstance(HashAlgorithm hashAlgorithm, ConvertMethod<ByteArray> convertMethod) {
		return ElementHashMethod.getInstance(hashAlgorithm, convertMethod, Mode.RECURSIVE);
	}

	/**
	 * Returns a new hash method for the default hash algorithm, a given convert method, and a given mode.
	 * <p>
	 * @param convertMethod The convert method
	 * @param mode          The mode
	 * @return The new hash method
	 */
	public static ElementHashMethod getInstance(ConvertMethod<ByteArray> convertMethod, Mode mode) {
		return ElementHashMethod.getInstance(HashAlgorithm.getInstance(), convertMethod, mode);
	}

	/**
	 * Returns a new hash method for a given hash algorithm, the default convert method, and a given mode.
	 * <p>
	 * @param hashAlgorithm The hash algorithm
	 * @param mode          The mode
	 * @return The new hash method
	 */
	public static ElementHashMethod getInstance(HashAlgorithm hashAlgorithm, Mode mode) {
		return ElementHashMethod.getInstance(hashAlgorithm, ConvertMethod.getInstance(ByteArray.class), mode);
	}

	/**
	 * Returns a new hash method for a given hash algorithm, a given convert method, and a given mode.
	 * <p>
	 * @param hashAlgorithm The hash algorithm
	 * @param convertMethod The convert method
	 * @param mode          The mode
	 * @return The new hash method
	 */
	public static ElementHashMethod
		   getInstance(HashAlgorithm hashAlgorithm, ConvertMethod<ByteArray> convertMethod, Mode mode) {
		if (hashAlgorithm == null || convertMethod == null || mode == null) {
			throw new IllegalArgumentException();
		}
		return new ElementHashMethod(hashAlgorithm, convertMethod, mode);
	}

}
