package com.example.zanabucinca.vantrackv10.Test;


import android.location.Location;

import com.example.zanabucinca.vantrackv10.Model.User;
import com.example.zanabucinca.vantrackv10.Operators.ApiConnector;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class UsersDatabaseTest extends TestCase {
	User newUser;
	String id="99";
	String mode="User";
	String route="URLA-IYTE";
	int isPublic=1;
	boolean isUse3g=false;
	ApiConnector apiConnector;
	ArrayList<ArrayList<String>> allUsersFromDB;
	String afterInsert="";
	String afterDelete="";



	@Before
	public void setUp(){

		Location l=new Location("");
		l.setLatitude(38.323094);
		l.setLongitude(26.763875);

		newUser=new User(id, isPublic, mode,isUse3g);
		newUser.setRoute(route);
		newUser.setLocation(l);
		apiConnector=new ApiConnector();
		allUsersFromDB=new ArrayList<>();

	}



	@Test
	public void testInsert(){

		apiConnector.insertUser(newUser);

		allUsersFromDB=setInfoAllUsers(apiConnector.getAllUsers());
		for (List<String> row : allUsersFromDB) {
			if(row.get(0).compareTo(id)==0){
				afterInsert=row.get(0);
			}
		}
		assertEquals(id, afterInsert);

	}
	@Test
	public void testDelete(){
		apiConnector.deleteUser(newUser);
		allUsersFromDB=setInfoAllUsers(apiConnector.getAllUsers());
		for (List<String> row : allUsersFromDB) {
			if(row.get(0).compareTo(id)==0){
				afterDelete=row.get(0);
			}
		}

		assertEquals("", afterDelete);
	}


	private ArrayList<ArrayList<String>> setInfoAllUsers(JSONArray jsonArray){
		ArrayList<ArrayList<String>> allLocations = new ArrayList<ArrayList<String>>();

		for(int i=0; i<jsonArray.length(); i++){
			JSONObject json=null;
			try{
				json=jsonArray.getJSONObject(i);

				ArrayList<String> loc=new ArrayList<String>();
				loc.add((json.getString("id")));
				loc.add(json.getString("isPublic"));
				loc.add(json.getString("route"));
				loc.add(json.getString("mode"));
				loc.add(json.getString("speed"));
				loc.add((json.getString("latitude")));
				loc.add((json.getString("longitude")));

				allLocations.add(i, loc);

			}catch(JSONException e)
			{
				e.printStackTrace();
			}catch (NullPointerException e){

			}
		}
		return allLocations;
	}
}
