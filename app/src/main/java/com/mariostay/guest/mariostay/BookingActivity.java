package com.mariostay.guest.mariostay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingActivity extends AppCompatActivity {
    private Property property;
    private Room room;
    private FirebaseFirestore db;
    private FirebaseMessaging fm;
    private FirebaseAuth fa;
    private Dialog dialog;
    private Toast mToast;
    private String uid;
    @BindView(R.id.booking_toolbar) Toolbar toolbar;
    @BindView(R.id.booking_date_dispaly) TextView b_date;
    @BindView(R.id.booking_screen_account_details) TextView accno;
    @BindView(R.id.booking_screen_tnc) CheckBox tnc;
    @BindView(R.id.booking_screen_book) Button book;

    private CollectionReference requestsCollection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent in = getIntent();
        if(in == null) { finish();return; }
        property = in.getParcelableExtra(MainActivity.KEY_PROPERTY);
        if(property == null) { finish();return; }
        room = in.getParcelableExtra(PropertyDetailsActivity.KEY_ROOM);
        if(room == null) { finish();return; }

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(property.getName());
        toolbar.setSubtitle(getString(R.string.booking_screen_floor_display, room.getRoomNo(), room.getFloor()));
        b_date.setText(getString(R.string.booking_screen_date_dispaly, SimpleDateFormat.getDateInstance().format(Calendar.getInstance().getTime())));

        tnc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                book.setEnabled(isChecked);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //searchAndNotifyHost();
                if(dialog == null) return;
                //String uid = fa.getCurrentUser().getUid();
                if(uid == null) {
                    d("Unable to get Profile ID");
                    return;
                }
                dialog.show();
                Map<String, String> rMap = new HashMap<>();
                rMap.put("date", new Date().toString());
                requestsCollection.document(uid).set(rMap);
                //db.collection("properties").document(property.getPID()).collection("rooms").document(room.getRoomId()).update("status", 1);
            }
        });

        mToast = new Toast(this);
        db = FirebaseFirestore.getInstance();
        //fm = FirebaseMessaging.getInstance();
        fa = FirebaseAuth.getInstance();
        FirebaseUser fu = fa.getCurrentUser();
        if(fu != null) uid = fu.getUid();

        db.collection("USERS").document(property.getHID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult()!=null) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> token = document.getData();
                        if(token == null) return;
                        String acct = (String)token.get("accountNumber");
                        String tokenid = (String) token.get("tokenId");

                        //fm.send(new RemoteMessage.Builder());
                        /*
                            This doesn't work the way I thought it would.
4920616d20736f20667275737472617465642c2049206d696768742073696d706c7920666c6970206d79206465736b206f7665722e
                         */

                        accno.setText("Account number: "+acct);
                        AlertDialog.Builder ab = new AlertDialog.Builder(BookingActivity.this)
                                .setTitle("Details for payment")
                                .setMessage("To complete the payment, transfer the amount to the given account number: "+ acct)
                                .setPositiveButton("OK", null);

                        dialog = ab.create();

                        //Toast.makeText(BookingActivity.this, "Notify Host", Toast.LENGTH_SHORT).show();

                        Log.d("TAG", "DocumentSnapshot data: " + token);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                }
                else {
                    Log.d("TAG", "get failed with ", task.getException());
                    d("Unable to connect to database");
                    finish();
                }
            }
        });

        requestsCollection = db.collection("propertyRequests").document(property.getPID()).collection("requests");
    }

    private void d(String s) {
        mToast.cancel();
        mToast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        mToast.show();
    }
}
