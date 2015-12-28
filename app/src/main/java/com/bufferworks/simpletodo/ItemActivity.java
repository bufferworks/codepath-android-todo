package com.bufferworks.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bufferworks.simpletodo.entity.Priority;
import com.bufferworks.simpletodo.entity.Status;

public class ItemActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etNotes;
    private Spinner prioritySpinner;
    private Spinner statusSpinner;
    private int itemPosition;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        final Spinner spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        final ArrayAdapter<CharSequence> prioritySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        prioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(prioritySpinnerAdapter);

        final Spinner spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        final ArrayAdapter<CharSequence> statusSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusSpinnerAdapter);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etNotes = (EditText) findViewById(R.id.etNotes);
        prioritySpinner = (Spinner) findViewById(R.id.spinnerPriority);
        statusSpinner = (Spinner) findViewById(R.id.spinnerStatus);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(SimpleTodoActivity.EDIT_ACTIVITY)) {
            etTitle.setText(getIntent().getStringExtra("itemTitle"));
            etNotes.setText(getIntent().getStringExtra("itemNotes"));
            prioritySpinner.setSelection(Priority.valueOf(getIntent().getStringExtra("itemPriority")).ordinal());
            statusSpinner.setSelection(Status.valueOf(getIntent().getStringExtra("itemStatus")).ordinal());
            itemPosition = getIntent().getIntExtra("itemPosition", -1);
        } else {
            itemPosition = -1;
        }
    }

    public void onSave(final View view) {
        final String itemTitle = etTitle.getText().toString();
        final String itemNotes = etNotes.getText().toString();
        Priority priority = Priority.LOW;
        Status status = Status.TODO;

        switch (prioritySpinner.getSelectedItemPosition()) {
            case 0: priority = Priority.LOW; break;
            case 1: priority = Priority.MEDIUM; break;
            case 2: priority = Priority.HIGH; break;
        }

        switch(statusSpinner.getSelectedItemPosition()) {
            case 0: status = Status.TODO; break;
            case 1: status = Status.DONE; break;
        }
        final Intent data = new Intent();
        data.putExtra("itemTitle", itemTitle);
        data.putExtra("itemNotes", itemNotes);
        data.putExtra("itemStatus", status.name());
        data.putExtra("itemPriority", priority.name());
        data.putExtra("itemPosition", itemPosition);
        setResult(RESULT_OK, data);
        finish();
    }
}
