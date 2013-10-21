package ch.bfh.unicrypt.crypto.keygenerator.old;

import java.security.KeyPairGenerator;

import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.RandomFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class DDHGroupKeyPairGeneratorClass extends KeyPairGenerator implements DDHGroupDistributedKeyPairGenerator {

  private final ZPlusMod zModPlus;
  private final DDHGroup ddhGroup;
  private final Element generator;
  private final Function keyGenerationFunction;

  public DDHGroupKeyPairGeneratorClass(final DDHGroup ddhGroup) {
    this(ddhGroup, ddhGroup.getDefaultGenerator());
  }

  public DDHGroupKeyPairGeneratorClass(final DDHGroup ddhGroup, final Element generator) {
    super(new ProductGroup(ddhGroup.getZModOrder(), ddhGroup));
    if ((generator == null) || !ddhGroup.contains(generator)) { // or is not a generator!
      throw new IllegalArgumentException();
    }
    this.zModPlus = this.getPrivateKeySpace();
    this.ddhGroup = this.getPublicKeySpace();
    this.generator = generator;
    this.keyGenerationFunction = this.createKeyGenerationFunction();
  }

  @Override
  public Function getKeyGenerationFunction() {
    return this.keyGenerationFunction;
  }

  @Override
  public ZPlusMod getPrivateKeySpace() {
    return (ZPlusMod) super.getPrivateKeySpace();
  }

  @Override
  public DDHGroup getPublicKeySpace() {
    return (DDHGroup) super.getPublicKeySpace();
  }

  @Override
  public Element combinePublicKeys(final Element... publicKeys) {
    return this.ddhGroup.apply(publicKeys);
  }

  private Function createKeyGenerationFunction() {
    //@formatter:off
    return new CompositeFunction(
        new RandomFunction(this.zModPlus), 
        new MultiIdentityFunction(this.zModPlus, 2), 
        new ProductFunction(
            new MultiIdentityFunction(this.zModPlus),
            new SelfApplyFunction(this.ddhGroup, this.zModPlus).partiallyApply(this.generator, 0)
//            this.createPublicKeyGenerationFunction()
));
    //@formatter:on
  }

//  private Function createPublicKeyGenerationFunction() {
//    //@formatter:off
//    return new CompositeFunctionClass(
//        new MultiIdentityFunctionClass(this.zModPlus, 1),
//        new PartiallyAppliedFunctionClass(
//            new SelfApplyFunctionClass(this.ddhGroup, this.zModPlus), this.generator, 0));
//    //@formatter:on
//  }

  @Override
  public Function getPrivateKeyGenerationFunction() {
    return ((CompositeFunction) this.getKeyGenerationFunction()).getFunctionAt(0);
  }

  @Override
  public Function getPublicKeyGenerationFunction() {
    final ProductFunction tmp = (ProductFunction) ((CompositeFunction) this.getKeyGenerationFunction()).getFunctionAt(2);
    return tmp.getAt(1);
  }

}
