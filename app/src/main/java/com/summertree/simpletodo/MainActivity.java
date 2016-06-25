package com.summertree.simpletodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etNewItem;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }

        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchEditItem(position);
            }
        });


    }

    public void launchEditItem(int position) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra("position", position);
        i.putExtra("itemContent", items.get(position));
        startActivityForResult(i, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String editedItem = data.getExtras().getString("editedItem");
            int itemPosition = data.getExtras().getInt("itemPosition", 0);
            items.set(itemPosition, editedItem);
            itemsAdapter.notifyDataSetChanged();
            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Todo Item Edited", Toast.LENGTH_SHORT).show();
            writeItems();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public void onAdditem(View view) {
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        writeItems();
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            //items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void hideSoftKeyboard(Activity activity) {
        View focusedView = activity.getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }
}