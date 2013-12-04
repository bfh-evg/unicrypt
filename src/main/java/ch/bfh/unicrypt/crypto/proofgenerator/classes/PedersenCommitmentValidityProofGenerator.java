package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractTCSSetMembershipProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class PedersenCommitmentValidityProofGenerator
	   extends AbstractTCSSetMembershipProofGenerator<CyclicGroup, Element> {

	private final PedersenCommitmentScheme pedersenCS;

	protected PedersenCommitmentValidityProofGenerator(PedersenCommitmentScheme pedersenCS, Element[] messages, HashMethod hashMethod) {
		super(messages, hashMethod);
		this.pedersenCS = pedersenCS;
	}

	public static PedersenCommitmentValidityProofGenerator getInstance(PedersenCommitmentScheme pedersenCS, Element[] messages) {
		return PedersenCommitmentValidityProofGenerator.getInstance(pedersenCS, messages, HashMethod.DEFAULT);
	}

	public static PedersenCommitmentValidityProofGenerator getInstance(PedersenCommitmentScheme pedersenCS, Element[] messages, HashMethod hashMethod) {
		if (pedersenCS == null || messages == null || messages.length < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}

		return new PedersenCommitmentValidityProofGenerator(pedersenCS, messages, hashMethod);
	}

	@Override
	protected Function abstractGetSetMembershipFunction() {
		return this.pedersenCS.getCommitmentFunction();
	}

	@Override
	protected Function abstractGetDeltaFunction() {
		ProductSet deltaFunctionDomain = ProductSet.getInstance(this.pedersenCS.getMessageSpace(), this.getSetMembershipProofFunction().getCoDomain());
		Function deltaFunction =
			   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
											 ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1),
																		 CompositeFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 0),
																									   GeneratorFunction.getInstance(this.pedersenCS.getMessageGenerator()),
																									   InvertFunction.getInstance(this.pedersenCS.getCyclicGroup()))),
											 ApplyFunction.getInstance(this.pedersenCS.getCyclicGroup()));
		return deltaFunction;
	}

}
