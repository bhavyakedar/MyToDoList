package com.hfad.mytodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button addTask, shareList;
    EditText task;
    Cursor cursor;
    TextWatcher textWatcher;
    SQLiteDatabase db;
    SQLiteDatabase db_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        final SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(MainActivity.this);
        db = sqLiteOpenHelper.getWritableDatabase();
        db_1 = sqLiteOpenHelper.getReadableDatabase();
        try {
            cursor = db_1.query("TODOLIST", new String[]{"_id", "TASK"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"TASK"},
                    new int[]{android.R.id.text1}, 0);
            listView.setAdapter(cursorAdapter);
            }
        }   catch (SQLiteException e)
        {
            Toast t = Toast.makeText(this,"Database Unavailable",Toast.LENGTH_LONG);
            t.show();
        }
        final AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                String task_name = ((TextView)view).getText().toString();
                task.setText("");
                Intent intent = new Intent(MainActivity.this,TaskActivity.class);
                intent.putExtra("task_name",task_name);
                startActivity(intent);
                finish();
            }
        };
        listView.setOnItemClickListener(itemClickListener);
        shareList = findViewById(R.id.shareList);
        shareList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message="";
                SQLiteOpenHelper sqLiteOpenHelper2 = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db_2 = sqLiteOpenHelper2.getReadableDatabase();
                Cursor cursor2 = db_2.query("TODOLIST", new String[]{"_id", "TASK"}, null, null, null, null, null);
                cursor2.moveToFirst();
                int i=1;
                message = Integer.toString(i)+") "+cursor2.getString(1)+"\n";
                while(cursor2.moveToNext())
                {
                    i++;
                    message = message+Integer.toString(i)+") "+cursor2.getString(1)+"\n";
                }
                Intent intent2 = new Intent(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT,message);
                startActivity(intent2);
            }
        });
        addTask = findViewById(R.id.button);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!task.getText().toString().isEmpty()){
                    addTask.setEnabled(true);
                    addTask.setTextColor(Color.WHITE);
                }
                else
                {
                    addTask.setEnabled(false);
                    addTask.setTextColor(Color.parseColor("#7e7575"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        task.addTextChangedListener(textWatcher);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = (EditText) findViewById(R.id.editText);
                String task_entered = task.getText().toString().trim();
                task.setText("");
                ContentValues contentValues = new ContentValues();
                contentValues.put("TASK",task_entered);
                db.insert("TODOLIST",null, contentValues);
                Cursor cursorNew =  db_1.query("TODOLIST", new String[]{"_id", "TASK"}, null, null, null, null, null);
                CursorAdapter newAdapter = new SimpleCursorAdapter(MainActivity.this,android.R.layout.simple_list_item_1,cursorNew,new String[] {"TASK"}, new int[] {android.R.id.text1});
                listView.setAdapter(newAdapter);
            }
        });
    }
    public void onRestart()
    {
        super.onRestart();
        Cursor cursorNew =  db_1.query("TODOLIST", new String[]{"_id", "TASK"}, null, null, null, null, null);
        CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
        adapter.changeCursor(cursorNew);
        cursor = cursorNew;
    }
}
