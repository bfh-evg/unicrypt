package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.Tuple;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public interface ProductDomainProofGenerator extends ProofGenerator {

  public Tuple generate(List<Element> secretInputs, Element publicInput);

  public Tuple generate(List<Element> secretInputs, Element publicInput, Element otherInput);

  public Tuple generate(List<Element> secretInputs, Element publicInput, Random random);

  public Tuple generate(List<Element> secretInputs, Element publicInput, Element otherInput, Random random);

  @Override
  public ProductGroup getDomain();

}
