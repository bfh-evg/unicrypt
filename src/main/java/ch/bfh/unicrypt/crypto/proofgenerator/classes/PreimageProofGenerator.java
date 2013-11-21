package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class PreimageProofGenerator
       extends AbstractPreimageProofGenerator<SemiGroup, SemiGroup, Function, Element, Element> {

  protected PreimageProofGenerator(final Function function, HashMethod hashMethod) {
    super(function, hashMethod);
  }

  public static PreimageProofGenerator getInstance(Function proofFunction) {
    return PreimageProofGenerator.getInstance(proofFunction, HashMethod.DEFAULT);
  }

  public static PreimageProofGenerator getInstance(Function proofFunction, HashMethod hashMethod) {
    if (proofFunction == null) {
      throw new IllegalArgumentException();
    }
    return new PreimageProofGenerator(proofFunction, hashMethod);
  }

}
