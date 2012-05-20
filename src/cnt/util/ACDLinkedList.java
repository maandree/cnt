/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.util;

import java.util.*;
import java.io.Serializable;


/**
 * <p>Associative circularly doubly linked list.</p>
 * <p>
 *    You should have an item more than once at the same time.
 * </p>
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class ACDLinkedList<T> extends CDLinkedList<T>
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    //Has default constructor
    
    
    
    /**
     * Mapping from item to their (last inserted) node
     */
    protected final HashMap<T, ListNode<T>> nodeMap = new HashMap<T, ListNode<T>>();
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
	super.clear();
	this.nodeMap.clear();
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ListNode<T> insertAfter(final T item, final ListNode<T> cursor)
    {
	final ListNode<T> rc = super.insertAfter(item, cursor);
	this.nodeMap.put(item, rc);
	return rc;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ListNode<T> insertBefore(final T item, final ListNode<T> cursor)
    {
	final ListNode<T> rc = super.insertBefore(item, cursor);
	this.nodeMap.put(item, rc);
	return rc;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final ListNode<T> node)
    {
	super.remove(node);
	this.nodeMap.remove(node.item);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ListNode<T> find(final T item)
    {
	final ListNode<T> rc = this.nodeMap.get(item);
	if (rc == null)
	    throw new NoSuchElementException();
	return rc;
    }
    
    
    /**
     * Gets whether the list contains an item
     * 
     * @param   item  The item
     * @return        Whether the list contains an item
     */
    public boolean contains(final T item) {
	return this.nodeMap.containsKey(item);
    }
    
    
    //inherit toString() from superclass.

}

