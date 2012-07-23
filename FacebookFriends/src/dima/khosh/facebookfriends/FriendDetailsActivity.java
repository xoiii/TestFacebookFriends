package dima.khosh.facebookfriends;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendDetailsActivity extends Activity {
	
    private TextView mProfileUrl;
    private TextView mBirthday;
    private TextView mWebsite;
    private TextView mAboutMe;    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.friend_detail);	
        
        mProfileUrl = (TextView) findViewById(R.id.tx_profile_url); 
        mBirthday = (TextView) findViewById(R.id.tx_birthday);
        mWebsite = (TextView) findViewById(R.id.tx_website);
        mAboutMe = (TextView) findViewById(R.id.tx_aboutme);        
        
        Bundle extras = getIntent().getExtras();
        
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(extras.getString("FRIEND_OBJ"));
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }    
       
        try {
			mProfileUrl.setText("Profile URL: " + jsonObject.getString("profile_url"));
			mBirthday.setText("Birthday: " + jsonObject.getString("birthday"));
			mWebsite.setText("Website: " + jsonObject.getString("website"));
			mAboutMe.setText("About: " + jsonObject.getString("about_me"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
