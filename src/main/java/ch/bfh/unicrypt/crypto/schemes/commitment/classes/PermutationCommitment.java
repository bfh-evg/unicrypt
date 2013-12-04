package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.interfaces.Function;

// RS: ProductZModPrime does not exist, so ProductGroup
public class PermutationCommitment
	   extends AbstractRandomizedCommitmentScheme<PermutationGroup, PermutationElement, ProductGroup, Tuple, ProductGroup> {

	@Override
	protected Function abstractGetCommitmentFunction() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
