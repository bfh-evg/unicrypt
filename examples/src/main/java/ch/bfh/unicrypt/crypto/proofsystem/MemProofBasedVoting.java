/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.crypto.proofsystem;

import ch.bfh.unicrypt.crypto.proofsystem.classes.DoubleDiscreteLogProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.EqualityPreimageProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.classes.PolynomialMembershipProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.Alphabet;
import ch.bfh.unicrypt.helper.MathUtil;
import ch.bfh.unicrypt.helper.Polynomial;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModPrime;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.classes.CounterModeRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author philipp
 */
public class MemProofBasedVoting {

	// P1 = 2*Q1+1 / O1 = 325*P1+1 (all prime, 1023/1024/1024+ bit)
	final static String Q1 = "62419754450729612647565739452383276575857601899739936725159851334944150841968063259516646199602063446032019699733384807429406029957259175802747488347623169473252390835604139741777023566843030652585465424928737851640689453666654947197163915037792214904944077385094485372296355878304667660119111336076574627993";
	final static String P1 = "124839508901459225295131478904766553151715203799479873450319702669888301683936126519033292399204126892064039399466769614858812059914518351605494976695246338946504781671208279483554047133686061305170930849857475703281378907333309894394327830075584429809888154770188970744592711756609335320238222672153149255987"; //1024
	final static String O1 = "40448000884072788995622599165144363221155726031031478997903583665043809745595304992166786737342137113028748765427233355214255107412303945920180372449259813818667549261471482552671511271314283862875381595353822127863166765975992405783762216944489355258403762145541226521248038609141424643757184145777620358939789";

	// P2 = 2*Q2+1 / O2 = 3157*P2+1 (all prime, 2047/2048/2048+ bit)
	final static String Q2 = "16158503035655503650357438344334975980222051334857742016065172713762327569433945446598600705761456731844358980460949009747059779575245460547544076193224141560315438683650498045875098875194826053398028819192033784138396109321309878080919047169238085235290822926018152521443787945770532904303776199561965192760957166694834171210342487393282284747428088017663161029038902829665513096354230157075129296432088558362971801859230928678799175576150822952201848806616643615613562842355410104862578550863465661734839271290328348967522998634176499319107762583194718667771801067716614802322659239302476074096777926805529797144183";
	final static String P2 = "32317006071311007300714876688669951960444102669715484032130345427524655138867890893197201411522913463688717960921898019494119559150490921095088152386448283120630877367300996091750197750389652106796057638384067568276792218642619756161838094338476170470581645852036305042887575891541065808607552399123930385521914333389668342420684974786564569494856176035326322058077805659331026192708460314150258592864177116725943603718461857357598351152301645904403697613233287231227125684710820209725157101726931323469678542580656697935045997268352998638215525166389437335543602135433229604645318478604952148193555853611059594288367";
	final static String O2 = "101992471161057539041056150829442368387161588025622067605403370169267811618267063658930367654766314891401593884669510149523441328678949346976098208931630781528711048971201943665563624100229742049048357906740117245481556242036107950446761025732230794005155674309026578715353189513703603691965435371635124296707161636177793288679681780426397781325766091567489872415293554660848718664187900751458216119079342980387078013335465621820580396236663994474298069667364254501752808660947348581892595813050195256870305480384552538683005167378922063702208197425125064230975608339427272632260625118477228979698862273996504079574086253";

	final static int SECURITY_FACTOR = 80;

	// P = a*Q+1 / O = 981*P+1 (all prime, 160/1024/1034 bit)
	final static String Q = "1081119563825030427708677600856959359670713108783";
	final static String P = "132981118064499312972124229719551507064282251442693318094413647002876359530119444044769383265695686373097209253015503887096288112369989708235068428214124661556800389180762828009952422599372290980806417384771730325122099441368051976156139223257233269955912341167062173607119895128870594055324929155200165347329";
	final static String O = "130321495703209326712681745125160476922996606413839451732525374062818832339517055163873995600381772645635265067955193809354362350122589914070367059649842168325664381397147571449753374147384845161190289037076295718619657452540690936633016438792088604556794094343720930134977497226293182174218430572096162040382421";

	final static int size = 10000;

	//
	//  #Credentials = 1'000'000
	// ============================
	// Voter: Registering...
	// Voter: Registering done.
	//   > Time: 10607
	// Voter: Ballot casting...
	//   > Time pi_1: 197796
	//   > Time pi_2: 945
	//   > Time pi_3: 6
	// Voter: Ballot casting done.
	//   > Time: 198753
	// Start verifying...
	// Ballot 1 is valid
	//   > Time: 123580
	//
	//
	public static void main(String[] args) {

		// 0. Preparation
		final CyclicGroup G_p = GStarModPrime.getInstance(new BigInteger(O, 10), new BigInteger(P, 10));
		final CyclicGroup G_q = GStarModPrime.getInstance(new BigInteger(P, 10), new BigInteger(Q, 10));
		final BulletinBoard BB = new BulletinBoard(G_p, G_q, 1, size);

		Voter voter = new Voter("Voter X", G_p, G_q, BB);

		// 1. Register
		long time = System.currentTimeMillis();
		voter.register();
		System.out.println("  > Time: " + (System.currentTimeMillis() - time));

		// 2. Cast Ballot
		time = System.currentTimeMillis();
		voter.castBallot();
		System.out.println("  > Time: " + (System.currentTimeMillis() - time));

		// 3. Verify casted ballots
		Verifier verifier = new Verifier(G_p, G_q, BB);
		time = System.currentTimeMillis();
		verifier.verifyBallots();
		System.out.println("  > Time: " + (System.currentTimeMillis() - time));

	}

	private static class Voter {

		private final CyclicGroup G_p;
		private final CyclicGroup G_q;

		private final BulletinBoard BB;

		private final String voterId;

		private Element alpha;
		private Tuple betas;
		private Element u;

		private Voter(final String voterId, final CyclicGroup G_p, final CyclicGroup G_q, final BulletinBoard BB) {
			this.G_p = G_p;
			this.G_q = G_q;

			this.voterId = voterId;
			this.BB = BB;
		}

		public void register() {
			System.out.println("Voter: Registering...");
			this.alpha = this.G_q.getZModOrder().getRandomElement();
			Element[] bs = new Element[this.BB.getNumberOfPrivateCredentials()];
			for (int i = 0; i < bs.length; i++) {
				bs[i] = this.G_q.getZModOrder().getRandomElement();
			}
			this.betas = Tuple.getInstance(bs);
			Element u = BB.getComQ().getMessageGenerators().getAt(0).selfApply(this.alpha);
			for (int i = 0; i < this.betas.getArity(); i++) {
				u = u.apply(BB.getComQ().getMessageGenerators().getAt(i + 1).selfApply(this.betas.getAt(i)));
			}
			this.u = u;

			BB.postCredential(this.voterId, this.u);
			System.out.println("Voter: Registering done.");
		}

		public void castBallot() {
			System.out.println("Voter: Ballot casting...");
			final RandomByteSequence randomGenerator = CounterModeRandomByteSequence.getInstance(ByteArray.getInstance((byte) 7));
			Element v = StringMonoid.getInstance(Alphabet.ALPHANUMERIC).getElement("YES");
			Element uHat = this.BB.getGHat().selfApply(this.betas.getAt(0));

			// pi_1
			long time = System.currentTimeMillis();
			Element uInZp = this.G_p.getZModOrder().getElement(this.u.getBigInteger());
			Element r = this.G_p.getZModOrder().getRandomElement(randomGenerator);
			Element cu = this.BB.getComP().commit(uInZp, r);

			PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(this.BB.getCredentialPolynomial(), this.BB.getComP());
			Tuple pi1 = pmps.generate(Tuple.getInstance(uInZp, r), cu);
			System.out.println("  > Time pi_1: " + (System.currentTimeMillis() - time));

			// pi_2
			time = System.currentTimeMillis();
			Element s = this.G_q.getZModOrder().getRandomElement();
			Tuple m = this.betas.insertAt(0, this.alpha);
			Element cab = this.BB.getComQ().commit(m, s);
			DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(this.BB.getComP(), this.BB.getComQ(), SECURITY_FACTOR);
			Triple pi2 = ddlps.generate(Tuple.getInstance(uInZp, r, s, m), Pair.getInstance(cu, cab));
			System.out.println("  > Time pi_2: " + (System.currentTimeMillis() - time));

			// pi_3
			time = System.currentTimeMillis();
			ProductSet space = (ProductSet) this.BB.getComQ().getCommitmentFunction().getDomain();
			Function f = CompositeFunction.getInstance(SelectionFunction.getInstance(space, 0, 1), GeneratorFunction.getInstance(this.BB.getGHat()));
			EqualityPreimageProofSystem apps = EqualityPreimageProofSystem.getInstance(this.BB.getComQ().getCommitmentFunction(), f);

			Triple pi3 = apps.generate(Tuple.getInstance(m, s), Tuple.getInstance(cab, uHat));
			System.out.println("  > Time pi_3: " + (System.currentTimeMillis() - time));

			Tuple proof = Tuple.getInstance(pi1, pi2, pi3);
			Tuple ballot = Tuple.getInstance(v, Triple.getInstance(cu, cab, uHat), proof);
			this.BB.postBallot(ballot);
			System.out.println("Voter: Ballot casting done.");
		}

	}

	private static class Verifier {

		private final CyclicGroup G_p;
		private final CyclicGroup G_q;

		private final BulletinBoard BB;

		private Verifier(final CyclicGroup G_p, final CyclicGroup G_q, final BulletinBoard BB) {
			this.G_p = G_p;
			this.G_q = G_q;
			this.BB = BB;
		}

		public void verifyBallots() {
			System.out.println("Start verifying...");
			PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(this.BB.getCredentialPolynomial(), this.BB.getComP());
			DoubleDiscreteLogProofSystem ddlps = DoubleDiscreteLogProofSystem.getInstance(this.BB.getComP(), this.BB.getComQ(), SECURITY_FACTOR);

			ProductSet space = (ProductSet) this.BB.getComQ().getCommitmentFunction().getDomain();
			Function f = CompositeFunction.getInstance(SelectionFunction.getInstance(space, 0, 1), GeneratorFunction.getInstance(this.BB.getGHat()));
			EqualityPreimageProofSystem apps = EqualityPreimageProofSystem.getInstance(this.BB.getComQ().getCommitmentFunction(), f);

			Tuple ballots = this.BB.getBallots();
			for (int i = 0; i < ballots.getArity(); i++) {
				Tuple ballot = (Tuple) ballots.getAt(i);
				Element v = ballot.getAt(0);
				Triple coms = (Triple) ballot.getAt(1);
				Triple proof = (Triple) ballot.getAt(2);

				boolean verify = pmps.verify(proof.getFirst(), coms.getFirst());
				verify = verify && ddlps.verify(proof.getSecond(), Pair.getInstance(coms.getFirst(), coms.getSecond()));
				verify = verify && apps.verify(proof.getThird(), Pair.getInstance(coms.getSecond(), coms.getThird()));

				System.out.println("Ballot " + (i + 1) + " is " + (verify ? "valid" : "INVALID"));
			}
		}

	}

	private static class BulletinBoard {

		private final CyclicGroup G_q;
		private final ZMod Z_p;
		private PolynomialElement credentialPolynomial;
		private final ArrayList<Element> credentials;
		private final ArrayList<Element> ballots;

		private final Tuple generators;
		private final Element gHat;
		private final PedersenCommitmentScheme com_p;
		private final GeneralizedPedersenCommitmentScheme com_q;

		public BulletinBoard(CyclicGroup G_p, CyclicGroup G_q, int numberOfPrivateCredentials, int numberOfInitialCredentials) {
			this.G_q = G_q;
			this.Z_p = G_p.getZModOrder();
			this.credentials = new ArrayList<>();
			this.ballots = new ArrayList<>();

			this.credentialPolynomial = PolynomialSemiRing.getInstance(Z_p).getRandomElement(numberOfInitialCredentials);

			Element[] gs = new Element[numberOfPrivateCredentials + 1];
			for (int i = 0; i < gs.length; i++) {
				gs[i] = G_q.getRandomGenerator();
			}

			this.generators = Tuple.getInstance(
				   G_p.getRandomGenerator(),
				   G_p.getRandomGenerator(),
				   G_q.getRandomGenerator(),
				   Tuple.getInstance(gs));

			this.gHat = G_q.getRandomGenerator();

			this.com_p = PedersenCommitmentScheme.getInstance(this.generators.getAt(0), this.generators.getAt(1));
			this.com_q = GeneralizedPedersenCommitmentScheme.getInstance(this.generators.getAt(2), (Tuple) this.generators.getAt(3));

		}

		public Element getGHat() {
			return this.gHat;
		}

		public int getNumberOfPrivateCredentials() {
			return this.com_q.getSize() - 1;
		}

		public PedersenCommitmentScheme getComP() {
			return this.com_p;
		}

		public GeneralizedPedersenCommitmentScheme getComQ() {
			return this.com_q;
		}

		public void postCredential(final String voterId, final Element credential) {
			if (credential == null || !credential.getSet().isEquivalent(this.G_q)) {
				throw new IllegalArgumentException();
			}
			this.credentials.add(this.Z_p.getElement(credential.getBigInteger()));

			DualisticElement zero = this.Z_p.getZeroElement();
			DualisticElement one = this.Z_p.getOneElement();
			Polynomial newRoot = Polynomial.getInstance(new DualisticElement[]{(DualisticElement) this.Z_p.getElement(credential.getBigInteger()).invert(), one}, zero, one);
			this.credentialPolynomial = this.credentialPolynomial.multiply(PolynomialSemiRing.getInstance(this.Z_p).getElement(newRoot));
		}

		public Subset getCredentials() {
			return Subset.getInstance(this.Z_p, this.credentials.toArray(new Element[0]));
		}

		public PolynomialElement getCredentialPolynomial() {
			return this.credentialPolynomial;
		}

		public void postBallot(Element ballot) {
			if (ballot == null) {
				throw new IllegalArgumentException();
			}
			this.ballots.add(ballot);
		}

		public Tuple getBallots() {
			return Tuple.getInstance(this.ballots.toArray(new Element[0]));
		}

	}

	public static void createParams() {
		BigInteger n = BigInteger.valueOf(4);
		int i = 0;
		while (!MathUtil.isPrime(n)) {
			n = new BigInteger(P, 10).multiply(BigInteger.valueOf(i)).add(BigInteger.ONE);
			i++;
		}
		System.out.println("O: " + i + " * P + 1= " + n);

		System.out.println("P/Q: " + (new BigInteger(P, 10)).subtract(BigInteger.ONE).divide(new BigInteger(Q, 10)));

		System.out.println("#Bit Q:" + (new BigInteger(Q, 10)).bitLength() + " Prime? " + MathUtil.isPrime(new BigInteger(Q, 10)));
		System.out.println("#Bit P:" + (new BigInteger(P, 10)).bitLength() + " Prime? " + MathUtil.isPrime(new BigInteger(P, 10)));
		System.out.println("#Bit O:" + n.bitLength());

	}

}
