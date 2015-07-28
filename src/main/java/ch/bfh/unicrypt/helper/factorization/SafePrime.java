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
package ch.bfh.unicrypt.helper.factorization;

import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.random.classes.RandomNumberGenerator;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Each instance of this class represents a safe prime number, i.e., a value {@code p} such that {@code (p-1)/2} is
 * prime. This class is a specialization of {@link Prime}.
 * <p>
 * @see Prime
 * @author R. Haenni
 * @version 2.0
 */
public class SafePrime
	   extends Prime {

	private static final long serialVersionUID = 1L;

	private static final SafePrime INSTANCE1024 = new SafePrime(new BigInteger("89884656743115795386465259539451236680898848947115328636715040578866337902750481566354238661203768010560056939935696678829394884407208311246423715319737062188883946712432742638151109800623047059726541476042502884419075341171231440736956555270413618581675255342293149119973622969239858152417678164812113740223"));
	private static final SafePrime INSTANCE2048 = new SafePrime(new BigInteger("16158503035655503650357438344334975980222051334857742016065172713762327569433945446598600705761456731844358980460949009747059779575245460547544076193224141560315438683650498045875098875194826053398028819192033784138396109321309878080919047169238085235290822926018152521443787945770532904303776199561965192760957166694834171210342487393282284747428088017663161029038902829665513096354230157075129296432088558362971801859230928678799175576150822952201848806616643615613562842355410104862578550863465661734839271290328348967522998634176499319107762583194718667771801067716614802322659239302476074096777926805529798824879"));
	private static final SafePrime INSTANCE3072 = new SafePrime(new BigInteger("4904802997684979031429751266652287185343487588181447618330743076143601865498555112868668022266559203625663078877490258721995264797270023560831442836093516200516055819853220249422024925494525813600122382903520906197364840270012052413988292184690761146180604389522384946371612875869038489784405654789562755666546621759776892408153190790080930100123746284224075121257652224788593802068214369290495086275786967073127915183202957500434821866026609283416272645553951861415817069299793203345162979862593723584529770402506155104819505875374380008547680367117472878708136497428006654308479264979152338818509590797044264172530642931949135881728647441773319439777155807723223165099627191170008146028545375587766944080959493647795765768349350646133842732758718957895411577422317390130051445859016247698037520949742756905563488653739484537428521855358075060657961012278379620619506576459855478234203189721457470807178553957231283671210463"));

	private static final Map<Integer, SafePrime> instances = new HashMap<Integer, SafePrime>();

	static {
		SafePrime.instances.put(1024, INSTANCE1024);
		SafePrime.instances.put(2048, INSTANCE2048);
		SafePrime.instances.put(3072, INSTANCE3072);
	}

	protected SafePrime(BigInteger safePrime) {
		super(safePrime);
	}

	/**
	 * Creates a new safe prime from a given integer value of type {@code int}. This method is a convenience method for
	 * {@link SafePrime#getInstance(java.math.BigInteger)}. Throws an exception if the given integer is not a safe
	 * prime.
	 * <p>
	 * @param safePrime The given integer value
	 * @return The new safe prime
	 */
	public static SafePrime getInstance(int safePrime) {
		return SafePrime.getInstance(BigInteger.valueOf(safePrime));
	}

	/**
	 * Creates a new safe prime from a given integer value of type {@link BigInteger}. Throws an exception if the given
	 * integer is not a safe prime.
	 * <p>
	 * @param safePrime The given integer value
	 * @return The new safe prime
	 */
	public static SafePrime getInstance(BigInteger safePrime) {
		if (safePrime == null || !MathUtil.isSafePrime(safePrime)) {
			throw new IllegalArgumentException();
		}
		return new SafePrime(safePrime);
	}

	/**
	 * Returns the smallest safe prime of a given bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The new safe prime
	 */
	public static SafePrime getFirstInstance(int bitLength) {
		if (bitLength < 3) {
			throw new IllegalArgumentException();
		}
		SafePrime safePrime = SafePrime.instances.get(bitLength);
		if (safePrime == null) {
			safePrime = SafePrime.getFirstInstance(MathUtil.powerOfTwo(bitLength - 1));
			SafePrime.instances.put(bitLength, safePrime);
		}
		return safePrime;
	}

	/**
	 * Returns the smallest safe prime greater or equal to {@code lowerBound}.
	 * <p>
	 * @param lowerBound The lower bound
	 * @return The new safe prime
	 */
	public static SafePrime getFirstInstance(BigInteger lowerBound) {
		if (lowerBound == null) {
			throw new IllegalArgumentException();
		}
		BigInteger twelve = BigInteger.valueOf(12);
		BigInteger safePrime;
		if (lowerBound.compareTo(BigInteger.valueOf(5)) <= 0) {
			safePrime = BigInteger.valueOf(5);
		} else if (lowerBound.compareTo(BigInteger.valueOf(7)) <= 0) {
			safePrime = BigInteger.valueOf(7);
		} else {
			safePrime = lowerBound.add(twelve.subtract(lowerBound.mod(twelve))).subtract(BigInteger.ONE);
			while (!MathUtil.isSafePrime(safePrime)) {
				safePrime = safePrime.add(twelve);
			}
		}
		return new SafePrime(safePrime);
	}

	/**
	 * Creates a new random safe prime of a given bit length using the library's default source of randomness.
	 * <p>
	 * @param bitLength The bit length
	 * @return The new safe prime
	 */
	public static SafePrime getRandomInstance(int bitLength) {
		return SafePrime.getRandomInstance(bitLength, RandomNumberGenerator.getInstance().getInstance());
	}

	/**
	 * Creates a new random safe prime of a given bit length using a given source of randomness.
	 * <p>
	 * @param bitLength             The bit length
	 * @param randomNumberGenerator The given random number generator
	 * @return The new safe prime
	 */
	public static SafePrime getRandomInstance(int bitLength, RandomNumberGenerator randomNumberGenerator) {
		if (bitLength < 3 || randomNumberGenerator == null) {
			throw new IllegalArgumentException();
		}
		// Special case with safe primes p=5 or p=7 not satisfying p mod 12 = 11
		if (bitLength == 3) {
			if (randomNumberGenerator.nextBoolean()) {
				return new SafePrime(BigInteger.valueOf(5));
			} else {
				return new SafePrime(BigInteger.valueOf(7));
			}
		}
		BigInteger prime;
		BigInteger safePrime;
		do {
			prime = randomNumberGenerator.nextBigInteger(bitLength - 1);
			safePrime = prime.shiftLeft(1).add(BigInteger.ONE);
		} while (!safePrime.mod(BigInteger.valueOf(12)).equals(BigInteger.valueOf(11)) || !MathUtil.isPrime(prime) || !MathUtil.isPrime(safePrime));
		return new SafePrime(safePrime);
	}

}
