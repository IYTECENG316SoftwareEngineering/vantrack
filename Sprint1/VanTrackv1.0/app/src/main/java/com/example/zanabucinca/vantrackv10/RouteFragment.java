package com.example.zanabucinca.vantrackv10;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RouteFragment extends Fragment {

    public interface OnRouteSelectedListener{ public void routeSelected(String route);}
    private OnRouteSelectedListener onRouteSelectedListener;
    private Button okbutton;


     @Override
    public void onAttach(Activity activity){
     super.onAttach(activity);
     try{
         onRouteSelectedListener=(OnRouteSelectedListener) activity;
     }catch(ClassCastException e){
         throw new ClassCastException(activity.toString()+
                 "must implement OnRouteSelectedListener.");
     }
 	}
    @Override
    public void onDetach(){
        onRouteSelectedListener=null;
    }

 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

     View view=inflater.inflate(R.layout.route_fragment, container, false);
     Spinner spinner = (Spinner) view.findViewById(R.id.routes_spinner);
     okbutton=(Button) view.findViewById(R.id.ok_button);
     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
             R.array.routes_array, android.R.layout.simple_spinner_item);
     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     spinner.setAdapter(adapter);
     spinner.setOnItemSelectedListener(new SpinnerListener());
     okbutton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String route=(String)okbutton.getTag();
             routeSelected(route);

         }
     });
     return view;
 }
    public void routeSelected(String route){
        if(onRouteSelectedListener!=null){
            onRouteSelectedListener.routeSelected(route);
        }
    }
    public class SpinnerListener implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            okbutton.setTag(""+parent.getItemAtPosition(pos).toString()+"");
        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}

