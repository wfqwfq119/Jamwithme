package cse110.jamwithme;

/**
 * Created by Storm Quark on 10/21/2016.
 */

public class User {
    private String name;
    private String personalBio;
    private int age;
    //private Song[] exampleSongs;
    //private ProfilePics[] profilePics;
    //private Location location;

    /** Constructors for User */
    public User() {
        name = "Default Name";
        personalBio = "Blah";
        age = 0;
    }

    public User(String username, String pBio, int userAge) {
        name = username;
        personalBio = pBio;
        age = userAge;
    }

    /** Getters for user info */
    public String getName() {
        return name;
    }

    public String getPersonalBio() {
        return personalBio;
    }

    public int getAge() {
        return age;
    }

    /** Setters for user info */
    public void setName(String newName) {
        name = newName;
    }

    public void setBio(String newBio) {
        personalBio = newBio;
    }

    public void setAge(int newAge) {
        age = newAge;
    }
}
