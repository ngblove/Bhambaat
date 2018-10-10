package com.example.niranjanpc.batterysaver;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    float BackLightValue = 0.001f; //dummy default value
    boolean state;
    Context context = this;
    TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = (TextView) findViewById(R.id.textView);
        Button UpdateSystemSetting = (Button) findViewById(R.id.updatesystemsetting);
        UpdateSystemSetting.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(context,"Ultra Mode is Enable", Toast.LENGTH_LONG).show();
                //claer application data
                clearApplicationData();
                // kill background processes
                int SysBackLightValue = (int) (BackLightValue * 255);
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        SysBackLightValue);
                ActivityManager amgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> list = amgr.getRunningAppProcesses();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
                        String[] pkgList = apinfo.pkgList;
                        if ((!apinfo.processName.startsWith("com.sec")) && ((apinfo.importance > 150) || (apinfo.processName.contains("google")))) {
                            for (int j = 0; j < pkgList.length; j++) {
                                amgr.killBackgroundProcesses(pkgList[j]);
                            }
                        }
                    }
                }

            }
        });
        Button disablenetworkservices = (Button) findViewById(R.id.button);
        disablenetworkservices.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Disable Network Services feaure is Enable", Toast.LENGTH_LONG).show();
                gps();
                //wifi disable
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);
                //Disable bluetooth
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                }
                //data connection
                mobilecheack();
                if (state) {
                    Intent intent = new Intent();
                    intent.setClassName("com.android.settings",
                            "com.android.settings.Settings$DataUsageSummaryActivity");
                    startActivityForResult(intent, 1);
                }
            }
        });
        //power saving mode
        Button PSM = (Button) findViewById(R.id.button2);
        PSM.setOnClickListener(new Button.OnClickListener() {
            private int mProgressStatus = 0;
            private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
            /*
                BatteryManager
                    The BatteryManager class contains strings and constants used for values in the
                    ACTION_BATTERY_CHANGED Intent, and provides a method for querying battery
                    and charging properties.
            */
            /*
                public static final String EXTRA_SCALE
                    Extra for ACTION_BATTERY_CHANGED: integer containing the maximum battery level.
                    Constant Value: "scale"
            */
                    // Get the battery scale
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    // Display the battery scale in TextView


            /*
                public static final String EXTRA_LEVEL
                    Extra for ACTION_BATTERY_CHANGED: integer field containing the current battery
                    level, from 0 to EXTRA_SCALE.

                    Constant Value: "level"
            */
                    // get the battery level
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    // Display the battery level in TextView


                    // Calculate the battery charged percentage
                    float percentage = level / (float) scale;
                    // Update the progress bar to display current battery charged percentage
                    mProgressStatus = (int) ((percentage) * 100);

                    // Show the battery charged percentage text inside progress bar
                    // Show the battery charged percentage in TextView


                    Toast.makeText(context, "\nPercentage : " + mProgressStatus + "%", Toast.LENGTH_LONG).show();
                }

            };

            //////
            @Override
            public void onClick(View v) {
                gps();
                //I think there is often a reason to clear the memory on Android. When you go on-line, Android (actually Google) preloads a bunch of apps as "cached". When you go off-line, it clears those and loads a bunch more. And then there are things like Google+ that just sits there hogging 50Mb of your poor phone's RAM. So if you need to remove all that unused metadata-gathering nonsense so that your app has some space, you could do something like this:
                ActivityManager amgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> list = amgr.getRunningAppProcesses();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
                        String[] pkgList = apinfo.pkgList;
                        if ((!apinfo.processName.startsWith("com.sec")) && ((apinfo.importance > 150) || (apinfo.processName.contains("google")))) {
                            for (int j = 0; j < pkgList.length; j++) {
                                amgr.killBackgroundProcesses(pkgList[j]);
                            }
                        }
                    }
                }
                //wifi disable
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);
                //Disable bluetooth
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                }
                //data connection
                mobilecheack();
                if (state) {
                    Intent intent = new Intent();
                    intent.setClassName("com.android.settings",
                            "com.android.settings.Settings$DataUsageSummaryActivity");
                    startActivityForResult(intent, 1);
                }
                //brightness
                int SysBackLightValue = (int) (BackLightValue * 255);

                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        SysBackLightValue);

                status.setText("Power Saving Mode is:ON");

            }
        });
        Button help = (Button) findViewById(R.id.button3);
        help.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Help.class);
                startActivity(i);
            }
        });


    }

    public void gps() {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[
                    ] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }


    private void mobilecheack() {
        // TODO Auto-generated method stub
        state = isMobileDataEnable();
    }

    public boolean isMobileDataEnable() {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // method is callable



            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean) method.invoke(cm);

        } catch (Exception e) {
            // Some problem accessible private API and do whatever error

            // handling here as you want..
        }
        return mobileDataEnabled;
    }

}


