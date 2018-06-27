package com.mariostay.guest.mariostay;

import android.content.Context;
import java.text.NumberFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BrowseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    //private PropertyAdapter adapter;
    @BindView(R.id.browse_recycler) RecyclerView rv;
    @BindView(R.id.browse_progress) ProgressBar progressBar;
    private Unbinder unbinder;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;

    private Toast mToast;

    /*private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;*/



    public BrowseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = new Toast(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse, container, false);
        unbinder = ButterKnife.bind(this, v);

        //adapter = new PropertyAdapter(this);

        //linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //rv.setLayoutManager(linearLayoutManager);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        rv.setLayoutManager(layoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());
        db = FirebaseFirestore.getInstance();

        setupAdapter();

        /*rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });*/


        // mocking network delay for API call
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);*/
        return v;
    }

    private void setupAdapter() {
        Query q = db.collection("properties");

        FirestoreRecyclerOptions<Property> res = new FirestoreRecyclerOptions.Builder<Property>()
                .setQuery(q, Property.class).build();

        adapter = new FirestoreRecyclerAdapter<Property, PropertyHolder>(res) {
            NumberFormat format = NumberFormat.getCurrencyInstance();

            @Override
            protected void onBindViewHolder(@NonNull PropertyHolder holder, int position, @NonNull Property model) {
                progressBar.setVisibility(View.GONE);
                final int actualPosition = holder.getAdapterPosition();
                holder.propName.setText(model.getName());
                //holder.rating.setRating(3.5);
                holder.reviews.setText(getString(R.string.property_reviews, 6));
                holder.rent.setText(getString(R.string.property_rent, format.format(10000)));
                //Glide.with(getActivity()).load(model.image).into(holder.imgview);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d("Clicked pos " + actualPosition);
                        //mListener.onFragmentInteraction(null);
                    }
                });
            }

            @NonNull
            @Override
            public PropertyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PropertyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item, parent, false));
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
    }

    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
/*
    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
        List<Movie> movies = Movie.createMovies(adapter.getItemCount());
        progressBar.setVisibility(View.GONE);
        adapter.addAll(movies);

        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;

    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        List<Movie> movies = Movie.createMovies(adapter.getItemCount());

        adapter.removeLoadingFooter();
        isLoading = false;

        adapter.addAll(movies);

        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void d(String st) {
        mToast.cancel();
        mToast = Toast.makeText(getActivity(), st, Toast.LENGTH_LONG);
        mToast.show();
    }

    class PropertyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.property_item_name) TextView propName;
        @BindView(R.id.property_item_rent) TextView rent;
        @BindView(R.id.property_item_reviews) TextView reviews;
        @BindView(R.id.property_item_rating) MaterialRatingBar rating;
        //@BindView(R.id.property_item_pic) ImageView pic;

        PropertyHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

    