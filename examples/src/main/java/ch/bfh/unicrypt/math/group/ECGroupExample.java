package ch.bfh.unicrypt.math.group;

import java.math.BigInteger;
import java.security.spec.ECField;
import java.security.spec.EllipticCurve;

import com.sun.corba.se.impl.orb.ParserTable.TestAcceptor1;
import com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler;

import ch.bfh.unicrypt.math.algebra.additive.classes.ECGroupElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECGroupFp;
import ch.bfh.unicrypt.math.algebra.additive.classes.SafeECGroupFp;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
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
		/*BigInteger n2=ec2.getOrder();
		System.out.println(g2.selfApply(n2));		//Must be Identity (-1,-1)
		System.out.println(MathUtil.arePrime(n2));	//Must be true
		
		long t1=System.currentTimeMillis();
		ECGroupElement w2=ec2.getRandomElement();
		t1=System.currentTimeMillis()-t1;
		System.out.println(w2);
		System.out.println("Computing time for random element w2= "+t1+" ms");
		t1=System.currentTimeMillis();
		w2=w2.selfApply(n2);
		t1=System.currentTimeMillis()-t1;
		System.out.println(w2);	//Must be Identity (-1,-1) if h=1
		System.out.println("Computing time for n2*w2= "+t1+" ms");*/
		
		
		
		//Point finding:
		ZMod f2=(ZMod) ec2.getFiniteField();
		BigInteger two=new BigInteger("2");
		BigInteger a=ec2.getA().getValue();
		BigInteger b=ec2.getB().getValue();
		BigInteger p= f2.getModulus();
		BigInteger x=new BigInteger("188DA80EB03090F67CBF20EB43A18800F4FF0AFD82FF1012",16);
		BigInteger y2=x.pow(3).add(a.multiply(x)).add(b).mod(p);
		BigInteger test=y2.modPow(p.subtract(BigInteger.ONE).divide(two),p);
		
		int i=0;
		int count=0;
		while(!test.equals(BigInteger.ONE)){
			//System.out.println(test);
			 x=x.add(BigInteger.ONE);
			 y2=x.pow(3).add(a.multiply(x)).add(b).mod(p);
			 test=y2.modPow(p.subtract(BigInteger.ONE).divide(two),p);
			 i++;
			 if(test.equals(BigInteger.ONE)){
				 count++;
				 System.out.println(x+" "+y2);
			 }
		}
		
		System.out.println(count);
		BigInteger y=y2.modPow(p.add(BigInteger.ONE).divide(new BigInteger("4")), p);
		System.out.println(y+" "+y.modPow(two, p)+" "+y2);
		
		System.out.println(p.mod(new BigInteger("4")));
		System.out.println(MathUtil.isPrime(p.subtract(BigInteger.ONE).divide(two)));
		
		//TEst Tonelli_Skanks
		BigInteger r2=new BigInteger("2");
		BigInteger p2=new BigInteger("41");
		BigInteger result = MathUtil.sqrtModp(r2,p2); // =+-17 mod 41
		
		System.out.println(result);
	}


}
