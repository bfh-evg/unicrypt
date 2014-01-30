/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.crypto.schemes.signature.classes;

import ch.bfh.unicrypt.crypto.keygenerator.classes.SchnorrSignatureKeyPairGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.signature.abstracts.AbstractRandomizedSignatureScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.AdapterFunction;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.classes.ModuloFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.classes.SharedDomainFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class SchnorrSignatureScheme<MS extends Set, ME extends Element>
	   extends AbstractRandomizedSignatureScheme<MS, ME, ProductGroup, Pair, ZModPrime> {

	private final CyclicGroup cyclicGroup;
	private final Element generator;
	private final MS messageSpace;

	protected SchnorrSignatureScheme(MS messageSpace, CyclicGroup cyclicGroup, ME generator) {
		this.cyclicGroup = cyclicGroup;
		this.generator = generator;
		this.messageSpace = messageSpace;
	}

	@Override
	protected KeyPairGenerator abstractGetKeyPairGenerator() {
		return SchnorrSignatureKeyPairGenerator.getInstance(this.getGenerator());
	}

	@Override
	public Function abstractGetSignatureFunction() {
		ZMod z_q = cyclicGroup.getZModOrder();
		ProductSet domain = ProductSet.getInstance(z_q, this.messageSpace, z_q);    // (x,m,k)
		Function fPrivateKey = SelectionFunction.getInstance(domain, 0);
		Function fRandomization = SelectionFunction.getInstance(domain, 2);
		Function fMessage = SelectionFunction.getInstance(domain, 1);

		//f1=g^{randomization}
		Function f1 = CompositeFunction.getInstance(fPrivateKey, GeneratorFunction.getInstance(this.generator));

		//f2=h(message||f1)
		Function f2 = HashFunction.getInstance(ProductSet.getInstance(this.messageSpace, f1.getCoDomain()));

		ProductSet f3Domain = ProductSet.getInstance(z_q, 3);

		//f3=randomization + f2*fPrivateKey
		Function f3 = CompositeFunction.getInstance(SharedDomainFunction.getInstance(SelectionFunction.getInstance(f3Domain, 0),
																					 CompositeFunction.getInstance(AdapterFunction.getInstance(f3Domain, 1, 2),
																												   SelfApplyFunction.getInstance((SemiGroup) z_q))),
													ApplyFunction.getInstance(z_q));

		//f()=fRandomization + privateKey*f2
		Function f = CompositeFunction.getInstance(SharedDomainFunction.getInstance(fRandomization, fPrivateKey,
																					CompositeFunction.getInstance(SharedDomainFunction.getInstance(fMessage, f1),
																												  CompositeFunction.getInstance(f2, ModuloFunction.getInstance(f2.getCoDomain(), z_q)))),
												   SharedDomainFunction.getInstance(SelectionFunction.getInstance(f3Domain, 2), f3));

		return f;
		//Not yet finished... Must return Tuple (e,s) not only s
	}

	@Override
	public Function abstractGetVerificationFunction() {
		//r_=g^s * y^{-e}
		//e_=H(m||r_)
		//E_ ?=? E
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public final CyclicGroup getCyclicGroup() {
		return this.cyclicGroup;
	}

	public final Element getGenerator() {
		return this.generator;
	}
//import java.util.Random;
//
//import ch.bfh.unicrypt.crypto.keygenerator.old.DDHGroupKeyPairGeneratorClass;
//import ch.bfh.unicrypt.crypto.schemes.signature.abstracts.AbstractRandomizedSignatureScheme;
//import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
//import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
//import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
//import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;
//import ch.bfh.unicrypt.math.function.classes.HashFunction;
//import ch.bfh.unicrypt.math.function.interfaces.Function;
//
//public class SchnorrSignatureScheme extends AbstractRandomizedSignatureScheme {
//
//  public static final HashFunction.HashAlgorithm DEFAULT_HASH_ALGORITHM = HashFunction.HashAlgorithm.SHA256;
//  public static final ConcatenateFunction.ConcatParameter DEFAULT_CONCAT_ALGORITHM = ConcatParameter.Plain;
//  public static final Mapper DEFAULT_MAPPER = new CharsetXRadixYMapperClass(CharsetXRadixYMapperClass.DEFAULT_CHARSET, CharsetXRadixYMapperClass.DEFAULT_RADIX);
//  private final ZPlusMod zPlusMod;
//  private final DDHGroup ddhGroup;
//  private final AtomicElement generator; // of ddhGroup
//
//  private final DDHGroupKeyPairGenerator keyGenerator;
//  private final Function signatureFunction;
//  private final Function verificationFunction;
//  private final HashFunction hashFunction;
//  private final ConcatenateFunction concatFunction;
//
//  public SchnorrSignatureScheme(final DDHGroup ddhGroup) {
//    this(ddhGroup, SchnorrSignatureScheme.DEFAULT_HASH_ALGORITHM, SchnorrSignatureScheme.DEFAULT_CONCAT_ALGORITHM, SchnorrSignatureScheme.DEFAULT_MAPPER);
//  }
//
//  public SchnorrSignatureScheme(final DDHGroup ddhGroup, final AtomicElement generator) {
//    this(ddhGroup,generator, SchnorrSignatureScheme.DEFAULT_HASH_ALGORITHM, SchnorrSignatureScheme.DEFAULT_CONCAT_ALGORITHM, SchnorrSignatureScheme.DEFAULT_MAPPER);
//  }
//
//  public SchnorrSignatureScheme(final DDHGroup ddhGroup, final HashAlgorithm hashAlgorithm, final ConcatParameter concatParameter, final Mapper mapper) {
//    this(ddhGroup, ddhGroup.getDefaultGenerator(), hashAlgorithm, concatParameter, mapper);
//  }
//
//  public SchnorrSignatureScheme(final DDHGroup ddhGroup,
//      final AtomicElement generator,
//      final HashAlgorithm hashAlgorithm,
//      final ConcatParameter concatParameter,
//      final Mapper mapper) {
//    if ((ddhGroup == null) || (generator == null) || !ddhGroup.contains(generator)) {
//      throw new IllegalArgumentException();
//    }
//    this.zPlusMod = ddhGroup.getZModOrder();
//    this.ddhGroup = ddhGroup;
//    this.generator = generator;
//
//    this.hashFunction = new HashFunction(hashAlgorithm, this.zPlusMod);
//    final ProductGroup concatGroup = new ProductGroup(this.getMessageSpace(), this.ddhGroup);
//    this.concatFunction = new ConcatenateFunction(concatGroup, concatParameter, mapper);
//    this.keyGenerator = new DDHGroupKeyPairGeneratorClass(ddhGroup, generator);
//    this.signatureFunction = this.createSignatureFunction();
//    this.verificationFunction = this.createVerificationFunction();
//  }
//
//  @Override
//  public Tuple sign(final Element privateKey, final Element message, final Element randomization) {
//    return (Tuple) super.sign(privateKey, message, randomization);
//  }
//
//  @Override
//  public DDHGroupKeyPairGenerator getKeyPairGenerator() {
//    return this.keyGenerator;
//  }
//
//  @Override
//  public Function getSignatureFunction() {
//    return this.signatureFunction;
//  }
//
//  @Override
//  public Function getVerificationFunction() {
//    return this.verificationFunction;
//  }
//
//  @Override
//  public HashFunction getHashFunction() {
//    return this.hashFunction;
//  }
//
//  @Override
//  public ProductGroup getSignatureSpace() {
//    return (ProductGroup) super.getSignatureSpace();
//  }
//
//  @Override
//  public ZPlusMod getRandomizationSpace() {
//    return this.zPlusMod;
//  }
//
//  private Function createSignatureFunction() {
//    return new SignatureFunctionClass(this.zPlusMod, this.generator, this.hashFunction, this.concatFunction);
//  }
//
//  // private Function createSignatureFunctionLeft(final ProductGroup domain) {
//  // return new CompositeFunctionClass(
//  // new MultiIdentityFunctionClass(domain, 2),
//  // new ProductFunctionClass(
//  // new SelectiveIdentityFunctionClass(1),
//  // new CompositeFunctionClass(
//  // new SelectiveIdentityFunctionClass(2),
//  // new PartiallyAppliedFunctionClass(new ApplyFunctionClass(), this.generator,
//  // 0)
//  //
//  // ;
//  // }
//
//  private Function createVerificationFunction() {
//    return new VerificationFunctionClass(this.zPlusMod, this.ddhGroup, this.generator, this.hashFunction, this.concatFunction);
//  }
//
//  private static class SignatureFunctionClass extends ProductDomainFunctionAbstract {
//
//    private final Element generator;
//    private final HashFunction hashFunction;
//    private final ConcatenateFunction concatFunction;
//
//    public SignatureFunctionClass(final ZPlusMod zPlusMod, final Element generator, final HashFunction hashFunction, final ConcatenateFunction concatFunction) {
//      super(SignatureFunctionClass.createDomain(zPlusMod), SignatureFunctionClass.createCoDomain(zPlusMod));
//      this.generator = generator;
//      this.hashFunction = hashFunction;
//      this.concatFunction = concatFunction;
//    }
//
//    @Override
//    public ProductGroup getCoDomain() {
//      return (ProductGroup) super.getCoDomain();
//    }
//
//    @Override
//    public Element apply(final Element element, final Random random) {
//      final Tuple tuple = (Tuple) element;
//      final AtomicElement privateKey = (AtomicElement) tuple.getElementAt(0);
//      final Element message = tuple.getElementAt(1);
//      final AtomicElement randomization = (AtomicElement) tuple.getElementAt(2);
//      final AtomicElement concatElement = this.concatFunction.apply(message, this.generator.selfApply(randomization));
//      final Element left = this.hashFunction.apply(concatElement);
//      final Element right = randomization.apply(left.selfApply(privateKey).invert());
//      return this.getCoDomain().getElement(left, right);
//    }
//
//    private static ProductGroup createDomain(final ZPlusMod zPlusMod) {
//      return new ProductGroup(zPlusMod, ZPlus.getInstance(), zPlusMod);
//    }
//
//    private static PowerGroup createCoDomain(final ZPlusMod zPlusMod) {
//      return new PowerGroup(zPlusMod, 2);
//    }
//
//  }
//
//  private static class VerificationFunctionClass extends ProductDomainFunctionAbstract {
//
//    private final Element generator;
//    private final HashFunction hashFunction;
//    private final ConcatenateFunction concatFunction;
//
//    public VerificationFunctionClass(final ZPlusMod zPlusMod,
//        final DDHGroup ddhGroup,
//        final Element generator,
//        final HashFunction hashFunction,
//        final ConcatenateFunction concatFunciton) {
//      super(VerificationFunctionClass.createDomain(zPlusMod, ddhGroup), BooleanGroup.getInstance());
//      this.generator = generator;
//      this.hashFunction = hashFunction;
//      this.concatFunction = concatFunciton;
//    }
//
//    @Override
//    public BooleanGroup getCoDomain() {
//      return (BooleanGroup) super.getCoDomain();
//    }
//
//    @Override
//    public Element apply(final Element element, final Random random) {
//      final Tuple tuple = (Tuple) element;
//      final Element publicKey = tuple.getElementAt(0);
//      final Element message = tuple.getElementAt(1);
//      final AtomicElement left = (AtomicElement) tuple.getElementAt(2, 0);
//      final AtomicElement right = (AtomicElement) tuple.getElementAt(2, 1);
//      final AtomicElement concatElement = this.concatFunction.apply(message, this.generator.selfApply(right).apply(publicKey.selfApply(left)));
//      final Element result = this.hashFunction.apply(concatElement);
//      return this.getCoDomain().getElement(left.equals(result));
//    }
//
//    private static ProductGroup createDomain(final ZPlusMod zPlusMod, final DDHGroup ddhGroup) {
//      return new ProductGroup(ddhGroup, ZPlus.getInstance(), new PowerGroup(zPlusMod, 2));
//    }
//
//  }

}
