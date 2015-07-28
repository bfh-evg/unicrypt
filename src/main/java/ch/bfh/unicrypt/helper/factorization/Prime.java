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

import ch.bfh.unicrypt.helper.map.HashMap2D;
import ch.bfh.unicrypt.helper.map.Map2D;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.random.classes.RandomNumberGenerator;
import java.math.BigInteger;

/**
 * Each instance of this class represents a prime number. This class is a specialization of {@link SpecialFactorization}
 * for the borderline case of a single prime factor {@code p} with exponent {@code 0}.
 * <p>
 * @see SpecialFactorization
 * @author R. Haenni
 * @version 2.0
 */
public class Prime
	   extends SpecialFactorization {

	private static final long serialVersionUID = 1L;

	// map of smallest prime numbers for common bit lenghts
	private static final Map2D<Integer, Integer, Prime> instances = HashMap2D.getInstance();

	static {
		Prime.instances.put(128, 2, new Prime(new BigInteger("170141183460469231731687303715884105757")));
		Prime.instances.put(160, 2, new Prime(new BigInteger("730750818665451459101842416358141509827966271787")));
		Prime.instances.put(192, 2, new Prime(new BigInteger("3138550867693340381917894711603833208051177722232017256453")));
		Prime.instances.put(224, 2, new Prime(new BigInteger("13479973333575319897333507543509815336818572211270286240551805124797")));
		Prime.instances.put(256, 2, new Prime(new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564820063")));
		Prime.instances.put(384, 2, new Prime(new BigInteger("19701003098197239606139520050071806902539869635232723333974146702122860885748605305707133127442457820403313995153777")));
		Prime.instances.put(512, 2, new Prime(new BigInteger("6703903964971298549787012499102923063739682910296196688861780721860882015036773488400937149083451713845015929093243025426876941405973284973216824503042159")));
		Prime.instances.put(768, 2, new Prime(new BigInteger("776259046150354467574489744231251277628443008558348305569526019013025476343188443165439204414323238975243865348565536603085790022057407195722143637520590569602227488010424952775132642815799222412631499596858234375446423426908029627")));
		Prime.instances.put(1024, 2, new Prime(new BigInteger("89884656743115795386465259539451236680898848947115328636715040578866337902750481566354238661203768010560056939935696678829394884407208311246423715319737062188883946712432742638151109800623047059726541476042502884419075341171231440736956555270413618581675255342293149119973622969239858152417678164812112069763")));
		Prime.instances.put(2048, 2, new Prime(new BigInteger("16158503035655503650357438344334975980222051334857742016065172713762327569433945446598600705761456731844358980460949009747059779575245460547544076193224141560315438683650498045875098875194826053398028819192033784138396109321309878080919047169238085235290822926018152521443787945770532904303776199561965192760957166694834171210342487393282284747428088017663161029038902829665513096354230157075129296432088558362971801859230928678799175576150822952201848806616643615613562842355410104862578550863465661734839271290328348967522998634176499319107762583194718667771801067716614802322659239302476074096777926805529798117247")));
		Prime.instances.put(3072, 2, new Prime(new BigInteger("2904802997684979031429751266652287185343487588181447618330743076143601865498555112868668022266559203625663078877490258721995264797270023560831442836093516200516055819853220249422024925494525813600122382903520906197364840270012052413988292184690761146180604389522384946371612875869038489784405654789562755666546621759776892408153190790080930100123746284224075121257652224788593802068214369290495086275786967073127915183202957500434821866026609283416272645553951861415817069299793203345162979862593723584529770402506155104819505875374380008547680367117472878708136497428006654308479264979152338818509590797044264172530642931949135881728647441773319439777155807723223165099627191170008146028545375587766944080959493647795765768349350646133842732758718957895411577422317390130051445859016247698037520949742756905563488653739484537428521855358075060657961012278379620619506576459855478234203189721457470807178553957231283664849139")));
		Prime.instances.put(4096, 2, new Prime(new BigInteger("522194440706576253345876355358312191289982124523691890192116741641976953985778728424413405967498779170445053357219631418993786719092896803631618043925682638972978488271854999170180795067191859157214035005927973113188159419698856372836167342172293308748403954352901852035642024370059304557233988891799014503343469488440893892973452815095130470299789726716411734651513348221529512507986199933857107770846917779942645743159118957217248367043905936319748237550094520674504208530837546834166925275516486044134775384991808184705966507606898412918594045916828375610659246423184062775112999150206172392431297837246097308511903252956622805412865917690043804311051417135098849101156584508839003337597742539960818209685142687562392007453579567729991395256699805775897135553415567045292136442139895777424891477161767258532611634530697452993846501061481697843891439474220308003706472837459911525285821188577408160690315522951458068463354171428220365223949985950890732881736611925133626529949897998045399734600887312408859224933727829625089164535236559716582775403784110923285873186648442456409760158728501220463308455437074192539205964902261490928669488824051563042951500651206733594863336608245755565801460390869016718045121902354170201577095747")));
		Prime.instances.put(1024, 160, new Prime(new BigInteger("89884656743115795386465259539451236680898848947115328636715040578866337902750481566354238661203768010560056939935696678829394884407208311246423715319737062188883946712432742638151109800623047059726541476042502884419075341171231440736956555270413618581675255529365358698328774708775703215219351545329613875969"), new BigInteger("730750818665451459101842416358141509827966271787")));
		Prime.instances.put(2048, 224, new Prime(new BigInteger("16158503035655503650357438344334975980222051334857742016065172713762327569433945446598600705761456731844358980460949009747059779575245460547544076193224141560315438683650498045875098875194826053398028819192033784138396109321309878080919047169238085235290822926018152521443787945770532904303776199561965192760957166694834171210342487393282284747428088017663161029038902829665513096354230157075129296432088558362971801859230928678799175576150822952201848806616643615613562842355410104862578550863465661734839271290328348967522998634176499319107762601824041814772893165831522227453224035124084988448041816607879141260367"), new BigInteger("13479973333575319897333507543509815336818572211270286240551805124797")));
		Prime.instances.put(3072, 256, new Prime(new BigInteger("2904802997684979031429751266652287185343487588181447618330743076143601865498555112868668022266559203625663078877490258721995264797270023560831442836093516200516055819853220249422024925494525813600122382903520906197364840270012052413988292184690761146180604389522384946371612875869038489784405654789562755666546621759776892408153190790080930100123746284224075121257652224788593802068214369290495086275786967073127915183202957500434821866026609283416272645553951861415817069299793203345162979862593723584529770402506155104819505875374380008547680367117472878708136497428006654308479264979152338818509590797044264172530642931949135881728647441773319439777155807723223165099627191170008146028545375587766944080959493647795765768349350646133842732758718957895411577422317390130051445859016247698037520949742756905563488653739484537428521855358075060716204433164749666917562781919225495884397992008274673412368259180131087873830227"), new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564820063")));
	}

	private BigInteger orderFactor;

	protected Prime() {
		this(MathUtil.TWO, null);
	}

	protected Prime(BigInteger prime) {
		this(prime, MathUtil.TWO);
	}

	protected Prime(BigInteger prime, BigInteger orderFactor) {
		super(prime, new BigInteger[]{prime}, new int[]{1});
		this.orderFactor = orderFactor;
	}

	public Prime getOrderFactor() {
		return Prime.getInstance(this.orderFactor);
	}

	/**
	 * Creates a new prime number from a given integer value of type {@code int}. This method is a convenience method
	 * for {@link Prime#getInstance(BigInteger)}. Throws an exception if the given integer is not prime.
	 * <p>
	 * @param prime The given integer value
	 * @return The new prime
	 */
	public static Prime getInstance(int prime) {
		return Prime.getInstance(BigInteger.valueOf(prime));
	}

	/**
	 * Creates a new prime number from a given integer value of type {@link BigInteger}. Throws an exception if the
	 * given integer is not prime.
	 * <p>
	 * @param prime The given integer value
	 * @return The new prime
	 */
	public static Prime getInstance(BigInteger prime) {
		if (prime == null || !MathUtil.isPrime(prime)) {
			throw new IllegalArgumentException();
		}
		return new Prime(prime);
	}

	/**
	 * Returns the smallest prime number of a given bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The new prime
	 */
	public static Prime getFirstInstance(int bitLength) {
		if (bitLength < 2) {
			throw new IllegalArgumentException();
		}
		if (bitLength == 2) {
			return new Prime();
		}
		// bitLength > 2
		return Prime.getFirstInstance(bitLength, 2);
	}

	public static Prime getFirstInstance(int bitLength1, int bitLength2) {
		if (bitLength1 < 2 || bitLength2 < 2 || (bitLength1 == 2 && bitLength2 > 2) || (bitLength1 > 2 && bitLength1 <= bitLength2)) {
			throw new IllegalArgumentException();
		}
		Prime prime = Prime.instances.get(bitLength1, bitLength2);
		if (prime == null) {
			if (bitLength1 == 2) {
				prime = new Prime(MathUtil.THREE);
			} else {
				// bitLength1 > 2
				BigInteger orderFactor = Prime.getFirstInstance(bitLength2).getValue();
				prime = Prime.getFirstInstance(bitLength1, orderFactor);
				if (prime.getValue().bitLength() > bitLength1) {
					throw new IllegalArgumentException();
				}
			}
			Prime.instances.put(bitLength1, bitLength2, prime);
		}
		return prime;
	}

	// helper method for bitLength > 2
	private static Prime getFirstInstance(int bitLength, BigInteger orderFactor) {
		BigInteger candidate = MathUtil.powerOfTwo(bitLength - 1).add(MathUtil.ONE);
		BigInteger increase;
		if (orderFactor.equals(MathUtil.TWO)) {
			increase = MathUtil.TWO;
		} else {
			increase = orderFactor.shiftLeft(1);
			// the smallest possible value is 2*divisor+1
			candidate = candidate.max(increase.add(MathUtil.ONE));
			// compute the smallest possible candidate
			if (!candidate.mod(increase).equals(MathUtil.ONE)) {
				candidate = candidate.subtract(MathUtil.ONE).divide(increase).add(MathUtil.ONE).multiply(increase).add(MathUtil.ONE);
			}
		}
		while (!MathUtil.isPrime(candidate)) {
			candidate = candidate.add(increase);
		}
		return new Prime(candidate, orderFactor);
	}

	/**
	 * Creates a new random prime number of a given bit length using the library's default source of randomness.
	 * <p>
	 * @param bitLength The bit length
	 * @return The new prime
	 */
	public static Prime getRandomInstance(int bitLength) {
		return Prime.getRandomInstance(bitLength, RandomNumberGenerator.getInstance());
	}

	/**
	 * Creates a new random prime number of a given bit length using a given source of randomness.
	 * <p>
	 * @param bitLength             The bit length
	 * @param randomNumberGenerator The given source of randomness
	 * @return The new prime
	 */
	public static Prime getRandomInstance(int bitLength, RandomNumberGenerator randomNumberGenerator) {
		if (bitLength < 2 || randomNumberGenerator == null) {
			throw new IllegalArgumentException();
		}
		BigInteger candidate;
		do {
			candidate = randomNumberGenerator.nextBigInteger(bitLength);
		} while (!MathUtil.isPrime(candidate));
		return Prime.getInstance(candidate);
	}

}
