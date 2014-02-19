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
package ch.bfh.unicrypt.math.algebra.additive;

import ch.bfh.unicrypt.Example;

public class ECGroupExample {

	public static void example1() {

		//Example 1
		/*
		 * StandardECZModPrime ec1=StandardECZModPrime.getInstance(SECECCParamsFp.secp224k1); ECZModPrimeElement
		 * g1=ec1.getDefaultGenerator(); ec1.getRandomElement(); BigInteger n2=ec1.getOrder();
		 * System.out.println(g1.selfApply(n2));	//order*generator -> (-1,-1) System.out.println(MathUtil.arePrime(n2));
		 * //order must be prime
		 *
		 * long t1=System.currentTimeMillis(); ECZModPrimeElement w2=ec1.getRandomElement();
		 * t1=System.currentTimeMillis()-t1; System.out.println(w2); System.out.println("Computing time for random
		 * element w2= "+t1+" ms"); t1=System.currentTimeMillis(); w2=w2.selfApply(n2);
		 * t1=System.currentTimeMillis()-t1; System.out.println(w2);	//Must be Identity (-1,-1) if h=1
		 * System.out.println("Computing time for n2*w2= "+t1+" ms");
		 */
	}

	public static void example2() {
		/*
		 * //Example 2 with "own" ECGroupFp ZModPrime field2=ZModPrime.getInstance(new
		 * BigInteger("DB7C2ABF62E35E668076BEAD208B",16)); ZModElement a2=field2.getElement(new
		 * BigInteger("DB7C2ABF62E35E668076BEAD2088",16)); ZModElement b2=field2.getElement(new
		 * BigInteger("659EF8BA043916EEDE8911702B22",16)); BigInteger order2=new
		 * BigInteger("DB7C2ABF62E35E7628DFAC6561C5",16); BigInteger h2=new BigInteger("1"); //ECZModPrime
		 * ec3=ECZModPrime.getInstance(field, a1, b1, order, h); ECZModPrime ec2=ECZModPrime.getInstance(field2, a2, b2,
		 * order2, h2); ECZModPrimeElement gen2=ec2.getDefaultGenerator(); System.out.println(gen2);
		 * System.out.println(gen2.selfApply(h2).selfApply(order2));	//order*h*generator -> (-1,-1)
		 */
		/* StandardECZModPrime ec=StandardECZModPrime.getInstance(SECECCParamsFp.secp112r1); BigInteger
		 * order=ec.getOrder(); ECZModPrimeElement e1=ec.getDefaultGenerator(); ECZModPrimeElement
		 * e2=e1.selfApply(order.subtract(BigInteger.ONE)); generator * order-1 e2=e2.apply(e1); should be identity
		 * (-1,-1)
		 *
		 * System.out.println(e2);
		 */
	}

	public static void main(final String[] args) {
		Example.runExamples();
	}

}
