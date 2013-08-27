package ch.bfh.unicrypt.crypto.hash.classes;

import ch.bfh.unicrypt.crypto.hash.interfaces.HashScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.cyclicgroup.classes.ZPlusMod;

public class HashSchemeClass implements HashScheme {

  private final HashFunction hashFunction;

  public HashSchemeClass(final String hashAlgorithmName) {
    this(HashFunction.HashAlgorithm.getAlgorithmByName(hashAlgorithmName), concatScheme);
  }
  
  public HashSchemeClass(final HashFunction.HashAlgorithm hashAlgorithm, final ConcatScheme concatScheme) {
    this(hashAlgorithm, concatScheme, hashAlgorithm.getCoDomain());
  }
  public HashSchemeClass(final String hashAlgorithmName, final ConcatScheme concatScheme, final ZPlusMod coDomain) {
    this(HashFunction.HashAlgorithm.getAlgorithmByName(hashAlgorithmName), concatScheme, coDomain);
  }

  public HashSchemeClass(final HashFunction.HashAlgorithm hashAlgorithm, final ConcatScheme concatScheme, final ZPlusMod coDomain) {
    if (concatScheme == null) {
      throw new IllegalArgumentException();
    }
    this.hashFunction = new HashFunction(hashAlgorithm, coDomain);
  }

  @Override
  public Element hash(final Element... elements) {
    return this.hashFunction.apply(Element.Factory.getInstance(elements));
  }

  @Override
  public HashFunction getHashFunction() {
    return this.hashFunction;
  }

}
