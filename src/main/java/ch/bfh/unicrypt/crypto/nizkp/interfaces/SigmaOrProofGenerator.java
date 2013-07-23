package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.ProductFunction;

public interface SigmaOrProofGenerator extends ProductDomainProofGenerator, ProductCoDomainProofGenerator {

	public Function[] getFunctions();

	@Override
	public ProductFunction getProofFunction();
}
