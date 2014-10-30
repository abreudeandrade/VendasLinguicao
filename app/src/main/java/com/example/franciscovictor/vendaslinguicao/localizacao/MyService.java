package com.example.franciscovictor.vendaslinguicao.localizacao;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.franciscovictor.vendaslinguicao.library.VendedorFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by franciscovictor on 10/25/14.
 */
public class MyService extends IntentService
{
    private static String KEY_SUCCESS = "success";

    double myLatitude, myLongitude;
    String cpf;

    public MyService()
    {
        super("MyServiceName");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        //Log.d("MyService", "About to execute MyTask");
        Bundle params = intent.getExtras();
        if (params != null)
        {
            cpf = params.getString("cpf");
        }
        this.sendNotification(this);
    }

    private void sendNotification(Context context)
    {
        GPSTracker gps;
        // Código GPS
        gps = new GPSTracker(MyService.this);
        // check if GPS enabled
        if (gps.canGetLocation())
        {
            myLatitude = gps.getLatitude(); //My latitude
            myLongitude = gps.getLongitude(); //My longitude
            new NetCheck().execute();
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    /**
     * Async Task to check whether internet connection is working.
     */
    class NetCheck extends AsyncTask<Boolean, Boolean, Boolean>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Boolean... params)
        {
            /**
             * Gets current device state and checks for working internet
             * connection by trying Google.
             **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected())
            {
                try
                {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200)
                    {
                        return true;
                    }
                }
                catch (MalformedURLException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        protected void onPostExecute(Boolean th)
        {
            if (th == true)
            {
                new AtualizandoLocalizacaoVendedor().execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Erro ao tentar conexão com a internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AtualizandoLocalizacaoVendedor extends AsyncTask<JSONObject, JSONObject, JSONObject>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected JSONObject doInBackground(JSONObject... args)
        {
            VendedorFunctions userFunction = new VendedorFunctions();
            JSONObject json = userFunction.AtualizarLocalizacaoVendedor(cpf, String.valueOf(myLatitude), String.valueOf(myLongitude));
            return json;
        }

        protected void onPostExecute(JSONObject json)
        {
            try
            {
                if (json.getString(KEY_SUCCESS) != null)
                {
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1)
                    {

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Erro atualizar os dados!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}