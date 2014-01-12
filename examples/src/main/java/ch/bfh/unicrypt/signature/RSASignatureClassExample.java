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
package ch.bfh.unicrypt.signature;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.util.mapper.classes.CharsetXRadixYMapperClass;
import ch.bfh.unicrypt.signature.classes.RSASignatureClass;
import ch.bfh.unicrypt.signature.interfaces.SignatureScheme;

public class RSASignatureClassExample {
  public static void main(final String[] args) {
    // Beispiel und Zahlen aus 'Handbook of Applied Cryptography': 11.20 Example
    final ZPlusMod n = new ZPlusModClass(BigInteger.valueOf(55465219));
    final Element publicKey = n.createElement(BigInteger.valueOf(5));
    final Element privateKey = n.createElement(BigInteger.valueOf(44360237));
    {
      final SignatureScheme signatureScheme = new RSASignatureClass(n);
      {
        final Element message = ZPlusClass.getInstance().createElement(31229978);
        final Element signature = signatureScheme.sign(privateKey, message);
        System.out.println(signature);
        System.out.println(signatureScheme.verify(publicKey, message, signature));
      }

      {
        final Element message = ZPlusClass.getInstance().createElement(new BigInteger("Hello".getBytes()));

        final Element signature = signatureScheme.sign(privateKey, message);
        System.out.println(signature);

        System.out.println(signatureScheme.verify(publicKey, message, signature));

        System.out.println("Must be false:" + signatureScheme.verify(publicKey, message.selfApply(2), signature));
      }
    }
    {
      final SignatureScheme signatureScheme = new RSASignatureClass(n, RSASignatureClass.DEFAULT_HASH_ALGORITHM, new CharsetXRadixYMapperClass(CharsetXRadixYMapperClass.DEFAULT_CHARSET,36));
      {
        final Element message = ZPlusClass.getInstance().createElement(31229978);
        final Element signature = signatureScheme.sign(privateKey, message);
        System.out.println(signature);
        System.out.println(signatureScheme.verify(publicKey, message, signature));
      }

      {
        final Element message = ZPlusClass.getInstance().createElement(new BigInteger("Hello".getBytes()));

        final Element signature = signatureScheme.sign(privateKey, message);
        System.out.println(signature);

        System.out.println(signatureScheme.verify(publicKey, message, signature));

        System.out.println("Must be false:" + signatureScheme.verify(publicKey, message.selfApply(2), signature));
      }

    }

  }
}
