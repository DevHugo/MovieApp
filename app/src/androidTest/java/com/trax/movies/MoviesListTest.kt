package com.trax.movies

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.trax.data.MoviesRepository
import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMoviesDomainModel
import io.mockk.every
import io.mockk.mockkClass
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.hamcrest.collection.IsMapContaining.hasEntry
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import java.util.*
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import org.koin.core.logger.Level

@RunWith(AndroidJUnit4::class)
class MoviesListTest : KoinTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Test
    fun test() {
        val mock = declareMock<MoviesRepository>()
        every { mock.fetchMovies(any()) } returns Completable.complete()
        every { mock.observeMovies() } returns Observable.just(
                DetailedMoviesDomainModel(
                        listOf(
                                DetailedMovieDomainModel(1, "hugo vs a test", "synopsis", "https://img.phonandroid.com/2021/01/forfait-Free-Mobile-60-Go.jpg", 90, Date(), "")
                        )
                )
        )
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent)

        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())

        onView(withId(R.id.fragment_movie_list))
                .check(matches(atPositionOnView(0, withText("hugo vs a test"), R.id.movies_list_movie_title)))
    }

    @After
    fun after() {
        stopKoin()
    }
}