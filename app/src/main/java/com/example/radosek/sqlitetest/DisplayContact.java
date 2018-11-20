package com.example.radosek.sqlitetest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends Activity {

    private DBHelper mydb;
    TextView name;
    TextView cost;
    Spinner spinner;

    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact2);

        name = (TextView) findViewById(R.id.editTextName);
        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            //ziskam ID ktere se ma editovat/zobrazit/mazat poslane z main aktivity
            int value = extras.getInt("id");
            if (value > 0){
                //z database vytahnu zaznam pod hledanym ID a ulozim do id_To_Update
                Cursor rs = mydb.getData(value);
                id_To_Update = value;
                rs.moveToFirst();

                //vytahnu zaznam se jmenem
                String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));

                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button)findViewById(R.id.buttonSave);
                b.setVisibility(View.INVISIBLE);

                name.setText((CharSequence)nam);
                name.setEnabled(false);
                name.setFocusable(false);
                name.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_contact, menu);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int value = extras.getInt("id");
            if(value>0){
                getMenuInflater().inflate(R.menu.display_contact, menu);
            }
            else{
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //moje zmeny
        Bundle extras = getIntent().getExtras();
        String value = String.valueOf(extras.getInt("id"));

        if (id == R.id.Edit_Contact) {
            Button saveButton = (Button)findViewById(R.id.buttonSave);
            saveButton.setVisibility(View.VISIBLE);

            name.setEnabled(true);
            name.setFocusableInTouchMode(true);
            name.setClickable(true);
        }

        if (id == R.id.Delete_Contact) {
            //TODO odstraneni zaznamu
//            mydb.removeCurrent(value);
            Toast.makeText(getApplicationContext(), value + " Deleted Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    public void saveButtonAction(View view)
    {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int value = extras.getInt("id");
            if(value > 0){
                //TODO update zaznamu
                mydb.updateContact(value, name.getText().toString());
            }
            else{
                //vlozeni zaznamu
                if(mydb.insertContact(name.getText().toString())){
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
