/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.crypto.schemes.signature.abstracts;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.crypto.schemes.signature.interfaces.SignatureScheme;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author R. Haenni
 * @param <MS>  Message space
 * @param <ME>  Message element
 * @param <SS>  Signature space
 * @param <SE>  Signature element
 * @param <SKS> Signature key space
 * @param <VKS> Verification key space
 * @param <KG>  Key generator
 */
public abstract class AbstractSignatureScheme<MS extends Set, ME extends Element, SS extends Set, SE extends Element, SKS extends Set, VKS extends Set, KG extends KeyPairGenerator>
	   extends AbstractScheme<MS>
	   implements SignatureScheme {

	private static final long serialVersionUID = 1L;

	protected final SS signatureSpace;
	protected final ConvertMethod convertMethod;
	protected final HashMethod hashMethod;
	protected KG keyPairGenerator;

	private Function signatureFunction;
	private Function verificationFunction;

	protected AbstractSignatureScheme(MS messageSpace, SS signatureSpace, ConvertMethod convertMethod, HashMethod hashMethod) {
		super(messageSpace);
		this.signatureSpace = signatureSpace;
		this.convertMethod = convertMethod;
		this.hashMethod = hashMethod;
		this.keyPairGenerator = null;
	}

	@Override
	public ConvertMethod getConvertMethod() {
		return this.convertMethod;
	}

	@Override
	public HashMethod getHashMethod() {
		return this.hashMethod;
	}

	@Override
	public SE sign(final Element privateKey, final Element message) {
		return (SE) this.getSignatureFunction().apply(privateKey, message);
	}

	@Override
	public BooleanElement verify(final Element publicKey, final Element message, final Element signature) {
		return (BooleanElement) this.getVerificationFunction().apply(publicKey, message, signature);
	}

	@Override
	public SS getSignatureSpace() {
		return this.signatureSpace;
	}

	@Override
	public final KeyPairGenerator getKeyPairGenerator() {

		if (this.keyPairGenerator == null) {
			this.keyPairGenerator = this.abstractGetKeyPairGenerator();
		}
		return this.keyPairGenerator;
	}

	@Override
	public final Function getSignatureFunction() {
		if (this.signatureFunction == null) {
			this.signatureFunction = this.abstractGetSignatureFunction();
		}
		return this.signatureFunction;
	}

	@Override
	public final Function getVerificationFunction() {
		if (this.verificationFunction == null) {
			this.verificationFunction = this.abstractGetVerificationFunction();
		}
		return this.verificationFunction;
	}

	@Override
	public SKS getSignatureKeySpace() {
		return (SKS) this.getKeyPairGenerator().getPrivateKeySpace();
	}

	@Override
	public VKS getVerificationKeySpace() {
		return (VKS) this.getKeyPairGenerator().getPublicKeySpace();
	}

	protected abstract KG abstractGetKeyPairGenerator();

	protected abstract Function abstractGetSignatureFunction();

	protected abstract Function abstractGetVerificationFunction();

}
