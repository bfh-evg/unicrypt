package ch.bfh.unicrypt.crypto.schemes.hash.classes;

import ch.bfh.unicrypt.crypto.schemes.hash.interfaces.HashScheme;
import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.HashFunction;

public class StandardHashScheme implements HashScheme {

  private final HashFunction hashFunction;

  public StandardHashScheme(final String hashAlgorithmName) {
    this(HashFunction.HashAlgorithm.getAlgorithmByName(hashAlgorithmName), concatScheme);
  }

  public StandardHashScheme(final HashFunction.HashAlgorithm hashAlgorithm, final ConcatScheme concatScheme) {
    this(hashAlgorithm, concatScheme, hashAlgorithm.getCoDomain());
  }

  public StandardHashScheme(final String hashAlgorithmName, final ConcatScheme concatScheme, final ZPlusMod coDomain) {
    this(HashFunction.HashAlgorithm.getAlgorithmByName(hashAlgorithmName), concatScheme, coDomain);
  }

  public StandardHashScheme(final HashFunction.HashAlgorithm hashAlgorithm, final ConcatScheme concatScheme, final ZPlusMod coDomain) {
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
