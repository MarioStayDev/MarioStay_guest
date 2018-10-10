package com.mariostay.guest.mariostay;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PropertyDetailsActivity extends AppCompatActivity {

    private boolean first;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    static final String KEY_ROOM = "com.mariostay.guest.mariostay.KEY_ROOM";
    private MenuItem favicon;
    private Property property;
    private AlertDialog alertRules;

    final int REQUEST_BOOKING = 105;
    private View tempView;
    private Resources res;
    private final int imgRight = 200, imgBottom = 200;
    //private List<Room> Rooms;
    //@BindViews({R.id.chip1, R.id.chip2, R.id.chip3, R.id.chip4, R.id.chip5, R.id.chip6, R.id.chip7, R.id.chip8, R.id.chip9, R.id.chip10, R.id.chip11, R.id.chip12, R.id.chip13, R.id.chip14, R.id.chip15})
    //List<ImageView> chips;
    @BindView(R.id.property_details_toolbar) Toolbar toolbar;
    @BindView(R.id.property_details_desc) TextView desc;

    @BindView(R.id.chip_lift) ImageView chip_lift;
    @BindView(R.id.chip_parking) ImageView chip_parking;
    @BindView(R.id.chip_cctv) ImageView chip_cctv;
    @BindView(R.id.chip_power) ImageView chip_power;
    @BindView(R.id.chip_playground) ImageView chip_playground;
    @BindView(R.id.chip_pool) ImageView chip_pool;
    @BindView(R.id.chip_garden) ImageView chip_garden;
    @BindView(R.id.chip_gym) ImageView chip_gym;
    @BindView(R.id.chip_tv) ImageView chip_tv;
    @BindView(R.id.chip_refridgerator) ImageView chip_fridge;
    @BindView(R.id.chip_washing_machine) ImageView chip_wash;
    @BindView(R.id.chip_water_purifier) ImageView chip_water;
    @BindView(R.id.chip_wifi) ImageView chip_wifi;
    @BindView(R.id.chip_sofa) ImageView chip_sofa;
    @BindView(R.id.chip_table) ImageView chip_table;

    @BindView(R.id.property_details_in_time) TextView inTime;
    @BindView(R.id.property_details_out_time) TextView outTime;
    @BindView(R.id.property_details_security_deposit) TextView securityDeposit;
    @BindView(R.id.property_details_notice_period) TextView noticePeriod;
    @BindView(R.id.property_details_min_stay) TextView minTime;
    @BindView(R.id.property_details_rules) TextView rules;
    @BindView(R.id.property_details_rooms_container) RecyclerView frame;

    @BindView(R.id.property_details_prop_pic) ImageView propPic;
    private Toast mToast;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        ButterKnife.bind(this);
        mToast = new Toast(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            property = getIntent().getParcelableExtra(MainActivity.KEY_PROPERTY);
            actionBar.setTitle(property.getName());
        }

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        first = true;
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_property_details, menu);
        favicon = menu.findItem(R.id.menu_property_fav);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_property_share:

                return true;
            case R.id.menu_property_fav:
                if(getString(R.string.menu_property_fav_no).equals(favicon.getTitle().toString())) {
                    favicon.setTitle(R.string.menu_property_fav);
                    favicon.setIcon(R.drawable.ic_favorite_white_24dp);
                }
                else {
                    favicon.setTitle(R.string.menu_property_fav_no);
                    favicon.setIcon(R.drawable.ic_favorite_border_white_24dp);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initData () {
        desc.setText(property.getShortDescription());
        Map<String, Boolean> imap = property.getAmenities();

        AlertDialog.Builder albu = new AlertDialog.Builder(this);
        albu.setTitle(getString(R.string.property_details_rules_alert));
        albu.setMessage(property.getRules() == null ? getString(R.string.property_details_rules_absent) : property.getRules());
        albu.setPositiveButton(R.string.OK_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertRules = albu.create();
        //System.out.println(imap.toString());
        /*System.out.println("Property is "+(property == null ? "" : "not ")+"null");
        System.out.println("Map is "+(imap == null ? "" : "not ")+"null");
        System.out.println("Chip is "+(chip_cctv == null ? "" : "not ")+"null");*/
        chip_lift.setVisibility(imap.get(getString(R.string.chip_text_lift)) ? View.VISIBLE : View.GONE);
        chip_parking.setVisibility(imap.get(getString(R.string.chip_text_parking)) ? View.VISIBLE : View.GONE);
        chip_cctv.setVisibility(imap.get(getString(R.string.chip_text_cctv)) ? View.VISIBLE : View.GONE);
        chip_power.setVisibility(imap.get(getString(R.string.chip_text_power)) ? View.VISIBLE : View.GONE);
        chip_playground.setVisibility(imap.get(getString(R.string.chip_text_playground)) ? View.VISIBLE : View.GONE);
        chip_pool.setVisibility(imap.get(getString(R.string.chip_text_pool)) ? View.VISIBLE : View.GONE);
        chip_garden.setVisibility(imap.get(getString(R.string.chip_text_garden)) ? View.VISIBLE : View.GONE);
        chip_gym.setVisibility(imap.get(getString(R.string.chip_text_gym)) ? View.VISIBLE : View.GONE);
        chip_tv.setVisibility(imap.get(getString(R.string.chip_text_tv)) ? View.VISIBLE : View.GONE);
        chip_fridge.setVisibility(imap.get(getString(R.string.chip_text_refridgerator)) ? View.VISIBLE : View.GONE);
        chip_wash.setVisibility(imap.get(getString(R.string.chip_text_washing_machine)) ? View.VISIBLE : View.GONE);
        chip_water.setVisibility(imap.get(getString(R.string.chip_text_water_purifier)) ? View.VISIBLE : View.GONE);
        chip_wifi.setVisibility(imap.get(getString(R.string.chip_text_wifi)) ? View.VISIBLE : View.GONE);
        chip_sofa.setVisibility(imap.get(getString(R.string.chip_text_sofa)) ? View.VISIBLE : View.GONE);
        chip_table.setVisibility(imap.get(getString(R.string.chip_text_table)) ? View.VISIBLE : View.GONE);

        String temp = property.getInTime();
        inTime.setText(getString(R.string.in_time_flexible, temp == null || temp.length() == 0 || temp.equals("0") ? "Flexible" : temp));
        temp = property.getOutTime();
        outTime.setText(getString(R.string.out_time_flexible, temp == null || temp.length() == 0 || temp.equals("0") ? "Flexible" : temp));
        securityDeposit.setText(getString(R.string.property_details_security_deposit, property.getSecurityMultiplier()));
        noticePeriod.setText(getString(R.string.property_details_notice_period, property.getNoticePeriod()));
        minTime.setText(getString(R.string.property_details_min_stay_time, property.getMinStayTime()));
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(getString(R.string.property_details_rules));
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //d("Dialogue");

                alertRules.show();
            }
        }, 8, 12, 0);
        rules.setMovementMethod(LinkMovementMethod.getInstance());
        rules.setText(sb);

    }

    private void init() {
        //initData();
        //Rooms = new ArrayList<>();
        StorageReference ref = storage.getReference("/users/PropertyPic/" + property.getPID() + "/0.jpg");
        GlideApp.with(this).load(ref).placeholder(R.drawable.ic_launcher_background).into(propPic);


        res = getResources();

        roomAdapter = new RoomAdapter();
        frame.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        frame.setAdapter(roomAdapter);
        //roomAdapter.addRoom(null);
        DocumentReference doc = db.collection("properties").document(property.getPID());
        doc.collection("rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                //Room i = doc.toObject(Room.class);
                                roomAdapter.addRoom(doc.toObject(Room.class));
                            }
                            roomAdapter.removeLoading(false);
                            //refreshRoomsList(true);
                        }
                        else {
                            Log.d("TAG", "Error getting documents : ", task.getException());
                            //refreshRoomsList(false);
                            roomAdapter.removeLoading(true);
                        }
                    }
                });

        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }

                if(documentSnapshot != null && documentSnapshot.exists()) {
                    Property np = documentSnapshot.toObject(Property.class);
                    if(first) first = false;
                    else d("This property was updated");
                    property = null;
                    property = np;
                    initData();
                }
                else {
                    d("This property no longer exists");
                    finish();
                }
            }
        });
    }

    /*private void refreshRoomsList(boolean isSuccessful) {

    }*/

    private void d(String s) {
        mToast.cancel();
        mToast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        mToast.show();
    }

    class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater inf = LayoutInflater.from(PropertyDetailsActivity.this);
        private List<Room> Rooms;
        private final int LOADING = 0, NOT_LOADING = 1;

        RoomAdapter() {
            Rooms = new ArrayList<>();
            Rooms.add(null);
        }

        void addRoom(Room room) {
            int lastIndex = Rooms.size() - 1;
            Rooms.remove(lastIndex);
            Rooms.add(room);
            notifyItemChanged(lastIndex);
            Rooms.add(null);
            notifyItemInserted(lastIndex + 1);
        }

        void removeLoading(boolean error) {
            int lastIndex = Rooms.size() - 1;
            Rooms.remove(lastIndex);
            notifyItemRemoved(lastIndex);
            if(error) {
                d("Some error occurred");
            }
        }

        @Override
        public int getItemViewType(int position) {
            return Rooms.get(position) == null ? LOADING : NOT_LOADING;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == LOADING) {
                View v = inf.inflate(R.layout.list_load, parent, false);
                return new LoadingHolder(v);
            }
            else {
                View v = inf.inflate(R.layout.list_rooms, parent, false);
                return new RoomHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder tholder, int position) {
            switch(tholder.getItemViewType()) {
                case LOADING:

                    break;
                case NOT_LOADING:
                    final Room t = Rooms.get(position);

                    final RoomHolder holder = (RoomHolder) tholder;
                    holder.roomNo.setText(getString(R.string.property_room_room_no, t.getRoomNo()));

                    final StorageReference ref0 = storage.getReference("/users/PropertyPic/" + property.getPID() + "/" + t.getRoomId() + "/0.jpg");
                    final StorageReference ref1 = storage.getReference("/users/PropertyPic/" + property.getPID() + "/" + t.getRoomId() + "/1.jpg");
                    final StorageReference ref2 = storage.getReference("/users/PropertyPic/" + property.getPID() + "/" + t.getRoomId() + "/2.jpg");
                    final StorageReference ref3 = storage.getReference("/users/PropertyPic/" + property.getPID() + "/" + t.getRoomId() + "/3.jpg");

                    GlideApp.with(PropertyDetailsActivity.this).load(ref0).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.roomPhoto);
                    GlideApp.with(PropertyDetailsActivity.this).load(ref0).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.tb0);
                    GlideApp.with(PropertyDetailsActivity.this).load(ref1).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.tb1);
                    GlideApp.with(PropertyDetailsActivity.this).load(ref2).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.tb2);
                    GlideApp.with(PropertyDetailsActivity.this).load(ref3).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.tb3);

                    View.OnClickListener selectPic = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int id = view.getId();


                            Drawable u = res.getDrawable(R.drawable.border_unselected);
                            Drawable s = res.getDrawable(R.drawable.border_selected);
                            int pc = res.getColor(R.color.colorPrimary);
                            int ac = res.getColor(R.color.colorAccent);

                            /*holder.tb0.setBackground(u);
                            holder.tb1.setBackground(u);
                            holder.tb2.setBackground(u);
                            holder.tb3.setBackground(u);*/

                            ((CircleImageView)holder.tb0).setBorderColor(pc);
                            ((CircleImageView)holder.tb1).setBorderColor(pc);
                            ((CircleImageView)holder.tb2).setBorderColor(pc);
                            ((CircleImageView)holder.tb3).setBorderColor(pc);

                            switch(id) {
                                case R.id.room_pic_thumb0:
                                    try {GlideApp.with(PropertyDetailsActivity.this).load(ref0).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.roomPhoto);}
                                    catch (Exception e) { Log.d("TAG", "IMAGE NOT PRESENT"); }
                                    break;
                                case R.id.room_pic_thumb1:
                                    GlideApp.with(PropertyDetailsActivity.this).load(ref1).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.roomPhoto);
                                    break;
                                case R.id.room_pic_thumb2:
                                    GlideApp.with(PropertyDetailsActivity.this).load(ref2).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.roomPhoto);
                                    break;
                                case R.id.room_pic_thumb3:
                                    GlideApp.with(PropertyDetailsActivity.this).load(ref3).   centerCrop()   .placeholder(R.drawable.ic_placeholder_384dp).into(holder.roomPhoto);
                                    break;

                            }
                            ((CircleImageView)view).setBorderColor(ac);
                            //view.setBackground(s);
                        }
                    };
                    holder.tb0.setOnClickListener(selectPic);
                    holder.tb1.setOnClickListener(selectPic);
                    holder.tb2.setOnClickListener(selectPic);
                    holder.tb3.setOnClickListener(selectPic);


                    Map<String, Boolean> m = t.getAmenities();
                    holder.chip_room_ac.setVisibility(m.get(getString(R.string.chip_text_ac)) ? View.VISIBLE : View.GONE);
                    holder.chip_room_tv.setVisibility(m.get(getString(R.string.chip_text_room_tv)) ? View.VISIBLE : View.GONE);
                    holder.chip_room_balcony.setVisibility(m.get(getString(R.string.chip_text_balcony)) ? View.VISIBLE : View.GONE);
                    holder.chip_room_wardrobe.setVisibility(m.get(getString(R.string.chip_text_wardrobe)) ? View.VISIBLE : View.GONE);
                    holder.chip_room_washroom.setVisibility(m.get(getString(R.string.chip_text_attached_washroom)) ? View.VISIBLE : View.GONE);
                    holder.chip_room_gyser.setVisibility(m.get(getString(R.string.chip_text_gyser)) ? View.VISIBLE : View.GONE);
                    holder.chip_room_sofa.setVisibility(m.get(getString(R.string.chip_text_sofa)) ? View.VISIBLE : View.GONE);
                    holder.chip_room_table.setVisibility(m.get(getString(R.string.chip_text_table)) ? View.VISIBLE : View.GONE);

                    //Put beds here
                    holder.bed_con.removeAllViews();
                    /*List<Integer> mbeds = t.getBedStats();
                    /*for(int i : mbeds) {
                        ImageView iv = new ImageView(PropertyDetailsActivity.this);
                        iv.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
                        if(i == 0) {
                            iv.setImageResource(R.drawable.bed);
                            if(!holder.buttonBook.isEnabled()) holder.buttonBook.setEnabled(true);
                        } else iv.setImageResource(R.drawable.bed_un);
                        //iv.setImageResource(i == 0 ? R.drawable.bed/*av** : R.drawable.bed_un/*unav**);
                        holder.bed_con.addView(iv);
                    }**
                    int imgRight = 200, imgBottom = 200;
                    for(int i : mbeds) {
                        TextView iv = new TextView(PropertyDetailsActivity.this);
                        iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        iv.setGravity(Gravity.CENTER);
                        if(i == 0) {
                            iv.setText(String.valueOf(t.getRent()));
                            Drawable d = getResources().getDrawable(R.drawable.bed);
                            d.setBounds(0, 0, imgRight, imgBottom);
                            iv.setCompoundDrawables(null, d, null, null);
                            //if(!holder.buttonBook.isEnabled()) holder.buttonBook.setEnabled(true);

                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent i = new Intent(PropertyDetailsActivity.this, BookingActivity.class);
                                    i.putExtra(MainActivity.KEY_PROPERTY, property);
                                    i.putExtra(KEY_ROOM, t);
                                    startActivity(i);

                                }
                            });
                            iv.setClickable(true);
                            //iv.setBackgroundResource(android.R.attr.selectableItemBackground);
                        } else {
                            iv.setText("not available");
                            Drawable d = getResources().getDrawable(R.drawable.bed_un);
                            d.setBounds(0, 0, imgRight, imgBottom);
                            iv.setCompoundDrawables(null, d, null, null);
                        }
                        holder.bed_con.addView(iv);
                    }*/

                    /*holder.buttonBook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(PropertyDetailsActivity.this, BookingActivity.class);
                            i.putExtra(MainActivity.KEY_PROPERTY, property);
                            i.putExtra(KEY_ROOM, t);
                            startActivity(i);
                        }
                    });*/

                    //int imgRight = 200, imgBottom = 200;
                    int b = t.getBeds();
                    List<String> bs = t.getBedStatsNew();
                    if(bs != null) {
                        for(String s : bs) {
                            TextView iv = new TextView(PropertyDetailsActivity.this);
                            iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            iv.setGravity(Gravity.CENTER);
                            iv.setText("not available");
                            Drawable d = res.getDrawable(R.drawable.bed_un);
                            d.setBounds(0, 0, imgRight, imgBottom);
                            iv.setCompoundDrawables(null, d, null, null);
                            holder.bed_con.addView(iv);
                            b--;
                        }
                    }
                    for(int i = 0; i < b; i++) {
                        TextView iv = new TextView(PropertyDetailsActivity.this);
                        iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        iv.setGravity(Gravity.CENTER);
                        iv.setText(String.valueOf(t.getRent()));
                        Drawable d = res.getDrawable(R.drawable.bed);
                        d.setBounds(0, 0, imgRight, imgBottom);
                        iv.setCompoundDrawables(null, d, null, null);
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(PropertyDetailsActivity.this, BookingActivity.class);
                                i.putExtra(MainActivity.KEY_PROPERTY, property);
                                i.putExtra(KEY_ROOM, t);
                                tempView = view;
                                startActivityForResult(i, REQUEST_BOOKING);

                            }
                        });
                        iv.setClickable(true);
                        holder.bed_con.addView(iv);
                    }
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return Rooms == null ? 0 : Rooms.size();
        }

        class RoomHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.property_room_photo) ImageView roomPhoto;
            @BindView(R.id.property_room_room_no) TextView roomNo;

            /* List of amenities */
            @BindView(R.id.chip_AC) ImageView chip_room_ac;
            @BindView(R.id.chip_TV) ImageView chip_room_tv;
            @BindView(R.id.chip_balcony) ImageView chip_room_balcony;
            @BindView(R.id.chip_wardrobe) ImageView chip_room_wardrobe;
            @BindView(R.id.chip_washroom) ImageView chip_room_washroom;
            @BindView(R.id.chip_Gyser) ImageView chip_room_gyser;
            @BindView(R.id.chip_sofa) ImageView chip_room_sofa;
            @BindView(R.id.chip_table) ImageView chip_room_table;

            @BindView(R.id.room_pic_thumb0) ImageView tb0;
            @BindView(R.id.room_pic_thumb1) ImageView tb1;
            @BindView(R.id.room_pic_thumb2) ImageView tb2;
            @BindView(R.id.room_pic_thumb3) ImageView tb3;

            /* Beds are limited to 5 as of now */
            @BindView(R.id.property_room_beds) FlexboxLayout bed_con;
            //@BindView(R.id.property_rooms_book) Button buttonBook;
            ImageView bed1, bed2, bed3, bed4, bed5, bed6;

            RoomHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        class LoadingHolder extends RecyclerView.ViewHolder {

            LoadingHolder(View v) {
                super(v);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_BOOKING:
                if(resultCode == RESULT_OK) {
                    //View ag = (TextView)tempView;
                    ((TextView) tempView).setText("not available");
                    Drawable d = res.getDrawable(R.drawable.bed_un);
                    d.setBounds(0, 0, imgRight, imgBottom);
                    ((TextView) tempView).setCompoundDrawables(null, d, null, null);
                    tempView.setOnClickListener(null);
                }
                tempView = null;
                break;
        }
    }
}
