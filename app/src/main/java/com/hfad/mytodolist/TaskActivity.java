package com.hfad.mytodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TaskActivity extends AppCompatActivity {
    Button yes, cancel, save;
    String task_name;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        save = findViewById(R.id.save);
        Intent intent = getIntent();
        task_name = intent.getStringExtra("task_name");
        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(TaskActivity.this);
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        editText = findViewById(R.id.editText);
        editText.setText(task_name);
        Cursor cursor = db.query("TODOLIST",new String[] {"_id","TASK","DESCRIPTION"},"TASK = ?",new String[] {task_name},null,null,null);
        if(cursor.moveToFirst()) {
            String description_1 = cursor.getString(2);
            editText.setText(description_1);
        }
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editText.getText().toString().isEmpty())
                {
                    save.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(TaskActivity.this);
                SQLiteDatabase db_2 = sqLiteOpenHelper.getWritableDatabase();
                String description = editText.getText().toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put("DESCRIPTION",description);
                db_2.update("TODOLIST",contentValues,"TASK = ?",new String[] {task_name});
            }
        });
        editText.addTextChangedListener(textWatcher);
        yes = findViewById(R.id.yes);
        cancel = findViewById(R.id.cancel);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(TaskActivity.this);
                SQLiteDatabase db_1 = sqLiteOpenHelper.getWritableDatabase();
                db_1.delete("TODOLIST","TASK = ?",new String[] {task_name});
                Intent intent1 = new Intent(TaskActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TaskActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
