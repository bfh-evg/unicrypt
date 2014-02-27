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

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.signature.abstracts.AbstractSignatureScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class RSASignatureScheme
	   extends AbstractSignatureScheme<ZMod, ZModElement, ZMod, ZModElement> {

	@Override
	protected KeyPairGenerator abstractGetKeyPairGenerator() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Function abstractGetSignatureFunction() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Function abstractGetVerificationFunction() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}

//import java.math.BigInteger;
//import java.util.Random;
//
//import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
//import ch.bfh.unicrypt.crypto.schemes.signature.abstracts.AbstractSignatureScheme;
//import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
//import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
//import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
//import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
//import ch.bfh.unicrypt.math.function.classes.HashFunction;
//import ch.bfh.unicrypt.math.function.interfaces.Function;
//
///**
// * Diese Klasse ist deshalb noch ein 'Hack', da die RSA-Gruppe noch nicht
// * vorhanden ist. Als Substitution wird eine {@link ZPlusMod} Gruppe benutzt.
// *
// * @author reto
// *
// */
//@SuppressWarnings("unused")
//public class RSASignatureScheme extends AbstractSignatureScheme {
//  public static final HashFunction.HashAlgorithm DEFAULT_HASH_ALGORITHM = HashFunction.HashAlgorithm.SHA256;
//  public static final Mapper DEFAULT_MAPPER = new CharsetXRadixYMapperClass(CharsetXRadixYMapperClass.DEFAULT_CHARSET, CharsetXRadixYMapperClass.DEFAULT_RADIX);
//
//  private final Function signatureFunction;
//  private final Function verificationFunction;
//  private final Function hashFunction;
//  private final Mapper mapper;
//
//  public RSASignatureScheme(final ZPlusMod rsaGroup) {
//    this(rsaGroup, RSASignatureScheme.DEFAULT_HASH_ALGORITHM, RSASignatureScheme.DEFAULT_MAPPER);
//  }
//
//  public RSASignatureScheme(final ZPlusMod rsaGroup, final HashAlgorithm hashAlgorithm, final Mapper mapper) {
//    if (rsaGroup == null) {
//      throw new IllegalArgumentException();
//    }
//    final Function xCryptFunction = new XcryptFunction(rsaGroup);
//    this.hashFunction = new HashFunction(hashAlgorithm, rsaGroup);
//    this.mapper = mapper;
//
//    this.signatureFunction = new SignatureFunction(xCryptFunction, hashAlgorithm, mapper);
//    this.verificationFunction = new VerificationFunction(xCryptFunction, hashAlgorithm, mapper);
//  }
//
//  @Override
//  public KeyPairGenerator getKeyPairGenerator() {
//    // TODO Auto-generated method stub
//    return null;
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
//  class XcryptFunction extends AbstractFunction {
//    public XcryptFunction(final ZPlusMod rsaGroup) {
//      // (publicKey,Message),cipherText
//      // (privateKey,cipherText),message
//      super(new PowerGroup(rsaGroup, 2), rsaGroup);
//    }
//
//    @Override
//    public Element apply(final Element element, final Random random) {
//      if (!this.getDomain().contains(element)) {
//        throw new IllegalArgumentException();
//      }
//      final TupleElement tupleElement = (TupleElement) element;
//      final BigInteger key = ((AdditiveElement) (tupleElement.getElementAt(0))).getValue();
//      final BigInteger message = ((AdditiveElement) (tupleElement.getElementAt(1))).getValue();
//      final ZPlusMod rsaGroup = (ZPlusMod) this.getCoDomain();
//      return rsaGroup.getElement(message.modPow(key, rsaGroup.getModulus()));
//
//    }
//
//    @Override
//    public Element apply(final Element element) {
//      return this.apply(element, (Random) null);
//    }
//  }
//
//  class SignatureFunction extends AbstractFunction {
//    private final Function xCryptFunction;
//    private final HashFunction hashFunction;
//    private final Mapper mapper;
//
//    public SignatureFunction(final Function xcryptFunction) {
//      this(xcryptFunction, RSASignatureScheme.DEFAULT_HASH_ALGORITHM, RSASignatureScheme.DEFAULT_MAPPER);
//    }
//
//    public SignatureFunction(final Function xCryptFunction, final HashAlgorithm hashAlgorithm, final Mapper mapper) {
//      // (privateKey,Message),Signature
//      super(new ProductGroup(xCryptFunction.getCoDomain(), ZPlus.getInstance()), xCryptFunction.getCoDomain());
//      this.xCryptFunction = xCryptFunction;
//      this.hashFunction = new HashFunction(hashAlgorithm, (ZPlusMod) xCryptFunction.getCoDomain());
//      this.mapper = mapper;
//    }
//
//    @Override
//    public Element apply(final Element element, final Random random) {
//      if (!this.getDomain().contains(element)) {
//        throw new IllegalArgumentException();
//      }
//      final TupleElement tupleElement = (TupleElement) element;
//      final Element key = tupleElement.getElementAt(0);
//      final AtomicElement message = this.mapper.getEncodedElement((AtomicElement) tupleElement.getElementAt(1));
//      final Element hashedMessage = this.hashFunction.apply(message);
//      final Element tuple = ((PowerGroup) this.xCryptFunction.getDomain()).getElement(key, hashedMessage);
//      return this.xCryptFunction.apply(tuple);
//
//    }
//
//    @Override
//    public Element apply(final Element element) {
//      return this.apply(element, (Random) null);
//    }
//  }
//
//  class VerificationFunction extends AbstractFunction {
//    private final Function xCryptFunction;
//    private final HashFunction hashFunction;
//    private final Mapper mapper;
//
//    public VerificationFunction(final Function xCryptFunction) {
//      this(xCryptFunction, RSASignatureScheme.DEFAULT_HASH_ALGORITHM, RSASignatureScheme.DEFAULT_MAPPER);
//    }
//
//    public VerificationFunction(final Function xCryptFunction, final HashAlgorithm hashAlgorithm, final Mapper mapper) {
//      // (PublicKey,Message,Signature),Boolean
//      super(new ProductGroup(xCryptFunction.getCoDomain(), ZPlus.getInstance(), xCryptFunction.getCoDomain()), BooleanGroup.getInstance());
//      this.hashFunction = new HashFunction(hashAlgorithm, (ZPlusMod) xCryptFunction.getCoDomain());
//      this.mapper = mapper;
//      this.xCryptFunction = xCryptFunction;
//    }
//
//    @Override
//    public Element apply(final Element element, final Random random) {
//      if (!this.getDomain().contains(element)) {
//        throw new IllegalArgumentException();
//      }
//      final TupleElement tupleElement = (TupleElement) element;
//      final AdditiveElement publicKey = (AdditiveElement) (tupleElement.getElementAt(0));
//      final AtomicElement message = this.mapper.getEncodedElement((AtomicElement) tupleElement.getElementAt(1));
//      final Element hashedMessage = this.hashFunction.apply(message);
//      final AdditiveElement signature = (AdditiveElement) (tupleElement.getElementAt(2));
//      final TupleElement verificationTuple = ((PowerGroup) this.xCryptFunction.getDomain()).createElement(publicKey, signature);
//      final boolean correct = hashedMessage.equals(this.xCryptFunction.apply(verificationTuple));
//      return BooleanGroup.getInstance().getElement(correct);
//    }
//
//    @Override
//    public Element apply(final Element element) {
//      return this.apply(element, (Random) null);
//    }
//  }
