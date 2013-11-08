package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.utility.MathUtil;

public class ECGroupFp extends ECGroup {

	protected ECGroupFp(ZModPrime Finitefiled, DualisticElement a,
			DualisticElement b, DualisticElement gx, DualisticElement gy,
			BigInteger order, BigInteger h) {
		super(Finitefiled, a, b, gx, gy, order, h);
	}
	
	protected ECGroupFp(ZModPrime Finitefiled, DualisticElement a,
			DualisticElement b, BigInteger order, BigInteger h) {
		super(Finitefiled, a, b, order, h);
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
		else if(p.equals(q.invert())){
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
	
	@Override
	protected ECGroupElement abstractInvert(Element element) {
		ECGroupElement r = (ECGroupElement) element;
	
		if (r.isZero()) {
			return this.getIdentityElement();
		}
	
		return new ECGroupElement(this, r.getX(), r.getY().invert());
	}

	@Override
	protected Boolean contains(DualisticElement x, DualisticElement y) {
		y = y.power(2);
		x = x.power(3).add(x.multiply(this.getA())).add(this.getB());

		return y.equals(x);
	}

	@Override
	protected ECGroupElement getRandomElementWithoutGenerator(Random random) {
		BigInteger p=((ZModPrime) this.getFiniteField()).getModulus();
		DualisticElement x=this.getFiniteField().getRandomElement(random);
		DualisticElement y=x.power(3).add(this.getA().multiply(x)).add(this.getB());
		boolean neg=x.getValue().mod(new BigInteger("2")).equals(BigInteger.ONE);
		
		while(!MathUtil.hasSquareRootModp(y.getValue(),p )){
			x=this.getFiniteField().getRandomElement(random);
			y=x.power(3).add(this.getA().multiply(x)).add(this.getB());
		}
		
		//if neg is true return solution 2(p-sqrt) of sqrtModp else solution 1
		if(neg){
			y=this.getFiniteField().getElement(p.subtract(MathUtil.sqrtModp(y.getValue(), p)));
		}
		else{
			y=this.getFiniteField().getElement(MathUtil.sqrtModp(y.getValue(), p));
		}
		
		return this.getElement(x, y);
	}

	@Override
	protected boolean isValid() {
		boolean c1,c2,c3,c4,c5,c61,c62;
		
		DualisticElement i4= getFiniteField().getElement(4);
		DualisticElement i27=getFiniteField().getElement(27);
		c1=!getA().power(3).multiply(i4).add(i27.multiply(getB().power(2))).equals(BigInteger.ZERO);
		
		c2=contains(this.getDefaultGenerator());
		
		c3=MathUtil.arePrime(getOrder());
		
		c4=0>=getH().compareTo(new BigInteger("4"));
		
		c5=getIdentityElement().equals(getDefaultGenerator().selfApply(getOrder()));
		
		c61=true; //_> Must be corrected!
		for (int i = 1; i < 20; i++) {
			
		}
		
		c62=!getOrder().multiply(getH()).equals(getP());
		
		return c1 && c2 && c3 && c4 && c5 && c61 && c62;
	}

	/**
	 * Return modulus of the finite field ZModPrime
	 * @return
	 */
	protected BigInteger getP(){
		return  ((ZMod) this.getFiniteField()).getModulus();
	}
	
	@Override
	public String toString(){
		return "y²=x³+"+getA().getValue()+"x+"+getB().getValue();
	}

	/**
	 * Returns an elliptic curve over Fp y²=x³+ax+b
	 * @param f Finite field of type ZModPrime
	 * @param a Element of Fp respresnting a in the curve equation
	 * @param b Element of Fp respresnting b in the curve equation
	 * @param order Order of the the used subgroup
	 * @param h Co-factor h*order= N -> total order of the group
	 * @return
	 */
	public static ECGroupFp getInstance(ZModPrime f,DualisticElement a, DualisticElement b, BigInteger order, BigInteger h){
		return new ECGroupFp(f, a, b, order, h);
	}
	
	/**
	 * Returns an elliptic curve over Fp y²=x³+ax+b
	 * @param f Finite field of type ZModPrime
	 * @param a Element of Fp respresnting a in the curve equation
	 * @param b Element of Fp respresnting b in the curve equation
	 * @param gx x-coordinate of the generator
	 * @param gy y-coordinate of the generator
	 * @param order Order of the the used subgroup
	 * @param h Co-factor h*order= N -> total order of the group
	 * @return
	 */
	public static ECGroupFp getInstance(ZModPrime f,DualisticElement a, DualisticElement b, DualisticElement gx, DualisticElement gy, BigInteger order, BigInteger h){
		return new ECGroupFp(f, a, b,gx, gy, order, h);
	}

}
