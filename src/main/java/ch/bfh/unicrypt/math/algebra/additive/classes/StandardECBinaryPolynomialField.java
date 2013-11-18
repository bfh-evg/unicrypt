package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.params.classes.SECECCParamsF2m;
import ch.bfh.unicrypt.math.params.classes.SECECCParamsFp;
import ch.bfh.unicrypt.math.params.interfaces.StandardECBinaryPolinomialFieldParams;
import ch.bfh.unicrypt.math.params.interfaces.StandardECZModParams;

public class StandardECBinaryPolynomialField extends ECBinaryPolynomialField {

	public StandardECBinaryPolynomialField(BinaryPolynomialField finiteField, BinaryPolynomialElement a,
			BinaryPolynomialElement b, BinaryPolynomialElement gx, BinaryPolynomialElement gy,
			BigInteger order, BigInteger h) {
		super(finiteField, a, b, gx, gy, order, h);
	}
	
	  public static StandardECBinaryPolynomialField getInstance(final StandardECBinaryPolinomialFieldParams params) {
		    BinaryPolynomialField field;
		    BinaryPolynomialElement a, b, gx, gy;
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
	  
	  public static void main(String[] args) {
		    

		    for (SECECCParamsF2m params : SECECCParamsF2m.values()) {
				
		      StandardECBinaryPolynomialField ec = StandardECBinaryPolynomialField.getInstance(params);
		      System.out.println(params.name() + "(\"" + ec.getA().getValue().toString(16) + "\",\"" + ec.getB().getValue().toString(16) + "\",\"" + ec.getDefaultGenerator().getX().getValue().toString(16) + "\",\"" + ec.getDefaultGenerator().getY().getValue().toString(16) + "\",\"" + ec.getOrder().toString(16) + "\",\"" + ec.getH() + "\"),");
		    }
		  }

}
