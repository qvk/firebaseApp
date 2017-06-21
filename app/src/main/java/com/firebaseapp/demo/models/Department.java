package com.firebaseapp.demo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Department object.
 */

public class Department {
    public String name;

    /**
     * Can be a json containing department specific info.
     */
    public String info;

    /**
     * list of auth user uid.
     */
    public List<String> modifiers;

    public Department() {

    }

    public Department(String name, String info, List<String> modifiers) {
        this.name = name;
        this.info = info;
        int listLength = modifiers == null ? 0 : modifiers.size();
        modifiers = new ArrayList<String>(listLength);
        Collections.copy(this.modifiers, modifiers);
    }
}
