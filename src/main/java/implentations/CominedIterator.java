package implentations;


import api.EdgeData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterator that taks a list of iterator and makes them useable as obe
 * @param <T>
 */
public class CominedIterator<T> implements Iterator<T> {
    ArrayList<Iterator<T>> its;
    int current;

    public CominedIterator(){
        its = new ArrayList<>();
        current = 0;
    }


    /**
     * add an iterator to the list
     * @param it
     */
    public void addIt(Iterator<T> it){
        its.add(it);
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        while (current < its.size() && !its.get(current).hasNext())
            current++;


        return current < its.size();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public T next() {
        while (current < its.size() && !its.get(current).hasNext())
            current++;
        return its.get(current).next();
    }
}
