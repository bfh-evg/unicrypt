package ch.bfh.unicrypt.crypto.nizkp.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.crypto.nizkp.interfaces.ProofGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.interfaces.Group;

public abstract class ProofGeneratorAbstract implements ProofGenerator {

  @Override
  public TupleElement generate(final Element secretInput, final Element publicInput) {
    return this.generate(secretInput, publicInput, (Random) null);
  }

  @Override
  public TupleElement generate(final Element secretInput, final Element publicInput, final Element otherInput) {
    return this.generate(secretInput, publicInput, otherInput, (Random) null);
  }

  @Override
  public TupleElement generate(final Element secretInput, final Element publicInput, final Random random) {
    return this.generate(secretInput, publicInput, (Element) null, random);
  }

  @Override
  public boolean verify(final TupleElement proof, final Element publicInput) {
    return this.verify(proof, publicInput, (Element) null);
  }

  @Override
  public Group getDomain() {
    return this.getProofFunction().getDomain();
  }

  @Override
  public Group getCoDomain() {
    return this.getProofFunction().getCoDomain();
  }

}
