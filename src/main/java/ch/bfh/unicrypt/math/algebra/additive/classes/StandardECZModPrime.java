package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.params.classes.SECECCParamsFp;
import ch.bfh.unicrypt.math.params.interfaces.StandardECZModParams;
import java.math.BigInteger;

public class StandardECZModPrime
			 extends ECZModPrime {

	private StandardECZModPrime(ZModPrime field, ZModElement a,
				 ZModElement b, ZModElement gx, ZModElement gy,
				 BigInteger order, BigInteger h) {
		super(field, a, b, gx, gy, order, h);
		// TODO Auto-generated constructor stub
	}

	public static StandardECZModPrime getInstance(final StandardECZModParams params) {
		ZModPrime field;
		ZModElement a, b, gx, gy;
		BigInteger order, h;

		field = params.getFiniteField();
		a = params.getA();
		b = params.getB();
		gx = params.getGx();
		gy = params.getGy();
		order = params.getOrder();
		h = params.getH();

		return new StandardECZModPrime(field, a, b, gx, gy, order, h);
	}

	public static void main(String[] args) {

		for (SECECCParamsFp params : SECECCParamsFp.values()) {

			StandardECZModPrime ec = StandardECZModPrime.getInstance(params);
			System.out.println(params.name() + "(\"" + ec.getP().toString(16) + "\",\"" + ec.getA().getValue().toString(16) + "\",\"" + ec.getB().getValue().toString(16) + "\",\"" + ec.getDefaultGenerator().getX().getValue().toString(16) + "\",\"" + ec.getDefaultGenerator().getY().getValue().toString(16) + "\",\"" + ec.getOrder().toString(16) + "\",\"" + ec.getH() + "\"),");
		}
	}

}
