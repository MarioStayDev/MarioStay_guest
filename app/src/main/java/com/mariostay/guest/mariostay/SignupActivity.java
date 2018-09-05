package com.mariostay.guest.mariostay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity
{
	@BindView(R.id.s_user) EditText mUser;
	@BindView(R.id.s_email) EditText mEmail;
	@BindView(R.id.s_pass) EditText mPass;
	@BindView(R.id.s_ph) EditText mPhone;
	//@BindView(R.id.s_otp) EditText mOtp;
	//@BindView(R.id.s_button_send) Button mBOtp;
	@BindView(R.id.s_otg) Button mBSignup;
	//@BindView(R.id.ProPic) CircleImageView imageView;

	// CircleImageView imageView = findViewById(R.id.ProPic);
	private Toast mToast;
	private boolean male=true,stud=true;
	private int OTG_LENGTH;

	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	//@BindView(R.id.SearchPic) FloatingActionButton searchPic;
	//private CircleImageView profilePic;
	private static int RESULT_LOAD_IMAGE=1;

	private static final int REQUEST_WRITE_PERMISSION = 786;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		db = FirebaseFirestore.getInstance();
		mAuth = FirebaseAuth.getInstance();

		ButterKnife.bind(this);

		/*searchPic.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//requestPermission();
				if (ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
				}
				else {
					readImage();
				}
			}
		});*/
		
		OTG_LENGTH=getResources().getInteger(R.integer.otp_length);
		mToast=new Toast(this);

		// USELESS BLOCK OF CODE :
		// START -->
		/*mUser=findViewById(R.id.s_user);
		mEmail=findViewById(R.id.s_email);
		mPass=findViewById(R.id.s_pass);
		mPhone=findViewById(R.id.s_ph);
		mOtp=findViewById(R.id.s_otp);
		mBSignup=findViewById(R.id.s_otg);
		mBOtp=findViewById(R.id.s_button_send);
		END -->*/

		// DO NOT DELETE,
		//	WILL BE USED IN A AFUTURE UPDATE
		/*mOtp.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence c,int i1,int i2,int i3) {}
			public void onTextChanged(CharSequence c,int i1, int i2,int i3) {}
			public void afterTextChanged(Editable e) {
				if(e.length()==OTG_LENGTH)
					mBSignup.setEnabled(true);
				else
					mBSignup.setEnabled(false);
			}
		});*/
		/*mBSignup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final Editable u=mUser.getText(),
						p=mPass.getText(),
						ph=mPhone.getText(),
						e=mEmail.getText();
				if(TextUtils.isEmpty(u)) d("Username required");
				else if(TextUtils.isEmpty(e)) d("Email required");
				else if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()) { d("Invalid email address!"); }
				else if(TextUtils.isEmpty(p)) d("Password required");
				else if(!Patterns.PHONE.matcher(ph).matches()) d("Invalid phone number");
				else {
					mAuth.createUserWithEmailAndPassword(e.toString(), p.toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if(task.isSuccessful()) {
								Log.d("TAG", "createUserWithEmailAndPassword:success");
								storeUserDetails(task.getResult().getUser().getUid(), u.toString(), male, stud, ph.toString());
								/*setResult(RESULT_OK);
								finish();**
							}
							else {
								Log.d("TAG", "createUserWithEmailAndPassword:failure", task.getException());
								d("Failed to create user");
							}
						}
					});
				}

				//new Register(u,p,ph,male,stud).execute();
				//d("Not yet implemented");
			}
		});*/
	}

	@OnClick(R.id.s_otg)
	void signup() {
		final Editable u=mUser.getText(),
				p=mPass.getText(),
				ph=mPhone.getText(),
				e=mEmail.getText();
		if(TextUtils.isEmpty(u)) d("Username required");
		else if(TextUtils.isEmpty(e)) d("Email required");
		else if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()) { d("Invalid email address!"); }
        else if(TextUtils.isEmpty(p)) d("Password required");
        else if(TextUtils.isEmpty(ph)) d("Phone number required");
		else if(!Patterns.PHONE.matcher(ph).matches()) d("Invalid phone number");
		else {
		    mBSignup.setEnabled(false);
			mAuth.createUserWithEmailAndPassword(e.toString(), p.toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if(task.isSuccessful()) {
						Log.d("TAG", "createUserWithEmailAndPassword:success");
						storeUserDetails(task.getResult().getUser().getUid(), u.toString(), male, stud, ph.toString());
								/*setResult(RESULT_OK);
								finish();*/
					}
					else {
						Log.d("TAG", "createUserWithEmailAndPassword:failure", task.getException());
						d("Failed to create user");
						mBSignup.setEnabled(true);
					}
				}
			});
		}
	}

	//@OnClick(R.id.SearchPic)
	void setPic() {
		if (ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
		}
		else {
			readImage();
		}
	}

	private void storeUserDetails(String docId, String name, boolean male, boolean student, String phoneNo) {
		Map<String, String> imap = new HashMap<>();
		imap.put("username", name);
		imap.put("gender", male ? "male" : "female");
		imap.put("workCategory", student ? "student" : "professional");
		imap.put("phone", phoneNo);
		db.collection("user").document(docId).set(imap).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if(task.isSuccessful()) {
					Log.d("TAG", "storeUser:success");
					setResult(RESULT_OK);
					finish();
				} else {
					Log.d("TAG", "storeUser:failure", task.getException());
					d("Failed to create user");
					mBSignup.setEnabled(true);
				}
			}
		});
	}
	
	public void setGender(View v) {
		switch(v.getId()) {
			case R.id.f:
				male=false;
				break;
			case R.id.m:
				male=true;
		}
	}
	
	public void setProf(View v) {
		switch(v.getId()) {
			case R.id.s:
				stud=true;
				break;
			case R.id.p:
				stud=false;
		}
	}
	
	/* DO NOT DELETE,
		WILL BE USED IN A FUTURE UPDATE
	public void send_otp(View v) {
		String u=mUser.getText().toString(),
			p=mPass.getText().toString(),
			ph=mPhone.getText().toString()
			,e=mEmail.getText().toString();
		if("".equals(u)) d("Username required");
		else if("".equals(e)) d("Email required");
		else if("".equals(p)) d("Password required");
		else if(!android.util.Patterns.PHONE.matcher(ph).matches()) d("Invalid phone number");
		else { d("not yet implemented"); }
	} */
	
	private void d(String s) {
		mToast.cancel();
		mToast=Toast.makeText(this,s,Toast.LENGTH_LONG);
		mToast.show();
	}

	private void readImage() {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_WRITE_PERMISSION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//success;
					readImage();
				}
				break;
		}

		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			if(selectedImage == null) return;
			String[] filePathColumn = {MediaStore.Images.Media.DATA};

			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			if (cursor == null) return;
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			//imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

			// String picturePath contains the path of selected Image
		}
	}
}