package com.example.android.qrscanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    DatabaseHelper Db;

    List<Scan> scanList;
    ListView scan_list_view;

    FloatingActionButton scan_fab;

    Button delete_btn;

    Button view_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        scanList = new ArrayList<>();
        scan_list_view = (ListView) findViewById(R.id.scan_list);

        viewAll();

        //Toast.makeText(this, scanList.get(2).getResult(), Toast.LENGTH_SHORT).show();

        ScansListAdapter adapter = new ScansListAdapter(this,R.layout.custom_layout,scanList);

        scan_list_view.setAdapter(adapter);

        scan_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ScanInfoActivity.class);
                int id = scanList.get(i).getId();
                String result = scanList.get(i).getResult();
                String type = scanList.get(i).getType();
                String date = scanList.get(i).getDate();

                intent.putExtra("result", result);
                intent.putExtra("type",type);
                intent.putExtra("date",date);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        scan_fab = (FloatingActionButton) findViewById(R.id.scan_fab);

        scan_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReaderActivity.class);
                startActivity(intent);
            }
        });
        //addData();

        //deleteData();
    }




   /* public void deleteData(){
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletedRows = myDb.deleteData(id.getText().toString());
                Toast.makeText(MainActivity.this, ""+deletedRows+" rows deleted.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addData(){
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInserted = myDb.insertData(date.getText().toString(),
                        type.getText().toString(),
                        result.getText().toString());
                if(isInserted)
                    Toast.makeText(MainActivity.this, "Insertion successful", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
    public void viewAll(){
        /*view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getAllData();

                if(res.getCount() == 0){
                    //Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    showMessage("Error","No data found.");
                    return;
                }
                else{
                    StringBuffer buffer = new StringBuffer();
                    while(res.moveToNext()){
                        buffer.append("ID: "+res.getString(0) +"\n");
                        buffer.append("Date: "+res.getString(1)+"\n");
                        buffer.append("Type: "+res.getString(2)+"\n");
                        buffer.append("Result: "+res.getString(3)+"\n\n");
                    }
                    //show data
                    showMessage("Data: "+res.getCount(),buffer.toString());
                }
            }
        });
        */
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0){
            showMessage("Error","No data found.");
            return;
        }
        else {
            int i = 0;
            while (res.moveToNext()) {
                scanList.add(new Scan(res.getInt(0), res.getString(2), res.getString(1), res.getString(3)));
                //Toast.makeText(this, "id = "+res.getInt(0), Toast.LENGTH_SHORT).show();
                i++;
            }
        }

    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

