package com.bufferworks.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bufferworks.simpletodo.adapter.ItemAdapter;
import com.bufferworks.simpletodo.entity.Item;
import com.bufferworks.simpletodo.entity.Priority;
import com.bufferworks.simpletodo.entity.Status;
import com.google.common.collect.Lists;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Arrays;

public class SimpleTodoActivity extends AppCompatActivity {

    public static final String EDIT_ACTIVITY = "EDIT_ACTIVITY";
    private final int EDIT_REQUEST_CODE = 20;
    private final int NEW_ITEM_REQUEST_CODE = 30;

    private ArrayList<Item> items = new ArrayList<>();
    private ItemAdapter itemsAdapter;
    private ListView lvItems;
    private boolean showDoneItems = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_todo);
        items = new ArrayList<>();
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ItemAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);

        setupDeleteItemListener();
        setupEditItemListener();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onAddItem(final MenuItem menuItem) {
        final Intent editItemIntent = new Intent(SimpleTodoActivity.this, ItemActivity.class);
        startActivityForResult(editItemIntent, NEW_ITEM_REQUEST_CODE);
    }

    public void showHideItems(final MenuItem menuItem) {
        if (menuItem.isChecked()) {
            showDoneItems = false;
            menuItem.setChecked(false);
        } else {
            showDoneItems = true;
            menuItem.setChecked(true);
        }
        readItems();
        itemsAdapter.notifyDataSetChanged();
    }

    private void setupDeleteItemListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> adapter, final View view, final int position, final long id) {
                final Item item = items.get(position);
                item.delete();
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void setupEditItemListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Intent editItemIntent = new Intent(SimpleTodoActivity.this, ItemActivity.class);
                final Item item = items.get(position);
                editItemIntent.putExtra("itemTitle", item.getTitle());
                editItemIntent.putExtra("itemNotes", item.getNotes());
                editItemIntent.putExtra("itemPriority", item.getPriority().name());
                editItemIntent.putExtra("itemStatus", item.getStatus().name());
                editItemIntent.putExtra("itemPosition", position);
                editItemIntent.putExtra(EDIT_ACTIVITY, EDIT_ACTIVITY);
                startActivityForResult(editItemIntent, EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            final int position = data.getIntExtra("itemPosition", -1);
            if (position >= 0) {
                final Item editedItem = items.get(position);
                editedItem.setTitle(data.getStringExtra("itemTitle"));
                editedItem.setNotes(data.getStringExtra("itemNotes"));
                editedItem.setPriority(Priority.valueOf(data.getStringExtra("itemPriority")));
                editedItem.setStatus(Status.valueOf(data.getStringExtra("itemStatus")));
                editedItem.save();
                itemsAdapter.notifyDataSetChanged();
            }
        } else if (resultCode == RESULT_OK && requestCode == NEW_ITEM_REQUEST_CODE) {
            final String itemTitle = data.getExtras().getString("itemTitle");
            final String itemNotes = data.getExtras().getString("itemNotes");
            final Priority priority = Priority.valueOf(data.getExtras().getString("itemPriority"));
            final Status status = Status.valueOf(data.getExtras().getString("itemStatus"));
            final Item item = new Item(itemTitle, itemNotes, priority, status);
            SugarRecord.saveInTx(Arrays.asList(item));
            items.add(item);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void readItems() {
        try {
            items.clear();
            items.addAll(showDoneItems ?
                    Lists.newArrayList(Select.from(Item.class).orderBy("created asc").list()) :
                    Lists.newArrayList(Select.from(Item.class).where(Condition.prop("status").eq(Status.TODO.name())).orderBy("created asc").list()));
            System.out.println("Items# " + items.size());
        } catch (Exception e) {
            items = new ArrayList<>();
        }
    }
}
