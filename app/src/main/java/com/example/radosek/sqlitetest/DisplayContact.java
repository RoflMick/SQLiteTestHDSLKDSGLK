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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends Activity {

    private DBHelper mydb;
    TextView name;
    TextView cost;
    Spinner type;

    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact2);

        name = (TextView) findViewById(R.id.editTextName);
        type = (Spinner) findViewById(R.id.spinnerType);
        cost = (TextView) findViewById(R.id.editTextCost);
        mydb = new DBHelper(this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.fuel_types));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(spinnerAdapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                Toast.makeText
                        (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            //ziskam ID ktere se ma editovat/zobrazit/mazat poslane z main aktivity
            int value = extras.getInt("id");

            if (value > 0){
                //z database vytahnu zaznam pod hledanym ID a ulozim do id_To_Update
                Cursor rs = mydb.getData(value);
                id_To_Update = value;
                Toast.makeText(getApplicationContext(), "Clicked on " + value, Toast.LENGTH_SHORT).show();
                rs.moveToFirst();

                //vytahnu zaznam se jmenem
                int idTodelete = rs.getInt(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_ID));

                String strName = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String strType = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TYPE));
                String strCost = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_COST));

                if (!rs.isClosed()) {
                    rs.close();
                }

                Button b = (Button)findViewById(R.id.buttonSave);
                b.setVisibility(View.INVISIBLE);

                name.setText(strName);
                name.setEnabled(false);
                name.setFocusable(false);
                name.setClickable(false);

//                int spinnerPosition = spinnerAdapter.getPosition(String.valueOf(type.getSelectedItem()));
//                Toast.makeText(this, "Selected type" + String.valueOf(type.getSelectedItem()), Toast.LENGTH_SHORT).show();
//                type.setSelection(spinnerPosition);
//                Toast.makeText(this, "Index " + spinnerAdapter.getPosition(type.getSelectedItem().toString()) + " " + type.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                type.setSelection(spinnerAdapter.getPosition(strType));
                type.setEnabled(false);
                type.setFocusable(false);
                type.setClickable(false);

                cost.setText(strCost);
                cost.setEnabled(false);
                cost.setFocusable(false);
                cost.setClickable(false);
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
        int itemId = item.getItemId();

        //moje zmeny
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");

        if (itemId == R.id.Edit_Contact) {
            Button saveButton = (Button)findViewById(R.id.buttonSave);
            saveButton.setVisibility(View.VISIBLE);

            name.setEnabled(true);
            name.setFocusableInTouchMode(true);
            name.setClickable(true);

            type.setEnabled(true);
            type.setFocusableInTouchMode(true);
            type.setClickable(true);

            cost.setEnabled(true);
            cost.setFocusableInTouchMode(true);
            cost.setClickable(true);
        }

        if (itemId == R.id.Delete_Contact) {
            //TODO odstraneni zaznamu
            mydb.removeContact(id);
            Toast.makeText(getApplicationContext(), "Item of id " + id + " successfully deleted", Toast.LENGTH_SHORT).show();
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
                mydb.updateContact(value, name.getText().toString(), String.valueOf(type.getSelectedItem()), cost.getText().toString());
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
            else{
                //vlozeni zaznamu
                if(mydb.insertContact(name.getText().toString(), String.valueOf(type.getSelectedItem()), cost.getText().toString())){
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                    Log.d("insert", name.getText().toString() + " " + String.valueOf(type.getSelectedItem()) + " " + cost.getText().toString());
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
