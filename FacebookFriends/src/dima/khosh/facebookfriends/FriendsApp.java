package dima.khosh.facebookfriends;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FriendsApp extends Application {
	public static final String APP_ID = "394180770642758";	
    public static Facebook mFacebook;
    public static AsyncFacebookRunner mAsyncRunner;
    public static JSONObject mFriendsList;
    public static FriendsGetProfilePics mGetPics;  
    private static final String TAG = "FriendsApp";    
       
    
    public static Bitmap getBitmap(Context context, String urlString) {
        Bitmap bm = null;
        try {
        	
        	
        	final File cacheFile = new File(Utils.createFilePath(Utils.getExternalCacheDir(context), urlString));
            
            if (cacheFile.exists()) {
            	bm = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
            	return bm;
            }
        	/*
            URL aURL = new URL(urlString);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            
            bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
            is.close();
            */
            
            HttpURLConnection urlConnection = null;
            BufferedOutputStream out = null;

            try {
                final URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                final InputStream in =
                        new BufferedInputStream(urlConnection.getInputStream(), Utils.IO_BUFFER_SIZE);
                out = new BufferedOutputStream(new FileOutputStream(cacheFile), Utils.IO_BUFFER_SIZE);

                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
               
                out.close();
                out = null;
                
                bm = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());

            } catch (final IOException e) {
                Log.e(TAG, "Error in downloadBitmap - " + e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error in downloadBitmap - " + e);
                    }
                }
            }            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return bm;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
    
        
}
