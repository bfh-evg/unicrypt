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
package ch.bfh.unicrypt.hash;

import java.math.BigInteger;

import ch.bfh.unicrypt.concat.classes.ConcatSchemeClass;
import ch.bfh.unicrypt.concat.interfaces.ConcatScheme;
import ch.bfh.unicrypt.hash.classes.HashSchemeClass;
import ch.bfh.unicrypt.hash.interfaces.HashScheme;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunctionClass.ConcatParameter;
import ch.bfh.unicrypt.math.function.classes.HashFunctionClass;
import ch.bfh.unicrypt.math.group.classes.BooleanGroupClass;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.BooleanGroup;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.util.mapper.classes.CharsetXRadixYMapperClass;

public class HashExample {
  public static void main(final String[] args) {
    final ConcatScheme concat = new ConcatSchemeClass(ConcatParameter.Plain, new CharsetXRadixYMapperClass());
    final HashScheme sha256 = new HashSchemeClass(HashFunctionClass.SHA256, concat, new ZPlusModClass(BigInteger.valueOf(10)));
    final BooleanGroup bg = BooleanGroupClass.getInstance();
    final ZPlus zp = ZPlusClass.getInstance();
    final ZPlusMod zpm = new ZPlusModClass(BigInteger.valueOf(200));
    final GStarSave zss = new GStarSaveClass(BigInteger.valueOf(23));

    final Element hashElement = sha256.hash(bg.createElement(false),
        zp.createElement(BigInteger.valueOf(13)),
        zpm.createElement(BigInteger.valueOf(10)),
        zss.createElement(BigInteger.valueOf(3)));
    System.out.println(hashElement);

  }
}
