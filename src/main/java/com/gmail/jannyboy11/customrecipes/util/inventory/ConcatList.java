package com.gmail.jannyboy11.customrecipes.util.inventory;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ConcatList<T> extends AbstractList<T> implements List<T> {
    
    private final List<? extends List<T>> builtFrom;
    
    public ConcatList(List<? extends List<T>> from) {
        this.builtFrom = Objects.requireNonNull(from);
    }

    @Override
    public boolean add(T arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int arg0, T arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int arg0, Collection<? extends T> arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object obj) {
        return builtFrom.stream().anyMatch(f -> f.contains(obj));
    }

    @Override
    public boolean containsAll(Collection<?> coll) {
        return coll.stream().allMatch(this::contains);
    }

    @Override
    public T get(int index) {
        if (index < 0) throw new IndexOutOfBoundsException("Index cannot be below 0!");
        
        int sizeAcc = 0;
        for (List<T> f : builtFrom) {
            int size = f.size();
            if (index >= sizeAcc && index < sizeAcc + size) {
                return f.get(index - sizeAcc);
            }
            sizeAcc += size;
        }
        
        throw new IndexOutOfBoundsException("Index cannot be above " + sizeAcc + "!");
    }

    @Override
    public int indexOf(Object obj) {
       int i = 0;
       for (List<T> f : builtFrom) {
           for (T t : f) {
               if (Objects.equals(t, obj)) return i;
               i++;
           }
       }
       return -1;
    }

    @Override
    public boolean isEmpty() {
        return builtFrom.stream().allMatch(List::isEmpty);
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public int lastIndexOf(Object obj) {
        int i = size();
        for (List<T> f : builtFrom) {
            for (T t : f) {
                if (Objects.equals(t, obj)) return i;
                i--;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIterator<T>() {
            int currentIndex = index;

            @Override
            public void add(T arg0) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasNext() {
                return currentIndex < size();
            }

            @Override
            public boolean hasPrevious() {
                return index > 0;
            }

            @Override
            public T next() {
                return get(currentIndex++);
            }

            @Override
            public int nextIndex() {
                return currentIndex + 1;
            }

            @Override
            public T previous() {
                return get(--currentIndex);
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(T t) {
                ConcatList.this.set(currentIndex, t);
            }
        };
    }

    @Override
    public boolean remove(Object arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T t) {
        if (index < 0) throw new IndexOutOfBoundsException("Index cannot be below 0!");
        
        int sizeAcc = 0;
        for (List<T> f : builtFrom) {
            int size = f.size();
            if (index >= sizeAcc && index < sizeAcc + size) {
                 return f.set(index - sizeAcc, t);
            }
            sizeAcc += size;
        }
        
        throw new IndexOutOfBoundsException("Index cannot be above " + sizeAcc + "!");
    }

    @Override
    public int size() {
        return builtFrom.stream().mapToInt(List::size).sum();
    }

    @Override
    public List<T> subList(int startInclusive, int endExclusive) {
        return super.subList(startInclusive, endExclusive);
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        int i = 0;
        for (T t : this) {
            array[i] = t;
            i++;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        int size = size();
        if (array.length < size) {
            array = (T[]) new Object[size];   
        }
        
        int i = 0;
        for (Object t : this) {
            array[i] = (T) t;
            i++;
        }
        return array;
    }

}
