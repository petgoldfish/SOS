package sos.com.sos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomReceiver extends ParsePushBroadcastReceiver {



    private final String TAG = CustomReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private Intent parseIntent;

    boolean check;
    private boolean type;

    public CustomReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        SharedPreferences sp = context.getSharedPreferences("LOGGED_IN", Context.MODE_PRIVATE);
        check = sp.getBoolean("isLoggedIn", true);

        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            type = json.getBoolean("type");

            Log.e(TAG, "Push received: " + json);

            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }


    private void parsePushJson(Context context, JSONObject json) {
        try {
            boolean isBackground = json.getBoolean("is_background");
            //JSONObject data = json.getJSONObject("data");
            //String title = data.getString("title");
            //String message = data.getString("message");

            if (!isBackground) {
                Intent resultIntent = new Intent(context, MainActivity.class);
                //resultIntent.putExtra("message",message);

                        showNotificationMessage(context, "New message", resultIntent);


            }

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        //intent.putExtras(parseIntent.getExtras());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(message, intent);
    }
}