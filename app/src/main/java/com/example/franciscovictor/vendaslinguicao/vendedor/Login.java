package com.example.franciscovictor.vendaslinguicao.vendedor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.franciscovictor.vendaslinguicao.R;
import com.example.franciscovictor.vendaslinguicao.library.VendedorFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Fragment
{
    Button btLogin;
    EditText editCpf, editSenha;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_vendedor_login, container, false);

        btLogin = (Button) rootView.findViewById(R.id.btLogin);
        editCpf = (EditText) rootView.findViewById(R.id.editCpf);
        editSenha = (EditText) rootView.findViewById(R.id.editSenha);
        btLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ((editCpf.length() != 11 || editSenha.length() == 0))
                {
                    Toast toast = Toast.makeText(getActivity(), "Dados incorretos!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    new NetCheck().execute();
                }
            }
        });

        return rootView;
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
            nDialog = new ProgressDialog(getActivity());
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
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                new LogandoVendedor().execute();
            }
            else
            {
                nDialog.dismiss();
                Toast.makeText(getActivity(), "Erro ao tentar conexão com a internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     */
    class LogandoVendedor extends AsyncTask<JSONObject, JSONObject, JSONObject>
    {
        String cpf, senha;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            cpf = editCpf.getText().toString();
            senha = editSenha.getText().toString();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Contatando Servidores");
            pDialog.setMessage("Entrando..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(JSONObject... args)
        {
            VendedorFunctions userFunction = new VendedorFunctions();
            JSONObject json = userFunction.LogarVendedor(senha, cpf);
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
                        pDialog.dismiss();

                        Intent upanel = new Intent(getActivity(), VendedorMain.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle params = new Bundle();

                        params.putString("cpf", cpf);
                        upanel.putExtras(params);


                        startActivity(upanel);
                        getActivity().finish();
                    }
                    else
                    {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Dados incorretos!", Toast.LENGTH_SHORT).show();
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
