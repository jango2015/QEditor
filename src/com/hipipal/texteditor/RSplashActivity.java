package com.hipipal.texteditor;

import io.presage.Presage;
import io.presage.utils.IADHandler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.zuowuxuxi.util.NAction;
import com.zuowuxuxi.util.NStorage;
import com.zuowuxuxi.util.NUtil;
import com.zuowuxuxi.view.AdSlidShowView;
import com.zuowuxuxi.view.AdSlidShowView.urlBackcall;

/**
 *	实时开屏，广告实时请求并且立即展现
 */
public class RSplashActivity extends Activity {
	
	private static final String TAG ="splash";
		
	public static final int SleepFinish = 1;
	
	public Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
        	if (msg.what == SleepFinish) {
    			jump();
        	}
            super.handleMessage(msg);   
       }   
  };  
  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qedit_splash);
		//Log.i("RSplashActivity", "onCreate");
		if (!NAction.checkIfPayIAP(getApplicationContext(), "ad") && NAction.getExtP(this, "ad.ogury").equals("1")) {
			Log.d(TAG, "load ogury");

			Presage.getInstance().setContext(this.getBaseContext());
			Presage.getInstance().start();

			reADListener();
		} else if (!NAction.checkIfPayIAP(getApplicationContext(), "ad") && NAction.getExtP(getApplicationContext(), "adx_" + TAG)
					.equals("1")) {
			showRecommandAd();
			new SleepThread().start();

		} else {
			//Log.d("RSplashActivity", "jump");
	        Message msg = new Message(); 
	        msg.what = SleepFinish;
	        RSplashActivity.this.myHandler.sendMessage(msg);

		}
	}
	
	
	/**
	 * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加waitingOnRestart判断。
	 * 另外，点击开屏还需要在onRestart中调用jumpWhenCanClick接口。
	 */
	public boolean waitingOnRestart=false;
	private void jumpWhenCanClick() {
		//Log.d("test", "this.hasWindowFocus():"+this.hasWindowFocus());
		if(this.hasWindowFocus()||waitingOnRestart){
			this.startActivity(new Intent(RSplashActivity.this, TedActivity.class));
			this.finish();
		}else{
			waitingOnRestart=true;
		}
		
	}
	
	/**
	 * 不可点击的开屏，使用该jump方法，而不是用jumpWhenCanClick
	 */
	private void jump() {
		this.startActivity(new Intent(RSplashActivity.this, TedActivity.class));
		this.finish();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(waitingOnRestart){
			jumpWhenCanClick();
		}
	}

	
	
    public void showRecommandAd() {
		if (NAction.getExtP(getApplicationContext(), "adx_" + TAG)
				.equals("1")) {

		String ad = NAction.getExtAdConf(getApplicationContext());

		final List<String> ltImgLink=new ArrayList<String>();
		List<String> ltResImg=new ArrayList<String>();
			try {
				JSONObject jsonObj = new JSONObject(ad);
				
				JSONArray arrAd = jsonObj.getJSONArray("full");
				for(int i=0;i<arrAd.length();i++){
					JSONObject json=arrAd.getJSONObject(i);
					String ad_code=json.getString("ad_code");
					if(!NUtil.checkAppInstalledByName(getApplicationContext(), ad_code)){
						//Log.d(TAG, "ad_code:"+ad_code);

						ltResImg.add(json.getString("ad_img"));
						String link = confGetUpdateURL(3)+"&linkid="+json.getString("adLink_id");
						ltImgLink.add(link);
					} else {
						//Log.d(TAG, "!ad_code:"+ad_code);

					}
					
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		

			if (ltResImg.size()>0) {
			    AdSlidShowView adSlid = (AdSlidShowView)findViewById(R.id.adSlid2);
				adSlid.setImagesFromUrl(ltResImg);
				adSlid.setOnUrlBackCall(new urlBackcall() {
					@Override
					public void onUrlBackCall(int i) {
						Intent intent = NAction.openRemoteLink(
								getApplicationContext(), ltImgLink.get(i));
						startActivity(intent);
					}
				});
				adSlid.setVisibility(View.VISIBLE);
			}
			//findViewById(R.id.adLine).setVisibility(View.VISIBLE);
			//findViewById(R.id.adTitle).setVisibility(View.VISIBLE);
		}
    }

	
	private class SleepThread extends Thread implements Runnable {

		@Override
		public void run() {
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        Message msg = new Message(); 
	        msg.what = SleepFinish;
	        RSplashActivity.this.myHandler.sendMessage(msg);
		}
	}
	

	public String confGetUpdateURL(int flag) {
		if (flag == 2) {
			return CONF.LOG_URL+this.getPackageName()+"/"+NUtil.getVersinoCode(this);
		} else if (flag == 3) {
			return CONF.AD_URL+this.getPackageName()+"/"+NUtil.getVersinoCode(this)+"?"
					+ NAction.getUserUrl(getApplicationContext());

		} else {
			return CONF.UPDATE_URL+this.getPackageName()+"/"+NUtil.getVersinoCode(this);

		}
	}
	
	private void reADListener() {
		Presage.getInstance().adToServe("interstitial", new IADHandler() {

			@Override
			public void onAdClosed() {
				Log.i("PRESAGE", "ad closed");
				finish();
			}

			@Override
			public void onAdFound() {
				Log.i("PRESAGE", "ad found");
			}

			@Override
			public void onAdNotFound() {

				Log.i("PRESAGE", "ad not found");

				if (!NAction.checkIfPayIAP(getApplicationContext(), "ad") && NAction.getExtP(getApplicationContext(), "adx_" + TAG)
						.equals("1")) {
					showRecommandAd();
					new SleepThread().start();

				} else {
					//Log.d("RSplashActivity", "jump");
			        Message msg = new Message(); 
			        msg.what = SleepFinish;
			        RSplashActivity.this.myHandler.sendMessage(msg);
	
				}
			
			}

		});
	}	
}
