package com.bufferworks.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    private TextView etEditItem;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (TextView) findViewById(R.id.etEditItem);
        final String editText = getIntent().getStringExtra("itemText");
        itemPosition = getIntent().getIntExtra("itemPosition", -1);
        etEditItem.setText(editText);
    }

    public void onSave(View view) {
        final String editedText = etEditItem.getText().toString();
        final Intent data = new Intent();
        data.putExtra("editedItemText", editedText);
        data.putExtra("itemPosition", itemPosition);
        setResult(RESULT_OK, data);
        finish();
    }
}
