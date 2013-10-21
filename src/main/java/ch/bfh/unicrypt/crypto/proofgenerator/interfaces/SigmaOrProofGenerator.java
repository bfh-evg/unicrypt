package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SigmaOrProofGenerator extends ProductDomainProofGenerator, ProductCoDomainProofGenerator {

	public Function[] getFunctions();

	@Override
	public ProductFunction getProofFunction();
}
