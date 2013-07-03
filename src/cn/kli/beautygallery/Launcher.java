package cn.kli.beautygallery;

import java.io.IOException;
import java.io.InputStream;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class Launcher extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.enter).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Launcher.this,SlideShowActivity.class));
				finish();
			}
        	
        });
        initFrontCover();
    }
    
    private void initFrontCover(){
    	//background
    	ImageView iv = (ImageView)findViewById(R.id.iv_front_cover);
    	Bitmap bm = getImageFromAssetsFile("front_cover.jpg");
    	iv.setImageBitmap(bm);
    	
    	//title
    	TextView tv = (TextView)findViewById(R.id.title);
    	
    	//description
        WebView wv = (WebView)findViewById(R.id.description);
        wv.setBackgroundColor(0);
        wv.loadUrl("file:///android_asset/html/description.html");
    }
    
    private Bitmap getImageFromAssetsFile(String fileName)  
    {  
        Bitmap image = null;  
        AssetManager am = getResources().getAssets();  
        try  
        {  
            InputStream is = am.open(fileName);  
            image = BitmapFactory.decodeStream(is);  
            is.close();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    
        return image;  
    
    }

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}  
    
    
}