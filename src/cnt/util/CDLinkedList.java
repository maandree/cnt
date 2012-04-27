/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.util;

import java.util.*;


/**
 * Circularly doubly linked list
 * 
 * @author  Peyman Eshtiagh
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class CDLinkedList<T> implements Iterable<T>
{
    //Has default constructor
    
    
    
    /**
     * The current node
     */
    protected ListNode<T> head = null;
    
    
    
    /**
     * Removes all elements in the list
     */
    public void clear()
    {
	this.head = null;
    }
    
    
    /**
     * Gets whether the list is empty
     * 
     * @return  <code>true</code> if the list is empty, otherwise <code>false</code>
     */
    public boolean isEmpty()
    {
	return this.head == null;
    }
    
    
    /**
     * Inserts a new item after current
     * 
     * @param  item  The item to add to the list
     */
    public ListNode<T> insertAfter(final T item)
    {
	return insertAfter(item, this.head);
    }
    
    
    /**
     * Inserts a new item after another
     * 
     * @param  item    The item to add to the list
     * @param  cursor  The reference node
     */
    public ListNode<T> insertAfter(final T item, final ListNode<T> cursor)
    {
	if (this.head == null)
	    return this.head = new ListNode<T>(item);
	
	final ListNode<T> newItem = new ListNode<T>(cursor, item, cursor.next);
	newItem.next.previous = newItem;
	cursor.next = newItem;
	return newItem;
    }
    
    
    /**
     * Inserts a new item before current
     * 
     * @param  item  The item to add to the list
     */
    public ListNode<T> insertBefore(final T item)
    {
	return insertBefore(item, this.head);
    }
    
    
    /**
     * Inserts a new item before another
     * 
     * @param  item    The item to add to the list
     * @param  cursor  The reference node
     */
    public ListNode<T> insertBefore(final T item, final ListNode<T> cursor)
    {
	if (this.head == null)
	    return this.head = new ListNode<T>(item);
	
	final ListNode<T> newItem = new ListNode<T>(cursor.previous, item, cursor);
	newItem.previous.next = newItem;
	cursor.previous = newItem;
	return newItem;
    }
    
    
    /**
     * Removes the current node from the list
     */
    public void remove()
    {
	remove(this.head);
    }
    
    
    /**
     * Removes a node from the list
     * 
     * @param  node  The node to remove
     */
    public void remove(final ListNode<T> node)
    {
	if (node.previous == node.next) //== node
	    this.clear();
	else
	{
	    node.previous.next = node.next;
	    node.next.previous = node.previous;
	}
    }
    
    
    /**
     * Find the first occurrence of an item in a list
     * 
     * @param   item  The item
     * @return        The first occurrence of the item
     */
    public ListNode<T> find(final T item)
    {
	if (this.isEmpty())
	    throw new NoSuchElementException();
	
	ListNode<T> current = this.head;
	
	while (current.next != this.head) // There are still elements to be inspected
	{
	    current = current.next;
	    if (current.item == item)
		return current;
	}
	
	throw new NoSuchElementException();
    }
    
    
    /**
     * Rotate the list one step forward
     */
    public void next()
    {
	this.head = this.head.next;
    }
    
    
    /**
     * Rotate the list one step backward
     */
    public void previous()
    {
	this.head = this.head.previous;
    }
    
    
    /**
     * Returns an enumeration of the elements. Use the {@link Enumeration}
     * methods on the returned object to fetch the elements sequentially.
     * 
     * @param  An enumeration of the elements
     */
    public Enumeration<T> elements()
    {
	return new ListEnumerator<T>(this);
    }
    
    
    /**
     * Returns an iterator over the elements. Use the {@link Iterator}
     * methods on the returned object to fetch the elements sequentially.
     * 
     * @param  An enumeration of the elements
     */
    public Iterator<T> iterator()
    {
	return new ListEnumerator<T>(this);
    }

}

