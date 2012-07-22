package dima.khosh.facebookfriends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendDetailsActivity extends Activity {
	
    private TextView mProfileUrl;
    private TextView mEmployer;    
    private TextView mPosition;
    private TextView mBirthday;
    private TextView mWebsite;
    private TextView mAboutMe;    
    
    private ImageView mBigImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.friend_detail);	
        
        mProfileUrl = (TextView) findViewById(R.id.tx_profile_url); 
        mEmployer = (TextView) findViewById(R.id.tx_employer);
        mPosition = (TextView) findViewById(R.id.tx_position);
        mBirthday = (TextView) findViewById(R.id.tx_birthday);
        mWebsite = (TextView) findViewById(R.id.tx_website);
        mAboutMe = (TextView) findViewById(R.id.tx_aboutme);        
        mBigImage = (ImageView) findViewById(R.id.big_image);
		
        Bundle extras = getIntent().getExtras();
        
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(extras.getString("FRIEND_OBJ"));
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }    
       
        try {
			mProfileUrl.setText(jsonObject.getString("profile_url"));
			mBirthday.setText(jsonObject.getString("birthday"));
			mWebsite.setText(jsonObject.getString("website"));
			mAboutMe.setText(jsonObject.getString("about_me"));
			JSONArray work = jsonObject.getJSONArray("work");
			JSONObject last_work = work.getJSONObject(0);
			mEmployer.setText(last_work.getString("employer"));
			mPosition.setText(last_work.getString("position"));			
			//mBigImage.setImageBitmap(bm)
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
