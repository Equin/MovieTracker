package com.example.movietracker.view.fragment.movie_details;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movietracker.R;
import com.example.movietracker.data.entity.movie_details.MovieDetailsEntity;
import com.example.movietracker.data.net.constant.NetConstant;
import com.example.movietracker.listener.OnBackPressListener;
import com.example.movietracker.model.model_impl.MovieInfoModelImpl;
import com.example.movietracker.presenter.MovieDetailsPresenter;
import com.example.movietracker.view.contract.MovieDetailsView;
import com.example.movietracker.view.fragment.BaseFragment;
import com.example.movietracker.view.helper.UtilityHelpers;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsFragment extends BaseFragment implements MovieDetailsView, OnBackPressListener {

    public static final String ARG_SELECTED_MOVIE_ID = "arg_selected_movie_id";
    private static String[] tabTitles = new String[]{"Info", "Cast", "Review", "Video"};
    private static final int ANIMATION_DURATION_IN_MS = 200;

    public interface MovieDetailsFragmentInteractionListener {
        void openNewFragmentInTab(Fragment fragment, String fragmentName);
    }

    public static MovieDetailsFragment newInstance(int movieId) {
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SELECTED_MOVIE_ID, movieId);
        movieDetailsFragment.setArguments(bundle);
        return movieDetailsFragment;
    }

    private MovieDetailsPresenter movieDetailsPresenter;
    private MovieDetailsFragmentInteractionListener movieDetailsFragmentInteractionListener;
    private Animator currentAnimator;

    @BindView(R.id.tabLayout_movieDetails)
    TabLayout tabLayoutMovieDetails;

    @BindView(R.id.imageView_moviePoster_details)
    ImageView imageViewMoviePoster;

    @BindView(R.id.textView_movieReleaseDate_details)
    TextView textViewMovieReleaseDate;

    @BindView(R.id.textView_movieDuration_details)
    TextView textViewMovieDuration;

    @BindView(R.id.textView_movieTitle_details)
    TextView textViewMovieTitle;

    @BindView(R.id.textView_MovieGenres_details)
    TextView textViewMovieGenres;

    @BindView(R.id.textView_nothingToShow)
    TextView textViewNothingToShow;

    @BindView(R.id.expanded_image)
    ImageView imageViewExpandedPoster;

    public MovieDetailsFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.movieDetailsPresenter = new MovieDetailsPresenter(this, new MovieInfoModelImpl());
        this.movieDetailsPresenter.getMovieDetails(getMovieIdFromArguments());
    }

    private void setupTabLayout(MovieDetailsEntity movieDetailsEntity) {
        for (int i = 0; i<tabTitles.length; i++) {
            tabLayoutMovieDetails.addTab(tabLayoutMovieDetails.newTab().setText(tabTitles[i]), i == 0);
        }

        replaceFragment(MovieInfoTabFragment.newInstance(movieDetailsEntity), "MovieInfoTabFragment");

        tabLayoutMovieDetails.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0 : replaceFragment(MovieInfoTabFragment.newInstance(movieDetailsEntity), "MovieInfoTabFragment");
                        break;
                    case 1 : replaceFragment(MovieCastTabFragment.newInstance(movieDetailsEntity.getMovieId()), "MovieCastTabFragment");
                        break;
                    case 2 : replaceFragment(MovieReviewTabFragment.newInstance(movieDetailsEntity.getMovieId()), "MovieReviewTabFragment");
                        break;
                    case 3 : replaceFragment(MovieVideoTabFragment.newInstance(movieDetailsEntity.getMovieId()), "MovieVideoTabFragment");
                        break;
                    default: replaceFragment(MovieInfoTabFragment.newInstance(movieDetailsEntity), "MovieInfoTabFragment");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //intentionally left empty
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //intentionally left empty
            }
        });
    }

    private void replaceFragment(Fragment fragment, String fragmentName) {
        this.movieDetailsFragmentInteractionListener.openNewFragmentInTab(fragment, fragmentName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieDetailsFragmentInteractionListener) {
            this.movieDetailsFragmentInteractionListener = (
                    MovieDetailsFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.textViewMovieDuration = null;
        this.textViewMovieGenres = null;
        this.textViewMovieReleaseDate = null;
        this.textViewMovieTitle = null;
        this.textViewNothingToShow = null;
        this.imageViewExpandedPoster = null;
        this.imageViewMoviePoster = null;
        this.movieDetailsFragmentInteractionListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (  this.movieDetailsPresenter != null) {
            this.movieDetailsPresenter.destroy();
        }
    }

    private int getMovieIdFromArguments() {
        if(getArguments() != null) {
            return   getArguments().getInt(ARG_SELECTED_MOVIE_ID);
        }

        return -1;
    }

    @Override
    public void showToast(int resourceId) {
        showToast(getString(resourceId));
    }

    @Override
    public void renderMovieDetails(MovieDetailsEntity movieDetailsEntity) {
        setupTabLayout(movieDetailsEntity);
        renderMovieDetailView(movieDetailsEntity);
    }

    @Override
    public void displayNothingToShowHint() {
        this.textViewNothingToShow.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean canGoBackOnBackPressed() {
        if (imageViewExpandedPoster.getVisibility() == View.VISIBLE) {
            imageViewExpandedPoster.setVisibility(View.GONE);
            imageViewMoviePoster.setAlpha(1f);
            return false;
        }
        return true;
    }

    @OnClick(R.id.imageView_moviePoster_details)
    public void onPosterImageClicked(View view) {
        zoomImageFromThumb(view, movieDetailsPresenter.getPosterImagePath());
    }

    /**
     * code took from https://developer.android.com/training/animation/zoom
     *
     * hiding poster ImageView, and showing hidden imageView fullscreen with animation of zooming
     * @param thumbView - initial imageView with small photo
     * @param posterImagePath - image path for higher resolution image
     */
    private void zoomImageFromThumb(final View thumbView, String posterImagePath) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        loadImageIntoImageView(imageViewExpandedPoster, posterImagePath, NetConstant.IMAGE_HIGHT_RES_URL);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        getActivity().findViewById(R.id.relativeLayout_movie_details_container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
           startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        imageViewExpandedPoster.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        imageViewExpandedPoster.setPivotX(0f);
        imageViewExpandedPoster.setPivotY(0f);

        currentAnimator = getAnimatorSetForZoomInImage(startBounds, finalBounds, startScale, thumbView);
        currentAnimator.start();

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        imageViewExpandedPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                currentAnimator = getAnimatorSetForZoomOutImage(startBounds, startScale, thumbView);
                currentAnimator.start();
            }
        });
    }

    // Construct and run the parallel animation of the four translation and
    // scale properties (X, Y, SCALE_X, and SCALE_Y).
    private AnimatorSet getAnimatorSetForZoomInImage(Rect startBounds, Rect finalBounds, float startScale, View thumbView) {
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(imageViewExpandedPoster, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(imageViewExpandedPoster, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(imageViewExpandedPoster, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(imageViewExpandedPoster,
                        View.SCALE_Y, startScale, 1f))
                .with(ObjectAnimator.ofFloat(thumbView, View.ALPHA, 1f, 0f ));
        set.setDuration(ANIMATION_DURATION_IN_MS);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        return set;
    }

    // Animate the four positioning/sizing properties in parallel,
    // back to their original values.
    private AnimatorSet getAnimatorSetForZoomOutImage(Rect startBounds, float startScaleFinal, View thumbView) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(imageViewExpandedPoster, View.X, startBounds.left))
                .with(ObjectAnimator
                        .ofFloat(imageViewExpandedPoster,
                                View.Y,startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(imageViewExpandedPoster,
                                View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(imageViewExpandedPoster,
                                View.SCALE_Y, startScaleFinal))
                .with(ObjectAnimator.ofFloat(thumbView, View.ALPHA, 0f, 1f ));
        set.setDuration(ANIMATION_DURATION_IN_MS);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                thumbView.setAlpha(1f);
                imageViewExpandedPoster.setVisibility(View.GONE);
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                thumbView.setAlpha(1f);
                imageViewExpandedPoster.setVisibility(View.GONE);
                currentAnimator = null;
            }
        });
        return set;
    }

    /**
     * rendering movie details into views
     *
     * @param movieDetailsEntity
     */
    private void renderMovieDetailView(MovieDetailsEntity movieDetailsEntity) {
        this.textViewMovieDuration.setText(
                UtilityHelpers.getAppropriateValue(movieDetailsEntity.getMovieRuntime()) + " min");
        this.textViewMovieTitle.setText(
                movieDetailsEntity.getMovieTitle());
        this.textViewMovieReleaseDate.setText(
                UtilityHelpers.getYear(movieDetailsEntity.getMovieReleaseDate()));
        this.textViewMovieGenres.setText(
                UtilityHelpers.getPipeDividedGenres(movieDetailsEntity.getGenres()));

       loadImageIntoImageView(this.imageViewMoviePoster, movieDetailsEntity.getMoviePosterPath(), NetConstant.IMAGE_BASE_URL);
    }

    private void loadImageIntoImageView(ImageView imageView, String posterPath, String baseUrl) {
        Glide
                .with(this)
                .load(baseUrl + posterPath)
                .centerCrop()
                .into(imageView);
    }
}
