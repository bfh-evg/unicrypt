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
public class HashMethod {

  public static final HashMethod DEFAULT = HashMethod.getInstance("SHA-256");

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
