package ch.bfh.unicrypt.crypto.schemes.commitment.abstracts;

import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.DeterministicCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractDeterministicCommitmentScheme<MS extends Set, ME extends Element, CS extends Set, CE extends Element>
			 extends AbstractCommitmentScheme<MS, CS>
			 implements DeterministicCommitmentScheme {

	@Override
	public final CE commit(final Element message) {
		return (CE) this.getCommitmentFunction().apply(message);
	}

	@Override
	public final BooleanElement decommit(final Element message, final Element commitment) {
		return (BooleanElement) this.getDecommitmentFunction().apply(message, commitment);
	}

	@Override
	protected final MS abstractGetMessageSpace() {
		return (MS) this.getCommitmentFunction().getDomain();
	}

	@Override
	protected Function abstractGetDecommitmentFunction() {
		ProductSet decommitmentDomain = ProductSet.getInstance(this.getMessageSpace(), this.getCommitmentSpace());
		return CompositeFunction.getInstance(
					 MultiIdentityFunction.getInstance(decommitmentDomain, 2),
					 ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(decommitmentDomain, 0),
																																		 this.getCommitmentFunction()),
																			 SelectionFunction.getInstance(decommitmentDomain, 1)),
					 EqualityFunction.getInstance(this.getCommitmentSpace()));
	}

}
