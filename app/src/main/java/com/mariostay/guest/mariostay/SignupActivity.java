package com.mariostay.guest.mariostay;

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
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity
{
	private EditText mUser,mEmail,mPass,mPhone,mOtp;
	private Button mBOtp,mBSignup;
	private Toast mToast;
	private boolean male=true,stud=true;
	private int OTG_LENGTH;

	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	FloatingActionButton searchPic;
	private CircleImageView profilePic;
	private static int RESULT_LOAD_IMAGE=1;

	private static final int REQUEST_WRITE_PERMISSION = 786;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			Intent i = new Intent(
					Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
	}

	private void requestPermission()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
		} else
		{
			Intent i = new Intent(
					Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		db = FirebaseFirestore.getInstance();
		mAuth = FirebaseAuth.getInstance();

		searchPic = (FloatingActionButton) findViewById(R.id.SearchPic);


		searchPic.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				requestPermission();


			}
		});
		
		OTG_LENGTH=getResources().getInteger(R.integer.otp_length);
		mToast=new Toast(this);
		mUser=findViewById(R.id.s_user);
		mEmail=findViewById(R.id.s_email);
		mPass=findViewById(R.id.s_pass);
		mPhone=findViewById(R.id.s_ph);
		mOtp=findViewById(R.id.s_otp);
		mBSignup=findViewById(R.id.s_otg);
		mBOtp=findViewById(R.id.s_button_send);
		mOtp.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence c,int i1,int i2,int i3) {}
			public void onTextChanged(CharSequence c,int i1, int i2,int i3) {}
			public void afterTextChanged(Editable e) {
				if(e.length()==OTG_LENGTH)
					mBSignup.setEnabled(true);
				else
					mBSignup.setEnabled(false);
			}
		});
		mBSignup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final Editable u=mUser.getText(),
						p=mPass.getText(),
						ph=mPhone.getText(),
						e=mEmail.getText();
				if(TextUtils.isEmpty(u)) d("Username required");
				else if(TextUtils.isEmpty(e)) d("Email required");
				else if(TextUtils.isEmpty(p)) d("Password required");
				else if(!android.util.Patterns.PHONE.matcher(ph).matches()) d("Invalid phone number");
				else {
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
							}
						}
					});
				}

				//new Register(u,p,ph,male,stud).execute();
				//d("Not yet implemented");
			}
		});
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
	}
	
	private void d(String s) {
		mToast.cancel();
		mToast=Toast.makeText(this,s,Toast.LENGTH_LONG);
		mToast.show();
	}
	
	/*private class Register extends AsyncTask<Void,Void,Void>
	{
		private String name,pass,phone;
		private char gender,profession;
		
		public Register(String n,String p,String ph,boolean m,boolean s) {
			name=n;pass=p;phone=ph;
			gender=m?'m':'f';
			profession=s?'s':'p';
		}

		@Override
		protected void onPreExecute() { super.onPreExecute(); }

		@Override
		protected void onPostExecute(Void result) { super.onPostExecute(result); }

		@Override
		protected Void doInBackground(Void[] p1) { return null; }

	}*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			if(selectedImage == null) return;
			String[] filePathColumn = {MediaStore.Images.Media.DATA};

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			CircleImageView imageView = (CircleImageView) findViewById(R.id.ProPic);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

			// String picturePath contains the path of selected Image
		}
	}
}