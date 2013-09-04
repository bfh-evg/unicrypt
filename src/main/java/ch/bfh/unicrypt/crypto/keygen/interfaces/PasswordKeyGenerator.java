package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface PasswordKeyGenerator extends KeyGenerator {

  public Element generateKey(String password);

  public Element generateKey(String password, byte[] salt);

}
