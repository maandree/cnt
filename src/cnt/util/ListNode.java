/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 *
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.util;


/**
 * Doubly linked node
 * 
 * @author  Peyman Eshtiagh
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ListNode<T>
{
    /**
     * <p>Constructor</p>
     * <p>
     *   The node will be linked to itself
     * </p>
     * 
     * @param  item  The item of th node
     */
    public ListNode(final T item)
    {
	this(null, item, null);
	this.previous = this.next = this;
    }
    
    /**
     * Constructor
     * 
     * @param  previous  The previous node in the list
     * @param  item      The item of th node
     * @param  next      The next node in the list
     */
    public ListNode(final ListNode<T> previous, final T item, final ListNode<T> next)
    {
	this.previous = previous;
	this.item     = item;
	this.next     = next;
    }
    
    
    
    /**
     * The item of the node
     */
    protected T item;
    
    /**
     * The previous node in the list
     */
    protected ListNode<T> previous;
    
    /**
     * The next node in the list
     */
    protected ListNode<T> next;
    
    
    
    /**
     * Gets the item of the node
     * 
     * @return  The item of the node
     */
    public T getItem()
    {
	return this.item;
    }
    
}

