package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.ProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractProofGenerator<E extends Element, PR extends Set, PU extends Set, PS extends Set, F extends Function> implements ProofGenerator {

  private F proofFunction;
  private Set otherInputSpace;
  private PS proofSpace;

  public AbstractProofGenerator(F proofFunction, Set otherInputSpace, PS proofSpace) {
    this.proofFunction = proofFunction;
    this.otherInputSpace = otherInputSpace;
    this.proofSpace = proofSpace;
  }

  @Override
  public E generate(final Element privateInput, final Element publicInput) {
    return this.generate(privateInput, publicInput, (Random) null);
  }

  @Override
  public E generate(final Element privateInput, final Element publicInput, final Element otherInput) {
    return this.generate(privateInput, publicInput, otherInput, (Random) null);
  }

  @Override
  public E generate(final Element privateInput, final Element publicInput, final Random random) {
    return this.generate(privateInput, publicInput, (Element) null, random);
  }

  @Override
  public E generate(Element privateInput, Element publicInput, Element otherInput, Random random) {
    if (privateInput == null || !this.getPrivateInputSpace().contains(privateInput)
            || publicInput == null || !this.getPublicInputSpace().contains(publicInput)
            || (otherInput != null && !this.getOtherInputSpace().contains(otherInput))) {
      throw new IllegalArgumentException();
    }
    return this.abstractGenerate(privateInput, publicInput, otherInput, random);
  }

  @Override
  public BooleanElement verify(Element proof, Element publicInput) {
    return this.verify(proof, publicInput, (Element) null);
  }

  @Override
  public BooleanElement verify(Element proof, Element publicInput, Element otherInput) {
    if (proof == null || !this.getProofSpace().contains(proof)
            || publicInput == null || !this.getPublicInputSpace().contains(publicInput)
            || (otherInput != null && !this.getOtherInputSpace().contains(otherInput))) {
      throw new IllegalArgumentException();
    }
    return this.abstractVerify(proof, publicInput, otherInput);
  }

  @Override
  public F getProofFunction() {
    return this.proofFunction;
  }

  @Override
  public PR getPrivateInputSpace() {
    return (PR) this.getProofFunction().getDomain();
  }

  @Override
  public PU getPublicInputSpace() {
    return (PU) this.getProofFunction().getCoDomain();
  }

  @Override
  public Set getOtherInputSpace() {
    return this.otherInputSpace;
  }

  @Override
  public PS getProofSpace() {
    return this.proofSpace;
  }

  protected abstract E abstractGenerate(Element secretInput, Element publicInput, Element otherInput, Random random);

  protected abstract BooleanElement abstractVerify(Element proof, Element publicInput, Element otherInput);

}
