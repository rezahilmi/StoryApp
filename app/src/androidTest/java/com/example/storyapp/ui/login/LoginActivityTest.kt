package com.example.storyapp.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.storyapp.R
import com.example.storyapp.ui.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest{

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginSuccess() {
        onView(withId(R.id.ed_login_email)).perform(typeText("rezahilmidafa@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("qwertyui"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText("Lanjut")).inRoot(isDialog()).perform(click())
        onView(withId(R.id.main_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.action_logout)).perform(click())
        onView(withId(R.id.welcome_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun testLogoutSuccess() {
        onView(withId(R.id.action_logout)).perform(click())
        onView(withId(R.id.welcome_layout)).check(matches(isDisplayed()))
    }
}