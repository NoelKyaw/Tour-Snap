package com.kay.toursnap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;


public class PhotoDetails extends ActionBarActivity implements View.OnClickListener {

    String fname;
    final static String basephoto = "http://10.10.3.85/toursnap/images/";

    EditText txtDesc;
    TextView txtLoc, txtFname;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        HashMap<String,String> obj = (HashMap) getIntent().getSerializableExtra("details");

        txtFname = (TextView)findViewById(R.id.txtfname_details);

        Button btnSave = (Button)findViewById(R.id.btnSave_details);
        btnSave.setOnClickListener(this);

        Button btnDelete = (Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        txtDesc = (EditText)findViewById(R.id.txtdescription_details);
        txtLoc = (TextView)findViewById(R.id.txtMap_details);

        txtFname.setText(obj.get("imgPath"));
        fname = txtFname.getText().toString();
        txtDesc.setText(obj.get("description"));
        txtLoc.setText(obj.get("location"));


        image = (ImageView) findViewById(R.id.imageView);

        //System.out.println("************" + obj.get("imgPath"));

        new AsyncTask<String, Void, Bitmap>() {
            Bitmap b;
            @Override
            protected Bitmap doInBackground(String... f) {
                String url = String.format("%s%s", basephoto, f[0]);
                try {
                    InputStream in = new URL(url).openStream();
                    b = BitmapFactory.decodeStream(in);
                } catch (Exception ex) {
                    Log.e("Bitmap Error", ex.toString());
                }
                return b;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                image.setImageBitmap(bitmap);
            }
        }.execute(obj.get("imgPath"));
    }

    @Override
    public void onClick(View v) {
        //System.out.println("=====================onClick");
        txtDesc = (EditText) findViewById(R.id.txtdescription_details);
        txtLoc = (TextView) findViewById(R.id.txtMap_details);
        txtFname = (TextView) findViewById(R.id.txtfname_details);

        //System.out.println("+++++++++++" + txtDesc.getText().toString() +  txtLoc.getText().toString() + txtFname.getText().toString());
        if (v.getId() == R.id.btnSave_details) {
            update(txtFname.getText().toString(),
                    txtDesc.getText().toString(), txtLoc.getText().toString());
        } else if (v.getId() == R.id.btnDelete)
        {
            txtFname = (TextView) findViewById(R.id.txtfname_details);
            delete(txtFname.getText().toString());
        }
        onRestart();
    }

    public void update( String imgPath, String description, String location) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                Photo.updatePhoto(params[0], params[1], params[2]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute(imgPath, description, location);
    }

    public void delete(String imgPath)
    {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                Photo.deletePhoto(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute(imgPath);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent a = new Intent(this, Main.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }
}
