/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class Alphabet {

  public static final Alphabet UNARY = Alphabet.getInstance("1", "1");
  public static final Alphabet BINARY = Alphabet.getInstance("01", "[01]");
  public static final Alphabet OCTAL = Alphabet.getInstance("01234567", "[0-7]");
  public static final Alphabet DECIMAL = Alphabet.getInstance("0123456789", "[0-9]");
  public static final Alphabet HEXADECIMAL = Alphabet.getInstance("0123456789ABCDEF", "[0-9A-F]");
  public static final Alphabet LOWER_CASE = Alphabet.getInstance("abcdefghijklmnopqrstuvwxyz", "[a-z]");
  public static final Alphabet UPPER_CASE = Alphabet.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "[A-Z]");
  public static final Alphabet LETTERS = Alphabet.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", "[A-Za-z]");
  public static final Alphabet ALPHANUMERIC = Alphabet.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", "[A-Za-z0-9]");
  public static final Alphabet BASE64 = Alphabet.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", "[A-Za-z0-9\\+\\/]");
  public static final Alphabet PRINTABLE_ASCII = Alphabet.getInstance('\u0020', '\u0072');

  private final String characters;
  private final String regExp;

  protected Alphabet(String characters, String regExp) {
    this.characters = characters;
    this.regExp = "^(" + regExp + ")*$";
  }

  public int getSize() {
    return this.characters.length();
  }

  public char getCharacter(int i) {
    if (i < 0 || i >= this.getSize()) {
      throw new IndexOutOfBoundsException();
    }
    return this.characters.charAt(i);
  }

  public boolean contains(char c) {
    return this.characters.lastIndexOf(c) >= 0;
  }

  public int getIndex(char c) {
    int index = this.characters.lastIndexOf(c);
    if (index < 0) {
      throw new IllegalArgumentException();
    }
    return index;
  }

  public boolean isValidString(String string) {
    if (this.regExp == null) {
      for (int i = 0; i < string.length(); i++) {
        if (!this.contains(string.charAt(i))) {
          return false;
        }
      }
      return true;
    } else {
      return string.matches("^(" + this.regExp + ")*$");
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.characters == null) ? 0 : this.characters.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Alphabet other = (Alphabet) obj;
    return this.characters.equals(other.characters);
  }

  public static Alphabet getInstance(String characters) {
    return new Alphabet(characters, null);
  }

  public static Alphabet getInstance(String characters, String regExp) {
    if (characters == null) {
      throw new IllegalArgumentException();
    }
    return new Alphabet(characters, regExp);
  }

  public static Alphabet getInstance(char lowestChar, char highestChar) {
    if (lowestChar > highestChar) {
      throw new IllegalArgumentException();
    }
    String characters = "";
    for (char c = lowestChar; c <= highestChar; c++) {
      characters = characters + c;

    }
    return new Alphabet(characters, null);
  }

}
