package com.kay.toursnap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.List;


public class Main extends ActionBarActivity implements AdapterView.OnItemClickListener{

    //TextView txtDescription, txtLocation;

    File file = null;
    Uri uri = null;
    boolean supplyFile = true;
    final static int CAPTURE_IMAGE_REQUEST_CODE = 101;

    final static String basephoto = "http://10.10.3.85/toursnap/images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //boolean useAsync = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(Main.this);

            new AsyncTask<Void, Void, List<Photo>>() {
                @Override
                protected List<Photo> doInBackground(Void... params) {
                    return Photo.list();
                }
                @Override
                protected void onPostExecute(List<Photo> result) {

                    MyAdapter adapter = new MyAdapter(Main.this, R.layout.photo, result);
                    list.setAdapter(adapter);
                }
            }.execute();


    }


    @Override
    public void onItemClick(AdapterView<?> av, View v, int position, long id) {
        Photo s = (Photo) av.getAdapter().getItem(position);
        Intent intent = new Intent(this, PhotoDetails.class);
        intent.putExtra("details", s);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            capturePhoto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (supplyFile) {
            file = new File("/sdcard/", "IMG" +
                    String.valueOf(System.currentTimeMillis()) + ".jpg");
            uri = Uri.fromFile(file);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = null;
                if (supplyFile) {
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        bmp = BitmapFactory.decodeStream(fis);
                    } catch (Exception e) {
                        Log.i("pics", e.toString());
                    }
                } else {
                    uri = data.getData();
                    bmp = (Bitmap) data.getExtras().get("data");
                }

                Intent intent = new Intent(Main.this, CaptureScreen.class);
                intent.putExtra("bmp", uri);
                startActivity(intent);
                /*ImageView image = (ImageView) findViewById(R.id.imageView);
                image.setImageBitmap(bmp);*/
            }
        }
    }
}
