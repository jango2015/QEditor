package com.hipipal.texteditor;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import greendroid.widget.QuickAction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zuowuxuxi.base.MyApp;
import com.zuowuxuxi.base._WBase;
import com.zuowuxuxi.common.GDBase;
import com.zuowuxuxi.util.FileHelper;
import com.zuowuxuxi.util.NAction;
import com.zuowuxuxi.util.NStorage;
import com.zuowuxuxi.util.NUtil;

public class _ABaseAct extends GDBase {
    protected static final int SCRIPT_EXEC_PY = 2235;  
    protected static final int SCRIPT_EXEC_CODE = 1235;  
	private AdView adMob = null;

    @Override
    protected void onDestroy() {
        LinearLayout modBanner = (LinearLayout)findViewById(R.id.modbanner);

        if (adMob!=null) {
            adMob.destroy();
        }
        
        if (modBanner!=null) {
            modBanner.removeAllViews();
        }

    	super.onDestroy();
    	
    }
	public void initAD(String pageId) {
		super.initAD(pageId);
		
		//Log.d(TAG, "initAD:" + pageId);
        LinearLayout modBanner = (LinearLayout)findViewById(R.id.modbanner);
        //Log.d(TAG, "initAD:"+modBanner.getHeight()+"-"+modBanner.getWidth());

        if (NUtil.netCheckin(getApplicationContext()) && !NAction.checkIfPayIAP(getApplicationContext(), "ad")) {
        	//tapjoyInit();
        	//modBanner = (LinearLayout)findViewById(R.id.modbanner);
        	//LinearLayout.LayoutParams adViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        	//modBanner.setLayoutParams(adViewLayoutParams);
        	//if (NAction.getCode(getApplicationContext()).endsWith("texteditor")) {
	        	if (!NAction.getExtP(getApplicationContext(), "ad_with_"+pageId).equals("0")) {

	
	                adMob = (AdView) findViewById(R.id.adView);
	                adMob.setVisibility(View.VISIBLE);
	                if (NAction.getExtP(getApplicationContext(), "ad_conf_admob_np").equals("")) { 
	                    try {
	                    	LinearLayout.LayoutParams adViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	                    	adMob.setLayoutParams(adViewLayoutParams);
	                    } catch (Exception e) { 
	
	                    }    
	                }
	
	                modBanner.removeAllViews();
	                try {
	                    AdRequest adr = new AdRequest.Builder().build();
	                    adMob.loadAd(adr);
	
	                    findViewById(R.id.modbanner_wrap).setVisibility(View.VISIBLE);
	                } catch (NoSuchMethodError e){
	                	
	                }
	
	               // }
	
	        	//}
        	}
        }    

	}

	public void onNotify(View v) {
	}
	
	//@SuppressLint("NewApi")
	protected void initWidgetTabItem(int flag) {
		String code = NAction.getCode(getApplicationContext());
		if (code.equals("qpyplus") || code.equals("qpy3") || code.startsWith("lua5")) {
			if (flag == 5) {

				addActionBarItem(getGDActionBar()
			        		.newActionBarItem(NormalActionBarItem.class)
			        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_folder_open_white)), 20);

			    /*addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_collection_new_2)), 35);
*/
				
			    addActionBarItem(getGDActionBar()
			        		.newActionBarItem(NormalActionBarItem.class)
			        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_note_add_white)), 30);

			    addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_more_vert_white)), 40);

			} else {
			    addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_more_vert_white)), 40);

			}
		    
		} else if (code.contains("texteditor")) {
			if (flag == 5) {
				addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_save_white)), 25);
//			    addActionBarItem(getGDActionBar()
//		        		.newActionBarItem(NormalActionBarItem.class)
//		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_new_a)), 30);
			}
		    addActionBarItem(getGDActionBar()
	        		.newActionBarItem(NormalActionBarItem.class)
	        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_more_vert_white)), 40);
		} else {
			
			
		}
	}
 
	public void onSetting(View v) {
		Intent intent = new Intent();
		intent.setClassName(this.getPackageName(), this.getPackageName()+".MSettingAct");
		startActivity(intent);
	}

	
	@Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
    	switch (item.getItemId()) {
    	case 40:
    			onSetting(null);
    			break;

    			//mBar.show(item.getItemView());

    	}
    	return 	super.onHandleActionBarItemClick(item, position);
    }



    protected static class MyQuickAction extends QuickAction {
        protected static final ColorFilter BLACK_CF = new LightingColorFilter(Color.BLACK, Color.BLACK);
        protected static final ColorFilter WHITE_CF = new LightingColorFilter(Color.WHITE, Color.WHITE);

        public MyQuickAction(Context ctx, int drawableId, int titleId) {
            super(ctx, buildDrawable(ctx, drawableId), titleId);
        }
        
        protected static Drawable buildDrawable(Context ctx, int drawableId) {
            Drawable d = ctx.getResources().getDrawable(drawableId);
            d.setColorFilter(BLACK_CF);
            return d;
        }        
    }
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
    
    public String getName() {
        return "QPythonPlayer";
      }
    /**/
    private static final int PID_INIT_VALUE = -1;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    ArrayList<String> mArguments = new ArrayList<String>();
    InputStream mIn;
    OutputStream mOut;
    FileDescriptor mFd;
    
    
    
    private String[] getEnvironmentArray(String pyPath) {

        String path = System.getenv("PATH");

        File filesDir = getFilesDir();
        
  	    List<String> environmentVariables = new ArrayList<String>();

	    environmentVariables.add("PATH="+filesDir+"/bin"+":"+path);
	    environmentVariables.add("LD_LIBRARY_PATH="+ ".:"+filesDir+"/lib/"+":"+filesDir+"/:"+filesDir.getParentFile()+"/lib/");
	    environmentVariables.add("ANDROID_PRIVATE="+filesDir);
    	File externalStorage = new File(Environment.getExternalStorageDirectory(), "com.quseit.lua5");

        if (!externalStorage.exists()) {
        	externalStorage.mkdir();
        }

	    environmentVariables.add("TMPDIR="+externalStorage+"/cache");
	    environmentVariables.add("TEMP="+externalStorage+"/cache");

        File td = new File(externalStorage+"/cache");
        if (!td.exists()) {
        	td.mkdir();
        }
        
	    environmentVariables.add("AP_HOST="+NStorage.getSP(this, "sl4a.hostname"));
	    environmentVariables.add("AP_PORT="+NStorage.getSP(this, "sl4a.port"));
	    environmentVariables.add("AP_HANDSHAKE="+NStorage.getSP(this, "sl4a.secue"));

	    environmentVariables.add("ANDROID_PUBLIC="+externalStorage);
	    environmentVariables.add("ANDROID_PRIVATE="+this.getFilesDir().getAbsolutePath());
	    environmentVariables.add("ANDROID_ARGUMENT="+pyPath);

  	    for (Entry<String, String> entry : System.getenv().entrySet()) {
  	      environmentVariables.add(entry.getKey() + "=" + entry.getValue());
  	    }
  	    String[] environment = environmentVariables.toArray(new String[environmentVariables.size()]);
  	    return environment;
    }
    
    public String getWorkingDirectory() {
        return Environment.getExternalStorageDirectory()+"/com.quseit.lua5/";
      }    
    
    public void playQScript(final String binaryPath, final String script, String argv1, boolean notify) {
    	//Log.d(TAG, "playQScript:"+script);
    	File f = new File(script);
        
    	
        int[] pid = new int[1];

        mArguments.add(script);
		if (argv1!=null) {
			mArguments.add(argv1);
		}
        String[] argumentsArray = mArguments.toArray(new String[mArguments.size()]);

        final File mLog = new File(String.format("%s", Environment.getExternalStorageDirectory()+"/com.quseit.lua5/cache/run.log"));
        File logDir = mLog.getParentFile();
        if (!logDir.exists()) {
        	FileHelper.createDirIfNExists(logDir.getAbsolutePath());
        }
        //mLog = new File( Environment.getExternalStorageDirectory()+"/"+getName()+".log" );
        //Log.d("Process", "logFile:"+mLog.getAbsolutePath());

        mFd = com.googlecode.android_scripting.Exec.createSubprocess(binaryPath, argumentsArray, getEnvironmentArray(f.getParentFile()+""), getWorkingDirectory(), pid);
        //Log.d("QPY", "binaryPath:"+binaryPath+"-argumentsArray:"+argumentsArray+"-getEnvironmentArray:"+getEnvironmentArray()+"-getWorkingDirectory:"+getWorkingDirectory()+"-pid:"+pid);
        final AtomicInteger mPid = new AtomicInteger(PID_INIT_VALUE);

        mPid.set(pid[0]);
        mOut = new FileOutputStream(mFd);
        mIn = new StreamGobbler(new FileInputStream(mFd), mLog, DEFAULT_BUFFER_SIZE);
        long  mStartTime = System.currentTimeMillis();

 
        new Thread(new Runnable() {
          public void run() {
        	int returnValue = com.googlecode.android_scripting.Exec.waitFor(mPid.get());
            //long mEndTime = System.currentTimeMillis();
            int pid = mPid.getAndSet(PID_INIT_VALUE);
            Log.d("", "out:"+mFd.out.toString());
                        
            Message msg = new Message();
            msg.what = returnValue;
            msg.obj = mArguments.get(0);
            
            Log.d("QPY", "Process " + pid + " exited with result code " + returnValue + ".");

            try {
                mIn.close();
              } catch (IOException e) {
                Log.e("QPY", e.getMessage());
              }
            
            try {
                mOut.close();
              } catch (IOException e) {
                  Log.e("QPY", e.getMessage());
              }
            
            //context.updateNotify(msg);

          }
        }).start();
    }
    
    
    @SuppressWarnings("deprecation")
	public void callLuaApi(String flag, String script, String luaCode) {
		String code = NAction.getCode(this);		
		// todo
		if (code.contains("lua") || (NUtil.checkAppInstalledByName(this, "com.quseit.lua5") || NUtil.checkAppInstalledByName(this, "com.quseit.lua5pro"))) {
			
			String scmd = getApplicationContext().getFilesDir()+"/bin/lua";
	    	if (Build.VERSION.SDK_INT >= 20) { 
	    		scmd = getApplicationContext().getFilesDir()+"/bin/lua-android5";
	    	}
	    	
        	String content = FileHelper.getFileContents(script);
        	boolean isQapp = content.contains("-- qlua:qapp");
        	if (isQapp) {
        		playQScript(scmd, script, null, true);
        	} else {
				String[] args = {scmd+" "+script+" && sh "+getFilesDir()+"/bin/end.sh && exit"};
				execInConsole(args);
        	}
		} else {
    		WBase.setTxtDialogParam(0, R.string.pls_install_lua, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
    				String plgUrl = NAction.getExtP(getApplicationContext(), "ext_plugin_pkg4");
    				if (plgUrl.equals("")) {
    					plgUrl = CONF.EXT_PLG_URL4;
    				}
    				try {
						Intent intent = NAction.openRemoteLink(getApplicationContext(), plgUrl);
						startActivity(intent);
    				} catch (Exception e) {
    					plgUrl = CONF.EXT_PLG_URL4;
						Intent intent = NAction.openRemoteLink(getApplicationContext(), plgUrl);
						startActivity(intent);
    				}
				}
				}, null);
    		
    		showDialog(_WBase.DIALOG_EXIT+dialogIndex);
    		dialogIndex++;


		}
    }

    
    /**
     * call the Qpython API
     * @param flag
     * @param param
     * @param pyCode is the python code to run 
     */
    @SuppressWarnings("deprecation")
	public void callPyApi(String flag, String param, String pyCode) {
    	String proxyCode = "";
    	String extPlgPlusName = com.zuowuxuxi.config.CONF.EXT_PLG_PLUS;
    	String extPlg3Name = CONF.EXT_PLG_3;
		String extPlgName = NAction.getExtP(getApplicationContext(), "ext_plugin");
		if (extPlgName.equals("")) {
			extPlgName = com.zuowuxuxi.config.CONF.EXT_PLG;
		}
		
		String plgUrl = NAction.getExtP(getApplicationContext(), "ext_plugin_pkg");
		if (plgUrl.equals("")) {
			plgUrl = com.zuowuxuxi.config.CONF.EXT_PLG_URL;
		}
		String localQPylib = "com.hipipal.qpylib";
		try {
			String localPlugin = this.getPackageName();
			Intent intent = new Intent();
			intent.setClassName(localPlugin, localQPylib+".MPyApi");
			intent.setAction(localQPylib+".action.MPyApi");
			
			Bundle mBundle = new Bundle(); 
			mBundle.putString("root", MyApp.getInstance().getRoot());
	
			mBundle.putString("app", NAction.getCode(getApplicationContext()));
			mBundle.putString("act", "onPyApi");
			mBundle.putString("flag", flag);
			mBundle.putString("param", param);
			mBundle.putString("pycode", proxyCode+pyCode);
	
			intent.putExtras(mBundle);
			
			startActivityForResult(intent, SCRIPT_EXEC_PY);
		} catch (Exception e) {
			
			// qpython 3
			if (pyCode.contains("#qpy3\n")) {
				if (NUtil.checkAppInstalledByName(getApplicationContext(), extPlg3Name)) {
					Intent intent = new Intent();
					intent.setClassName(extPlg3Name, extPlg3Name+".MPyApi");
					intent.setAction(extPlg3Name+".action.MPyApi");
					
					Bundle mBundle = new Bundle(); 
					mBundle.putString("app", NAction.getCode(getApplicationContext()));
					mBundle.putString("act", "onPyApi");
					mBundle.putString("flag", flag);
					mBundle.putString("param", param);
					mBundle.putString("pycode", proxyCode+pyCode);
		
					intent.putExtras(mBundle);
					
					startActivityForResult(intent, SCRIPT_EXEC_PY);
		
				} else {
					
		    		WBase.setTxtDialogParam(0, R.string.pls_install_qpy, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
		    				String plgUrl = NAction.getExtP(getApplicationContext(), "ext_plugin_pkg3");
		    				if (plgUrl.equals("")) {
		    					plgUrl = CONF.EXT_PLG_URL3;
		    				}
		    				try {
								Intent intent = NAction.openRemoteLink(getApplicationContext(), plgUrl);
								startActivity(intent);
		    				} catch (Exception e) {
		    					plgUrl = CONF.EXT_PLG_URL3;
								Intent intent = NAction.openRemoteLink(getApplicationContext(), plgUrl);
								startActivity(intent);
		    				}
						}
					}, null);
		    		showDialog(_WBase.DIALOG_EXIT+dialogIndex);
		    		dialogIndex++;
		    		
				}
				
			} else { //
				
				if (NUtil.checkAppInstalledByName(getApplicationContext(), extPlgPlusName)) {
					Intent intent = new Intent();
					intent.setClassName(extPlgPlusName, extPlgPlusName+".MPyApi");
					intent.setAction(extPlgPlusName+".action.MPyApi");
					
					Bundle mBundle = new Bundle(); 
					mBundle.putString("app", NAction.getCode(getApplicationContext()));
					mBundle.putString("act", "onPyApi");
					mBundle.putString("flag", flag);
					mBundle.putString("param", param);
					mBundle.putString("pycode", proxyCode+pyCode);
		
					intent.putExtras(mBundle);
					
					startActivityForResult(intent, SCRIPT_EXEC_PY);
		
				} else if (NUtil.checkAppInstalledByName(getApplicationContext(), extPlgName)) {
					
					Intent intent = new Intent();
					intent.setClassName(extPlgName, extPlgName+".MPyApi");
					intent.setAction(extPlgName+".action.MPyApi");
					
					Bundle mBundle = new Bundle(); 
					mBundle.putString("app", NAction.getCode(getApplicationContext()));
					mBundle.putString("act", "onPyApi");
					mBundle.putString("flag", flag);
					mBundle.putString("param", param);
					mBundle.putString("pycode", proxyCode+pyCode);
		
					intent.putExtras(mBundle);
					
					startActivityForResult(intent, SCRIPT_EXEC_PY);
					
				}  else {
								
		    		WBase.setTxtDialogParam(0, R.string.pls_install_qpy, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
		
		    				String plgUrl = NAction.getExtP(getApplicationContext(), "ext_plugin_pkg");
		    				if (plgUrl.equals("")) {
		    					plgUrl = com.zuowuxuxi.config.CONF.EXT_PLG_URL;
		    				}
		    				try {
								Intent intent = NAction.openRemoteLink(getApplicationContext(), plgUrl);
								startActivity(intent);
		    				} catch (Exception e) {
		    					plgUrl = com.zuowuxuxi.config.CONF.EXT_PLG_URL2;
								Intent intent = NAction.openRemoteLink(getApplicationContext(), plgUrl);
								startActivity(intent);
		    				}
						}
					}, null);
		    		showDialog(_WBase.DIALOG_EXIT+dialogIndex);
		    		dialogIndex++;
				}
			}

		}
    }

    public void onAbout(View v) {
		Intent intent2 = new Intent(getApplicationContext(), OAboutAct.class);
		startActivity(intent2);
    	//overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
    
    public void onGSetting(View v) {
    	Intent intent = new Intent(this, MSettingAct.class);
    	startActivity(intent);
    }

	@Override
	public Class<?> getUpdateSrv() {
		return null;
	}
	
    @SuppressWarnings("deprecation")
	@SuppressLint("DefaultLocale")
	public void playFromRemote(String link) {
    	String code = NAction.getCode(getApplicationContext());
        if (code.startsWith("mn")) {
        	
			String a8Name = NAction.getExtP(getApplicationContext(), "extend_a8_pkg");
			if (a8Name.equals("")) {
				a8Name = CONF.A8_PLAY;
			}
			
			String pluginName1 = NAction.getExtP(getApplicationContext(), "extend_plugin_pkg1");
			if (pluginName1.equals("")) {
				pluginName1 = CONF.PLAY_PLUGIN_1;
			}
			
			String pluginName2 = NAction.getExtP(getApplicationContext(), "extend_plugin_pkg2");
			if (pluginName2.equals("")) {
				pluginName2 = CONF.PLAY_PLUGIN_2;
			}
			if (NUtil.checkAppInstalledByName(getApplicationContext(), pluginName2)) {
				
	    		Intent intent = new Intent();
	    		intent.setClassName(pluginName2, "com.hipipal.p.PLAPlayerAct");
	    		intent.setAction("android.intent.action.VIEW");
	    		Uri uri = Uri.parse(link);
	    		intent.setDataAndType(uri , "video/*");
	    		startActivity(intent);
	    		
			} else if (NUtil.checkAppInstalledByName(getApplicationContext(), pluginName1)) {
				
	    		Intent intent = new Intent();
	    		intent.setClassName(pluginName1, "com.hipipal.p.PLAPlayerAct");
	    		intent.setAction("android.intent.action.VIEW");
	    		Uri uri = Uri.parse(link);
	    		intent.setDataAndType(uri , "video/*");
	    		startActivity(intent);
			} else {
				
				boolean useDefault = false;
				int indexOfDot = link.lastIndexOf('.');
				if (indexOfDot != -1) {
					String extension = link.substring(indexOfDot).toLowerCase();
					if (extension.compareTo(".mp4") == 0) {
						useDefault = true;
					}
				}
			
				if (!useDefault) {
			    	String a8VName = this.getPackageName();
			    	
					Intent intent = new Intent();
					intent.setClassName(a8VName, "com.hipipal.p.FFMpegPlayer");
					intent.setAction("android.intent.action.VIEW");
					Uri uri = Uri.parse(link);
					intent.setDataAndType(uri , "video/*");
					startActivity(intent);
				} else {
			    	String a8VName = this.getPackageName();
			    	
					Intent intent = new Intent();
					intent.setClassName(a8VName, "com.hipipal.m.PLAPlayerAct");
					intent.setAction("android.intent.action.VIEW");
					Uri uri = Uri.parse(link);
					intent.setDataAndType(uri , "video/*");
					startActivity(intent);
				}
			}
			
        } else {	// 不是MVP        	
			String pkgName = NAction.getExtP(getApplicationContext(), "extend_a8_pkg");
			if (pkgName.equals("")) {
				pkgName = CONF.A8_PLAY;
			}
			if (NUtil.checkAppInstalledByName(getApplicationContext(), pkgName)) {
	        	String a8Name = CONF.A8_PLAY;
	        	
	    		Intent intent = new Intent();
	    		intent.setClassName(a8Name, "com.hipipal.mna8.PLAPlayerAct");
	    		intent.setAction("android.intent.action.VIEW");
	    		
	    		Uri uri = Uri.parse(link);
	    		intent.setDataAndType(uri , "video/*");
	    		startActivity(intent);
				
			} else {
				// 检查MVP是否在
				String mpvName = CONF.MPV_PLAY;
				if (NUtil.checkAppInstalledByName(getApplicationContext(), mpvName)) {
		    		Intent intent = new Intent();
		    		intent.setClassName(mpvName, "com.hipipal.m.PLAPlayerAct");
		    		intent.setAction("android.intent.action.VIEW");
		    		
		    		Uri uri = Uri.parse(link);
		    		intent.setDataAndType(uri , "video/*");
		    		startActivity(intent);
				} else {
				
		    		WBase.setTxtDialogParam(0, R.string.pls_install_a8_play, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String pkgUrl = NAction.getExtP(getApplicationContext(), "extend_a8_play_url");
							if (pkgUrl.equals("")) {
								pkgUrl = CONF.A8_PLAY_URL;
							}
							
							//if (pkgUrl.startsWith("http:")) {
							try {
								Intent intent = NAction.openRemoteLink(getApplicationContext(), pkgUrl);
								startActivity(intent);
							} catch (Exception e) {
								pkgUrl = CONF.A8_PLAY_URL2;
								Intent intent = NAction.openRemoteLink(getApplicationContext(), pkgUrl);
								startActivity(intent);
							}
						}
					}, null);
		    		showDialog(_WBase.DIALOG_EXIT+dialogIndex);
		    		dialogIndex++;
				}
			}        	
        }
    }
    
    private static final int SCRIPT_CONSOLE_CODE = 1237;

    public void execInConsole(String[] args) {
    	Intent intent = new Intent();
		intent.setClassName(this.getPackageName(), "jackpal.androidterm.Term");
		intent.putExtra(CONF.EXTRA_CONTENT_URL0, "main");
		intent.putExtra("ARGS", args);
		startActivityForResult(intent,SCRIPT_CONSOLE_CODE);

    }

	@Override
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
	
	
}