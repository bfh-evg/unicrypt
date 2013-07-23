package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;

public interface ProductProofGenerator extends ProductDomainProofGenerator, ProductCoDomainProofGenerator {

  public TupleElement generate(List<Element> secretInputs, List<Element> publicInputs);

  public TupleElement generate(List<Element> secretInputs, List<Element> publicInputs, Element otherInput);

  public TupleElement generate(List<Element> secretInputs, List<Element> publicInputs, Random random);

  public TupleElement generate(List<Element> secretInputs, List<Element> publicInputs, Element otherInput, Random random);

}
