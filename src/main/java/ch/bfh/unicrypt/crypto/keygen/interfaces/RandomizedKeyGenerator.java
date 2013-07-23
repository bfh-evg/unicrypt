package ch.bfh.unicrypt.crypto.keygen.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;

public interface RandomizedKeyGenerator extends KeyGenerator{

  public Element generateKey();

  public Element generateKey(Random random);
}
