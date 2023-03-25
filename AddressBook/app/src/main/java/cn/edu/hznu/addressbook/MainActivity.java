package cn.edu.hznu.addressbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"addressList",null,1);
    private List<Address> addressList = new ArrayList<>();
    GloablId gloablId = new GloablId(); //用来确保新建数据时id唯一
    public RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager((this));
    listAdapter adapter; //多个方法调用，直接塞到全局变量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateDatabase(); //更新addressList
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new listAdapter(addressList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity","onResume");
        super.onResume();
        updateDatabase(); //更新addressList
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    public void updateDatabase(){
        addressList.clear();
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("contact",null,null,null,null,null,null);
        if(cursor !=null){
            while(cursor.moveToNext()){
                int  id =  cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
                Log.d("MainActivity", "id:" + id);
                Log.d("MainActivity", "name:" + name);
                Log.d("MainActivity", "mobile:" + mobile);
                gloablId.setId(id+1);
                addressList.add(new Address(name,mobile));
            }
            cursor.close();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder>{
        private List<Address> mAddressList;
        class ViewHolder extends RecyclerView.ViewHolder{
            View addressView;
            CheckBox cheBtn;
            TextView nameText;
            TextView numberText;
            public  ViewHolder(View view){
                super(view);
                addressView = view;
                cheBtn =  view.findViewById(R.id.che);
                nameText =  view.findViewById(R.id.nam);
                numberText = view.findViewById(R.id.num);
            }
        }
        public listAdapter(List<Address> addresslist){
            mAddressList = addresslist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresslist,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.nameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+holder.numberText.getText().toString()));
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Address address = mAddressList.get(position);
            holder.nameText.setText(address.getName());
            holder.numberText.setText(address.getMobile());
        }

        @Override
        public int getItemCount() {
            return mAddressList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                break;

            case R.id.del:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                for (int i = recyclerView.getChildCount()-1; i >=0; i--) {

                    Log.d("MainActivity", String.valueOf(recyclerView.getChildAt(i)));
                    LinearLayout layout = (LinearLayout) recyclerView.getChildAt(i);
                    CheckBox cheBox = layout.findViewById(R.id.che);
                    TextView textView = layout.findViewById(R.id.nam);
                    if(cheBox.isChecked()){
                        db.delete("contact","name=?",new String[]{String.valueOf(textView.getText())});
                        addressList.remove(i);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemChanged(0,addressList.size());
                    }
                }

                break;
            default:
        }
        return true;
    }
}