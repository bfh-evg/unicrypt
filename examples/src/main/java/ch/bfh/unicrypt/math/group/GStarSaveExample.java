package ch.bfh.unicrypt.math.group;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;

public class GStarSaveExample {
public static void main(String[] args) {
  GStarSave g=new GStarSaveClass(BigInteger.valueOf(23));
  System.out.println(g.getOrderGroup());  
  Element generator=g.getDefaultGenerator();
  for(int i=1;i<g.getOrderGroup().getOrder().intValue();i++){
    System.out.println(generator.selfApply(i));
  }
  Set<Element> s=new HashSet<Element>();
  while(s.size()<g.getOrder().intValue())
  {
    Element e=g.createRandomElement();
    if(s.add(e))
      System.out.println(e);
  }  
  
  
}
}
