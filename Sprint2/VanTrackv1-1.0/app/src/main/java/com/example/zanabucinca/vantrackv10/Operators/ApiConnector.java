package com.example.zanabucinca.vantrackv10.Operators;

import android.util.Log;

import com.example.zanabucinca.vantrackv10.Model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;


public class ApiConnector{
    private String LOG_TAG = "ApiConnector";
    private final String urlGetAllUsers="http://yasin.findik.se/getAllUsers.php";
    private final String urlInsertUser="http://yasin.findik.se/insertUser.php";
    private final String urlUpdateUser="http://yasin.findik.se/updateUser.php";
    private final String urlDeleteUser="http://yasin.findik.se/deleteUser.php";

    // for get all public users from db
    public JSONArray getAllUsers(){
        HttpEntity httpEntity=null;

        try
        {
            DefaultHttpClient httpClient=new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlGetAllUsers);
            HttpResponse httpResponse=httpClient.execute(httpGet);
            httpEntity=httpResponse.getEntity();

            //Log.d("Entity Response for getalluser:", httpEntity.toString());

        } catch (ClientProtocolException e){

            Log.e(LOG_TAG+" for gettingUsers","ClientProtocolException occured");
            //e.printStackTrace();
        } catch (IOException e){

            Log.e(LOG_TAG+" for gettingUsers","I/O exeption occured");
            // e.printStackTrace();
        }catch(Exception e){
            Log.e(LOG_TAG, "Error in http connection "+e.toString());

        }
        JSONArray jsonArray=null;

        if(httpEntity != null){
            try{
                String entityResponse= EntityUtils.toString(httpEntity);
                //Log.d("Entity Response for getalluser:", entityResponse);
                jsonArray=new JSONArray(entityResponse);
            }catch(JSONException e)
            {

                Log.e(LOG_TAG+" for gettingUsers","json exeption occured");
              //  e.printStackTrace();
            }catch(IOException e){

                Log.e(LOG_TAG+" for gettingUsers","I/O exeption occured");
              //  e.printStackTrace();
            }catch(Exception e){

                Log.e(LOG_TAG, "Error in http connection "+e.toString());

            }
        }
        return jsonArray;
    }
    // for insertion current user to db
    public boolean insertUser(User newUser){

        ArrayList<NameValuePair> nameValuePairs = newUser.getNameValuePairs();
        boolean isExist = false;
        boolean isInserted = false;
        try{

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlInsertUser);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response =httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            JSONObject jsonObject=null;
            if(entity!=null){
                try{
                    String entityResponse= EntityUtils.toString(entity);
                    //Log.d("Entity Response for insertUser:", entityResponse);
                    jsonObject=new JSONObject(entityResponse);
                    isExist=Boolean.parseBoolean(jsonObject.getString("isExist"));
                  //  Log.d("jsonArry for insertUser",jsonObject.getString("isExist"));
                    Log.d(LOG_TAG+" for insertion",""+isExist);

                }catch(JSONException e)
                {
                    Log.e(LOG_TAG+" for insertion","json exeption occured");
                   // e.printStackTrace();
                    return isInserted;
                }catch(IOException e){

                    Log.e(LOG_TAG+" for insertion","I/O exeption occured");
                  //  e.printStackTrace();
                    return isInserted;
                }

                if(isExist){
                    Log.d(LOG_TAG+" for insertion","calling update. User already exist in db(bad close)");
                    return updateUser(newUser);
                }
            }

            //  is = entity.getContent();
         //   Log.d("pass for insertUser ", "connection success ");
        }catch(Exception e){
            Log.e(LOG_TAG, "Error in http connection "+e.toString());
            return false;
        }
        return !isInserted;
    }
    // for update current user location or other information to db
    public boolean updateUser(User newUser){
        ArrayList<NameValuePair> nameValuePairs = newUser.getNameValuePairs();
        boolean isSucceed = false;
      //  boolean isUpdated = false;
        try{

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlUpdateUser);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response =httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            //Log.d("Entity Response for UpdateUser:", entity.toString());
            JSONObject jsonObject=null;
            if(entity!=null){
                try{
                    String entityResponse= EntityUtils.toString(entity);
                   // Log.d("Entity Response for UpdateUser:", entityResponse);
                    jsonObject=new JSONObject(entityResponse);
                    isSucceed=Boolean.parseBoolean(jsonObject.getString("isSucceed"));
                   // Log.d("jsonArry for UpdateUser",jsonObject.getString("isSucceed"));
                    Log.d(LOG_TAG+" for update",""+isSucceed);
                    return isSucceed;
                }catch(JSONException e)
                {

                    Log.e(LOG_TAG+" for update","json exeption occured");
                 //   e.printStackTrace();
                    return isSucceed;
                }catch(IOException e){
                    Log.e(LOG_TAG+" for update","I/O exeption occured");
                 //   e.printStackTrace();
                    return isSucceed;
                }
            }

            //  is = entity.getContent();
           // Log.d(LOG_TAG+" for insertion", "connection success ");
        }catch(Exception e)
        {
            Log.e(LOG_TAG+" for update", "Error in http connection "+e.toString());
            return isSucceed;
        }

        return isSucceed;
    }
    // for delete if current user closed application, it must be deleted from db
    public boolean deleteUser(User newUser){

        ArrayList<NameValuePair> nameValuePairs = newUser.getNameValuePairs();
        boolean isDeleted = false;
        try{

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlDeleteUser);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response =httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            JSONObject jsonObject=null;
            if(entity!=null){
                try{
                    String entityResponse= EntityUtils.toString(entity);
                    //Log.d("Entity Response for InsertUser:", entityResponse);
                    jsonObject=new JSONObject(entityResponse);
                    isDeleted=Boolean.parseBoolean(jsonObject.getString("isSucceed"));
                  //  Log.d(LOG_TAG,jsonObject.getString("isDeleted"));
                    Log.d(LOG_TAG+"deletions process is ",""+isDeleted);

                }catch(JSONException e)
                {
                    Log.e(LOG_TAG+" for deletion","json exeption occured");
                  //  e.printStackTrace();
                }
            }

            //  is = entity.getContent();
           // Log.d(LOG_TAG, "connection success ");
        }catch(Exception e){
            Log.e(LOG_TAG+" for deletion", "Error in http connection "+e.toString());
            return false;
        }
        return !isDeleted;
    }
}
