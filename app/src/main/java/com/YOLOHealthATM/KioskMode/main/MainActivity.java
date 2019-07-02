package com.YOLOHealthATM.KioskMode.main;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Button lockTaskButton;
    private PackageManager mPackageManager;

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponentName;

    public static final String EXTRA_FILEPATH = "com.YOLOHealthATM.KioskMode.main.EXTRA_FILEPATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lockTaskButton = findViewById(R.id.start_lock_button);

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminComponentName = DeviceAdminReceiver.getComponentName(this);
        mPackageManager = this.getPackageManager();

        if ( mDevicePolicyManager.isDeviceOwnerApp(getApplicationContext().getPackageName())) {

            Intent lockIntent = new Intent(getApplicationContext(), LockedActivity.class);

            mPackageManager.setComponentEnabledSetting(new ComponentName(getApplicationContext(), LockedActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            startActivity(lockIntent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), R.string.not_lock_whitelisted,Toast.LENGTH_SHORT).show();
        }

        // Check to see if started by LockActivity and disable LockActivity if so

        Intent intent = getIntent();

        if(intent.getIntExtra(LockedActivity.LOCK_ACTIVITY_KEY,0) == LockedActivity.FROM_LOCK_ACTIVITY){

            mDevicePolicyManager.clearPackagePersistentPreferredActivities(mAdminComponentName,getPackageName());

            mPackageManager.setComponentEnabledSetting(new ComponentName(getApplicationContext(), LockedActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }


}
