package com.kay.toursnap;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by student on 27/1/15.
 */
public class Photo extends HashMap<String, String> {

    final static String baseurl = "http://10.10.3.85/toursnap";
    final static String baselist = "http://10.10.3.85/toursnap/Service.svc/list";


    public Photo(String imgPath, String description, String location) {
        put("imgPath", imgPath);
        put("description", description);
        put("location", location);
    }

    public static List<Photo> list() {
        List<Photo> list = new ArrayList<Photo>();

        String url1 = String.format("%s/Service.svc/List", baseurl);

        String url2 = "http://10.10.3.85/toursnap/Service.svc/GetPhoto";
        JSONArray a = JSONParser.getJSONArrayFromUrl(url1);
        try{
            for(int i = 0; i < a.length() ; i++){
                String id = a.getString(i);
                //Log.e(id)
                String url3 = String.format("%s/%s",url2,id);
                //http://192.168.38.116/WCFService5/IMG1
                JSONObject obj = JSONParser.getJSONFromUrl(url3);

                //System.out.println("======OBJ======" + obj.toString());
                list.add(new Photo(obj.getString("ImgPath"),obj.getString("Description"),obj.getString("Location")));
            }
        }catch(Exception e){
            Log.e("Photo ", "JSONArray error");
        }
        return (list);
    }

    public static Photo getPhoto(String imgPath) {
        Photo p = null;
        try {
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/Service.svc/GetPhoto/%s", baseurl, imgPath));
            p = new Photo(a.getString("ImgPath"), a.getString("Description"), a.getString("Location"));
        } catch (Exception e) {
            Log.e("getPhoto", "JSON error");
        }
        return p;
    }

    public static void insertPhoto(String imgPath, String description, String location) {

        try {
            JSONObject image = new JSONObject();
            image.put("ImgPath", imgPath);
            image.put("Description", description);
            image.put("Location", location);

            String json = image.toString();
            System.out.println("=============" + json);
            String result = JSONParser.postStream(
                    String.format("%s/Service.svc/InsertPhoto", baseurl),json);
        } catch (Exception e) {
            Log.e("insertPhoto", "JSON error");
        }
    }

    public static void updatePhoto(String imgPath, String description, String location) {
        try {
            JSONObject customer = new JSONObject();

            customer.put("ImgPath", imgPath);
            customer.put("Description", description);
            customer.put("Location", location);
            String json = customer.toString();
            String result = JSONParser.postStream(
                    String.format("%s/Service.svc/UpdatePhoto", baseurl),
                    json);
        } catch (Exception e) {
            Log.e("updatePhoto", "JSON error");
        }
    }

    public static void deletePhoto(String imgPath) {
        try {

            String result = JSONParser.getStream(String.format("%s/Service.svc/DeletePhoto/%s",baseurl,imgPath));
        } catch (Exception e) {
            Log.e("deletePhoto", "JSON error");
        }
    }
}
