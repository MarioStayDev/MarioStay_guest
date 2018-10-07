package com.mariostay.guest.mariostay;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity
{
	@BindView(R.id.login_email) EditText mEmail;
	@BindView(R.id.login_password) EditText mPassword;
	@BindView(R.id.login_button_login) Button buttonLogin;
	@BindView(R.id.login_button_signup) Button buttonSignup;
	private final int REQUEST_SIGNUP=101;
	private Unbinder unbinder;
	private Toast mToast;

	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		unbinder = ButterKnife.bind(this);
		mToast = new Toast(this);

		mAuth = FirebaseAuth.getInstance();
		
		buttonLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Editable n,p;
				n = mEmail.getText();
				p = mPassword.getText();
				if(TextUtils.isEmpty(n)) d("Username required");
				else if(TextUtils.isEmpty(p)) d("Password required");
				else if(!Patterns.EMAIL_ADDRESS.matcher(n).matches()) d("Invalid email address");
				else {
					buttonLogin.setEnabled(false);
					buttonSignup.setEnabled(false);
					mAuth.signInWithEmailAndPassword(n.toString(), p.toString())
							.addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
								@Override
								public void onComplete(@NonNull Task<AuthResult> task) {
									if (task.isSuccessful()) {
										//FirebaseUser user = mAuth.getCurrentUser();
										Log.d("TAG", "signInWithEmail:success");
										setResult(RESULT_OK);
										finish();
									} else {
										buttonLogin.setEnabled(true);
										buttonSignup.setEnabled(true);
										Log.d("TAG", "signInWithEmail:error", task.getException());
										Exception e = task.getException();
										if (e instanceof FirebaseAuthInvalidUserException)
											d("Invalid username");
										else if (e instanceof FirebaseAuthInvalidCredentialsException)
											d("Invalid password");
										else d("Some error occurred");
									}
								}
							});
				}
			}
		});
		buttonSignup.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent ri = new Intent(LoginActivity.this,SignupActivity.class);
					startActivityForResult(ri,REQUEST_SIGNUP);
				}
			});
		
		setResult(RESULT_CANCELED);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

	/* DO NOT DELETE,
		COMING SOON IN A FUTURE UPDATE
	public void guest(View v) {

		mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()) {
					//FirebaseUser user = task.getResult().getUser();
					Log.d("TAG", "signInAnonymously:success");
					setResult(RESULT_OK);
					finish();
				}
				else {
					Log.d("TAG", "signInAnonymously:error", task.getException());
					d("Some error occurred");
				}
			}
		});
		/*Intent data = new Intent();
		data.putExtra("GUEST", true);
		setResult(RESULT_OK, data);
		finish();**
	}*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case REQUEST_SIGNUP:
				if(resultCode == RESULT_OK) {
					setResult(RESULT_OK);
					finish();
				}
		}
	}
	
	private void d(String s) {
		mToast.cancel();
		mToast = Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT);
		mToast.show();
	}
	
}