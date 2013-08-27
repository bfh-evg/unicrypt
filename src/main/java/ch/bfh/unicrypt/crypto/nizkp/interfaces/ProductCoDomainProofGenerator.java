package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.Tuple;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public interface ProductCoDomainProofGenerator extends ProofGenerator {

  public Tuple generate(Element secretInput, List<Element> publicInputs);

  public Tuple generate(Element secretInput, List<Element> publicInputs, Element otherInput);

  public Tuple generate(Element secretInput, List<Element> publicInputs, Random random);

  public Tuple generate(Element secretInput, List<Element> publicInputs, Element otherInput, Random random);

  public boolean verify(Tuple proof, List<Element> publicInputs);

  public boolean verify(Tuple proof, List<Element> publicInputs, Element otherInput);

  @Override
  public ProductGroup getCoDomain();

}
