package ch.bfh.unicrypt.crypto.signature.classes;

import java.util.Random;

import ch.bfh.unicrypt.crypto.keygen.classes.DDHGroupKeyPairGeneratorClass;
import ch.bfh.unicrypt.crypto.keygen.interfaces.DDHGroupKeyPairGenerator;
import ch.bfh.unicrypt.crypto.signature.abstracts.AbstractRandomizedSignatureScheme;
import ch.bfh.unicrypt.crypto.signature.interfaces.SchnorrSignatureScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.abstracts.ProductDomainFunctionAbstract;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.classes.HashFunction.HashAlgorithm;
import ch.bfh.unicrypt.math.function.interfaces.ConcatenateFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.HashFunction;
import ch.bfh.unicrypt.math.group.classes.BooleanGroup;
import ch.bfh.unicrypt.math.group.classes.PowerGroup;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.BooleanGroup;
import ch.bfh.unicrypt.math.group.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.group.interfaces.PowerGroup;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.utility.mapper.classes.CharsetXRadixYMapperClass;
import ch.bfh.unicrypt.math.utility.mapper.interfaces.Mapper;

public class SchnorrSignatureScheme extends AbstractRandomizedSignatureScheme {

  public static final HashFunction.HashAlgorithm DEFAULT_HASH_ALGORITHM = HashFunction.HashAlgorithm.SHA256;
  public static final ConcatenateFunction.ConcatParameter DEFAULT_CONCAT_ALGORITHM = ConcatParameter.Plain;
  public static final Mapper DEFAULT_MAPPER = new CharsetXRadixYMapperClass(CharsetXRadixYMapperClass.DEFAULT_CHARSET, CharsetXRadixYMapperClass.DEFAULT_RADIX);
  private final ZPlusMod zPlusMod;
  private final DDHGroup ddhGroup;
  private final AtomicElement generator; // of ddhGroup

  private final DDHGroupKeyPairGenerator keyGenerator;
  private final Function signatureFunction;
  private final Function verificationFunction;
  private final HashFunction hashFunction;
  private final ConcatenateFunction concatFunction;

  public SchnorrSignatureScheme(final DDHGroup ddhGroup) {
    this(ddhGroup, SchnorrSignatureScheme.DEFAULT_HASH_ALGORITHM, SchnorrSignatureScheme.DEFAULT_CONCAT_ALGORITHM, SchnorrSignatureScheme.DEFAULT_MAPPER);
  }

  public SchnorrSignatureScheme(final DDHGroup ddhGroup, final AtomicElement generator) {
    this(ddhGroup,generator, SchnorrSignatureScheme.DEFAULT_HASH_ALGORITHM, SchnorrSignatureScheme.DEFAULT_CONCAT_ALGORITHM, SchnorrSignatureScheme.DEFAULT_MAPPER);
  }

  public SchnorrSignatureScheme(final DDHGroup ddhGroup, final HashAlgorithm hashAlgorithm, final ConcatParameter concatParameter, final Mapper mapper) {
    this(ddhGroup, ddhGroup.getDefaultGenerator(), hashAlgorithm, concatParameter, mapper);
  }

  public SchnorrSignatureScheme(final DDHGroup ddhGroup,
      final AtomicElement generator,
      final HashAlgorithm hashAlgorithm,
      final ConcatParameter concatParameter,
      final Mapper mapper) {
    if ((ddhGroup == null) || (generator == null) || !ddhGroup.contains(generator)) {
      throw new IllegalArgumentException();
    }
    this.zPlusMod = ddhGroup.getZPlusModOrder();
    this.ddhGroup = ddhGroup;
    this.generator = generator;

    this.hashFunction = new HashFunction(hashAlgorithm, this.zPlusMod);
    final ProductGroup concatGroup = new ProductGroup(this.getMessageSpace(), this.ddhGroup);
    this.concatFunction = new ConcatenateFunction(concatGroup, concatParameter, mapper);
    this.keyGenerator = new DDHGroupKeyPairGeneratorClass(ddhGroup, generator);
    this.signatureFunction = this.createSignatureFunction();
    this.verificationFunction = this.createVerificationFunction();
  }

  @Override
  public TupleElement sign(final Element privateKey, final Element message, final Element randomization) {
    return (TupleElement) super.sign(privateKey, message, randomization);
  }

  @Override
  public DDHGroupKeyPairGenerator getKeyPairGenerator() {
    return this.keyGenerator;
  }

  @Override
  public Function getSignatureFunction() {
    return this.signatureFunction;
  }

  @Override
  public Function getVerificationFunction() {
    return this.verificationFunction;
  }

  @Override
  public HashFunction getHashFunction() {
    return this.hashFunction;
  }

  @Override
  public ProductGroup getSignatureSpace() {
    return (ProductGroup) super.getSignatureSpace();
  }

  @Override
  public ZPlusMod getRandomizationSpace() {
    return this.zPlusMod;
  }

  private Function createSignatureFunction() {
    return new SignatureFunctionClass(this.zPlusMod, this.generator, this.hashFunction, this.concatFunction);
  }

  // private Function createSignatureFunctionLeft(final ProductGroup domain) {
  // return new CompositeFunctionClass(
  // new MultiIdentityFunctionClass(domain, 2),
  // new ProductFunctionClass(
  // new SelectiveIdentityFunctionClass(1),
  // new CompositeFunctionClass(
  // new SelectiveIdentityFunctionClass(2),
  // new PartiallyAppliedFunctionClass(new ApplyFunctionClass(), this.generator,
  // 0)
  //
  // ;
  // }

  private Function createVerificationFunction() {
    return new VerificationFunctionClass(this.zPlusMod, this.ddhGroup, this.generator, this.hashFunction, this.concatFunction);
  }

  private static class SignatureFunctionClass extends ProductDomainFunctionAbstract {

    private final Element generator;
    private final HashFunction hashFunction;
    private final ConcatenateFunction concatFunction;

    public SignatureFunctionClass(final ZPlusMod zPlusMod, final Element generator, final HashFunction hashFunction, final ConcatenateFunction concatFunction) {
      super(SignatureFunctionClass.createDomain(zPlusMod), SignatureFunctionClass.createCoDomain(zPlusMod));
      this.generator = generator;
      this.hashFunction = hashFunction;
      this.concatFunction = concatFunction;
    }

    @Override
    public ProductGroup getCoDomain() {
      return (ProductGroup) super.getCoDomain();
    }

    @Override
    public Element apply(final Element element, final Random random) {
      final TupleElement tuple = (TupleElement) element;
      final AtomicElement privateKey = (AtomicElement) tuple.getElementAt(0);
      final Element message = tuple.getElementAt(1);
      final AtomicElement randomization = (AtomicElement) tuple.getElementAt(2);
      final AtomicElement concatElement = this.concatFunction.apply(message, this.generator.selfApply(randomization));
      final Element left = this.hashFunction.apply(concatElement);
      final Element right = randomization.apply(left.selfApply(privateKey).invert());
      return this.getCoDomain().getElement(left, right);
    }

    private static ProductGroup createDomain(final ZPlusMod zPlusMod) {
      return new ProductGroup(zPlusMod, ZPlus.getInstance(), zPlusMod);
    }

    private static PowerGroup createCoDomain(final ZPlusMod zPlusMod) {
      return new PowerGroup(zPlusMod, 2);
    }

  }

  private static class VerificationFunctionClass extends ProductDomainFunctionAbstract {

    private final Element generator;
    private final HashFunction hashFunction;
    private final ConcatenateFunction concatFunction;

    public VerificationFunctionClass(final ZPlusMod zPlusMod,
        final DDHGroup ddhGroup,
        final Element generator,
        final HashFunction hashFunction,
        final ConcatenateFunction concatFunciton) {
      super(VerificationFunctionClass.createDomain(zPlusMod, ddhGroup), BooleanGroup.getInstance());
      this.generator = generator;
      this.hashFunction = hashFunction;
      this.concatFunction = concatFunciton;
    }

    @Override
    public BooleanGroup getCoDomain() {
      return (BooleanGroup) super.getCoDomain();
    }

    @Override
    public Element apply(final Element element, final Random random) {
      final TupleElement tuple = (TupleElement) element;
      final Element publicKey = tuple.getElementAt(0);
      final Element message = tuple.getElementAt(1);
      final AtomicElement left = (AtomicElement) tuple.getElementAt(2, 0);
      final AtomicElement right = (AtomicElement) tuple.getElementAt(2, 1);
      final AtomicElement concatElement = this.concatFunction.apply(message, this.generator.selfApply(right).apply(publicKey.selfApply(left)));
      final Element result = this.hashFunction.apply(concatElement);
      return this.getCoDomain().getElement(left.equals(result));
    }

    private static ProductGroup createDomain(final ZPlusMod zPlusMod, final DDHGroup ddhGroup) {
      return new ProductGroup(ddhGroup, ZPlus.getInstance(), new PowerGroup(zPlusMod, 2));
    }

  }

}
