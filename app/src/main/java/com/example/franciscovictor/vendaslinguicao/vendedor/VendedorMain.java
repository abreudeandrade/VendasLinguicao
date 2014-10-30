package com.example.franciscovictor.vendaslinguicao.vendedor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franciscovictor.vendaslinguicao.R;
import com.example.franciscovictor.vendaslinguicao.library.VendedorFunctions;
import com.example.franciscovictor.vendaslinguicao.localizacao.GPSTracker;
import com.example.franciscovictor.vendaslinguicao.localizacao.MyStartServiceReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VendedorMain extends ActionBarActivity
{
    Button btSalvarAlteracoes;
    TextView textNome, textDisp;
    EditText editDescricao;
    Switch switchStatus;
    RadioButton rbTen, rbFifteen, rbThirty;

    public int tempoAtualiza;
    GPSTracker gps;

    double myLatitude, myLongitude;

    private static String cpf;
    // JSON Response node names
    private static String KEY_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if (params != null)
        {
            cpf = params.getString("cpf");
        }
        setContentView(R.layout.activity_vendedor_main);
        btSalvarAlteracoes = (Button) findViewById(R.id.btSalvarAlteracoes);
        textNome = (TextView) findViewById(R.id.textNomeVendedor);
        textDisp = (TextView) findViewById(R.id.textDispVendedor);
        editDescricao = (EditText) findViewById(R.id.editAtualizaDescricao);
        switchStatus = (Switch) findViewById(R.id.switchDispVendedor);
        rbTen = (RadioButton) findViewById(R.id.rbTen);
        rbFifteen = (RadioButton) findViewById(R.id.rbFifteen);
        rbThirty = (RadioButton) findViewById(R.id.rbThirty);
        new NetCheck().execute();
        btSalvarAlteracoes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent downloader = new Intent(VendedorMain.this, MyStartServiceReceiver.class);
                Bundle params = new Bundle();
                params.putString("cpf", cpf);
                downloader.putExtras(params);
                downloader.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(VendedorMain.this, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(VendedorMain.this.ALARM_SERVICE);
                if (rbTen.isChecked())
                {
                    tempoAtualiza = 10;
                }
                else if (rbFifteen.isChecked())
                {
                    tempoAtualiza = 15;
                }
                else if (rbThirty.isChecked())
                {
                    tempoAtualiza = 30;
                }
                if (switchStatus.isChecked())
                {
                    // Código GPS
                    gps = new GPSTracker(VendedorMain.this);
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
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (tempoAtualiza * 1000), tempoAtualiza * 1000, pendingIntent);
                    //MANDAR PRO SERV!!!!!
                }
                else
                {
                    alarmManager.cancel(pendingIntent);
                    //MANDAR PRO SERV!!!!!!
                }
                new AtualizandoDadosVendedor().execute();
            }
        });
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
            nDialog = new ProgressDialog(VendedorMain.this);
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
                new RecebendoDadosVendedor().execute();
            }
            else
            {
                nDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Erro ao tentar conexão com a internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     */
    class RecebendoDadosVendedor extends AsyncTask<JSONObject, JSONObject, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected JSONObject doInBackground(JSONObject... args)
        {
            VendedorFunctions userFunction = new VendedorFunctions();
            JSONObject json = userFunction.ReceberInformacoesVendedor(cpf);
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
                        String nome = json.getString("nome");
                        String disp = json.getString("status");
                        String desc = json.getString("descricao");
                        textNome.setText("Nome: " + nome);
                        textDisp.setText("Disponibilidade: " + disp);
                        editDescricao.setText(desc);
                        editDescricao.setSelection(editDescricao.getText().length());
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

    class AtualizandoDadosVendedor extends AsyncTask<JSONObject, JSONObject, JSONObject>
    {
        private ProgressDialog pDialog;
        String desc;
        boolean on;
        String status = "2";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(VendedorMain.this);
            pDialog.setTitle("Contatando Servidores");
            pDialog.setMessage("Entrando..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            on = switchStatus.isChecked();
            if (on)
            {
                status = "1";
            }
            else
            {
                status = "0";
            }
            desc = editDescricao.getText().toString();
        }

        protected JSONObject doInBackground(JSONObject... args)
        {
            VendedorFunctions userFunction = new VendedorFunctions();
            JSONObject json = userFunction.AtualizarInformacoesVendedor(cpf, desc, status, String.valueOf(myLatitude), String.valueOf(myLongitude));
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
                        new RecebendoDadosVendedor().execute();
                        pDialog.dismiss();
                    }
                    else
                    {
                        pDialog.dismiss();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vendedor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
