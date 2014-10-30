package com.example.franciscovictor.vendaslinguicao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MyActivity extends ActionBarActivity {

    Button btnMsg;
    Button btnLigar;

    String nomeVendedor = "Testando ";
    String number = "+554898189691";//"+554899016747";//
    String uri = "tel:" + number;
    String uriSms = "smsto:" + number;

    Activity myActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!contactExists(myActivity, number)) {
                    //private EditText mPhoneNumber = (EditText) findViewById(R.id.phone);
                    Intent addIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    addIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    addIntent.putExtra(ContactsContract.Intents.Insert.NAME, nomeVendedor);
                    addIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number);//mPhoneNumber.getText());
                    addIntent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                    addIntent.putExtra("finishActivityOnSaveCompleted", true);
                    startActivity(addIntent);
                //}
            }
        });

        Button btnMsg = (Button) findViewById(R.id.btnMsg);
        btnMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //private EditText mPhoneNumber = (EditText) findViewById(R.id.phone);
                    /*Intent addIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    addIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    addIntent.putExtra(ContactsContract.Intents.Insert.PHONE, Uri.parse(number));//mPhoneNumber.getText());
                    addIntent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                    addIntent.putExtra("finishActivityOnSaveCompleted", true);
                    startActivity(addIntent);*/

                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uriSms));
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
                catch(Exception e){
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriSms));
                    sendIntent.setPackage("com.android.mms");
                    sendIntent.putExtra("sms_body", "Quero comprar um ingresso agora!");
                    startActivity(sendIntent);
                };
            }
        });

        Button btnSms = (Button) findViewById(R.id.btnSms);
        btnSms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriSms));
                sendIntent.setPackage("com.android.mms");
                sendIntent.putExtra("sms_body", "Quero comprar um ingresso agora!");
                startActivity(sendIntent);
            }
        });

        Button btnLigar = (Button) findViewById(R.id.btnLigar);
        btnLigar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_DIAL);
                sendIntent.setData(Uri.parse(uri));
                startActivity(sendIntent);
            }
        });

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

    /*public boolean contactExists(Activity _activity, String number) {
        if (number != null) {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER,
                    ContactsContract.PhoneLookup.DISPLAY_NAME };
            Cursor cur = _activity.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    return true;
                }
            } finally {
                if (cur != null)
                    cur.close();
            }
            return false;
        } else {
            return false;
        }
    }*/
}
