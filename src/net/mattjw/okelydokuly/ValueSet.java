/*
 * Author:   Matt J Williams
 *           http://www.mattjw.net
 *           mattjw@mattjw.net
 * Date:     2014
 * License:  MIT License
 */

package net.mattjw.okelydokuly;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/*
 * This class tracks the possible remaining values a particular Sudoku cell may
 * be assigned with; i.e., in CSP parlance, this is the domain of a cell.
 *
 * Internally, the values are stored as Integer objects inside a Set. The class
 * is designed so that, externally, only integer primitives need to be handled. The
 * methods will handle turning the primitives into Integer objects and vice-versa.
 * 
 * A Set is an appropriate data structure for the purpose holding domain values
 * because:
 *  - it will not store duplicate values (if an attempt to add a duplicate value
 *    is made, the Set will simply be unchanged) (similarly, attempts to remove
 *    elements that already exist are allowed (they simply have no effect)) 
 *  - there is no ordering (the order of the values is irrelevant)
 *  - it supports all the necessary operations (add, remove and contains)
 * Furthermore, the Java API's HashSet implementation guarantees constant time
 * complexity for these operations.
 */
public class ValueSet implements Iterable<Integer>
{
    private Set<Integer> set;
    
    public ValueSet()
    {
        set = new HashSet<Integer>();
    }
    
    public boolean add( int val ) 
    {
        Integer valObj = new Integer( val );
        return set.add( valObj );
    }
    
    
    public boolean remove( int val )
    {
        Integer valObj = new Integer( val );
        return set.remove( valObj );
    }
    
    
    public int size()
    {
        return set.size();
    }
    
    
    public boolean contains( int val )
    {
        Integer valObj = new Integer( val );
        return set.contains( valObj );
    }
    
    
    public boolean isEmpty()
    {
        return set.isEmpty();
    }
    
    
    public Iterator<Integer> iterator()
    {
        return set.iterator();
    }
    
    
    public String toString()
    {
        return set.toString();
    }
}
