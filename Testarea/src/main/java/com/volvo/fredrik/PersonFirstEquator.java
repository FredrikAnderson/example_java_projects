package com.volvo.fredrik;

import org.apache.commons.collections4.Equator;

public class PersonFirstEquator implements Equator<Person> {

    @Override
    public boolean equate(Person o1, Person o2) {
        return o1.equals(o2);
    }

    @Override
    public int hash(Person o) {
        return o.hashCode();
    }
}
