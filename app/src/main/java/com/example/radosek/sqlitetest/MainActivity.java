package com.example.radosek.sqlitetest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    DBHelper mydb;
    private ListView obj;
    public static ArrayList<Long> arrayListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this);
        //naplnim pole polozek
        mydb.setAllContacs();
        //ziskam do jedno listu vsechny polozky
        ArrayList arrayList = mydb.getAllContacsName();

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayList);

        obj = (ListView)findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //abych vedel jake id v poli mam hledat
                int id_To_Search = arg2+1;
                Log.d("Clicked item id", " "+ arg2);
                //TODO 2: zavolat aktivitu, ktera bude zobrazovat informace o zaznamu v db a predat ji hledane id zaznamu
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), DisplayContact.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addNew){
            //TODO 1: v menu (pokud je zvoleno vytvoreni noveho itemu) zavolat novou aktivity na pridani kontaktu
            Intent intent = new Intent(getApplicationContext(), DisplayContact.class);
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("id", 0);
            intent.putExtras(dataBundle);
            startActivity(intent);
        }

        if (id == R.id.Delete_All_Contact){
            mydb.removeAll();
            Toast.makeText(getApplicationContext(), "Deleted All Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
