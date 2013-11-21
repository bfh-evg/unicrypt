/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class HashMethod
			 extends UniCrypt {

	public static final HashMethod DEFAULT = HashMethod.getInstance("SHA-256", true);

	private final String algorithmName;
	private final boolean recursive;
	private int length;

	protected HashMethod(String algorithmName, boolean recursive, int length) {
		this.algorithmName = algorithmName;
		this.recursive = recursive;
		this.length = length;
	}

	public MessageDigest getMessageDigest() {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(this.algorithmName);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException();
		}
		return messageDigest;
	}

	public boolean isRecursive() {
		return this.recursive;
	}

	public String getAlgortihmName() {
		return this.algorithmName;
	}

	public int getLength() {
		return this.length;
	}

	@Override
	public String standardToStringContent() {
		if (this.isRecursive()) {
			return this.algorithmName + ",recursive";
		}
		return this.algorithmName;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.algorithmName != null ? this.algorithmName.hashCode() : 0);
		hash = 97 * hash + (this.recursive ? 1 : 0);
		hash = 97 * hash + this.length;
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
		final HashMethod other = (HashMethod) obj;
		if ((this.algorithmName == null) ? (other.algorithmName != null) : !this.algorithmName.equals(other.algorithmName)) {
			return false;
		}
		if (this.recursive != other.recursive) {
			return false;
		}
		return this.length == other.length;
	}

	public static HashMethod getInstance(String algorithmName) {
		return HashMethod.getInstance(algorithmName, true);
	}

	public static HashMethod getInstance(String algorithmName, boolean recursive) {
		if (algorithmName == null) {
			throw new IllegalArgumentException();
		}
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(algorithmName);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException();
		}
		return new HashMethod(algorithmName, recursive, messageDigest.getDigestLength());
	}

}
