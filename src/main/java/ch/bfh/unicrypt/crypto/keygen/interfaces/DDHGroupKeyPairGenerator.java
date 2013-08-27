package ch.bfh.unicrypt.crypto.keygen.interfaces;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.cyclicgroup.classes.ZPlusMod;
import ch.bfh.unicrypt.math.cyclicgroup.interfaces.DDHGroup;

public interface DDHGroupKeyPairGenerator extends KeyPairGenerator {

  @Override
  public ZPlusMod getPrivateKeySpace();

  @Override
  public DDHGroup getPublicKeySpace();

  @Override
  public Element getPrivateKey(Element keyPair);

  @Override
  public Element getPublicKey(Element keyPair);

}
