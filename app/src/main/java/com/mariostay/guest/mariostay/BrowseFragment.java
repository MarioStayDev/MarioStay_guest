package com.mariostay.guest.mariostay;

import android.content.Context;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BrowseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.browse_recycler) RecyclerView rv;
    //@BindView(R.id.browse_progress) ProgressBar progressBar;
    @BindView(R.id.browse_progress) TextView progressBar;
    private Unbinder unbinder;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    //private FirestoreRecyclerAdapter adapter;
    private PropertyAdapter adapter;

    private Toast mToast;
    private NumberFormat format;

    private final String KEY_PROPERTY = "com.mariostay.guest.mariostay.KEY_PRPERTY";

    public BrowseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = new Toast(getActivity());
        format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("INR"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse, container, false);
        unbinder = ButterKnife.bind(this, v);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        rv.setLayoutManager(layoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        setupAdapter();

        return v;
    }

    private void setupAdapter() {

        adapter = new PropertyAdapter(getContext());

        //Query q = db.collection("properties").whereGreaterThan("rooms", 0);
        CollectionReference props = db.collection("properties");
        /*props.whereGreaterThan("rooms", 0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("BrowseFragment", "Query rooms successful");

                    progressBar.setVisibility(View.GONE);
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        adapter.addProperty(doc.toObject(Property.class));
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("BrowseFragment", "Query rooms failed", task.getException());
                    progressBar.setVisibility(View.GONE);
                }
            }
        });*/

        /*FirestoreRecyclerOptions<Property> res = new FirestoreRecyclerOptions.Builder<Property>()
                .setQuery(q, Property.class).build();

        adapter = new FirestoreRecyclerAdapter<Property, PropertyHolder>(res) {

            @Override
            protected void onBindViewHolder(@NonNull PropertyHolder holder, int position, @NonNull final Property model) {
                progressBar.setVisibility(View.GONE);
                final int actualPosition = holder.getAdapterPosition();
                holder.propName.setText(model.getName());
                /*holder.rating.setRating(3.5);
                holder.reviews.setText(getString(R.string.property_reviews, 6));**
                holder.rent.setText(getString(R.string.property_rent, format.format(10000)));
                //Glide.with(getActivity()).load(model.image).into(holder.imgview);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //d("Clicked pos " + actualPosition);
                        Bundle bundle = new Bundle();
                        //bundle.putString(KEY_PROPERTY, model.getPID());
                        bundle.putParcelable(KEY_PROPERTY, model);
                        mListener.onPropertyClicked(bundle);
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
        };*/

        //adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        props.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                /*List<String> cities = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("name") != null) {
                        cities.add(doc.getString("name"));
                    }
                }
                Log.d("TAG", "Current cites in CA: " + cities);*/
                Property p;
                for(DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            p = dc.getDocument().toObject(Property.class);
                            Log.d("TAG", "Property added : " + p.getPID());
                            if(p.getRooms() > 0) {
                                adapter.addProperty(p);
                                adapter.notifyItemInserted(adapter.getItemCount() - 1);
                                if(adapter.getItemCount() > 0 && progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
                            }
                            else Log.d("TAG", "no rooms in : " + p.getPID());
                            break;
                        case MODIFIED:
                            p = dc.getDocument().toObject(Property.class);
                            Log.d("TAG", "Property modified : " + p.getPID() + ", rooms : " + p.getRooms());
                            if(p.getRooms() > 0 && adapter.replaceProperty(p) < 0) {
                                Log.d("TAG", "Error updating pid:" + p.getPID());
                                Log.d("TAG", "Trying to add it");
                                adapter.addProperty(p);
                                adapter.notifyItemInserted(adapter.getItemCount() - 1);
                                progressBar.setVisibility(View.GONE);
                            }
                            else if(p.getRooms() == 0) {
                                if (adapter.removeProperty(p) < 0) Log.d("TAG", "Error removing pid:" + p.getPID());
                                else if(adapter.getItemCount() == 0 && progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);
                            }
                            break;
                        case REMOVED:
                            p = dc.getDocument().toObject(Property.class);
                            Log.d("TAG", "Property removed : " + p.getPID());
                            if(p.getRooms() > 0) {
                                if (adapter.removeProperty(p) < 0) Log.d("TAG", "Error removing pid:" + p.getPID());
                                else if(adapter.getItemCount() == 0 && progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);
                            }
                    }
                }
            }
        });
    }

    class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyHolder> {
        List<Property> properties;
        LayoutInflater layoutInflater;

        PropertyAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
            properties = new ArrayList<>();
        }

        @NonNull
        @Override
        public PropertyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PropertyHolder(layoutInflater.inflate(R.layout.property_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PropertyHolder holder, int position) {
            final Property model = properties.get(position);

            holder.propName.setText(model.getName());
            /*holder.rating.setRating(3.5);
            holder.reviews.setText(getString(R.string.property_reviews, 6));*/
            holder.rent.setText(getString(R.string.property_rent, format.format(model.getRent())));
            StorageReference ref = storage.getReference("/users/PropertyPic/" + model.getPID() + "/0.jpg");
            GlideApp.with(getActivity()).load(ref).placeholder(R.drawable.ic_launcher_background).into(holder.pic);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //d("Clicked pos " + actualPosition);
                    Bundle bundle = new Bundle();
                    //bundle.putString(KEY_PROPERTY, model.getPID());
                    bundle.putParcelable(KEY_PROPERTY, model);
                    mListener.onPropertyClicked(bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return properties == null ? 0 : properties.size();
        }

        void addProperty(Property property) {
            properties.add(property);
            //notifyItemInserted(properties.indexOf(property));
        }

        int replaceProperty(Property property) {
            int pos = -1;
            String pid = property.getPID();
            for(Property p : properties) {
                if(pid.equals(p.getPID())) {
                    pos = properties.indexOf(p);
                    properties.remove(pos);
                    properties.add(pos, property);
                    notifyItemChanged(pos);
                    break;
                }
            }
            return pos;
        }

        int removeProperty(Property property) {
            int pos = -1;
            String pid = property.getPID();
            for(Property p : properties) {
                if(pid.equals(p.getPID())) {
                    pos = properties.indexOf(p);
                    properties.remove(pos);
                    notifyItemRemoved(pos);
                    break;
                }
            }
            return pos;
        }

        class PropertyHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.property_item_name) TextView propName;
            @BindView(R.id.property_item_rent) TextView rent;
            /*@BindView(R.id.property_item_reviews) TextView reviews;
            @BindView(R.id.property_item_rating) MaterialRatingBar rating;*/
            @BindView(R.id.property_item_pic) ImageView pic;

            PropertyHolder(View v) {
                super(v);
                ButterKnife.bind(this,v);
            }
        }
    }

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
        void onPropertyClicked(Bundle bundle);
    }

    private void d(String st) {
        mToast.cancel();
        mToast = Toast.makeText(getActivity(), st, Toast.LENGTH_LONG);
        mToast.show();
    }

    /*class PropertyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.property_item_name) TextView propName;
        @BindView(R.id.property_item_rent) TextView rent;
        /*@BindView(R.id.property_item_reviews) TextView reviews;
        @BindView(R.id.property_item_rating) MaterialRatingBar rating;**
        //@BindView(R.id.property_item_pic) ImageView pic;

        PropertyHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }*/

    /*@Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }*/
}

    