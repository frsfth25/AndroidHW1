package com.farisfath25.android_hw1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private ListView lvItems;

    private ArrayList<String> tdl;
    private ArrayAdapter<String> adapter;

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
        readFromFile(); // initially read from file for activities
    /*
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.todolist));
        try
        {
            if (scan.hasNext())
            {
                while(scan.hasNext()){
                    String line = scan.nextLine();
                    tdl.add(line);
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, "No item found!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException ioe)
        {
            e.printStackTrace();
        }

        scan.close();
    */
        adapter = new ArrayAdapter<String>(this, R.layout.my_item_view, R.id.txtWord, tdl);
        lvItems.setAdapter(adapter);
    }

    public void btnClickAdd(View v)
    {
        EditText newItem = (EditText) findViewById(R.id.input_item);

        String item = newItem.getText().toString();
        if (item.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Activity name cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            adapter.add(adapter.getCount() + "\t" + item);
            //   String name = adapter.getCount()+"\t"+item;
            adapter.notifyDataSetChanged();
            newItem.setText("");
            writeToFile(); //add into the file
        }
    }

    private void readFromFile()
    {
        File fDir = getFilesDir();
        File baseFile = new File(fDir, "base.txt");
        try {
            tdl = new ArrayList<String>(FileUtils.readLines(baseFile));
        } catch (IOException e) {
            tdl = new ArrayList<String>();
        }
    }

    private void writeToFile()
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
        final int pos = index;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Activity")
                .setMessage("Enter the new name:")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        tdl.set(pos, task);
                        adapter.notifyDataSetChanged();

                        writeToFile(); //update the name inside the file
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long id)
    {
        // Remove the item from array at the clicked index
        tdl.remove(index);
        // Tell the adapter for change
        adapter.notifyDataSetChanged();

        writeToFile(); //after deletion, needs to update the foto

        // Return true signals that the operation is executed successfully
        return true;
    }
}
