package com.geniny.noteapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

public class NoteActivity extends AppCompatActivity {

    Bundle bundle;
    TextView title, content, tags;
    Toast toast;
    ImageButton delete;
    Note note = new Note();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        bundle = getIntent().getExtras();
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        tags = findViewById(R.id.tags);
        delete = findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);

        if(bundle == null){
            note = new Note();
            note.tags = "";
            note.content = "";
            note.title = "";
            title.setHint(dateFormat.format(note.date));
            return;
        }
        note = App.getInstance().getDao().getById(bundle.getLong("uid"));
        boolean flag = bundle.getBoolean("flag");
        if(!flag)
            delete.setVisibility(View.VISIBLE);

        title.setText(note.title);
        content.setText(note.content);
        tags.setText(note.tags);

    }

    public void saveClick(View view) {
        note.content = content.getText().toString();
        if(note.content.isEmpty()){
            toast = Toast.makeText(getApplicationContext(), "Заметка пуста", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        note.title = title.getText().toString();
        if(note.title.isEmpty()){
            note.title = dateFormat.format(note.date);
        }
        note.tags = tags.getText().toString();
        if(bundle == null) {
            App.getInstance().getDao().insert(note);
            toast = Toast.makeText(getApplicationContext(), "Заметка добавлена", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
            {
            App.getInstance().getDao().update(note);
                toast = Toast.makeText(getApplicationContext(), "Заметка сохранена", Toast.LENGTH_SHORT);
                toast.show();
        }

        finish();
    }


    @Override
    public void onBackPressed()
    {
        if((!note.content.equals(content.getText().toString()) || !note.title.equals(title.getText().toString()) || !note.tags.equals(tags.getText().toString())))
            openQuitDialog();
        else
            super.onBackPressed();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                  NoteActivity.this);
        quitDialog.setTitle("Заметка не сохранена , вернуться назад?");

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        quitDialog.show();
    }

    public void deleteClick(View view)
    {
        openDeleteDialog();
    }

    private void openDeleteDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                NoteActivity.this);
        quitDialog.setTitle("Вы уверены, что хотите удалить заметку??");

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                App.getInstance().getDao().delete(note);
                Toast.makeText(getApplicationContext(), "Заметка удалена", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        quitDialog.show();
    }
}
