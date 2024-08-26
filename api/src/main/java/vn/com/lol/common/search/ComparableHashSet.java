package vn.com.lol.common.search;

import java.util.HashSet;
import java.util.Set;

public class ComparableHashSet<Y> extends HashSet<Y> implements Comparable<ComparableHashSet<Y>> {

    public ComparableHashSet(Set<Y> set) {
        super(set);
    }

    @Override
    public int compareTo(ComparableHashSet<Y> other) {
        int sizeComparison = Integer.compare(this.size(), other.size());
        if (sizeComparison != 0) {
            return sizeComparison;
        }

        return this.toString().compareTo(other.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparableHashSet<?> that = (ComparableHashSet<?>) o;
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}