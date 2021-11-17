package com.example.carbonfootprints.Models;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public long distanceWalked;
    public long distanceDrove;
    public long milesSaved;
    public long lastTrip;

    public User() { }

    public User(String email) {
        this.email = email;
        this.distanceDrove = 0;
        this.distanceWalked = 0;
        this.milesSaved = 0;
        this.lastTrip = 0;
    }
}
