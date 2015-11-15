package sos.com.sos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReportActivity extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://torrid-fire-5006.firebaseio.com";
    private Firebase mFirebaseRef;
    EditText editText1, editText2, editText4, editText3, editText5, editText6, editText7;
    ImageView imageView;
    String imageString = null;
    Button attachButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Anonymous Tip");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseRef = new Firebase(FIREBASE_URL).child("report");
        mFirebaseRef.keepSynced(true);

        editText2 = (EditText) findViewById(R.id.editText2);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);

        imageView = (ImageView) findViewById(R.id.imageView);
        if (imageString == null)
            imageView.setVisibility(View.INVISIBLE);
        attachButton = (Button) findViewById(R.id.attachImage);
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);

            }
        });

        Button button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Report test = new Report(editText1.toString(), editText2.toString(), editText3.toString(), editText4.toString(), editText5.toString(), editText6.toString(), editText7.toString(), imageString);
                mFirebaseRef.push().setValue(test);
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");
                editText6.setText("");
                editText7.setText("");
                imageView.setVisibility(View.INVISIBLE);
                attachButton.setVisibility(View.VISIBLE);


            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                //convert image to string
                Bitmap bmp = ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 300, 300);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                bmp.recycle();
                byte[] bytes = byteArrayOutputStream.toByteArray();
                imageString = Base64.encodeToString(bytes, Base64.DEFAULT);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(parseImage(imageString));
                attachButton.setVisibility(View.INVISIBLE);
                // Create a new, auto-generated child of that chat location, and save our chat data there


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public Bitmap parseImage(String string) {
        byte[] imageBytes = Base64.decode(string, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bmp;
    }

}
