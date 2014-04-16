package com.expenseshare.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.expenseshare.fragments.LoginFragment;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;



public class LoginActivity extends FragmentActivity implements
ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {
 
	private LoginFragment loginFragment;
	Facebook fb;
	SharedPreferences sp;
	private String TAG = "LoginActivity";
	
	 /* Request code used to invoke sign in user interactions. */
	  private static final int RC_SIGN_IN = 0;

	  /* Client used to interact with Google APIs. */
	  private GoogleApiClient mGoogleApiClient;

	  /* A flag indicating that a PendingIntent is in progress and prevents
	   * us from starting further intents.
	   */
	  private boolean mIntentInProgress;

	  
	  /* Track whether the sign-in button has been clicked so that we know to resolve
		 * all issues preventing sign-in without waiting.
		 */
		private boolean mSignInClicked;

		/* Store the connection result from onConnectionFailed callbacks so that we can
		 * resolve them when the user clicks sign-in.
		 */
		private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			
		setContentView(R.layout.activity_login);
		/*mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API, null)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();*/

		 try {
		        PackageInfo info = getPackageManager().getPackageInfo(
		                "com.expenseshare.activities", 
		                PackageManager.GET_SIGNATURES);
		        for (Signature signature : info.signatures) {
		            MessageDigest md = MessageDigest.getInstance("SHA");
		            md.update(signature.toByteArray());
		            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		            }
		    } catch (NameNotFoundException e) {

		    } catch (NoSuchAlgorithmException e) {

		    }
		 
	       //new code 
		    if (savedInstanceState == null) {
		        // Add the fragment on initial activity setup
		    	loginFragment = new LoginFragment();
		        getSupportFragmentManager()
		        .beginTransaction()
		        .add(android.R.id.content, loginFragment)
		        .commit();
		    } else {
		        // Or set the fragment from restored state info
		    	loginFragment = (LoginFragment) getSupportFragmentManager()
		        .findFragmentById(android.R.id.content);
		    } // new code end
		//commented old working code
		    /*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new LoginFragment()).commit();
		}*/
		 
		/* if(savedInstanceState == null){
			 getSupportFragmentManager().beginTransaction().add(R.id.container, new UserSettingsFragment()).commit();
		 }*/
		
	}

	public void onClick(View view) {
		  /*if (view.getId() == R.id.sign_in_button
		    && !mGoogleApiClient.isConnecting()) {
		    mSignInClicked = true;
		    resolveSignInError();
		  }*/
		  if(view.getId() == R.id.authButton){
			  //Add logic to check session state.
			  //Get Access token 
			  // Go to next Activity
			  
			// start Facebook Login
			    Session.openActiveSession(this, true, new Session.StatusCallback() {

			      // callback when session changes state
			      @Override
			      public void call(Session session, SessionState state, Exception exception) {
			        if (session.isOpened()) {
			        	//To be used to logout
			                  /* try{
			                        session.close();
			                        session.closeAndClearTokenInformation();
			                        //state.isClosed();
			                    }catch (Exception e) {
			                        Log.e(TAG, "getUserIdMethod--->"+e);
			                    }*/

			        	
			          // make request to the /me API
			         /* Request.newMeRequest(session, new Request.GraphUserCallback() {

			            // callback after Graph API response with user object
			            @Override
			            public void onCompleted(GraphUser user, Response response) {
			              if (user != null) {
			                Toast.makeText(getApplicationContext(), user.getFirstName(), Toast.LENGTH_SHORT).show();
			              }
			            }
			          });*/
			        }
			      }
			    });
		  }
		  if (view.getId() == R.id.sign_out_button) {
			    if (mGoogleApiClient.isConnected()) {
			      Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			      mGoogleApiClient.disconnect();
			     // mGoogleApiClient.connect();
			    }
			  }

		}
	
	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() {
	  if (mConnectionResult.hasResolution()) {
	    try {
	      mIntentInProgress = true;
	     startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
	          RC_SIGN_IN, null, 0, 0, 0);
	    } catch (SendIntentException e) {
	      // The intent was canceled before it was sent.  Return to the default
	      // state and attempt to connect to get an updated ConnectionResult.
	      mIntentInProgress = false;
	      mGoogleApiClient.connect();
	    }
	  }
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.action_settings, menu);
		return true;
	}

/*	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}
*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
		    // Store the ConnectionResult so that we can use it later when the user clicks
		    // 'sign-in'.
		    mConnectionResult = result;

		    if (mSignInClicked) {
		      // The user has already clicked 'sign-in' so we attempt to resolve all
		      // errors until the user is signed in, or they cancel.
		      resolveSignInError();
		    }
		  }
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		 mSignInClicked = false;
		  Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }

}
