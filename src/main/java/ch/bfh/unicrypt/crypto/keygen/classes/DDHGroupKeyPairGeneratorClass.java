package ch.bfh.unicrypt.crypto.keygen.classes;

import ch.bfh.unicrypt.crypto.keygen.abstracts.KeyPairGeneratorAbstract;
import ch.bfh.unicrypt.crypto.keygen.interfaces.DDHGroupDistributedKeyPairGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.RandomFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.DDHGroup;

public class DDHGroupKeyPairGeneratorClass extends KeyPairGeneratorAbstract implements DDHGroupDistributedKeyPairGenerator {

  private final ZPlusMod zModPlus;
  private final DDHGroup ddhGroup;
  private final Element generator;
  private final Function keyGenerationFunction;

  public DDHGroupKeyPairGeneratorClass(final DDHGroup ddhGroup) {
    this(ddhGroup, ddhGroup.getDefaultGenerator());
  }

  public DDHGroupKeyPairGeneratorClass(final DDHGroup ddhGroup, final Element generator) {
    super(new ProductGroup(ddhGroup.getOrderGroup(), ddhGroup));
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
        new IdentityFunction(this.zModPlus, 2), 
        new ProductFunction(
            new IdentityFunction(this.zModPlus),
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
  public Function getPrivateKeyGeneratorFunction() {
    return ((CompositeFunction) this.getKeyGenerationFunction()).getFunctionAt(0);
  }

  @Override
  public Function getPublicKeyGeneratorFunction() {
    final ProductFunction tmp = (ProductFunction) ((CompositeFunction) this.getKeyGenerationFunction()).getFunctionAt(2);
    return tmp.getFunctionAt(1);
  }

}
