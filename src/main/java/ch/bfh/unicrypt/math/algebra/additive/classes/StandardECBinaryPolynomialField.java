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
package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.params.interfaces.StandardECBinaryPolinomialFieldParams;
import java.math.BigInteger;

public class StandardECBinaryPolynomialField
	   extends ECBinaryPolynomialField {

	public StandardECBinaryPolynomialField(PolynomialField finiteField, PolynomialElement a,
		   PolynomialElement b, PolynomialElement gx, PolynomialElement gy,
		   BigInteger order, BigInteger h) {
		super(finiteField, a, b, gx, gy, order, h);
	}

	public static StandardECBinaryPolynomialField getInstance(final StandardECBinaryPolinomialFieldParams params) {
		PolynomialField field;
		PolynomialElement a, b, gx, gy;
		BigInteger order, h;

		field = params.getFiniteField();
		a = params.getA();
		b = params.getB();
		gx = params.getGx();
		gy = params.getGy();
		order = params.getOrder();
		h = params.getH();

		return new StandardECBinaryPolynomialField(field, a, b, gx, gy, order, h);
	}

//	public static void main(String[] args) {
//
//		for (SECECCParamsF2m params : SECECCParamsF2m.values()) {
//
//			StandardECBinaryPolynomialField ec = StandardECBinaryPolynomialField.getInstance(params);
//			System.out.println(params.name() + "(\"" + ec.getA().getBigInteger().toString(16) + "\",\"" + ec.getB().getBigInteger().toString(16) + "\",\"" + ec.getDefaultGenerator().getX().getBigInteger().toString(16) + "\",\"" + ec.getDefaultGenerator().getY().getBigInteger().toString(16) + "\",\"" + ec.getOrder().toString(16) + "\",\"" + ec.getCoFactor() + "\"),");
//		}
//	}
}
