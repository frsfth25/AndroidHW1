package com.farisfath25.android_hw1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/*
Faris Fathurrahman - 14050141015
HW#1
CENG427 - Mobile Programming Devices
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private ListView lvItems;

    private ArrayList<String> tdl;
    private ArrayAdapter<String> adapter;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String formattedDate = df.format(c.getTime());

    //private int followCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = findViewById(R.id.lv_main);
        lvItems.setOnItemClickListener(this);
        lvItems.setOnItemLongClickListener(this);

        tdl = new ArrayList<>();
        init();
    }

    private void init()
    {
        fileRead(); // initially read from file for activities

        adapter = new ArrayAdapter<>(this, R.layout.my_item_view, R.id.txtWord, tdl);
        lvItems.setAdapter(adapter);
    }

    public void btnClickAdd(View v)
    {
        EditText newItem = findViewById(R.id.input_item);

        String item = newItem.getText().toString();
        item = item.trim();
        if (item.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Activity name cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            adapter.add(item);

            adapter.notifyDataSetChanged();

            fileWrite(); //save into the file

            newItem.setText("");
        }
    }

    private void fileRead()
    {
        File fDir = getFilesDir();
        File baseFile = new File(fDir, "base.txt");
        try {
            tdl = new ArrayList<>(FileUtils.readLines(baseFile));
        } catch (IOException e) {
            tdl = new ArrayList<>();
        }
    }

    private void fileWrite()
    {
        File fDir = getFilesDir();
        File baseFile = new File(fDir, "base.txt");
        try {
            FileUtils.writeLines(baseFile, tdl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int index, long id)
    {
        final EditText taskEditText = new EditText(this);
        taskEditText.setText(adapter.getItem(index).toString());
        final int pos = index;

        //final String theText = lvItems.getItemAtPosition(index).toString();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_edit)
                .setTitle("Edit Activity")
                .setMessage("Enter the new name:")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        task = task.trim();
                        if (task.isEmpty())
                        {
                            Toast.makeText(MainActivity.this, "No change was made", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            tdl.set(pos, task);
                            adapter.notifyDataSetChanged();

                            fileWrite(); //update the name inside the file

                            Toast.makeText(MainActivity.this, "Name is successfully changed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long id)
    {
        final int pos = index;

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Deletion Check")
                .setMessage("Sure to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from array at the clicked index
                        tdl.remove(pos);
                        // Tell the adapter for change
                        adapter.notifyDataSetChanged();

                        fileWrite(); //after deletion, needs to update the file

                        Toast.makeText(MainActivity.this, "Activity has been successfully deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();

        // Return true signals that the operation is executed successfully
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_lock_power_off)
                .setTitle("Closing Check")
                .setMessage("Really quit app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
