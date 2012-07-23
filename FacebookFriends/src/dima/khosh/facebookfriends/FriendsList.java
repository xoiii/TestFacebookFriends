package dima.khosh.facebookfriends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsList extends Activity implements OnItemClickListener {
    private Handler mHandler;

    protected ListView friendsList;
    protected static JSONArray jsonArray;

    /*
     * Layout the friends' list
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        setContentView(R.layout.friends_list);

        Bundle extras = getIntent().getExtras();
        String apiResponse = extras.getString("API_RESPONSE");

        try {
                jsonArray = new JSONArray(apiResponse);            
        } catch (JSONException e) {
            showToast("Error: " + e.getMessage());
            return;
        }
        friendsList = (ListView) findViewById(R.id.friends_list);
        friendsList.setOnItemClickListener(this);
        friendsList.setAdapter(new FriendListAdapter(this));

    }

    /*
     * Clicking on a friend show friend's details
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
        try {
            
            String friendJsonObj = jsonArray.getJSONObject(position).toString();
            
            Intent myIntent = new Intent(getApplicationContext(), FriendDetailsActivity.class);
            myIntent.putExtra("FRIEND_OBJ", friendJsonObj);
            startActivity(myIntent);
            
        } catch (JSONException e) {
            showToast("Error: " + e.getMessage());
        }
    }


    public void showToast(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(FriendsList.this, msg, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    /**
     * Definition of the list adapter
     */
    public class FriendListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        FriendsList friendsList;

        public FriendListAdapter(FriendsList friendsList) {
            this.friendsList = friendsList;
            if (FriendsApp.mGetPics == null) {
            	FriendsApp.mGetPics = new FriendsGetProfilePics(friendsList.getBaseContext());
            }
            FriendsApp.mGetPics.setListener(this);
            mInflater = LayoutInflater.from(friendsList.getBaseContext());
        }

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(position);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            View hView = convertView;
            if (convertView == null) {
                hView = mInflater.inflate(R.layout.friend_item, null);
                ViewHolder holder = new ViewHolder();
                holder.profile_pic = (ImageView) hView.findViewById(R.id.profile_pic);
                holder.name = (TextView) hView.findViewById(R.id.name);
                holder.info = (TextView) hView.findViewById(R.id.info);
                hView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) hView.getTag();
            try {
            	holder.profile_pic.setImageBitmap(FriendsApp.mGetPics.getImage(
            			jsonObject.getString("uid"), jsonObject.getString("pic_square")));
            } catch (JSONException e) {
                holder.name.setText("");
            }
            try {
                holder.name.setText(jsonObject.getString("name"));
            } catch (JSONException e) {
                holder.name.setText("");
            }
            try {
                    JSONObject location = jsonObject.getJSONObject("current_location");
                    holder.info.setText(location.getString("city") + ", "
                            + location.getString("state"));

            } catch (JSONException e) {
                holder.info.setText("");
            }
            return hView;
        }

    }

    class ViewHolder {
        ImageView profile_pic;
        TextView name;
        TextView info;
    }
}
