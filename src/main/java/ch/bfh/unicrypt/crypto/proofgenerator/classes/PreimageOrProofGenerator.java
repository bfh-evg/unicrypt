package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ModuloFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Arrays;
import java.util.Random;

public class PreimageOrProofGenerator
			 extends AbstractProofGenerator<ProductSet, Pair, ProductSet, Tuple, ProductSet, Triple> {

	private final Function[] proofFunctions;
	private final ProductFunction proofFunction;
	private final HashMethod hashMethod;

	protected PreimageOrProofGenerator(final Function[] functions, HashMethod hashMethod) {
		this.proofFunctions = functions;
		this.proofFunction = ProductFunction.getInstance(functions);
		this.hashMethod = hashMethod;
	}

	public static PreimageOrProofGenerator getInstance(Function[] proofFunctions) {
		return PreimageOrProofGenerator.getInstance(proofFunctions, HashMethod.DEFAULT);
	}

	public static PreimageOrProofGenerator getInstance(Function[] proofFunctions, HashMethod hashMethod) {
		if (proofFunctions == null || proofFunctions.length < 2 || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new PreimageOrProofGenerator(proofFunctions, hashMethod);
	}

	public static PreimageOrProofGenerator getInstance(final Function proofFunction, int arity) {
		return PreimageOrProofGenerator.getInstance(proofFunction, arity, HashMethod.DEFAULT);
	}

	public static PreimageOrProofGenerator getInstance(final Function proofFunction, int arity, final HashMethod hashMethod) {
		if (proofFunction == null || arity < 2 || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		Function[] functions = new Function[arity];
		Arrays.fill(functions, proofFunction);
		return new PreimageOrProofGenerator(ProductFunction.getInstance(proofFunction, arity).getAll(), hashMethod);
	}

	@Override
	protected ProductSet abstractGetPrivateInputSpace() {
		return ProductSet.getInstance(proofFunction.getDomain(), ZMod.getInstance(proofFunctions.length));
	}

	@Override
	protected ProductSet abstractGetPublicInputSpace() {
		return proofFunction.getCoDomain();
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(
					 this.getCommitmentSpace(),
					 ProductSet.getInstance(this.getChallengeSpace(), this.proofFunctions.length),
					 this.getResponseSpace());
	}

	public ProductSet getCommitmentSpace() {
		return this.proofFunction.getCoDomain();
	}

	public ProductSet getResponseSpace() {
		return this.proofFunction.getDomain();
	}

	public ZMod getChallengeSpace() {
		return ZMod.getInstance(this.proofFunction.getDomain().getMinimalOrder());
	}

	public final Tuple getCommitments(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (Tuple) proof.getFirst();
	}

	public final Tuple getChallenges(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (Tuple) proof.getSecond();
	}

	public final Tuple getResponses(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (Tuple) proof.getThird();
	}

	public Pair createPrivateInput(Element secret, int index) {
		if (index < 0 || index >= proofFunctions.length || !proofFunctions[index].getDomain().contains(secret)) {
			throw new IllegalArgumentException();
		}
		final Element[] domainElements = ((ProductMonoid) proofFunction.getDomain()).getIdentityElement().getAll();
		domainElements[index] = secret;

		return (Pair) this.getPrivateInputSpace().getElement(
					 proofFunction.getDomain().getElement(domainElements),
					 ZMod.getInstance(proofFunctions.length).getElement(index));
	}

	@Override
	protected Triple abstractGenerate(Pair privateInput, Tuple publicInput, Element proverId, Random random) {

		// Extract secret input value and index from private input
		final int index = privateInput.getSecond().getValue().intValue();
		final Element secret = ((Tuple) privateInput.getFirst()).getAt(index);

		// Create lists for proof elements (t, c, s)
		final Element[] commitments = new Element[proofFunctions.length];
		final Element[] challenges = new Element[proofFunctions.length];
		final Element[] responses = new Element[proofFunctions.length];

		// Get challenge space and initialze the summation of the challenges
		final ZMod challengeSpace = this.getChallengeSpace();
		ZModElement sumOfChallenges = challengeSpace.getIdentityElement();
		int z = challengeSpace.getOrder().intValue();
		// Create proof elements (simulate proof) for all but the known secret
		for (int i = 0; i < proofFunctions.length; i++) {
			if (i == index) {
				continue;
			}

			// Create random challenge and response
			ZModElement c = challengeSpace.getRandomElement(random);
			Function f = proofFunctions[i];
			Element s = f.getDomain().getRandomElement(random);

			sumOfChallenges = sumOfChallenges.add(c);
			challenges[i] = c;
			responses[i] = s;
			// Calculate commitment based on the the public value and the random challenge and response
			// t = f(s)/(y^c)
			commitments[i] = f.apply(s).apply(((Tuple) publicInput).getAt(i).selfApply(c).invert());
		}

		// Create the proof of the known secret (normal preimage-proof, but with a special challange)
		// - Create random element and calculate commitment
		final Element randomElement = proofFunctions[index].getDomain().getRandomElement(random);
		commitments[index] = proofFunctions[index].apply(randomElement);

		// - Create overall proof challenge
		final ZModElement challenge = this.createChallenge(Tuple.getInstance(commitments), publicInput, proverId);
		// - Calculate challenge based on the overall challenge and the chosen challenges for the simulated proofs
		challenges[index] = challenge.subtract(sumOfChallenges);
		// - finally compute response element
		responses[index] = randomElement.apply(secret.selfApply(challenges[index]));

		// Return proof
		return (Triple) this.getProofSpace().getElement(Tuple.getInstance(commitments),
																										Tuple.getInstance(challenges),
																										Tuple.getInstance(responses));

	}

	protected ZModElement createChallenge(final Element commitment, final Element publicInput, final Element proverId) {
		Tuple toHash = (proverId == null
					 ? Tuple.getInstance(publicInput, commitment)
					 : Tuple.getInstance(publicInput, commitment, proverId));

		FiniteByteArrayElement hashValue = toHash.getHashValue(hashMethod);
		System.out.println("toHash: " + toHash);
		System.out.println("Spaces: " + hashValue.getSet() + ", " + this.getChallengeSpace());
		return ModuloFunction.getInstance(hashValue.getSet(), this.getChallengeSpace()).apply(hashValue);
	}

	@Override
	protected BooleanElement abstractVerify(Triple proof, Tuple publicInput, Element proverId) {

		// Extract (t, c, s)
		final Tuple commitments = this.getCommitments(proof);
		final Tuple challenges = this.getChallenges(proof);
		final Tuple responses = this.getResponses(proof);

		// 1. Check whether challenges sum up to the overall challenge
		final ZModElement challenge = this.createChallenge(commitments, publicInput, proverId);
		ZModElement sumOfChallenges = this.getChallengeSpace().getIdentityElement();
		for (int i = 0; i < challenges.getArity(); i++) {
			sumOfChallenges = sumOfChallenges.add(challenges.getAt(i));
		}
		if (!challenge.isEqual(sumOfChallenges)) {
			return BooleanSet.FALSE;
		}

		// 2. Verify all subproofs
		for (int i = 0; i < proofFunctions.length; i++) {
			Element a = proofFunctions[i].apply(responses.getAt(i));
			Element b = commitments.getAt(i).apply(publicInput.getAt(i).selfApply(challenges.getAt(i)));
			if (!a.isEqual(b)) {
				return BooleanSet.FALSE;
			}
		}

		// Proof is valid!
		return BooleanSet.TRUE;
	}

}
