package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.interfaces.Function;

// TODO Define and Implement!
public abstract class AbstractSetMembershipProofGenerator<PRS extends SemiGroup, PUS extends SemiGroup, F extends Function, PUE extends Element, PRE extends Element>
	   extends AbstractProofGenerator<PRS, PUS, ProductSet, Tuple> {
}
