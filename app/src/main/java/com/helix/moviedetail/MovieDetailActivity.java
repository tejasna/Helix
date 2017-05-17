package com.helix.moviedetail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.helix.HelixApplication;
import com.helix.R;
import com.helix.data.Credits;
import com.helix.data.Crew;
import com.helix.data.Genre;
import com.helix.data.Images;
import com.helix.data.Movie;
import com.helix.data.MovieDetail;
import com.helix.data.source.MoviesRepositoryComponent;
import com.helix.utils.ActivityUtils;
import com.helix.utils.DateUtil;
import com.helix.widget.RoundedCornersTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmList;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import me.relex.circleindicator.CircleIndicator;
import timber.log.Timber;

import static com.helix.utils.PreConditions.checkNotNull;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

  @Inject MovieDetailPresenter presenter;

  @BindView(R.id.appbar) AppBarLayout appBarLayout;

  @BindView(R.id.toolbar) Toolbar toolbar;

  @BindView(R.id.poster) AppCompatImageView poster;

  @BindView(R.id.progress_bar) ProgressBar progress;

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @BindView(R.id.view_pager) ViewPager viewPager;

  @BindView(R.id.indicator) CircleIndicator circleIndicator;

  @BindView(R.id.poster_card) CardView cardView;

  private MovieDetailAdapter recyclerAdapter;

  private CarouselAdapter carouselAdapter;

  private Picasso picasso;

  private Unbinder unbinder;

  private MovieDetailContract.Presenter activePresenter;

  private CompositeDisposable compositeSubscriptions;

  private int movieId;

  private boolean firstLoad = true;

  private ToolbarState toolbarState;

  private AppBarLayout.OnOffsetChangedListener appBarListener;

  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.movie_detail_act);

    unbinder = ButterKnife.bind(this);

    MoviesRepositoryComponent component =
        ((HelixApplication) getApplication()).getRepositoryComponent();

    picasso = component.getPicasso();

    setSharedElementView(picasso);

    setSupportActionBar(toolbar);
    //noinspection ConstantConditions
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    setListener();

    DaggerMovieDetailComponent.builder()
        .moviesRepositoryComponent(component)
        .movieDetailPresenterModule(new MovieDetailPresenterModule(MovieDetailActivity.this))
        .build()
        .inject(this);
  }

  private void setListener() {

    appBarListener = (appBarLayout1, verticalOffset) -> {
      if (verticalOffset == 0) {
        if (toolbarState != ToolbarState.EXPANDED && !firstLoad) {
          if (cardView.getVisibility() == View.INVISIBLE) {
            ActivityUtils.circularRevealShow(cardView);
          }
        } else {
          firstLoad = false;
        }
        toolbarState = ToolbarState.EXPANDED;
      } else if (Math.abs(verticalOffset) >= appBarLayout1.getTotalScrollRange()) {
        if (toolbarState != ToolbarState.COLLAPSED) {
          if (cardView.getVisibility() == View.VISIBLE) {
            ActivityUtils.circularRevealHide(cardView);
          }
        }
        toolbarState = ToolbarState.COLLAPSED;
      } else {
        toolbarState = ToolbarState.IDLE;
      }
    };

    appBarLayout.addOnOffsetChangedListener(appBarListener);
  }

  private void setSharedElementView(Picasso picasso) {

    movieId = getIntent().getIntExtra("movie_id", -1);

    picasso.load(getIntent().getStringExtra("poster_url"))
        .transform(new RoundedCornersTransformation(5, 5))
        .into(poster, new Callback() {
          @Override public void onSuccess() {
            scheduleStartPostponedTransition(poster);
          }

          @Override public void onError() {

          }
        });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    activePresenter.stop();
    compositeSubscriptions.clear();
    unbinder.unbind();
  }

  @Override public void showMovieBrief(Movie movie) {
    if (recyclerAdapter == null) {
      LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      recyclerView.setLayoutManager(layoutManager);
      recyclerAdapter = new MovieDetailAdapter(movie, picasso);
      recyclerView.setAdapter(recyclerAdapter);
    }
  }

  @Override public void showMovieDetail(MovieDetail movieDetail) {
    recyclerAdapter.setMovieDetail(movieDetail);
  }

  @Override public void showImages(Images images) {
    if (carouselAdapter == null) {
      carouselAdapter = new CarouselAdapter(this, images, picasso);
      viewPager.setAdapter(carouselAdapter);
      circleIndicator.setViewPager(viewPager);
      viewPager.setCurrentItem(0);
    }
  }

  @Override public void showCredits(Credits credits) {
    recyclerAdapter.setMovieCredits(credits);
  }

  @Override public void showLoadingImagesError() {
    ActivityUtils.showSnackBar(getString(R.string.movie_detail_images_error), toolbar);
  }

  @Override public void showLoadingCreditsError() {
    ActivityUtils.showSnackBar(getString(R.string.movie_detail_credits_error), toolbar);
  }

  @Override public void setLoadingIndicator(boolean active) {
    int visibility = active ? View.VISIBLE : View.GONE;
    progress.setVisibility(visibility);
  }

  @Override public void showNoMovieDetail() {
    ActivityUtils.showSnackBar(getString(R.string.movie_detail_empty), toolbar);
  }

  @Override public void showLoadingMovieDetailError() {
    ActivityUtils.showSnackBar(getString(R.string.movie_detail_error), toolbar);
  }

  @Override public boolean isActive() {
    return !isFinishing();
  }

  @Override public void setPresenter(MovieDetailContract.Presenter presenter) {
    this.activePresenter = checkNotNull(presenter);
    activePresenter.setMovieId(movieId);
    activePresenter.start();
  }

  private void scheduleStartPostponedTransition(final View sharedElement) {
    sharedElement.getViewTreeObserver()
        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
            startPostponedEnterTransition();
            return true;
          }
        });
  }

  @Override public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  private class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int NAME = 1;
    private int INFO = 2;
    private int CAST_HEADER = 3;
    private int CAST = 4;
    private int FOOTER = 5;
    private Movie movie;
    private MovieDetail movieDetail;
    private Credits credits;
    private Picasso picasso;
    private Transformation transformation;
    private Crew director;
    private boolean isFirstBinding = true;

    MovieDetailAdapter(Movie movie, Picasso picasso) {
      this.picasso = picasso;
      this.movie = movie;
      transformation = new RoundedCornersTransformation(5, 5);
      compositeSubscriptions = new CompositeDisposable();
    }

    void setMovieDetail(MovieDetail movieDetail) {
      this.movieDetail = movieDetail;
      notifyItemInserted(1);
    }

    void setMovieCredits(Credits credits) {
      this.credits = credits;
      notifyDataSetChanged();
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      if (viewType == NAME) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_detail_header, parent, false);
        return new VHHeader(view);
      } else if (viewType == INFO) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_detail_info, parent, false);
        return new MovieDetailActivity.MovieDetailAdapter.VHInfo(view);
      } else if (viewType == CAST_HEADER) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_detail_cast_header, parent, false);
        return new MovieDetailActivity.MovieDetailAdapter.VHCastHeader(view);
      } else if (viewType == CAST) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_detail_cast_item, parent, false);
        return new MovieDetailActivity.MovieDetailAdapter.VHCast(view);
      } else if (viewType == FOOTER) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_detail_footer, parent, false);
        return new MovieDetailActivity.MovieDetailAdapter.VHFooter(view);
      }
      throw new RuntimeException(
          parent.getContext().getString(R.string.exception_no_view_type_found));
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

      if (holder.getItemViewType() == NAME) {

        VHHeader movieHeader = (VHHeader) holder;

        movieHeader.name.setText(movie.getTitle());

        Float popularity = movie.getVoteAverage() / 2;

        double fractionalPart = popularity % 1;
        double integralPart = (popularity - fractionalPart);

        observePopularityRating(integralPart, fractionalPart, movieHeader, recyclerAdapter);

        movieHeader.overview.setText(movie.getOverview());
      } else if (holder.getItemViewType() == INFO) {

        MovieDetailActivity.MovieDetailAdapter.VHInfo movieInfo =
            (MovieDetailActivity.MovieDetailAdapter.VHInfo) holder;

        StringBuilder genreString = new StringBuilder();
        RealmList<Genre> genreList = movie.getGenres();

        for (int i = 0; i < genreList.size(); i++) {
          genreString.append(genreList.get(i).getName());
          if (i != genreList.size() - 1) genreString.append(",");
        }

        movieInfo.genre.setText(genreString);

        movieInfo.releaseDate.setText(
            String.valueOf(DateUtil.getDate(movieDetail.getReleaseDate())));

        movieInfo.runTime.setText(movieInfo.itemView.getContext()
            .getString(R.string.movie_detail_runtime, String.valueOf(movieDetail.getRuntime())));

        movieInfo.revenue.setText(String.valueOf(movieDetail.getFormatedRevenue()));

        String voteAverage = String.valueOf(movieDetail.getVoteAverage()) +
            "/10" +
            "(" +
            movie.getVoteCount() +
            ")";

        movieInfo.voteAverage.setText(voteAverage);

        if (director != null) {
          movieInfo.director.setText(director.getName());
          picasso.load(director.getProfileUrl())
              .transform(new RoundedCornersTransformation(5, 5))
              .into(movieInfo.directorPhoto);
        }
      } else if (holder.getItemViewType() == CAST) {
        MovieDetailActivity.MovieDetailAdapter.VHCast movieCast =
            (MovieDetailActivity.MovieDetailAdapter.VHCast) holder;

        int finalPosition = position - 3;
        if (director == null) {
          this.director = credits.getDirector();
          if (this.director == null) {
            director = new Crew();
            director.setName("N/A");
          }
        }

        movieCast.name.setText(credits.getCast().get(finalPosition).getName());
        movieCast.aka.setText(credits.getCast().get(finalPosition).getCharacter());
        picasso.load(credits.getCast().get(finalPosition).getProfileUrl())
            .transform(transformation)
            .into(movieCast.imageCast);
      }
    }

    @Override public int getItemCount() {
      if (movieDetail == null) {
        return 1;
      } else if (credits == null) {
        return 3;
      } else {
        return 4 + credits.getCast().size();
      }
    }

    @Override public int getItemViewType(int position) {
      if (position == 0) {
        return NAME;
      } else if (position == 1) {
        return INFO;
      } else if (position == 2) {
        return CAST_HEADER;
      } else if (credits == null && position == 3) {
        return FOOTER;
      } else if (credits != null && position == credits.getCast().size() + 3) {
        return FOOTER;
      } else {
        return CAST;
      }
    }

    private void observePopularityRating(double integralPart, double fractionalPart,
        VHHeader movieHeader, MovieDetailAdapter movieDetailAdapter) {

      if (isFirstBinding) {

        Drawable emptyStar =
            ContextCompat.getDrawable(movieHeader.itemView.getContext(), R.drawable.ic_empty_star);

        Drawable halfStar =
            ContextCompat.getDrawable(movieHeader.itemView.getContext(), R.drawable.ic_half_star);

        Drawable fullStar =
            ContextCompat.getDrawable(movieHeader.itemView.getContext(), R.drawable.ic_full_star);

        isFirstBinding = false;

        if (integralPart > 0) {

          compositeSubscriptions.add(Observable.intervalRange(0, 5, 500, 150, TimeUnit.MILLISECONDS)
              .subscribeOn(AndroidSchedulers.mainThread())
              .observeOn(AndroidSchedulers.mainThread())
              .doOnError(throwable -> Timber.e(throwable.getMessage()))
              .subscribe(aLong -> {

                Timber.d(aLong + "");
                if (integralPart >= aLong) {
                  if (integralPart == aLong && fractionalPart < 0.5) {
                    drawStar(movieHeader, aLong, movieDetailAdapter, halfStar);
                  } else {
                    drawStar(movieHeader, aLong, movieDetailAdapter, fullStar);
                  }
                } else {
                  drawStar(movieHeader, aLong, movieDetailAdapter, emptyStar);
                }
              }));
        }
      }
    }

    private void drawStar(VHHeader view, Long position, MovieDetailAdapter adapter,
        Drawable drawable) {

      switch (String.valueOf(position)) {

        case "0":
          view.star0.setBackgroundDrawable(drawable);
          ActivityUtils.animate(view.star0);
          break;
        case "1":
          view.star1.setBackgroundDrawable(drawable);
          ActivityUtils.animate(view.star1);
          break;
        case "2":
          view.star2.setBackgroundDrawable(drawable);
          ActivityUtils.animate(view.star2);
          break;
        case "3":
          view.star3.setBackgroundDrawable(drawable);
          ActivityUtils.animate(view.star3);
          break;
        case "4":
          view.star4.setBackgroundDrawable(drawable);
          ActivityUtils.animate(view.star4);
          break;
      }

      adapter.notifyDataSetChanged();
    }

    private class VHHeader extends RecyclerView.ViewHolder {

      View itemView;
      AppCompatTextView name;
      AppCompatTextView overview;
      LinearLayout viewGroupPopularity;
      AppCompatImageView star0;
      AppCompatImageView star1;
      AppCompatImageView star2;
      AppCompatImageView star3;
      AppCompatImageView star4;

      VHHeader(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.name = (AppCompatTextView) itemView.findViewById(R.id.name);
        this.overview = (AppCompatTextView) itemView.findViewById(R.id.overview);
        this.viewGroupPopularity = (LinearLayout) itemView.findViewById(R.id.view_group_popularity);
        this.star0 = (AppCompatImageView) itemView.findViewById(R.id.star_0);
        this.star1 = (AppCompatImageView) itemView.findViewById(R.id.star_1);
        this.star2 = (AppCompatImageView) itemView.findViewById(R.id.star_2);
        this.star3 = (AppCompatImageView) itemView.findViewById(R.id.star_3);
        this.star4 = (AppCompatImageView) itemView.findViewById(R.id.star_4);
      }
    }

    private class VHInfo extends RecyclerView.ViewHolder {

      View itemView;
      AppCompatTextView genre;
      AppCompatTextView releaseDate;
      AppCompatTextView runTime;
      AppCompatTextView voteAverage;
      AppCompatTextView revenue;
      AppCompatTextView director;
      AppCompatImageView directorPhoto;
      AppCompatImageView imgImdb;

      VHInfo(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.genre = (AppCompatTextView) itemView.findViewById(R.id.genre);
        this.releaseDate = (AppCompatTextView) itemView.findViewById(R.id.release_date);
        this.runTime = (AppCompatTextView) itemView.findViewById(R.id.run_time);
        this.voteAverage = (AppCompatTextView) itemView.findViewById(R.id.vote_average);
        this.revenue = (AppCompatTextView) itemView.findViewById(R.id.revenue);
        this.director = (AppCompatTextView) itemView.findViewById(R.id.txt_crew);
        this.directorPhoto = (AppCompatImageView) itemView.findViewById(R.id.img_crew);
        this.imgImdb = (AppCompatImageView) itemView.findViewById(R.id.img_imdb);
        this.imgImdb.setOnClickListener(
            view -> activePresenter.loadImdbPage(movieDetail.getImdbId(), view.getContext()));
      }
    }

    private class VHCastHeader extends RecyclerView.ViewHolder {

      View itemView;

      VHCastHeader(View itemView) {
        super(itemView);
        this.itemView = itemView;
      }
    }

    private class VHCast extends RecyclerView.ViewHolder {

      View itemView;
      AppCompatTextView name;
      AppCompatTextView aka;
      AppCompatImageView imageCast;

      VHCast(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.name = (AppCompatTextView) itemView.findViewById(R.id.name);
        this.aka = (AppCompatTextView) itemView.findViewById(R.id.aka);
        this.imageCast = (AppCompatImageView) itemView.findViewById(R.id.img_cast);
      }
    }

    private class VHFooter extends RecyclerView.ViewHolder {

      View itemView;

      VHFooter(View itemView) {
        super(itemView);
        this.itemView = itemView;
      }
    }
  }

  class CarouselAdapter extends PagerAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    Images images;
    Picasso picasso;

    CarouselAdapter(Context context, Images images, Picasso picasso) {
      this.context = context;
      this.images = images;
      this.picasso = picasso;
      mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public int getCount() {
      if (images.getBackdrops().size() >= 5) {
        return 5;
      } else {
        return images.getBackdrops().size();
      }
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      View itemView = mLayoutInflater.inflate(R.layout.movie_detail_pager_item, container, false);

      ImageView imageView = (ImageView) itemView.findViewById(R.id.image);

      picasso.load(images.getBackdrops().get(position).getBackDropUrl()).into(imageView);

      container.addView(itemView);

      return itemView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((LinearLayout) object);
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }
  }

  private enum ToolbarState {
    EXPANDED,
    COLLAPSED,
    IDLE
  }

  /**
   * Override return transition to nothing if toolbar state is expanded
   * since the shared element will not be visible when the toolbar state is
   * {@link ToolbarState#COLLAPSED}
   */
  @Override public void onBackPressed() {
    appBarLayout.removeOnOffsetChangedListener(appBarListener);
    if (toolbarState == ToolbarState.EXPANDED) {
      super.onBackPressed();
    } else {
      finish();
    }
  }
}
