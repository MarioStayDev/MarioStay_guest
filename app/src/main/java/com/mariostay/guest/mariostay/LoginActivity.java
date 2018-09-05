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
		//mToast=Toast.makeText(this,"",Toast.LENGTH_SHORT);
		mToast = new Toast(this);
		/*mEmail = findViewById(R.id.email);
		mPassword = findViewById(R.id.password);
		buttonLogin = findViewById(R.id.button_login);
		buttonSignup = findViewById(R.id.button_signup);*/

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
				/*if("".equals(n))
					d("Username required");
				else if("".equals(p))
					d("Password required");
				else
					new ValidateLogin(n,p).execute();*/
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
	
	/*private class ValidateLogin extends AsyncTask<Void,Void,Void>
	{
		private String data,error, na, em = "dummy@email.com";
		private URL url;
		
		ValidateLogin(String n,String p) {
			na = n;
			try {
				data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(na, "UTF-8");
				data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(p, "UTF-8");
			}
			catch(UnsupportedEncodingException e) {error = e.toString();}
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			buttonLogin.setEnabled(false);
			buttonSignup.setEnabled(false);
			try {
				//url = new URL("http://client.epizy.com/mario/login.php");
				url = new URL("http://skhastagir98.000webhostapp.com/login.php");
			}
			catch(MalformedURLException e) {error = e.toString();}
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			buttonLogin.setEnabled(true);
			buttonSignup.setEnabled(true);
			if(error == null) {
				JSONObject jobj;
				try {
					jobj = new JSONObject(data);
					if(jobj.getInt("success") == 1) {
						Intent data = new Intent();
						data.putExtra("GUEST", false);
						getSharedPreferences(MainActivity.KEY_SHARED_PREFERENCE,MODE_PRIVATE).edit().putString(MainActivity.KEY_USER_NAME, na).putString(MainActivity.KEY_EMAIL, em).apply();
						setResult(RESULT_OK, data);
						finish();
					}
					else {
						d(jobj.getString("message"));
					}
				}
				catch(JSONException e) {d(e.toString());}
			}
			else {
				d(error);
			}
		}

		@Override
		protected Void doInBackground(Void[] p1)
		{
			try {
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setUseCaches(true);
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(5000);
				
				//conn.setDoOutput(true); 
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
				wr.write(data); 
				wr.flush();
				wr.close();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;

				while((line = reader.readLine()) != null)
					sb.append(line).append("\n");
				reader.close();

				data = sb.toString();
			}
			catch(UnknownHostException e) {error = "No internet connection";}
			catch(IOException e) {error = e.toString();}
			//catch(InterruptedException e) {error = e.toString();}
			return null;
		}

	}*/
	
}