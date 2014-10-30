package com.example.franciscovictor.vendaslinguicao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.franciscovictor.vendaslinguicao.vendedor.Vendedor;


public class MainActivity extends ActionBarActivity {

    // instaciar os buttons
    Button btMainCliente, btLoginVendedor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // marotagem com buttons
        btMainCliente = (Button) findViewById(R.id.btMainCliente);
        btLoginVendedor = (Button) findViewById(R.id.btLoginVendedor);

        btMainCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // telaCliente();

                Intent intentMainCliente = new Intent(MainActivity.this, ClienteMain.class);
                startActivity(intentMainCliente);

            }
        });

        btLoginVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // telaVendedor();

                Intent intentLoginVendedor = new Intent(MainActivity.this, Vendedor.class);
                startActivity(intentLoginVendedor);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
