package com.example.franciscovictor.vendaslinguicao.localizacao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by franciscovictor on 10/25/14.
 */
public class MyStartServiceReceiver extends BroadcastReceiver
{
    String cpf;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent dailyUpdater = new Intent(context, MyService.class);
        Bundle params = intent.getExtras();

        if (params != null)
        {
            cpf = params.getString("cpf");
            Bundle params2 = new Bundle();
            params.putString("cpf", cpf);
            dailyUpdater.putExtras(params);
        }
        context.startService(dailyUpdater);
        //Log.d("AlarmReceiver", "Called context.startService from AlarmReceiver.onReceive");
    }
}