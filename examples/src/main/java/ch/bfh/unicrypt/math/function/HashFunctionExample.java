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
package ch.bfh.unicrypt.math.function;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunctionClass;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunctionClass.ConcatParameter;
import ch.bfh.unicrypt.math.function.classes.HashFunctionClass;
import ch.bfh.unicrypt.math.function.interfaces.ConcatenateFunction;
import ch.bfh.unicrypt.math.function.interfaces.HashFunction;
import ch.bfh.unicrypt.math.group.classes.PowerGroupClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;
import ch.bfh.unicrypt.math.util.mapper.classes.CharsetXRadixYMapperClass;

public class HashFunctionExample {
  public static void main(final String[] args) {
    {
      final ConcatenateFunction concat = new ConcatenateFunctionClass(new PowerGroupClass(ZPlusClass.getInstance(), 3),
          ConcatParameter.Plain,
          new CharsetXRadixYMapperClass());
      final HashFunction hash = new HashFunctionClass(HashFunctionClass.SHA1);
      System.out.println("CoDomain: " + hash.getCoDomain());

      final ZPlus zPlus = ZPlusClass.getInstance();

      System.out.println("SHA-1: 'Hello Rolf'");
      {
        final AdditiveElement element0 = zPlus.createEncodedElement(new BigInteger("Hello Rolf".getBytes()));
        final AdditiveElement hashElement = hash.apply(element0);
        final BigInteger hashValue = new BigInteger(1, hashElement.getBigInteger().toByteArray());
        System.out.println(hashValue.toString(16));
      }
      {
        final AdditiveElement element1 = zPlus.createEncodedElement(new BigInteger("Hello".getBytes()));
        final AdditiveElement element2 = zPlus.createEncodedElement(new BigInteger(" ".getBytes()));
        final AdditiveElement element3 = zPlus.createEncodedElement(new BigInteger("Rolf".getBytes()));
        final AtomicElement concatElement = concat.apply(element1, element2, element3);
        System.out.println(new String(concatElement.getBigInteger().toByteArray()));
        final Element hashElement = hash.apply(concatElement);
        final BigInteger hashValue = new BigInteger(1, ((AtomicElement) hashElement).getBigInteger().toByteArray());
        System.out.println(hashValue.toString(16));

      }
      {
        final ConcatenateFunction concat2 = new ConcatenateFunctionClass(new PowerGroupClass(ZPlusClass.getInstance(), 3),
            ConcatParameter.Pipe,
            new CharsetXRadixYMapperClass(Charset.forName("UTF-8"), 10));
        final AdditiveElement element1 = zPlus.createElement(new BigInteger("100")).times(2);
        final AdditiveElement element2 = zPlus.createElement(new BigInteger("200")).times(2);
        final AdditiveElement element3 = zPlus.createElement(new BigInteger("300")).times(2);
        final AtomicElement concatElement = concat2.apply(element1, element2, element3);
        System.out.println(new String(concatElement.getBigInteger().toByteArray()));
        final Element hashElement = hash.apply(concatElement);
        final BigInteger hashValue = new BigInteger(1, ((AtomicElement) hashElement).getBigInteger().toByteArray());
        System.out.println(hashValue.toString(16));

      }
    }
    {

      final HashFunction hash = new HashFunctionClass(HashFunctionClass.HashAlgorithm.SHA256);
      System.out.println("CoDomain: " + hash.getCoDomain());
      final ZPlus group = ZPlusClass.getInstance();

      System.out.println("SHA-256: 'Hello|Rolf'");
      {
        final Element element0 = group.createElement(new BigInteger("Hello|Rolf".getBytes()));
        final Element hashElement = hash.apply(element0);
        final BigInteger hashValue = new BigInteger(1, ((AtomicElement) hashElement).getBigInteger().toByteArray());
        System.out.println(hashValue.toString(16));
      }
      {
        final Element element1 = group.createEncodedElement(new BigInteger("Hello".getBytes()));
        final Element element2 = group.createEncodedElement(new BigInteger("Rolf".getBytes()));
        final ConcatenateFunction concat = new ConcatenateFunctionClass(new PowerGroupClass(ZPlusClass.getInstance(), 2),
            ConcatParameter.Pipe,
            new CharsetXRadixYMapperClass());
        final AtomicElement concatElement = concat.apply(element1, element2);
        System.out.println(new String(concatElement.getBigInteger().toByteArray()));
        final Element hashElement = hash.apply(concatElement);
        final BigInteger hashValue = new BigInteger(1, ((AtomicElement) hashElement).getBigInteger().toByteArray());
        System.out.println(hashValue.toString(16));
      }
    }
    // Brute-Test vs. direct Java-Implementation (It is surprisingly fast... Is
    // there something wrong?)
    // It has to result in 'done'
    // If it results in 'false' ... 'done' something is wrong
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final HashFunction hash = new HashFunctionClass(HashFunctionClass.SHA256);
      System.out.println("CoDomain: " + hash.getCoDomain());
      final ZPlus group = ZPlusClass.getInstance();
      for (int i = 0; i < 100000; i++) {
        final Element element0 = group.createRandomElement();
        final byte[] elements0Bytes = ((AtomicElement) element0).getBigInteger().toByteArray();
        final Element hashElement = hash.apply(element0);
        final BigInteger digestJava = new BigInteger(1, digest.digest(elements0Bytes));
        final BigInteger hashValue = new BigInteger(1, ((AtomicElement) hashElement).getBigInteger().toByteArray());
        if (!digestJava.equals(hashValue)) {
          System.out.println(false);
        }
      }
      System.out.println("done");
    } catch (final NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
