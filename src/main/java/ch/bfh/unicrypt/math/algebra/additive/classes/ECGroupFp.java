package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public class ECGroupFp extends ECGroup {

	protected ECGroupFp(ZMod Finitefiled, DualisticElement a,
			DualisticElement b, DualisticElement gx, DualisticElement gy,
			BigInteger order, BigInteger h) {
		super(Finitefiled, a, b, gx, gy, order, h);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected ECGroupElement abstractApply(Element element1, Element element2) 
	{
		DualisticElement s, rx, ry, px, py, qx, qy;
		ECGroupElement p= (ECGroupElement) element1;
		ECGroupElement q=(ECGroupElement) element2;
		px = p.getX();
		py = p.getY();
		qx = q.getX();
		qy = q.getY();
		
		if(p.isZero()){
			return q;
		}
		else if(q.isZero()){
			return p;
		}
		else if(px.equals(qx) && !py.equals(qy)){
			return this.getIdentityElement();
		}
		else if (element1.equals(element2)) {
			DualisticElement three=this.getFiniteField().getElement(3);
			DualisticElement two=this.getFiniteField().getElement(2);
			s =  ((px.power(2).multiply(three)).apply(this.getA())).divide(py.multiply(two));
			rx = s.power(2).apply(px.multiply(two).invert());
			ry = s.multiply(px.subtract(rx)).apply(py.invert());
			return this.getElement(rx, ry);
		} else {
			s =  py.apply(qy.invert()).divide(px.apply(qx.invert()));
			rx = (s.power(2).apply(px.invert()).apply(qx.invert()));
			ry = py.invert().add(s.multiply(px.apply(rx.invert())));
			return this.getElement(rx, ry);
		}

	}
	
	public BigInteger getP(){
		return  ((ZMod) this.getFiniteField()).getModulus();
	}

}
