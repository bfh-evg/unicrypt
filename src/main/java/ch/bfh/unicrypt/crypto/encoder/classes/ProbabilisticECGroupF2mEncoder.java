package ch.bfh.unicrypt.crypto.encoder.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.crypto.encoder.exceptions.ProbabilisticEncodingException;
import ch.bfh.unicrypt.crypto.encoder.interfaces.ProbabilisticEncoder;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModTwo;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
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




public class ProbabilisticECGroupF2mEncoder
	   extends AbstractEncoder<ZModPrime, ZModElement, ECPolynomialField, ECPolynomialElement>
	   implements ProbabilisticEncoder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ZMod zMod;
	private ECPolynomialField ecPoly;
	protected static final int SHIFT = 10;
	
	

	private ProbabilisticECGroupF2mEncoder(ZMod zMod, ECPolynomialField ecPoly) {
		super();
		this.zMod = zMod;
		this.ecPoly = ecPoly;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new ECF2mEncodingFunction(this.zMod, this.ecPoly);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new ECF2mDecodingFunction(this.getCoDomain(), this.getDomain());
	}
	
	public static ProbabilisticECGroupF2mEncoder getInstance(final ZMod zMod,final ECPolynomialField ec) {
		if (ec == null || zMod==null) {
			throw new IllegalArgumentException();
		}
		return new ProbabilisticECGroupF2mEncoder(zMod,ec);
	}
	
	
	static class ECF2mEncodingFunction extends AbstractFunction<ECF2mEncodingFunction, ZMod, ZModElement, ECPolynomialField, ECPolynomialElement>{

		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected ECF2mEncodingFunction(ZMod domain, ECPolynomialField coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ECPolynomialElement abstractApply(ZModElement element,
				RandomByteSequence randomByteSequence) {
			boolean firstOption=true;
			BigInteger r=element.getBigInteger().shiftLeft(SHIFT);
			element=this.getDomain().getElement(r);
						
			ZModToBinaryPolynomialEncoder enc=ZModToBinaryPolynomialEncoder.getInstance(this.getDomain(),this.getCoDomain().getFiniteField());
			PolynomialElement<ZModTwo> x=enc.encode(element);
			
			int i=0;
			while(i<(1 << SHIFT) && !hasY(x)){
				i++;
				element=element.add(this.getDomain().getOneElement());
				x=enc.encode(element);
				
				if (i >= (1 << SHIFT)) {
					firstOption=false;
					throw new ProbabilisticEncodingException(element + " can not be encoded");
				}
			}
			if(firstOption){
				PolynomialElement<ZModTwo> y=this.calculateY(x);
				return this.getCoDomain().getElement(x, y);
			}
			else{
			
				r=element.invert().getBigInteger().shiftLeft(SHIFT);
				element=this.getDomain().getElement(r);
				x=enc.encode(element);
			
				i=0;
				while(i<(1 << SHIFT) && !hasY(x)){
					i++;
					element=element.add(this.getDomain().getOneElement());
					x=enc.encode(element);
				
					if (i >= (1 << SHIFT)) {
						firstOption=false;
						throw new ProbabilisticEncodingException(element + " can not be encoded");
					}
				}
			
				PolynomialElement<ZModTwo> y=this.calculateY2(x);
				return this.getCoDomain().getElement(x, y);
			}
			
		}
		
		private PolynomialElement<ZModTwo> calculateY2(
				PolynomialElement<ZModTwo> x) {
			ECPolynomialField ec=this.getCoDomain();
			PolynomialElement<ZModTwo> t = x.add(ec.getA()).add(ec.getB().divide(x.square()));
			PolynomialElement<ZModTwo> l = ec.getFiniteField().solveQuadradicEquation(t);
			return l.add(l.getSet().getOneElement()).multiply(x);

		}

		private PolynomialElement<ZModTwo> calculateY(PolynomialElement<ZModTwo> x){
			ECPolynomialField ec=this.getCoDomain();
			PolynomialElement<ZModTwo> t = x.add(ec.getA()).add(ec.getB().divide(x.square()));
			PolynomialElement<ZModTwo> l = ec.getFiniteField().solveQuadradicEquation(t);
			return l.multiply(x);
		}
		
		private boolean hasY(PolynomialElement<ZModTwo> x){
			ECPolynomialField ec=this.getCoDomain();
			PolynomialElement<ZModTwo> t = x.add(ec.getA()).add(ec.getB().divide(x.square()));
			return ec.getFiniteField().hasQuadradicEquationSolution(t);
		}
		
	}
	
	static class ECF2mDecodingFunction extends AbstractFunction<ECF2mDecodingFunction, ECPolynomialField, ECPolynomialElement, ZMod, ZModElement>{


		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ECF2mDecodingFunction(ECPolynomialField domain, ZMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(ECPolynomialElement element,
				RandomByteSequence randomByteSequence) {
			ZModToBinaryPolynomialEncoder enc=ZModToBinaryPolynomialEncoder.getInstance(this.getCoDomain(),this.getDomain().getFiniteField());
			PolynomialElement<ZModTwo> x= element.getX();
			PolynomialElement<ZModTwo> y= element.getY();
			PolynomialElement<ZModTwo> yEnc2=this.calculateY2(x);
			
			if(y.isEquivalent(yEnc2)){
				BigInteger r=enc.decode(element.getX()).getBigInteger();
				return this.getCoDomain().getElement(r.shiftRight(SHIFT));
			}
			else{
				BigInteger r=enc.decode(element.getX()).getBigInteger();
				return this.getCoDomain().getElement(r.shiftRight(SHIFT)).invert();
			}

		}
		
		private PolynomialElement<ZModTwo> calculateY2(
				PolynomialElement<ZModTwo> x) {
			ECPolynomialField ec=this.getDomain();
			PolynomialElement<ZModTwo> t = x.add(ec.getA()).add(ec.getB().divide(x.square()));
			PolynomialElement<ZModTwo> l = ec.getFiniteField().solveQuadradicEquation(t);
			return l.add(l.getSet().getOneElement()).multiply(x);

		}
		
		
	}

}