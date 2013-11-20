package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.ProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

public abstract class AbstractProofGenerator<E extends Element, PRS extends Set, PUS extends Set, OS extends Set, PS extends Set>
       implements ProofGenerator {

  @Override
  public final E generate(final Element privateInput, final Element publicInput) {
    return this.generate(privateInput, publicInput, (Random) null);
  }

  @Override
  public final E generate(final Element privateInput, final Element publicInput, final Element otherInput) {
    return this.generate(privateInput, publicInput, otherInput, (Random) null);
  }

  @Override
  public final E generate(final Element privateInput, final Element publicInput, final Random random) {
    return this.generate(privateInput, publicInput, (Element) null, random);
  }

  @Override
  public final E generate(Element privateInput, Element publicInput, Element otherInput, Random random) {
    if (privateInput == null || !this.getPrivateInputSpace().contains(privateInput)
           || publicInput == null || !this.getPublicInputSpace().contains(publicInput)
           || (otherInput != null && !this.getOtherInputSpace().contains(otherInput))) {
      throw new IllegalArgumentException();
    }
    return this.abstractGenerate(privateInput, publicInput, otherInput, random);
  }

  @Override
  public final BooleanElement verify(Element proof, Element publicInput) {
    return this.verify(proof, publicInput, (Element) null);
  }

  @Override
  public final BooleanElement verify(Element proof, Element publicInput, Element otherInput) {
    if (proof == null || !this.getProofSpace().contains(proof)
           || publicInput == null || !this.getPublicInputSpace().contains(publicInput)
           || (otherInput != null && !this.getOtherInputSpace().contains(otherInput))) {
      throw new IllegalArgumentException();
    }
    return this.abstractVerify(proof, publicInput, otherInput);
  }

  @Override
  public final PRS getPrivateInputSpace() {
    return this.abstractGetPrivateInputSpace();
  }

  @Override
  public final PUS getPublicInputSpace() {
    return this.abstractGetPublicInputSpace();
  }

  @Override
  public final OS getOtherInputSpace() {
    return this.abstractGetOtherInputSpace();
  }

  @Override
  public final PS getProofSpace() {
    return this.abstractGetProofSpace();
  }

  protected abstract E abstractGenerate(Element secretInput, Element publicInput, Element otherInput, Random random);

  protected abstract BooleanElement abstractVerify(Element proof, Element publicInput, Element otherInput);

  protected abstract PRS abstractGetPrivateInputSpace();

  protected abstract PUS abstractGetPublicInputSpace();

  protected abstract OS abstractGetOtherInputSpace();

  protected abstract PS abstractGetProofSpace();

}
