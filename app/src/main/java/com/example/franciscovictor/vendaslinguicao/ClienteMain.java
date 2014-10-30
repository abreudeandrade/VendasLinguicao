package com.example.franciscovictor.vendaslinguicao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.franciscovictor.vendaslinguicao.library.VendedorFunctions;
import com.example.franciscovictor.vendaslinguicao.localizacao.GPSTracker;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClienteMain extends ActionBarActivity
{

    Button btAtualizarLista;
    ListView listaVendedores;
    ArrayAdapter arrayAdapter;
    GPSTracker gps;

    List<VendedorLista> entries = new ArrayList<VendedorLista>();
    Double myLatitude, myLongitude;

    private static String KEY_SUCCESS = "success";

    // products JSONArray
    JSONArray vendedores = null;
    ArrayList<HashMap<String, String>> vendedoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_main);


        // Código GPS
        gps = new GPSTracker(ClienteMain.this);
        // check if GPS enabled
        if (gps.canGetLocation)
        {
            myLatitude = gps.getLatitude(); //My latitude
            myLongitude = gps.getLongitude(); //My longitude
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        //marotagem dos buttons
        btAtualizarLista = (Button) findViewById(R.id.btAtualizarLista);

        listaVendedores = (ListView) findViewById(R.id.listaVendedores);


        new NetCheck().execute();

        btAtualizarLista.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        // Populate the list, through the adapter

    }

    private List<VendedorLista> getNewsEntries()
    {
        // Let's setup some test data.
        // Normally this would come from some asynchronous fetch into a data source
        // such as a sqlite database, or an HTTP request


        return entries;
    }

    /**
     * Async Task to check whether internet connection is working.
     */
    class NetCheck extends AsyncTask<Boolean, Boolean, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            nDialog = new ProgressDialog(ClienteMain.this);
            nDialog.setTitle("Checando internet..");
            nDialog.setMessage("Carregando..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
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
                nDialog.dismiss();
                new VerificaVendedoresDisponiveis().execute();
            }
            else
            {
                nDialog.dismiss();
                Toast.makeText(ClienteMain.this, "Erro ao tentar conexão com a internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     */
    class VerificaVendedoresDisponiveis extends AsyncTask<JSONObject, JSONObject, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected JSONObject doInBackground(JSONObject... args)
        {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            VendedorFunctions userFunction = new VendedorFunctions();
            JSONObject json = userFunction.SelecionaVendedoresDisponiveis();
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
                        vendedores = json.getJSONArray("vendedores");
                        // looping through All Products
                        for (int i = 0; i < vendedores.length(); i++)
                        {
                            JSONObject c = vendedores.getJSONObject(i);
                            // Storing each json item in variable
                            String nome = c.getString("nome");
                            String latitude = c.getString("latitude");
                            String longitude = c.getString("longitude");
                            String descricao = c.getString("descricao");



                            String distancia = String.valueOf(gps.getDistance(myLatitude, myLongitude, Double.parseDouble(latitude), Double.parseDouble(longitude)));

                            entries.add(
                                    new VendedorLista(
                                            nome,
                                            descricao,
                                            distancia
                                    )
                            );

                            Toast.makeText(getApplicationContext(),entries.get(0).getDistancia(),Toast.LENGTH_LONG).show();

                        }

                        // Setup the list view
                        NewsEntryAdapter newsEntryAdapter = new NewsEntryAdapter(ClienteMain.this, R.layout.news_entry_list_item);

                        listaVendedores.setAdapter(newsEntryAdapter);
                        //Setup the click listener
                        listaVendedores.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                            {
                                //Toast.makeText(getBaseContext(), "You clicked on " + arg2, Toast.LENGTH_SHORT).show();

                                Intent intentMainCliente = new Intent(ClienteMain.this, MyActivity.class);

                                Bundle params = new Bundle();

                                params.putString("nome", entries.(arg2).getNome();
                                intentMainCliente.putExtras(params);
                                startActivity(intentMainCliente);




                            }
                        });
                        for (VendedorLista entry : entries)
                        {
                            newsEntryAdapter.add(entry);
                            Toast.makeText(getApplicationContext(),entry.getNome(),Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Erro ao pegar os dados!", Toast.LENGTH_SHORT).show();
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
