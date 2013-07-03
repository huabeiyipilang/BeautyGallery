package cn.kli.beautygallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.kli.utils.klilog;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.baidu.mobstat.StatService;

public class SlideShowActivity extends Activity {
	
	private final static String PIC_SAVE_PATH = "BeautyGallery";
	
	private final static int CMD_INIT = 1;
	
	private View mAdsContainer;
	private GalleryView mGallery;
	private GalleryAdapter mAdapter;
	private AdView mAdView;
	private boolean mShowAd = true;
	private boolean mAdFirstLoad = true;
	private ProgressDialog mLoadingDialog;
	
	private Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slide_show);
	}
	
	private void init(){

		mLoadingDialog = new ProgressDialog(SlideShowActivity.this);
		mLoadingDialog.setTitle(R.string.loading);
		mLoadingDialog.setMessage(SlideShowActivity.this.getString(R.string.loading));
		mLoadingDialog.show();
		
		mAdsContainer = findViewById(R.id.adview_container);
		
		mAdapter = new GalleryAdapter(this);
		
		mGallery = (GalleryView)findViewById(R.id.gallery1);
		mGallery.setAdapter(mAdapter);
		mGallery.setSpacing(40);
		
		findViewById(R.id.button1).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				saveAssetPic2Sd(mAdapter.getCurrentPicName());
			}
			
		});
		
		mAdView = (AdView)findViewById(R.id.adView);
		mAdView.setListener(new AdViewListener(){

			@Override
			public void onAdClick(JSONObject arg0) {
				mShowAd = false;
				mAdsContainer.setVisibility(View.GONE);
			}

			@Override
			public void onAdFailed(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAdReady(AdView arg0) {
				if(mAdFirstLoad){
					klilog.i("onAdReady  mAdFirstLoad = true;");
					mAdFirstLoad = false;
					mAdsContainer.setVisibility(View.VISIBLE);
				}
				
			}

			@Override
			public void onAdShow(JSONObject arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAdSwitch() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onVideoFinish() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onVideoStart() {
				// TODO Auto-generated method stub
				
			}
			
		});
		mLoadingDialog.cancel();
		mLoadingDialog.dismiss();
	}
	
	
	//save pic from asset to sdcard
	private boolean saveAssetPic2Sd(String picName){
		boolean result = false;
		AssetManager am = getAssets();
		FileOutputStream fos = null;
		InputStream is = null;
		String outputFileName = randomFileName(picName);
		//is sdcard enable
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File appDir = Environment.getExternalStoragePublicDirectory(PIC_SAVE_PATH);
			File file = new File(appDir, outputFileName);
			try {
				//Make sure app directory exists
				appDir.mkdirs();
				
				//read buffer from source pic
				is = am.open(picName);
				byte[] buffer = new byte[is.available()];
				is.read(buffer);
				
				//write buffer to sdcard
				fos = new FileOutputStream(file);
				fos.write(buffer);
				
				result = true;
				Toast.makeText(this, getResources().getString(R.string.save_to_sd,
						file.toString()), Toast.LENGTH_SHORT).show();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(is != null){
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			if(!result){
				Toast.makeText(this, R.string.save_error, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, R.string.no_sdcard, Toast.LENGTH_SHORT).show();
		}
		return result;
	}
	
	private String randomFileName(String origin){
		int lastPoint = origin.lastIndexOf(".");
		String newName = origin.substring(lastPoint);
		return System.currentTimeMillis() + newName;
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!mAdFirstLoad){
			mAdsContainer.setVisibility(mShowAd ? View.VISIBLE : View.GONE);
		}else{
			init();
		}
		StatService.onResume(this);
	}  
}
