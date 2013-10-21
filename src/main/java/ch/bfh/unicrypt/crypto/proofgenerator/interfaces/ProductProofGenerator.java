package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface ProductProofGenerator extends ProductDomainProofGenerator, ProductCoDomainProofGenerator {

  public Tuple generate(List<Element> secretInputs, List<Element> publicInputs);

  public Tuple generate(List<Element> secretInputs, List<Element> publicInputs, Element otherInput);

  public Tuple generate(List<Element> secretInputs, List<Element> publicInputs, Random random);

  public Tuple generate(List<Element> secretInputs, List<Element> publicInputs, Element otherInput, Random random);

}
