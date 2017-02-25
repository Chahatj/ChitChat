package com.example.chahat.mychat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

/**
 * Created by chahat on 22/3/16.
 */
public class FetchGroupMemberRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://chahatjain.freeoda.com/";



    public FetchGroupMemberRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeMemberDataInBackground(GroupChat message,
                                               FetchMember messageCallback) {
        // progressDialog.show();
        new StoreMemberDataAsyncTask(message, messageCallback).execute();
    }

    public void fetchMemberDataAsyncTask(GroupChat message, FetchMember messageCallback) {
        progressDialog.show();
        new fetchMemberDataAsyncTask(message,messageCallback).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

    public class StoreMemberDataAsyncTask extends AsyncTask<Void, Void, Void> {
        GroupChat message;
        FetchMember messageCallback;

        public StoreMemberDataAsyncTask(GroupChat message, FetchMember messageCallback) {
            this.message = message;
            this.messageCallback=messageCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("Sender", message.sender+""));
            dataToSend.add(new BasicNameValuePair("Reciever1", message.rec1+""));
            dataToSend.add(new BasicNameValuePair("Reciever2", message.rec2+""));
            dataToSend.add(new BasicNameValuePair("Reciever3", message.rec3+""));
            dataToSend.add(new BasicNameValuePair("Reciever4", message.rec4+""));
          //  dataToSend.add(new BasicNameValuePair("GroupMessage",message.groupmessage));
            dataToSend.add(new BasicNameValuePair("GroupName",message.groupname));


            Log.v("datatosendbefore", "" + dataToSend);


            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "GroupMemberSend.php");

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
            messageCallback.done(null);
        }

    }

    public class fetchMemberDataAsyncTask extends AsyncTask<Void, Void,GroupChat> {
        GroupChat message;
        FetchMember messageCallback;
        List<String> list=new ArrayList<>();


        public fetchMemberDataAsyncTask(GroupChat message,FetchMember messageCallback) {
            this.message = message;
            this.messageCallback = messageCallback;
        }





        @Override
        protected GroupChat doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("GroupName", message.getGroupname()));
            //dataToSend.add(new BasicNameValuePair("GroupName", message.getGroupname()+""));
            // dataToSend.add(new BasicNameValuePair("Reciever", message.Reciever+""));
            //dataToSend.add(new BasicNameValuePair("Message", message.message));

            //     Log.v("datatosend",dataToSend+"");

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "FetchMemberData.php");

            GroupChat returnedMember = null;

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



                  //  String message = js.getString("GroupName");
                    long Sender = js.getLong("Sender");
                    long Rec1 = js.getLong("Recieverone");
                    long Rec2 = js.getLong("Recievertwo");
                    long Rec3 = js.getLong("Recieverthree");
                    long Rec4 = js.getLong("Recieverfour");

                    returnedMember= new GroupChat("",Sender,Rec1,Rec2,Rec3,Rec4,"");


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedMember;
        }

        @Override
        protected void onPostExecute(GroupChat returnedlist) {
            super.onPostExecute(returnedlist);
            progressDialog.dismiss();
            messageCallback.done(returnedlist);
        }
    }
}


