package com.kay.toursnap;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;



public class CaptureScreen extends ActionBarActivity implements View.OnClickListener{

    GPSTracker gps;
    /*File file = null;*/
    Uri uri = null;
   /* boolean supplyFile = true;
    final static int CAPTURE_IMAGE_REQUEST_CODE = 101;*/

    private TextView txtLocation, txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_screen);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            return;
        }

        ImageView image = (ImageView) findViewById(R.id.imageView);
        Intent i = getIntent();
        uri = (Uri) i.getExtras().get("bmp");
        image.setImageURI(uri);

        txtDescription = (TextView) findViewById(R.id.txtdescription);

        Button b = (Button) findViewById(R.id.btnUpload);
        b.setOnClickListener(this);

        Button btnLocation = (Button) findViewById(R.id.btnAddLocation);
        btnLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddLocation) {
            txtLocation = (TextView) findViewById(R.id.txtMap);
            currentlocation();
        } else if (v.getId() == R.id.btnUpload) {
            uploadPhoto();
        }
    }

    private void uploadPhoto() {
        new AsyncTask<Uri, Void, Void>(){
            @Override
            protected Void doInBackground(Uri... arg) {
                String f = getRealPathFromURI(arg[0]);
                Transfer.uploadFile(f);
                return null;
            }
            protected void onPostExecute(Void v) {
                new AsyncTask<Uri, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Uri... arg) {
                        File f = new File(getRealPathFromURI(arg[0]));
                        String n = f.getName();

                        String path = n;
                        String desc = txtDescription.getText().toString();
                        String loc = txtLocation.getText().toString();

                        insert(path,desc,loc);

                        try {
                            InputStream in = new URL(String.format("%s/images/%s", Transfer.base, n)).openStream();
                            Bitmap b = BitmapFactory.decodeStream(in);
                            return b;
                        } catch (Exception e) {
                            return null;
                        }
                    }

                    protected void onPostExecute(Bitmap b) {
                        ImageView v = (ImageView) findViewById(R.id.imageView);
                        v.setImageBitmap(b);
                    }
                }.execute(uri);
                onRestart();
            }
        }.execute(uri);
    }

    private String getRealPathFromURI(Uri uri)
    {
        String filePath;
        if (uri != null && "content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().
                    query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        } else {
            filePath = uri.getPath();
        }
        return(filePath);
    }

    private void currentlocation() {
        gps = new GPSTracker(CaptureScreen.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String location;
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(
                        latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    location = address.getAddressLine(0);
                    txtLocation.setText(location);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            gps.showSettingsAlert();
        }
    }

    public void insert(String path,String loc,String desc){
        new AsyncTask<String,Void,Void>(){
            @Override
            protected Void doInBackground(String... params){
                Photo.insertPhoto(params[0], params[1], params[2]);
                return null;
            }
            @Override
            protected void onPostExecute(Void v){

            }
        }.execute(path, loc, desc);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent a = new Intent(this, Main.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }



}
