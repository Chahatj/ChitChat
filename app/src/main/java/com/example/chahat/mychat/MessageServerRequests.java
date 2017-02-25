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
import java.util.jar.Attributes;

/**
 * Created by chahat on 19/3/16.
 */
public class MessageServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://chahatjain.freeoda.com/";

    public MessageServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeMessageDataInBackground(Message message,
                                          MessageCallback messageCallback) {
        progressDialog.show();
        new StoreMessageDataAsyncTask(message, messageCallback).execute();
    }

    public void fetchMessageDataAsyncTask(Message message, MessageCallback messageCallback) {
        progressDialog.show();
        new fetchMessageDataAsyncTask(message,messageCallback).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

    public class StoreMessageDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Message message;
        MessageCallback messageCallback;

        public StoreMessageDataAsyncTask(Message message, MessageCallback messageCallback) {
            this.message = message;
            this.messageCallback=messageCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("Sender", message.Sender+""));
            dataToSend.add(new BasicNameValuePair("Reciever", message.Reciever+""));
            dataToSend.add(new BasicNameValuePair("Message",message.message));


            Log.v("datatosendbefore", "" + dataToSend);


            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "MessageSend.php");

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

    public class fetchMessageDataAsyncTask extends AsyncTask<Void, Void,List<String>> {
       Message message;
        MessageCallback messageCallback;
        List<String> list=new ArrayList<>();


        public fetchMessageDataAsyncTask(Message message, MessageCallback messageCallback) {
            this.message = message;
            this.messageCallback = messageCallback;
        }





        @Override
        protected List<String> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("Reciever", message.getReciever()+""));
            dataToSend.add(new BasicNameValuePair("Sender", message.getSender()+""));
           // dataToSend.add(new BasicNameValuePair("Reciever", message.Reciever+""));
            //dataToSend.add(new BasicNameValuePair("Message", message.message));

        Log.v("datatosend",dataToSend+"");

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams,
                    CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS
                    + "FetchMessageData.php");

            Message returnedMessage = null;

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



                    String message = js.getString("Message");

                    returnedMessage= new Message(-1,-1,message);

                    String messageone = returnedMessage.getMessage();

                    Log.v("num",messageone+"");



                    list.add(i,messageone);


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
