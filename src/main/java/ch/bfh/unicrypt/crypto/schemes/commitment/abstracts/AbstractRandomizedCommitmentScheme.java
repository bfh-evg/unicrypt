package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.RandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.AdapterFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractRandomizedCommitmentScheme<MS extends Set, CS extends Set, CE extends Element, RS extends Set>
       extends AbstractCommitmentScheme<MS, CS>
       implements RandomizedCommitmentScheme {

  @Override
  public final MS getMessageSpace() {
    return (MS) ((ProductSet) this.getCommitmentFunction().getDomain()).getAt(0);
  }

  @Override
  public final RS getRandomizationSpace() {
    return (RS) ((ProductSet) this.getCommitmentFunction().getDomain()).getAt(1);
  }

  @Override
  public final CE commit(final Element message, final Element randomization) {
    return (CE) this.getCommitmentFunction().apply(message, randomization);
  }

  @Override
  public final BooleanElement decommit(final Element message, final Element randomization, final Element commitment) {
    return (BooleanElement) this.getDecommitmentFunction().apply(message, randomization, commitment);
  }

  @Override
  protected Function abstractGetDecommitmentFunction() {
    ProductSet decommitmentDomain = ProductSet.getInstance(this.getMessageSpace(), this.getRandomizationSpace(), this.getCommitmentSpace());
    return CompositeFunction.getInstance(
           MultiIdentityFunction.getInstance(decommitmentDomain, 2),
           ProductFunction.getInstance(CompositeFunction.getInstance(AdapterFunction.getInstance(decommitmentDomain, 0, 1),
                                                                     this.getCommitmentFunction()),
                                       SelectionFunction.getInstance(decommitmentDomain, 2)),
           EqualityFunction.getInstance(this.getCommitmentSpace()));
  }

}
