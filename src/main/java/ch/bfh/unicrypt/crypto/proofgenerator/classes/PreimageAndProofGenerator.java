package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class PreimageAndProofGenerator
       extends AbstractPreimageProofGenerator<ProductSemiGroup, ProductSemiGroup, ProductFunction, Tuple, Tuple> {

  protected PreimageAndProofGenerator(final ProductFunction proofFunction, String hashAlgorithm) {
    super(proofFunction, hashAlgorithm);
  }

  public static PreimageAndProofGenerator getInstance(final Function... proofFunctions) {
    return PreimageAndProofGenerator.getInstance(proofFunctions, "STANDARDHASH"); // TODO STANDARDHASH
  }

  public static PreimageAndProofGenerator getInstance(final Function[] proofFunctions, final String hashAlgorithm) {
    return new PreimageAndProofGenerator(ProductFunction.getInstance(proofFunctions), hashAlgorithm);
  } // TODO TEST HASHALGORITGHM

  public static PreimageAndProofGenerator getInstance(final Function proofFunction, int arity) {
    return PreimageAndProofGenerator.getInstance(proofFunction, arity, "STANDARDHASH"); // TODO STANDARDHASH
  }

  public static PreimageAndProofGenerator getInstance(final Function proofFunction, int arity, final String hashAlgorithm) {
    return new PreimageAndProofGenerator(ProductFunction.getInstance(proofFunction, arity), hashAlgorithm);
  } // TODO TEST HASHALGORITGHM

  public Function[] getProofFunctions() {
    return this.getProofFunction().getAll();
  }

}
