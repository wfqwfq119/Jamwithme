package cse110.jamwithme;

import com.firebase.geofire.GeoLocation;
import java.io.File;

/**
 * Created by Storm Quark on 10/21/2016.
 */

public class User {
    private String name;
    private String personalBio;
    private int age;
    private File profile_jam;
    //private Song[] exampleSongs;
    //private ProfilePics[] profilePics;
    private GeoLocation location;

    /** Constructors for User */
    public User() {
        name = "Default Name";
        personalBio = "Default Bio";
        age = 0;
        location = new GeoLocation(0.0, 0.0);
        //profile_jam = null;
    }

    public User(String inname, String pBio, int userAge, GeoLocation g) {
        name = inname;
        personalBio = pBio;
        age = userAge;
        location = g;
    }

    /** Getters for user info */
    public String getName() { return name; }
    public String getPersonalBio() {
        return personalBio;
    }

    public int getAge() {
        return age;
    }

    public GeoLocation getLocation() {
        return location;
    }

    /** Setters for user info */
    public void setName(String newName) {
        name = newName;
    }
    public void setBio(String newBio) {
        //500 character limit
        if(newBio.length() < 500)
            personalBio = newBio;
        else
            personalBio = newBio.substring(0, 499);
    }

    //public User getUsr() { return this; }

    public void setAge(int newAge) {
        age = newAge;
    }

    public void setLocation(GeoLocation newLoc) {
        location = newLoc;
    }

    //public void setProfile_jam(File nextjam) { profile_jam = nextjam; }
}
