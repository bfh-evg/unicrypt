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
package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class PedersenCommitmentScheme<CS extends CyclicGroup, CE extends Element>
			 extends AbstractRandomizedCommitmentScheme<ZMod, ZModElement, CS, CE, ZModPrime> {

	private final CS cyclicGroup;
	private final CE randomizationGenerator;
	private final CE messgeGenerator;

	protected PedersenCommitmentScheme(CS cyclicGroup, CE randomizationGenerator, CE messageGenerator) {
		this.cyclicGroup = cyclicGroup;
		this.randomizationGenerator = randomizationGenerator;
		this.messgeGenerator = messageGenerator;
	}

	public final CS getCyclicGroup() {
		return this.cyclicGroup;
	}

	public final CE getRandomizationGenerator() {
		return this.randomizationGenerator;
	}

	public final CE getMessageGenerator() {
		return this.messgeGenerator;
	}

	@Override
	protected Function abstractGetCommitmentFunction() {
		return CompositeFunction.getInstance(
					 ProductFunction.getInstance(GeneratorFunction.getInstance(this.getMessageGenerator()),
																			 GeneratorFunction.getInstance(this.getRandomizationGenerator())),
					 ApplyFunction.getInstance(this.getCyclicGroup()));
	}

	public static <CS extends CyclicGroup, CE extends Element> PedersenCommitmentScheme<CS, CE> getInstance(CS cyclicGroup) {
		return PedersenCommitmentScheme.<CS, CE>getInstance(cyclicGroup, (RandomReferenceString) null);
	}

	public static <CS extends CyclicGroup, CE extends Element> PedersenCommitmentScheme<CS, CE> getInstance(CS cyclicGroup, RandomReferenceString randomReferenceString) {
		return PedersenCommitmentScheme.<CS, CE>getInternalInstance(cyclicGroup, randomReferenceString);
	}

	public static PedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarMod gStarMod) {
		return PedersenCommitmentScheme.<GStarMod, GStarModElement>getInstance(gStarMod, (RandomReferenceString) null);
	}

	public static PedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarMod gStarMod, RandomReferenceString randomReferenceString) {
		return PedersenCommitmentScheme.<GStarMod, GStarModElement>getInternalInstance(gStarMod, randomReferenceString);
	}

	public static PedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarModElement firstGenerator, GStarModElement secondGenerator) {
		if (!firstGenerator.getSet().isEquivalent(secondGenerator.getSet())) {
			throw new IllegalArgumentException();
		}
		return new PedersenCommitmentScheme<GStarMod, GStarModElement>(firstGenerator.getSet(), firstGenerator, secondGenerator);
	}

	private static <CS extends CyclicGroup, CE extends Element> PedersenCommitmentScheme<CS, CE> getInternalInstance(CS cyclicGroup, RandomReferenceString randomReferenceString) {
		if (randomReferenceString == null) {
			randomReferenceString = PseudoRandomReferenceString.getInstance();
		} else {
			randomReferenceString.reset();
		}
		Element[] generators = cyclicGroup.getIndependentGenerators(2, randomReferenceString);
		CE randomizationGenerator = (CE) generators[0];
		CE messageGenerator = (CE) generators[1];
		return new PedersenCommitmentScheme<CS, CE>(cyclicGroup, randomizationGenerator, messageGenerator);
	}

}
