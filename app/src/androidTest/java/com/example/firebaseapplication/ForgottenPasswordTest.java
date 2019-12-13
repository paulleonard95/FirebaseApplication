package com.example.firebaseapplication;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class ForgottenPasswordTest {

    @Rule
    public ActivityTestRule<ForgottenPassword> activityRule
            = new ActivityTestRule<>(ForgottenPassword.class);

    private ForgottenPassword forgottenPassword = null;

    @Before
    public void setUp() throws Exception {
        forgottenPassword = activityRule.getActivity();
    }

    @Test
    public void testLaunch()
    {
        View view = forgottenPassword.findViewById(R.id.editText_Forgot_Password_Email);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        forgottenPassword = null;
    }
}