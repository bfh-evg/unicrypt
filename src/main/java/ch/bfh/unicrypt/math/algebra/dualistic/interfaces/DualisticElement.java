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
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface DualisticElement extends AdditiveElement, MultiplicativeElement {

  // The following methods are overridden from Element with an adapted return type
  @Override
  public SemiRing getSet();

  @Override
  public DualisticElement apply(Element element);

  @Override
  public DualisticElement applyInverse(Element element);

  @Override
  public DualisticElement selfApply(BigInteger amount);

  @Override
  public DualisticElement selfApply(Element amount);

  @Override
  public DualisticElement selfApply(int amount);

  @Override
  public DualisticElement selfApply();

  @Override
  public DualisticElement invert();

  // The following methods are overridden from AdditiveElement with an adapted return type
  @Override
  public DualisticElement add(Element element);

  @Override
  public DualisticElement subtract(Element element);

  @Override
  public DualisticElement times(BigInteger amount);

  @Override
  public DualisticElement times(Element amount);

  @Override
  public DualisticElement times(int amount);

  @Override
  public DualisticElement timesTwo();

  @Override
  public DualisticElement minus();

  // The following methods are overridden from MultiplicativeElement with an adapted return type
  @Override
  public DualisticElement multiply(Element element);

  @Override
  public DualisticElement divide(Element element);

  @Override
  public DualisticElement power(BigInteger amount);

  @Override
  public DualisticElement power(Element amount);

  @Override
  public DualisticElement power(int amount);

  @Override
  public DualisticElement square();

  @Override
  public DualisticElement oneOver();

}
