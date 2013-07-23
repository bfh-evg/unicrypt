package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.element.Element;

public interface PasswordDerivedKeyGenerator extends KeyGenerator{

  public Element generateKey(String password, byte[] salt);

}
