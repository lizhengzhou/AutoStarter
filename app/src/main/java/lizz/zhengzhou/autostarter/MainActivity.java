package lizz.zhengzhou.autostarter;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "lizz.AutoStarter";
    static final String AutoStart = "OP_AUTO_START";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Launched");

        TextView hello = (TextView) findViewById(R.id.hello);

        StringBuffer str = new StringBuffer();

        str.append("Thanks for Launch me !\n");
        str.append("I will auto launch 'io.cordova.myapp6e632a' when next time Android Booted !\n");
        str.append("MODEL:" + Build.MODEL + "\n");
        str.append("SDK Version:" + Build.VERSION.SDK_INT + "\n");
        str.append("Android Version:" + Build.VERSION.RELEASE + "\n");

        hello.setText(str);

        boolean granted = isPermissionGranted(AutoStart);
        str.append("AutoStart Has Granted : " + granted);
        hello.setText(str);

        Context context = this.getApplicationContext();
        Intent i = context.getPackageManager().getLaunchIntentForPackage("io.cordova.myapp6e632a");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        if (i == null) {
            Toast.makeText(context, "NotInstalled:io.cordova.myapp6e632a", Toast.LENGTH_LONG).show();
        } else {
            context.startActivity(i);
        }
    }


    private boolean isPermissionGranted(String permissionCode) {
        try {
            Object object = getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);

            if (method == null) {
                return false;
            }
            Object[] arrayOfObject = new Object[3];
            arrayOfObject[0] = Integer.valueOf(permissionCode);
            arrayOfObject[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject[2] = getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
