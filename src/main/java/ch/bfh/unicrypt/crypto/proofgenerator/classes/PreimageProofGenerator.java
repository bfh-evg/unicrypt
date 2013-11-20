package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class PreimageProofGenerator
       extends AbstractPreimageProofGenerator<SemiGroup, SemiGroup, Function, Element, Element> {

  protected PreimageProofGenerator(final Function function, String hashAlgorithm) {
    super(function, hashAlgorithm);
  }

  public static PreimageProofGenerator getInstance(Function function) {
    return PreimageProofGenerator.getInstance(function, "STANDARDHASH"); // TODO STANDARDHASH
  }

  public static PreimageProofGenerator getInstance(Function function, String hashAlgorithm) {
    if (function == null) {
      throw new IllegalArgumentException();
    }
    return new PreimageProofGenerator(function, hashAlgorithm); // TODO TEST HASHALGORITGHM
  }

}
