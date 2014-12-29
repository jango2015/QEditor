package com.hipipal.texteditor;

import jackpal.androidterm.AssetExtract;
import jackpal.androidterm.ResourceManager;
import jackpal.androidterm.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import greendroid.graphics.drawable.ActionBarDrawable;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zuowuxuxi.base.MyApp;
import com.zuowuxuxi.base._WBase;
import com.zuowuxuxi.common.GDBase;
import com.zuowuxuxi.util.NAction;
import com.zuowuxuxi.util.NUtil;

public class _ABaseAct extends GDBase {
    protected static final int SCRIPT_EXEC_PY = 2235;  
    protected static final int SCRIPT_EXEC_CODE = 1235;  

	@SuppressLint("NewApi")
	protected void initWidgetTabItem(int flag) {
		/*addActionBarItem(getGDActionBar()
        		.newActionBarItem(NormalActionBarItem.class)
        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_save_as)), 10);*/
		
		String code = NAction.getCode(getApplicationContext());
		if (code.equals("qpyplus") || code.equals("qpy3")) {
			addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_local)), 20);

			
		    addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_new_a)), 30);
		    
		} else if (code.equals("texteditor")) {
			if (flag == 0) {
				addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_local)), 20);
			    addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_new_a)), 30);
			}
		    addActionBarItem(getGDActionBar()
	        		.newActionBarItem(NormalActionBarItem.class)
	        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_action_overflow)), 40);
		} else {
			addActionBarItem(getGDActionBar()
	        		.newActionBarItem(NormalActionBarItem.class)
	        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_save_as)), 10);
		}
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
    
    /**
     * call the Qpython API
     * @param flag
     * @param param
     * @param pyCode is the python code to run 
     */
    @SuppressWarnings("deprecation")
	public void callPyApi(String flag, String param, String pyCode) {
    	//Log.d(TAG, "callPyApi:"+pyCode);
    	//String appCode = NAction.getCode(getApplicationContext());
    	//String proxyHost = NAction.getProxyHost(getApplicationContext());
    	//String proxyPort = NAction.getProxyPort(getApplicationContext());
    	String proxyCode = "";
    	/*if (!proxyHost.equals("")) {
    		if (proxyPort.equals("")) {
    			proxyPort = "80";
    		}
    		proxyCode = "PROXY = {'http':'"+proxyHost+":"+proxyPort+"'}\n";
    	} else {
    		proxyCode = "PROXY = {}\n";
    	}*/
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
		// call local api
		//if (!appCode.startsWith("xx")) {

		//} else {
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
					
		    		WBase.setTxtDialogParam(0, R.string.pls_install_ext_plg, new DialogInterface.OnClickListener() {
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
								
		    		WBase.setTxtDialogParam(0, R.string.pls_install_ext_plg, new DialogInterface.OnClickListener() {
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
		// TODO Auto-generated method stub
		return null;
	}
	
    @SuppressWarnings("deprecation")
	@SuppressLint("DefaultLocale")
	public void playFromRemote(String link) {
    	//String process = NUtil.getCpuProcessFromByInfo();
    	//String features = NUtil.getCpuFeaturesFromByInfo();
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
			// 如果cpu为armv7 并且 系统已经安装了A8 Player, 则使用A8Player
			/*if (NUtil.checkAppInstalledByName(getApplicationContext(), a8Name)) {
	        	//Toast.makeText(getApplicationContext(), R.string.using_optimized_play, Toast.LENGTH_SHORT).show();
	        	
	    		Intent intent = new Intent();
	    		intent.setClassName(a8Name, "com.hipipal.mna8.PLAPlayerAct");
	    		intent.setAction("android.intent.action.VIEW");
	    		Uri uri = Uri.parse(link);
	    		intent.setDataAndType(uri , "video/*");
	    		startActivity(intent);
	    		
			} else {*/
				
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
				/*
		    	Intent intent = new Intent(this, PLAPlayerAct.class);
				ArrayList<Uri> playlist = new ArrayList<Uri>();
				Uri selectedUri = null;
				Uri tempUri = Uri.parse(link);
				playlist.add(tempUri);
				selectedUri = tempUri;
				intent.putExtra("selected", 0);
				intent.putExtra("playlist", playlist);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(selectedUri);
				this.startActivity(intent);  */
			}
			
        } else {	// 不是MVP
        	//
        	
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
							/*} else {
								Intent intent = NAction.openMarketLink(pkgUrl);
								startActivity(intent);
	
							}*/
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

    public void unpackData(final String resource, File target) {
    	ResourceManager resourceManager = new ResourceManager(this);

        // The version of data in memory and on disk.
        String data_version = resourceManager.getString(resource + "_version");
        String disk_version = "0";
        boolean isPrivate =  resource.startsWith("private");


        //Log.d(TAG, "data_version:"+data_version+"-"+resource + "_version"+"-"+resourceManager);
        // If no version, no unpacking is necessary.
        if (data_version == null) {
            return;
        }

        // Check the current disk version, if any.
        String filesDir = target.getAbsolutePath();
        String disk_version_fn = filesDir + "/" + resource + ".version";

        try {
            byte buf[] = new byte[64];
            InputStream is = new FileInputStream(disk_version_fn);
            int len = is.read(buf);
            disk_version = new String(buf, 0, len);
            is.close();
        } catch (Exception e) {
            disk_version = "0";
        }

        // If the disk data is out of date, extract it and write the
        // version file.
        /*boolean extract = false;
        if (disk_version.equals("")) {
        	extract = true;
        } else {
        	Float data_v = Float.parseFloat(data_version);
        	Float disk_v = Float.parseFloat(disk_version);
        	if (data_v.intValue()>disk_v.intValue()) {
        		extract = true;
        	}
        }*/
        
        //Log.d(TAG, "data_version:"+Math.round(Double.parseDouble(data_version))+"-disk_version:"+Math.round(Double.parseDouble(disk_version))+"-RET:"+(int)(Double.parseDouble(data_version)-Double.parseDouble(disk_version)));
        if ((int)(Double.parseDouble(data_version)-Double.parseDouble(disk_version))>0 || disk_version.equals("0")) {
            Log.v(TAG, "Extracting " + resource + " assets.");
            //recursiveDelete(target);

            target.mkdirs();

            AssetExtract ae = new AssetExtract(this);
            if (!ae.extractTar(resource + ".mp3", target.getAbsolutePath())) {
                Toast.makeText(this,"Could not extract " + resource + " data.", Toast.LENGTH_SHORT).show();
            }

            try {
            	/*if (resource.equals("private")) {
            		Toast.makeText(getApplicationContext(), R.string.first_load, Toast.LENGTH_SHORT).show();
            	}*/
                // Write .nomedia.
                new File(target, ".nomedia").createNewFile();

                // Write version file.
                FileOutputStream os = new FileOutputStream(disk_version_fn);
                os.write(data_version.getBytes());
                os.close();
            } catch (Exception e) {
                Log.w("python", e);
                Toast.makeText(this, "Could not extract " + resource + " data, make sure your device have enough space.", Toast.LENGTH_LONG).show();
            }
        } else {
        	Log.d(TAG, "NO EXTRACT");

        }
        if (isPrivate) {
			File bind = new File(getFilesDir()+"/bin");
			for (File bin : bind.listFiles()) {
				try {
		  			Log.d(TAG, "chmod:"+bin.getAbsolutePath());
		          
					FileUtils.chmod(bin, 0755);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
        }

    }



	@Override
	public String confGetUpdateURL(int flag) {
		if (flag == 2) {
			return CONF.LOG_URL+this.getPackageName()+"/"+NUtil.getVersinoCode(this);

		} else {
			return CONF.UPDATE_URL+this.getPackageName()+"/"+NUtil.getVersinoCode(this);

		}
	}
	
}