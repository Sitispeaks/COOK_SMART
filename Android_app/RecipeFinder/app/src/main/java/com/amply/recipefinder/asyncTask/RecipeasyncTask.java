package com.amply.recipefinder.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeasyncTask extends AsyncTask<Void, Void, Void> {

    // new RecipeasyncTask().execute();


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {
        // Your background method
//        newlist = new ArrayList<HashMap<String, String>>();
//        json = jsonParser.makeHttpRequest(json, "POST");
//        try {
//            newarray = new JSONArray(json);
//            for (int i = 0; i < countdisplay; i++) {
//                HashMap<String, String> eachnew = new HashMap<String, String>();
//                newobject = newarray.getJSONObject(i);
//                eachnew.put("id", newobject.getString("ID"));
//                eachnew.put("name", newobject.getString("Name"));
//                newlist.add(eachnew);
//            }
//        } catch (
//        JSONException e) {
//            Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
//        tvInfo.setText("Finish");
    }


}
