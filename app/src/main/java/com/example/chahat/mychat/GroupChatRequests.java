package com.example.chahat.mychat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
 * Created by chahat on 21/3/16.
 */
public class GroupChatRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://chahatjain.freeoda.com/";



    public GroupChatRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }



    public void storeGroupChatDataInBackground(GroupChat message,
                                             GroupChatCallback messageCallback) {
       // progressDialog.show();
        new StoreGroupChatDataAsyncTask(message, messageCallback).execute();
    }

    public void fetchGroupChatDataAsyncTask(GroupChat message, GroupChatCallback messageCallback) {
        progressDialog.show();
        new fetchGroupChatDataAsyncTask(message,messageCallback).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

    public class StoreGroupChatDataAsyncTask extends AsyncTask<Void, Void, Void> {
        GroupChat message;
        GroupChatCallback messageCallback;

        public StoreGroupChatDataAsyncTask(GroupChat message, GroupChatCallback messageCallback) {
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
            dataToSend.add(new BasicNameValuePair("GroupMessage",message.groupmessage));
            dataToSend.add(new BasicNameValuePair("GroupName",message.groupname));


            Log.v("datatosendbefore", "" + dataToSend);


            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "GroupMessageSend.php");

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

    public class fetchGroupChatDataAsyncTask extends AsyncTask<Void, Void,List<String>> {
        GroupChat message;
        GroupChatCallback messageCallback;
        List<String> list=new ArrayList<>();


        public fetchGroupChatDataAsyncTask(GroupChat message, GroupChatCallback messageCallback) {
            this.message = message;
            this.messageCallback = messageCallback;
        }





        @Override
        protected List<String> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("Sender", message.getSender()+""));
            dataToSend.add(new BasicNameValuePair("GroupName", message.getGroupname()+""));
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
                    + "FetchGroupMessageData.php");

            GroupChat returnedMessage = null;

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



                    String message = js.getString("GroupMessage");
                    long Sender = js.getLong("Sender");

                    returnedMessage= new GroupChat("",Sender,-1,-1,-1,-1,message);

                    String messageone = returnedMessage.getGroupmessage();
                    long sender = returnedMessage.getSender();

                    Log.v("num",messageone+"");



                    list.add(i,sender+":"+messageone);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<String > returnedlist) {
            super.onPostExecute(returnedlist);
            progressDialog.dismiss();
            messageCallback.done(returnedlist);
        }
    }
}

