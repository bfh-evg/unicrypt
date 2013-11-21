package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public abstract class AbstractPreimageProofGenerator<PRS extends SemiGroup, PUS extends SemiGroup, F extends Function, PUE extends Element, PRE extends Element>
       extends AbstractProofGenerator<PRS, PUS, ProductSet, Tuple> {

  private final F proofFunction;
  private final HashMethod hashMethod;

  protected AbstractPreimageProofGenerator(F proofFunction, HashMethod hashMethod) {
    this.proofFunction = proofFunction;
    this.hashMethod = hashMethod;
  }

  public final F getProofFunction() {
    return this.proofFunction;
  }

  public final HashMethod getHashMethod() {
    return this.hashMethod;
  }

  public final PUS getCommitmentSpace() {
    return this.getPublicInputSpace();
  }

  public final ZMod getChallengeSpace() {
    return ZMod.getInstance(this.getPrivateInputSpace().getMinOrder());
  }

  public final PRS getResponseSpace() {
    return this.getPrivateInputSpace();
  }

  public final PUE getCommitment(final Tuple proof) {
    if (!this.getProofSpace().contains(proof)) {
      throw new IllegalArgumentException();
    }
    return (PUE) proof.getAt(0);
  }

  public final ZModElement getChallenge(final Tuple proof) {
    if (!this.getProofSpace().contains(proof)) {
      throw new IllegalArgumentException();
    }
    return (ZModElement) proof.getAt(1);
  }

  public final PRE getResponse(final Tuple proof) {
    if (!this.getProofSpace().contains(proof)) {
      throw new IllegalArgumentException();
    }
    return (PRE) proof.getAt(2);
  }

  @Override
  protected final PRS abstractGetPrivateInputSpace() {
    return (PRS) this.getProofFunction().getDomain();
  }

  @Override
  protected final PUS abstractGetPublicInputSpace() {
    return (PUS) this.getProofFunction().getCoDomain();
  }

  @Override
  protected final ProductSet abstractGetProofSpace() {
    return ProductSet.getInstance(this.getCommitmentSpace(), this.getChallengeSpace(), this.getResponseSpace());
  }

  @Override
  protected final Tuple abstractGenerate(Element secretInput, Element publicInput, Element otherInput, Random random) {
    // TODO
    return null;
  }

  @Override
  protected final BooleanElement abstractVerify(Element proof, Element publicInput, Element otherInput) {
    // TODO
    return null;
  }

// CODE COPIED FROM OLD VERSION FOR REUSE
//    @Override
//  public Tuple generate(final Element secretInput, final Element publicInput, final Element otherInput, final Random random) {
//    if ((secretInput == null) || !this.getResponseSpace().contains(secretInput) || (publicInput == null)
//           || !this.getCommitmentSpace().contains(publicInput)) {
//      throw new IllegalArgumentException();
//    }
//    final Element randomElement = this.getResponseSpace().getRandomElement(random);
//    final Element commitment = this.getProofFunction().apply(randomElement);
//    final AdditiveElement challenge = this.createChallenge(commitment, publicInput, otherInput);
//    final Element response = randomElement.apply(secretInput.selfApply(challenge));
//    return this.getProofSpace().getElement(commitment, response);
//  }
//
//  public AdditiveElement createChallenge(final Element commitment, final Element publicInput, final Element otherInput) {
//    AdditiveElement hashInput;
//    if (otherInput == null) {
//      final ProductGroup pg = new ProductGroup(publicInput.getSet(), commitment.getSet());
//      final ConcatenateFunction concatFunction = new ConcatenateFunction(pg, this.concatParameter, this.mapper);
//      hashInput = concatFunction.apply(publicInput, commitment);
//    } else {
//      final ProductGroup pg = new ProductGroup(publicInput.getSet(), commitment.getSet(), otherInput.getSet());
//      final ConcatenateFunction concatFunction = new ConcatenateFunction(pg, this.concatParameter, this.mapper);
//      hashInput = concatFunction.apply(publicInput, commitment, otherInput);
//    }
//    return this.getHashFunction().apply(hashInput);
//  }
//
//  @Override
//  public boolean verify(final Tuple proof, final Element publicInput, final Element otherInput) {
//    if ((proof == null) || !this.getProofSpace().contains(proof) || (publicInput == null) || !this.getCommitmentSpace().contains(publicInput)) {
//      throw new IllegalArgumentException();
//    }
//    AdditiveElement hashInput;
//    if (otherInput == null) {
//      final ProductGroup pg = new ProductGroup(publicInput.getSet(), this.getCommitment(proof).getGroup());
//      final ConcatenateFunction concatFunction = new ConcatenateFunction(pg, this.concatParameter, this.mapper);
//      hashInput = concatFunction.apply(publicInput, this.getCommitment(proof));
//    } else {
//      final ProductGroup pg = new ProductGroup(publicInput.getSet(), this.getCommitment(proof).getGroup(), otherInput.getSet());
//      final ConcatenateFunction concatFunction = new ConcatenateFunction(pg, this.concatParameter, this.mapper);
//      hashInput = concatFunction.apply(publicInput, this.getCommitment(proof), otherInput);
//    }
//    final AdditiveElement challenge = this.getHashFunction().apply(hashInput);
//    final Element left = this.getProofFunction().apply(this.getResponse(proof));
//    final Element right = this.getCommitment(proof).apply(publicInput.selfApply(challenge));
//    return left.isEqual(right);
//  }
}
