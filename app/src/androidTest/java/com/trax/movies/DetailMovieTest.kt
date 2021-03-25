package com.trax.movies

import android.content.Intent
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
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
import com.trax.movies.detail_movie.DetailMovieFragment
import com.trax.movies.detail_movie.DetailMovieFragment.Companion.ARG_MOVIE_ID
import com.trax.movies.detail_movie.DetailMovieFragmentArgs
import org.koin.core.logger.Level

@RunWith(AndroidJUnit4::class)
class DetailMovieTest : KoinTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Test
    fun nominalCase() {
        val mock = declareMock<MoviesRepository>()
        every { mock.observeMovie(1) } returns Observable.just(
            DetailedMovieDomainModel(1,
                "hugo vs a test",
                "synopsis",
                "https://img.phonandroid.com/2021/01/forfait-Free-Mobile-60-Go.jpg",
                90,
                Date(1),
                "https://movietrailers.ap…-year-trailer-1_720p.mov"))

        val fragmentArgs = DetailMovieFragmentArgs(1).toBundle()
        launchFragmentInContainer<DetailMovieFragment>(fragmentArgs)

        onView(withId(R.id.detail_movie_title)).check(matches(withText("hugo vs a test")))
        onView(withId(R.id.detail_movie_synopsis)).check(matches(withText("synopsis")))
        onView(withId(R.id.detail_movie_runtime)).check(matches(withText("90 minutes")))
        onView(withId(R.id.detail_movie_release_date)).check(matches(withText("1970")))

        onView(withId(R.id.detail_movie_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun emptyTrailerMustHideButton() {
        val mock = declareMock<MoviesRepository>()
        every { mock.observeMovie(1) } returns Observable.just(
                DetailedMovieDomainModel(1,
                        "hugo vs a test",
                        "synopsis",
                        "https://img.phonandroid.com/2021/01/forfait-Free-Mobile-60-Go.jpg",
                        90,
                        Date(1),
                        ""))

        val fragmentArgs = DetailMovieFragmentArgs(1).toBundle()
        launchFragmentInContainer<DetailMovieFragment>(fragmentArgs)

        onView(withId(R.id.detail_movie_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

}