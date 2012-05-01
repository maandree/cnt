/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.test;
import cnt.util.*;


/**
 * Test class for {@link ACDLinkedList}
 * 
 * @author  Peyman Eshtiagh
 */
public class ACDLinkedListTest
{
    public static void main(final String... args)
    {
	ACDLinkedList linkedList = new ACDLinkedList();
	String P= "Peyman";
	String C= "Calle";
	String M= "Matte";
	String F= "Test";
	linkedList.insertAfter(P);
	linkedList.insertAfter(C);
	linkedList.insertAfter(M);
	
	System.out.println(linkedList.contains(P));
	System.out.println(linkedList.contains(C));
	System.out.println(linkedList.contains(M));
	linkedList.remove(linkedList.find("Matte"));
	System.out.println(linkedList.contains(M));
	linkedList.insertBefore(F);
    }    
}

