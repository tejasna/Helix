package com.helix.upcoming;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.helix.HelixApplication;
import com.helix.R;
import com.helix.data.Genre;
import com.helix.data.Movie;
import com.helix.utils.ActivityUtils;
import com.helix.utils.DateUtil;
import com.helix.widget.EndlessRecyclerViewScrollListener;
import com.helix.widget.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.List;

import static com.helix.utils.PreConditions.checkNotNull;

public class UpcomingFragment extends Fragment implements UpcomingContract.View {

  private UpcomingContract.Presenter presenter;

  private Unbinder unbinder;

  public Picasso picasso;

  private UpcomingAdapter recyclerAdapter;

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @BindView(R.id.refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

  public UpcomingFragment() {
  }

  public static UpcomingFragment newInstance() {
    return new UpcomingFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    picasso = ((HelixApplication) getContext().getApplicationContext()).getRepositoryComponent()
        .getPicasso();
    recyclerAdapter = new UpcomingAdapter(new ArrayList<>(0), picasso);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.upcoming_frag, container, false);

    unbinder = ButterKnife.bind(this, rootView);

    swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadMovies(true));

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);

    EndlessRecyclerViewScrollListener scrollListener =
        new EndlessRecyclerViewScrollListener(layoutManager) {

          @Override public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            loadMore(page + 1);
          }
        };

    recyclerView.addOnScrollListener(scrollListener);

    recyclerView.setAdapter(recyclerAdapter);

    return rootView;
  }

  private void loadMore(int page) {
    this.presenter.loadMore(page);
  }

  @Override public void setPresenter(UpcomingContract.Presenter presenter) {
    this.presenter = checkNotNull(presenter);
  }

  @Override public void onStart() {
    super.onStart();
    presenter.start();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
    presenter.stop();
  }

  @Override public void setLoadingIndicator(boolean active) {
    swipeRefreshLayout.setRefreshing(active);
  }

  @Override public void showMovies(List<Movie> movies) {
    if (recyclerView.getVisibility() == View.GONE) {
      recyclerView.setVisibility(View.VISIBLE);
    }
    recyclerAdapter.addData(movies);
  }

  @Override public void showNoMovies() {
    ActivityUtils.showSnackBar(getString(R.string.upcoming_empty), swipeRefreshLayout);
  }

  @Override public void showLoadingMoviesError() {
    swipeRefreshLayout.setRefreshing(false);
    ActivityUtils.showSnackBar(getString(R.string.upcoming_error), swipeRefreshLayout);
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  private static class UpcomingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int HEADER = 1;
    private static int ITEM = 2;
    private static int FOOTER = 3;
    private List<Movie> movies;
    private Picasso picasso;
    private final Transformation transformation;

    UpcomingAdapter(List<Movie> movies, Picasso picasso) {
      this.picasso = picasso;
      this.movies = new ArrayList<>();
      transformation = new RoundedCornersTransformation(5, 5);
      addToList(movies);
    }

    private void addData(List<Movie> movies) {
      addToList(movies);
      notifyDataSetChanged();
    }

    private void addToList(List<Movie> movies) {
      checkNotNull(movies);
      this.movies.addAll(movies);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if (viewType == ITEM) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movies_item, parent, false);
        return new UpcomingAdapter.VHMovie(view);
      } else if (viewType == HEADER) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.upcoming_header, parent, false);
        return new UpcomingAdapter.VHHeader(view);
      } else if (viewType == FOOTER) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.upcoming_footer, parent, false);
        return new UpcomingAdapter.VHFooter(view);
      }
      throw new RuntimeException(
          parent.getContext().getString(R.string.exception_no_view_type_found));
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

      if (holder.getItemViewType() == ITEM) {
        VHMovie movie = (VHMovie) holder;
        int finalPosition = position - 1;
        movie.title.setText(movies.get(finalPosition).getTitle());
        movie.genre.setText(movies.get(finalPosition).getGenreIds().toString());
        movie.review.setText(movies.get(finalPosition).getOverview());

        StringBuilder genreString = new StringBuilder();

        RealmList<Genre> genreList = movies.get(finalPosition).getGenres();
        for (int i = 0; i < genreList.size(); i++) {
          genreString.append(genreList.get(i).getName());
          if (i != genreList.size() - 1) genreString.append(",");
        }

        movie.genre.setText(genreString.toString());
        String voteAverage =
            movies.get(finalPosition).getVoteAverage() > 0.0 ? movies.get(finalPosition)
                .getVoteAverage()
                .toString() : "N/A";
        movie.voteAverage.setText(voteAverage);
        movie.date.setText(DateUtil.getDate(movies.get(finalPosition).getReleaseDate()));
        String adult = movies.get(finalPosition).getAdult() ? "A" : "U/A";
        movie.adult.setText(adult);
        picasso.load(movies.get(finalPosition).getPosterUrl())
            .transform(transformation)
            .into(movie.poster);
      }
    }

    @Override public int getItemCount() {
      return movies.size() + 2;
    }

    @Override public int getItemViewType(int position) {
      if (position == 0) {
        return HEADER;
      } else if (position == movies.size() + 1) {
        return FOOTER;
      } else {
        return ITEM;
      }
    }

    private class VHMovie extends RecyclerView.ViewHolder {

      View itemView;
      AppCompatImageView poster;
      AppCompatTextView title;
      AppCompatTextView genre;
      AppCompatTextView date;
      AppCompatTextView review;
      AppCompatTextView adult;
      AppCompatTextView voteAverage;

      VHMovie(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.poster = (AppCompatImageView) itemView.findViewById(R.id.poster);
        this.title = (AppCompatTextView) itemView.findViewById(R.id.title);
        this.genre = (AppCompatTextView) itemView.findViewById(R.id.genre);
        this.date = (AppCompatTextView) itemView.findViewById(R.id.date);
        this.review = (AppCompatTextView) itemView.findViewById(R.id.review);
        this.adult = (AppCompatTextView) itemView.findViewById(R.id.adult);
        this.voteAverage = (AppCompatTextView) itemView.findViewById(R.id.vote_average);
      }
    }

    private class VHHeader extends RecyclerView.ViewHolder {

      View itemView;

      VHHeader(View itemView) {
        super(itemView);
        this.itemView = itemView;
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
}
