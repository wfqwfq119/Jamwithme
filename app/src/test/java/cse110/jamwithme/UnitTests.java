package cse110.jamwithme;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnitTests {

    private User user;

    @Test
    public void testUserConstructor() {

        /*Testing default constructor*/
        user = new User();

        assertTrue(user.getName().equals("Default Name"));
        assertTrue(user.getAge() == 0);
        assertTrue(user.getPersonalBio().equals("Default Bio"));

    }

    @Test
    public void testUserSetName() {

        user = new User();

        /*Testing setName*/
        user.setName("Testfirstname Testlastname");

        assertFalse(user.getName().equals("Default Name"));
        assertTrue(user.getName().equals("Testfirstname Testlastname"));

    }

    @Test
    public void testUserSetAge() {

        user = new User();

        /*Testing setAge*/
        user.setAge(20);

        assertTrue(user.getAge() == 20);

    }

    @Test
    public void testUserSetBio(){

        user = new User();

        /*Testing setPersonalBio*/
        user.setBio("This is my new bio.");

        assertTrue(user.getPersonalBio().equals("This is my new bio."));

    }


}