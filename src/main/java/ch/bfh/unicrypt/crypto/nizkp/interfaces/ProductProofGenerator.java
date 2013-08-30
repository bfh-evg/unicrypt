package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.product.interfaces.Tuple;

public interface ProductProofGenerator extends ProductDomainProofGenerator, ProductCoDomainProofGenerator {

  public Tuple generate(List<Element> secretInputs, List<Element> publicInputs);

  public Tuple generate(List<Element> secretInputs, List<Element> publicInputs, Element otherInput);

  public Tuple generate(List<Element> secretInputs, List<Element> publicInputs, Random random);

  public Tuple generate(List<Element> secretInputs, List<Element> publicInputs, Element otherInput, Random random);

}
