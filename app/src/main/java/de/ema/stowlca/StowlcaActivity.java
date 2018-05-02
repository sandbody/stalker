package de.ema.stowlca;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.Toast;


public class StowlcaActivity extends AppCompatActivity {

    private ListView listView;
    private ContactAdapter customAdapter;
    private ArrayList<Contact> contactModelArrayList;

    public  static final int RequestPermissionCode  = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stowlca);

        EnableRuntimePermission();

        listView = findViewById(R.id.listView);

        contactModelArrayList = new ArrayList<>();

        Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , null
                ,null
                ,null
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Contact contactModel = new Contact();
            contactModel.setName(name);
            contactModel.setNumber(phoneNumber);
            contactModelArrayList.add(contactModel);
//            Log.d("name>>",name+"  "+phoneNumber);
        }
        phones.close();

        customAdapter = new ContactAdapter(this,contactModelArrayList);
        listView.setAdapter(customAdapter);

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                StowlcaActivity.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(StowlcaActivity.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(StowlcaActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(StowlcaActivity.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(StowlcaActivity.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
