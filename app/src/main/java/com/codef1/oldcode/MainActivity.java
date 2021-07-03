package com.codef1.oldcode;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.codef1.oldcode.data.PWDbHelper;
import com.codef1.oldcode.data.PasswordContract;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PWAdapter mAdapter;
    private PWDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.lvPw);
        mDbHelper = new PWDbHelper(this);
        ArrayList<Password> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(PasswordContract.PWEntry.TABLE,
                new String[]{
                        PasswordContract.PWEntry._ID,
                        PasswordContract.PWEntry.COL_PW_NAME,
                        PasswordContract.PWEntry.COL_PW_VALUE,
                }, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(PasswordContract.PWEntry._ID);
            int titleIndex = cursor.getColumnIndex(PasswordContract.PWEntry.COL_PW_NAME);
            int valueIndex = cursor.getColumnIndex(PasswordContract.PWEntry.COL_PW_VALUE);
            arrayList.add(new Password(cursor.getLong(idIndex), cursor.getString(titleIndex), cursor.getString(valueIndex)));
        }
        cursor.close();
        sqLiteDatabase.close();

        mAdapter = new PWAdapter(this, arrayList);
        listView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_pw:
                final LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                final EditText titleEditText = new EditText(this);
                titleEditText.setHint("激活信息");
                linearLayout.addView(titleEditText);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("添加新的激活码")
                        .setView(linearLayout)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = String.valueOf(titleEditText.getText());
                                String[] resCode = title.split("-");
                                //445bcda3-eb5b-4a41-90cf-28573cac2f4b
                                 //4b4fc
                                String value = ""+ resCode[0].charAt(0)+resCode[1].charAt(1)+resCode[2].charAt(2)+resCode[3].charAt(3)+resCode[4].charAt(5);
                                // PERSIST
                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(PasswordContract.PWEntry.COL_PW_NAME, title);
                                values.put(PasswordContract.PWEntry.COL_PW_VALUE, value);
                                long id = db.insertWithOnConflict(PasswordContract.PWEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                // VIEW
                                mAdapter.add(new Password(id, title, value));
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deletePw(View view){
        View parent = (View) view.getParent();
        int position = (int) parent.getTag(R.id.pos);
        Password password = mAdapter.getItem(position);
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        sqLiteDatabase.delete(PasswordContract.PWEntry.TABLE, PasswordContract.PWEntry._ID + " = ? ",
                new String[]{Long.toString(password.getID())});
        sqLiteDatabase.close();
        mAdapter.remove(password);
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvValue;
    }

    private class PWAdapter extends ArrayAdapter<Password> {

        public PWAdapter(@NonNull Context context, ArrayList<Password> data) {
            super(context, R.layout.item, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Password password = getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
                viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
                viewHolder.tvValue = convertView.findViewById(R.id.tvValue);
                convertView.setTag(R.id.tag, viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag(R.id.tag);
            }
            viewHolder.tvTitle.setText(password.getTitle());
            viewHolder.tvValue.setText("激活码：  " + password.getValue());
            convertView.setTag(R.id.pos, position);
            return convertView;
        }
    }


}
