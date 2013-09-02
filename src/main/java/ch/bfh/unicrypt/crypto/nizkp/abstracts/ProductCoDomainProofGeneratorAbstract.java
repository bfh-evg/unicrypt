package ch.bfh.unicrypt.crypto.nizkp.abstracts;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.nizkp.interfaces.ProductCoDomainProofGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.product.classes.Tuple;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public abstract class ProductCoDomainProofGeneratorAbstract extends ProofGeneratorAbstract implements ProductCoDomainProofGenerator {

  @Override
  public Tuple generate(final Element secretInput, final List<Element> publicInputs) {
    return this.generate(secretInput, this.getCoDomain().getElement(publicInputs));
  }

  @Override
  public Tuple generate(final Element secretInput, final List<Element> publicInputs, final Element otherInput) {
    return this.generate(secretInput, this.getCoDomain().getElement(publicInputs), otherInput);
  }

  @Override
  public Tuple generate(final Element secretInput, final List<Element> publicInputs, final Random random) {
    return this.generate(secretInput, this.getCoDomain().getElement(publicInputs), random);
  }

  @Override
  public Tuple generate(final Element secretInput, final List<Element> publicInputs, final Element otherInput, final Random random) {
    return this.generate(secretInput, this.getCoDomain().getElement(publicInputs), otherInput, random);
  }

  @Override
  public boolean verify(final Tuple proof, final List<Element> publicInputs) {
    return this.verify(proof, this.getCoDomain().getElement(publicInputs));
  }

  @Override
  public boolean verify(final Tuple proof, final List<Element> publicInputs, final Element otherInput) {
    return this.verify(proof, this.getCoDomain().getElement(publicInputs), otherInput);
  }

  @Override
  public ProductGroup getCoDomain() {
    return (ProductGroup) super.getCoDomain();
  }

}
