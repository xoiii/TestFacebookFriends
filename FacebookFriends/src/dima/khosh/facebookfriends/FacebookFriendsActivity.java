package dima.khosh.facebookfriends;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;

import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import dima.khosh.facebookfriends.SessionEvents.AuthListener;
import dima.khosh.facebookfriends.SessionEvents.LogoutListener;


public class FacebookFriendsActivity extends Activity{

    String[] permissions = { "friends_about_me", "friends_birthday",
            "friends_education_history", "friends_hometown", 
            "friends_interests", "friends_location",
            "friends_notes", "friends_photos",
            "friends_status", "friends_website",
            "friends_work_history" };        
    ProgressDialog dialog;    

    private LoginButton mLoginButton;
    private TextView mText;
    private Button mGetFriendsButton;    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		        	
		setContentView(R.layout.activity_main);
		
        mLoginButton = (LoginButton) findViewById(R.id.login);
        mText = (TextView) findViewById(R.id.txt);        
        mGetFriendsButton = (Button) findViewById(R.id.getfriendslist);        
        
   	 // Create the Facebook Object using the app id.
        FriendsApp.mFacebook = new Facebook(FriendsApp.APP_ID);
        // Instantiate the asyncrunner object for asynchronous api calls.
        FriendsApp.mAsyncRunner = new AsyncFacebookRunner(FriendsApp.mFacebook);	
        
        SessionStore.restore(FriendsApp.mFacebook, this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        mLoginButton.init(this, FriendsApp.mFacebook, permissions);
        
        mGetFriendsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog = ProgressDialog.show(FacebookFriendsActivity.this, "",
                        getString(R.string.please_wait), true, true);
                
                String query = "select name, first_name, last_name, uid, current_location, " +
                		"website, work, about_me, " +
                		"birthday, sex, profile_url, pic_square, pic_small " +
                		"from user where uid in " +
                		"(select uid2 from friend where uid1=me()) " +
                		"order by name";
                Bundle params = new Bundle();
                params.putString("method", "fql.query");
                params.putString("query", query);
                FriendsApp.mAsyncRunner.request(null, params,
                        new FriendsRequestListener());	
            }
        });
        mGetFriendsButton.setVisibility(FriendsApp.mFacebook.isSessionValid() ?
                View.VISIBLE :
                View.INVISIBLE);        
        			
	}
	
    @Override
    public void onResume() {
        super.onResume();
        if(FriendsApp.mFacebook != null) {
            if (!FriendsApp.mFacebook.isSessionValid()) {
                //mText.setText("You are logged out! ");
                //mUserPic.setImageBitmap(null);
            } else {
            	FriendsApp.mFacebook.extendAccessTokenIfNeeded(this, null);
            }
        }
    }	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		FriendsApp.mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    
    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
            mText.setText("You have logged in! ");
            mGetFriendsButton.setVisibility(View.VISIBLE);
        }

        public void onAuthFail(String error) {
            mText.setText("Login Failed: " + error);
        }
    }

    public class SampleLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
            mText.setText("Logging out...");
        }

        public void onLogoutFinish() {
            mText.setText("You have logged out! ");
            mGetFriendsButton.setVisibility(View.INVISIBLE);

        }
    }
    
    public class FriendsRequestListener extends BaseRequestListener {

        @Override
        public void onComplete(final String response, final Object state) {
        	
            dialog.dismiss();
            Intent myIntent = new Intent(getApplicationContext(), FriendsList.class);
            myIntent.putExtra("API_RESPONSE", response);
            startActivity(myIntent);            

        }

        public void onFacebookError(FacebookError error) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }	

}
