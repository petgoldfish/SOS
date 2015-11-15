package sos.com.sos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

            String phoneNumber = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);

            if(phoneNumber.equals("*##")) {
                Intent intent1 = new Intent(context, AmbulanceActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);

            }

            else if(phoneNumber.equals("*#")) {
                Intent intent1 = new Intent(context, FireActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);

            }

        }
    }
}
