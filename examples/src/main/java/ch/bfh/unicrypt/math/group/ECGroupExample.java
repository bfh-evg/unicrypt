package ch.bfh.unicrypt.math.group;

import java.math.BigDecimal;
import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrimeElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.StandardECZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.params.classes.SECECCParamsFp;
import ch.bfh.unicrypt.math.params.interfaces.StandardECZModParams;
import ch.bfh.unicrypt.math.utility.MathUtil;

public class ECGroupExample {
	
	public static void main(String[] args){
		
		
		//Example 1
		/*
		StandardECZModPrime ec1=StandardECZModPrime.getInstance(SECECCParamsFp.secp224k1);
		ECZModPrimeElement g1=ec1.getDefaultGenerator();
		ec1.getRandomElement();
		BigInteger n2=ec1.getOrder();
		System.out.println(g1.selfApply(n2));		//Must be Identity (-1,-1)
		System.out.println(MathUtil.arePrime(n2));	//Must be true
		
		long t1=System.currentTimeMillis();
		ECZModPrimeElement w2=ec1.getRandomElement();
		t1=System.currentTimeMillis()-t1;
		System.out.println(w2);
		System.out.println("Computing time for random element w2= "+t1+" ms");
		t1=System.currentTimeMillis();
		w2=w2.selfApply(n2);
		t1=System.currentTimeMillis()-t1;
		System.out.println(w2);	//Must be Identity (-1,-1) if h=1
		System.out.println("Computing time for n2*w2= "+t1+" ms");
		*/
		
		
		/*
		//Example Tonelli_Skanks
		BigInteger r2=new BigInteger("659EF8BA043916EEDE8911702B20",16);
		BigInteger p2=new BigInteger("DB7C2ABF62E35E668076BEAD208B",16);
		BigInteger result = MathUtil.sqrtModp(r2,p2); // =+-17 mod 41
		
		System.out.println("-+ "+result);
		*/
		
		
		/*
		//Example 2 with "own" ECGroupFp
		ZModPrime field2=ZModPrime.getInstance(new BigInteger("DB7C2ABF62E35E668076BEAD208B",16));
		ZModElement a2=field2.getElement(new BigInteger("DB7C2ABF62E35E668076BEAD2088",16));
		ZModElement b2=field2.getElement(new BigInteger("659EF8BA043916EEDE8911702B22",16));
		BigInteger order2=new BigInteger("DB7C2ABF62E35E7628DFAC6561C5",16);
		BigInteger h2=new BigInteger("1");
		//ECZModPrime ec3=ECZModPrime.getInstance(field, a1, b1, order, h);
		ECZModPrime ec2=ECZModPrime.getInstance(field2, a2, b2, order2, h2);
		ECZModPrimeElement gen2=ec2.getDefaultGenerator();
		System.out.println(gen2);
		System.out.println(gen2.selfApply(h2).selfApply(order2));
		*/
		
		/*StandardECZModPrime ec=StandardECZModPrime.getInstance(SECECCParamsFp.secp112r1);
		BigInteger order=ec.getOrder();
		ECZModPrimeElement e1=ec.getDefaultGenerator();
		ECZModPrimeElement e2=e1.selfApply(order.subtract(BigInteger.ONE));
		e2=e2.apply(e1);
		
		System.out.println(e2);
		*/
		
		
	}


}
