package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class PreimageAndProofGenerator
       extends AbstractPreimageProofGenerator<ProductSemiGroup, ProductSemiGroup, ProductFunction, Tuple, Tuple> {

  protected PreimageAndProofGenerator(final ProductFunction proofFunction, HashMethod hashMethod) {
    super(proofFunction, hashMethod);
  }

  public static PreimageAndProofGenerator getInstance(final Function... proofFunctions) {
    return PreimageAndProofGenerator.getInstance(proofFunctions, HashMethod.DEFAULT);
  }

  public static PreimageAndProofGenerator getInstance(final Function[] proofFunctions, final HashMethod hashMethod) {
    return new PreimageAndProofGenerator(ProductFunction.getInstance(proofFunctions), hashMethod);
  }

  public static PreimageAndProofGenerator getInstance(final Function proofFunction, int arity) {
    return PreimageAndProofGenerator.getInstance(proofFunction, arity, HashMethod.DEFAULT);
  }

  public static PreimageAndProofGenerator getInstance(final Function proofFunction, int arity, final HashMethod hashMethod) {
    if (hashMethod == null) {
      throw new IllegalArgumentException();
    }
    return new PreimageAndProofGenerator(ProductFunction.getInstance(proofFunction, arity), hashMethod);
  }

  public Function[] getProofFunctions() {
    return this.getProofFunction().getAll();
  }

}
