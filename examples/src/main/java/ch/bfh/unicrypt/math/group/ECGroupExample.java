package ch.bfh.unicrypt.math.group;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.additive.classes.ECGroupElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECGroupFp;
import ch.bfh.unicrypt.math.algebra.additive.classes.SafeECGroupFp;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.utility.MathUtil;

public class ECGroupExample {
	
	public static void main(String[] args){
		/*SafeECGroupFp ec1=SafeECGroupFp.getInstance("test");
		Field f=ec1.getFiniteField();
		DualisticElement px=f.getElement(new BigInteger("5"));
		DualisticElement py=f.getElement(new BigInteger("1"));
		DualisticElement qx=f.getElement(new BigInteger("3"));
		DualisticElement qy=f.getElement(new BigInteger("8"));
		ECGroupElement p =ec1.getElement(px, py);
		ECGroupElement q =ec1.getElement(qx, qy);
		
		//Resultat geprüft mit http://christelbach.com/ECCalculator.aspx
		ECGroupElement w=p.add(q);	//(1,11)
		System.out.println(w);
		w=w.add(q);				//(8,6) -> Identity
		System.out.println(w);
		w=w.add(p);					//(10,8)
		System.out.println(w);
		System.out.println(w.getValue());*/
		
		SafeECGroupFp ec2=SafeECGroupFp.getInstance("secp192r1");
		ECGroupElement g2=ec2.getDefaultGenerator();
		BigInteger n2=ec2.getOrder();
		System.out.println(g2.selfApply(n2));		//Must be Identity (-1,-1)
		System.out.println(MathUtil.arePrime(n2));	//Must be true
		
		ECGroupElement w2=ec2.getRandomElement();
		System.out.println(w2);
		System.out.println(w2.selfApply(n2));	//Must be Identity (-1,-1) if h=1

		
	}

}
