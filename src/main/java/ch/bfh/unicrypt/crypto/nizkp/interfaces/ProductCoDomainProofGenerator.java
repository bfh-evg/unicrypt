package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public interface ProductCoDomainProofGenerator extends ProofGenerator {

  public TupleElement generate(Element secretInput, List<Element> publicInputs);

  public TupleElement generate(Element secretInput, List<Element> publicInputs, Element otherInput);

  public TupleElement generate(Element secretInput, List<Element> publicInputs, Random random);

  public TupleElement generate(Element secretInput, List<Element> publicInputs, Element otherInput, Random random);

  public boolean verify(TupleElement proof, List<Element> publicInputs);

  public boolean verify(TupleElement proof, List<Element> publicInputs, Element otherInput);

  @Override
  public ProductGroup getCoDomain();

}
