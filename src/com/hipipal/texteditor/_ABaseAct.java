package com.hipipal.texteditor;

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
import android.view.View;

import com.zuowuxuxi.base.MyApp;
import com.zuowuxuxi.base._WBase;
import com.zuowuxuxi.common.GDBase;
import com.zuowuxuxi.util.NAction;
import com.zuowuxuxi.util.NUtil;

public class _ABaseAct extends GDBase {
    protected static final int SCRIPT_EXEC_PY = 2235;  
    protected static final int SCRIPT_EXEC_CODE = 1235;  

	public void onNotify(View v) {
	}
	
	@SuppressLint("NewApi")
	protected void initWidgetTabItem(int flag) {
		String code = NAction.getCode(getApplicationContext());
		if (code.equals("qpyplus") || code.equals("qpy3") || code.startsWith("lua5")) {
			if (flag == 5) {

				addActionBarItem(getGDActionBar()
			        		.newActionBarItem(NormalActionBarItem.class)
			        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_local)), 20);

			    /*addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_collection_new_2)), 35);
*/
				
			    addActionBarItem(getGDActionBar()
			        		.newActionBarItem(NormalActionBarItem.class)
			        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_new_a)), 30);

			    addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_action_overflow)), 40);

			} else {
			    addActionBarItem(getGDActionBar()
		        		.newActionBarItem(NormalActionBarItem.class)
		        		.setDrawable(new ActionBarDrawable(this, R.drawable.ic_action_overflow)), 40);

			}
		    
		} else if (code.contains("texteditor")) {
			if (flag == 5) {
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
    
    @SuppressWarnings("deprecation")
	public void callLuaApi(String flag, String param, String luaCode) {
		String code = NAction.getCode(this);		
		// todo
		if (code.contains("lua") || (NUtil.checkAppInstalledByName(this, "com.quseit.lua5") || NUtil.checkAppInstalledByName(this, "com.quseit.lua5pro"))) {
			String scmd = "lua";
	    	if (Build.VERSION.SDK_INT >= 20) { 
	    		scmd = "lua-android5";
	
	    	} 
	
			String[] args = {scmd+" "+param+" && sh "+getFilesDir()+"/bin/end.sh && exit"};
			execInConsole(args);
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

		} else {
			return CONF.UPDATE_URL+this.getPackageName()+"/"+NUtil.getVersinoCode(this);

		}
	}
	
}