package com.firebaseapp.demo.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Movie object.
 */

public class Movie {
    public String name;
    public List<Department> departments;

    public Movie() {

    }

    public Movie(String name) {
        this.name = name;
    }

    public Movie(String name, List<Department> departments) {
        this.name = name;
        int listLength = departments == null ? 0 : departments.size();
        departments = new ArrayList<Department>(listLength);
        Collections.copy(this.departments, departments);
    }
}
