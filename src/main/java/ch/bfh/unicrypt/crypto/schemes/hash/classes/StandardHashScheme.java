package ch.bfh.unicrypt.crypto.schemes.hash.classes;

import ch.bfh.unicrypt.crypto.schemes.hash.abstracts.AbstractHashScheme;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class StandardHashScheme
       extends AbstractHashScheme {

  @Override
  protected Function abstractGetHashFunction() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

//  private final HashFunction hashFunction;
//
//  public StandardHashScheme(final String hashAlgorithmName) {
//    this(HashFunction.HashAlgorithm.getAlgorithmByName(hashAlgorithmName), concatScheme);
//  }
//
//  public StandardHashScheme(final HashFunction.HashAlgorithm hashAlgorithm, final ConcatScheme concatScheme) {
//    this(hashAlgorithm, concatScheme, hashAlgorithm.getCoDomain());
//  }
//
//  public StandardHashScheme(final String hashAlgorithmName, final ConcatScheme concatScheme, final ZPlusMod coDomain) {
//    this(HashFunction.HashAlgorithm.getAlgorithmByName(hashAlgorithmName), concatScheme, coDomain);
//  }
//
//  public StandardHashScheme(final HashFunction.HashAlgorithm hashAlgorithm, final ConcatScheme concatScheme, final ZPlusMod coDomain) {
//    if (concatScheme == null) {
//      throw new IllegalArgumentException();
//    }
//    this.hashFunction = new HashFunction(hashAlgorithm, coDomain);
//  }
}
