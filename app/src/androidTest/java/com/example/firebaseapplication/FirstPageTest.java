package com.example.firebaseapplication;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class FirstPageTest {
    @Rule
    public ActivityTestRule<FirstPage> activityRule
            = new ActivityTestRule<>(FirstPage.class);

    private FirstPage firstPage = null;

    @Before
    public void setUp() throws Exception {
        firstPage = activityRule.getActivity();
    }

    @Test
    public void testLaunch()
    {
        View view = firstPage.findViewById(R.id.autoCompleteTextViewCitySearch);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        firstPage = null;
    }
}