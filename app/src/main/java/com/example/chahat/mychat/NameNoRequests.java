package com.example.chahat.mychat;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by chahat on 18/3/16.
 */
public class NameNoRequests {

    List<Long> list=new ArrayList<>();

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://chahatjain.freeoda.com/";

    String singleName,singleNumber;
    String[] nameArray,numberArray;

    public NameNoRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeNameNoDataInBackground(NameNo nameNo, NameNoCallback nameNoCallback) {
        progressDialog.show();
        new StoreNameNoDataAsyncTask(nameNo, nameNoCallback).execute();
    }

    public void fetchNameNoDataAsyncTask(NameNo nameNo, NameNoCallback nameNoCallback) {
        progressDialog.show();
        new fetchNameNoDataAsyncTask(nameNo,nameNoCallback).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

   public class StoreNameNoDataAsyncTask extends AsyncTask<Void, Void, Void> {
        NameNo nameNo;
        NameNoCallback nameNoCallback;

        public StoreNameNoDataAsyncTask(NameNo nameNo, NameNoCallback nameNoCallback) {
            this.nameNo = nameNo;
            this.nameNoCallback = nameNoCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("Name", nameNo.name));
            dataToSend.add(new BasicNameValuePair("Phone", nameNo.no+""));


            Log.v("datatosendbefore", "" + dataToSend);


            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));

                Log.v("datatosend", "" + post);

                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            nameNoCallback.done(null);
        }

    }

    public class fetchNameNoDataAsyncTask extends AsyncTask<Void, Void, List<Long> > {
        NameNo nameNo;
        NameNoCallback nameNoCallback;

        public fetchNameNoDataAsyncTask(NameNo nameNo,NameNoCallback nameNoCallback) {
            this.nameNo = nameNo;
            this.nameNoCallback = nameNoCallback;
        }

        @Override
        protected List<Long> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
//            dataToSend.add(new BasicNameValuePair("Name", nameNo.name));
  //          dataToSend.add(new BasicNameValuePair("Phone", nameNo.no+""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "FetchNameNoData.php");

            NameNo returnedNameNo = null;
            long num;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);


                JSONObject jObject = new JSONObject(result);

                JSONArray apps = jObject.getJSONArray("apps");

                Log.v("array",apps+"");

                int n = apps.length();

                Log.v("appslength",n+"");

                for (int i=0;i<n;i++)
                {
                    Log.v("loop",i+"");

                    JSONObject js = apps.getJSONObject(i);



                    String name = js.getString("Name");

                    String phones = js.getString("Phone");
                    Log.v("phone",phones);

                    long phone = Long.parseLong(phones);
                    returnedNameNo= new NameNo(name,phone);

                    num = returnedNameNo.getNo();

                    Log.v("num",num+"");



                    list.add(i,num);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Long> returnedlist) {
            super.onPostExecute(returnedlist);
            progressDialog.dismiss();
            nameNoCallback.done(returnedlist);
        }
    }




}

