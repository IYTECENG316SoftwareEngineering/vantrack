package com.example.zanabucinca.vantrackv10;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ApiConnector {

    public JSONArray GetAllUsers(){
        String url="http://yasin.findik.se/getAllUsers.php";
        HttpEntity httpEntity=null;

        try
        {
            DefaultHttpClient httpClient=new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse=httpClient.execute(httpGet);
            httpEntity=httpResponse.getEntity();

        } catch (ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        JSONArray jsonArray=null;

        if(httpEntity != null){
            try{
                String entityResponse= EntityUtils.toString(httpEntity);
                Log.e("Entity Response:", entityResponse);
                jsonArray=new JSONArray(entityResponse);
            }catch(JSONException e)
            {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public void InsertUser(User newUser){

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("mode",newUser.getMode()));
        nameValuePairs.add(new BasicNameValuePair("route",newUser.getRoute()));
        nameValuePairs.add(new BasicNameValuePair("longitude",Double.toString(newUser.getLongitude())));
        nameValuePairs.add(new BasicNameValuePair("latitude",Double.toString(newUser.getLatitude())));
        InputStream is = null;
        String result = "";
        //http post
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://yasin.findik.se/insertUser.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        }catch(Exception e)
        {
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
    }
	
}
