/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
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
    public void clear() {
	this.head = null;
    }
    
    
    /**
     * Gets whether the list is empty
     * 
     * @return  <code>true</code> if the list is empty, otherwise <code>false</code>
     */
    public boolean isEmpty() {
	return this.head == null;
    }
    
    
    /**
     * Inserts a new item after current
     * 
     * @param  item  The item to add to the list
     */
    public ListNode<T> insertAfter(final T item) {
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
    public ListNode<T> insertBefore(final T item) {
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
    public void remove() {
	remove(this.head);
    }
    
    /**
     * Removes a node from the list
     * 
     * @param  node  The node to remove
     */
    public void remove(final ListNode<T> node)
    {
	if ((node.previous == node.next) && (node.next == node))
	    this.clear();
	else
	{
	    if (this.head == node)
		this.head = node.previous;
	    
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
	
	 if (current.item.equals(item))
	     return current;
	
	while (current.next != this.head) // There are still elements to be inspected
	{
	    current = current.next;
	    if (current.item.equals(item))
		return current;
	}
	
	throw new NoSuchElementException();
    }
    
    
    /**
     * Rotate the list one step forward
     */
    public void next() {
	if (this.head != null)
	    this.head = this.head.next;
    }
    
    
    /**
     * Rotate the list one step backward
     */
    public void previous() {
	if (this.head != null)
	    this.head = this.head.previous;
    }
    
    
    /**
     * Rotates the list to another node by jumping to it
     * 
     * @param  node  The node
     */
    public void jump(final ListNode<T> node) {
	this.head = node;
    }
    
    
    /**
     * Gets the current item
     * 
     * @return  The current item
     */
    public T get() {
	return this.getNode().item;
    }
    
    /**
     * Gets the current node
     * 
     * @return  The current node
     */
    public ListNode<T> getNode() {
	return this.head;
    }
    
    
    /**
     * Gets the next item
     * 
     * @return  The next item
     */
    public T getNext() {
	return this.getNextNode().item;
    }
    
    /**
     * Gets the next node
     * 
     * @return  The next node
     */
    public ListNode<T> getNextNode() {
	return this.head.next;
    }
    
    
    /**
     * Gets the previous item
     * 
     * @return  The previous item
     */
    public T getPrevious() {
	return this.getPreviousNode().item;
    }
    
    /**
     * Gets the previous node
     * 
     * @return  The previous node
     */
    public ListNode<T> getPreviousNode() {
	return this.head.previous;
    }
    
    
    /**
     * Returns an enumeration of the elements. Use the {@link Enumeration}
     * methods on the returned object to fetch the elements sequentially.
     * 
     * @param  An enumeration of the elements
     */
    public Enumeration<T> elements() {
	return new ListEnumerator<T>(this);
    }
    
    /**
     * Returns an iterator over the elements. Use the {@link Iterator}
     * methods on the returned object to fetch the elements sequentially.
     * 
     * @param  An enumeration of the elements
     */
    public Iterator<T> iterator() {
	return new ListEnumerator<T>(this);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
	if (this.head == null)
	    return "[empty]";
	
	final StringBuilder buf = new StringBuilder("[items: ");
	ListNode<T> cursor = this.head;
	
	for (;;)
	{
	    buf.append(cursor.getItem().toString());
	    if ((cursor = cursor.next) == this.head)
		break;
	    buf.append(", ");
	}
	
	buf.append("]");
	return buf.toString();
    }

}

