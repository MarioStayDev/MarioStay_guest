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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity
{
	private EditText mUser,mEmail,mPass,mPhone,mOtp;
	private Button mBOtp,mBSignup;
	private Toast mToast;
	private boolean male=true,stud=true;
	private int OTG_LENGTH;

	FloatingActionButton searchPic;
	private CircleImageView profilePic;
	private static int RESULT_LOAD_IMAGE=1;

	private static final int REQUEST_WRITE_PERMISSION = 786;

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
		{
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
		mToast=Toast.makeText(this,"",Toast.LENGTH_SHORT);
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
				//new Register(u,p,ph,male,stud).execute();
				d("Not yet implemented");
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
	
	private class Register extends AsyncTask<Void,Void,Void>
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

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
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