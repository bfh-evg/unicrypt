package ch.bfh.unicrypt.math.group;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.additive.classes.ECGroupElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECGroupFp;
import ch.bfh.unicrypt.math.algebra.additive.classes.SafeECGroupFp;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.utility.MathUtil;

public class ECGroupExample {
	
	public static void main(String[] args){
		
		/*
		//Example 1
		SafeECGroupFp ec1=SafeECGroupFp.getInstance("test");
		FiniteField f=ec1.getFiniteField();
		DualisticElement px=f.getElement(new BigInteger("5"));
		DualisticElement py=f.getElement(new BigInteger("1"));
		DualisticElement qx=f.getElement(new BigInteger("3"));
		DualisticElement qy=f.getElement(new BigInteger("8"));
		ECGroupElement p =ec1.getElement(px, py);
		ECGroupElement q =ec1.getElement(qx, qy);
		
		//Results checked with http://christelbach.com/ECCalculator.aspx
		ECGroupElement w=p.add(q);	//(1,11)
		System.out.println(w);
		w=w.add(q);				//(8,6) -> Identity
		System.out.println(w);
		w=w.add(p);					//(10,8)
		System.out.println(w);
		System.out.println(w.getValue());
		*/
		
		
		//Example 2
		SafeECGroupFp ec2=SafeECGroupFp.getInstance("secp224r1");
		ECGroupElement g2=ec2.getDefaultGenerator();
		ec2.getRandomElement();
		BigInteger n2=ec2.getOrder();
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
		System.out.println("Computing time for n2*w2= "+t1+" ms");
		
		
		
		/*
		//Example Tonelli_Skanks
		BigInteger r2=new BigInteger("659EF8BA043916EEDE8911702B20",16);
		BigInteger p2=new BigInteger("DB7C2ABF62E35E668076BEAD208B",16);
		BigInteger result = MathUtil.sqrtModp(r2,p2); // =+-17 mod 41
		
		System.out.println("-+ "+result);
		*/
		
		
		/*
		//Example 3 with "own" ECGroupFp
		ZModPrime field=ZModPrime.getInstance(new BigInteger("DB7C2ABF62E35E668076BEAD208B",16));
		DualisticElement a1=field.getElement(new BigInteger("DB7C2ABF62E35E668076BEAD2088",16));
		DualisticElement b1=field.getElement(new BigInteger("659EF8BA043916EEDE8911702B22",16));
		BigInteger order=new BigInteger("DB7C2ABF62E35E7628DFAC6561C5",16);
		BigInteger h=new BigInteger("1");
		ECGroupFp ec3=ECGroupFp.getInstance(field, a1, b1, order, h);
		ECGroupElement gen3=ec3.getDefaultGenerator();
		System.out.println(gen3);
		System.out.println(gen3.selfApply(h).selfApply(order));
		*/
		
		
	}


}
