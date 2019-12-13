package com.example.firebaseapplication;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private String email = "paul@gmail.com";
    private String password = "Password1";
    private String first_name = "Paul";
    private String last_name = "Leonard";

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkEmailIsCorrect()
    {
        assertEquals(email, "paul@gmail.com");
    }

    @Test
    public void checkEmailIsNotCorrect()
    {
        assertNotEquals(email, "pl@gmail.com");
    }

    @Test
    public void checkIfPasswordIsCorrect()
    {
        assertEquals(password, "Password1");
    }

    @Test
    public void checkIfNotCorrectPassword()
    {
        assertNotEquals(password, "Password");
    }

    @Test
    public void checkIfFirtNameTextIsFilled()
    {
        assertNotNull(first_name);
    }

    @Test
    public void checkIfLastNameTextIsFilled()
    {
        assertNotNull(last_name);
    }
}