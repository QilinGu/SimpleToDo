package com.summertree.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    int itemPosition;
    String itemContent;
    EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        itemContent = getIntent().getStringExtra("itemContent");
        itemPosition = getIntent().getIntExtra("position", 0);
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        etEditItem.setText(itemContent);
        etEditItem.setSelection(etEditItem.getText().length());
    }

    public void onSaveItem(View view) {
        Intent data = new Intent();
        data.putExtra("editedItem",etEditItem.getText().toString());
        data.putExtra("itemPosition", itemPosition);
        setResult(RESULT_OK, data);
        finish();
    }
}
