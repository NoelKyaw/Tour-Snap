package com.kay.toursnap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Photo> {

    String fname;
    final static String basephoto = "http://10.10.3.85/toursnap/myimage.aspx?image=";
    private List<Photo> items;

    public MyAdapter(Context context, int resource, List<Photo> items) {
        super(context, resource, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.photo, null);
        Photo p = items.get(position);
        //System.out.println("*****************"+p.toString());

        if (p != null) {

            TextView name = (TextView) v.findViewById(R.id.txtfname);
            TextView des = (TextView) v.findViewById(R.id.txtdescription_photo);
            TextView loc = (TextView) v.findViewById(R.id.txtLoc_photo);

            name.setText(p.get("imgPath"));
            des.setText(p.get("description"));
            loc.setText(p.get("location"));

            fname = name.getText().toString();

            //ImageView filename = (ImageView)
            final ImageView image = (ImageView) v.findViewById(R.id.imageView);

            new AsyncTask<String, Void, Bitmap>() {
                Bitmap b;
                @Override
                protected Bitmap doInBackground(String... f) {

                    //String url = "" + baseurl + filename[0];
                    String url = String.format("%s%s&size=100", basephoto, f[0]);


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
            }.execute(p.get("imgPath"));

        }
        return v;

    }
}