package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public interface ProofGenerator {
  // For increased convenience, we assume that the ProofSpace is always a
  // ProductGroup
  // and that proofs are always TupleElements

  public TupleElement generate(Element secretInput, Element publicInput);

  public TupleElement generate(Element secretInput, Element publicInput, Element otherInput);

  public TupleElement generate(Element secretInput, Element publicInput, Random random);

  public TupleElement generate(Element secretInput, Element publicInput, Element otherInput, Random random);

  public boolean verify(TupleElement proof, Element publicInput);

  public boolean verify(TupleElement proof, Element publicInput, Element otherInput);

  public ProductGroup getProofSpace();

  public Function getProofFunction();

  public Group getDomain();

  public Group getCoDomain();

}
