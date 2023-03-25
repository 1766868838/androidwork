package cn.edu.hznu.labaddressclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private final List<Address> addressList = new ArrayList<>();
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((this));
        bundle = getIntent().getExtras();
        updateDatabase();
        recyclerView.setLayoutManager(linearLayoutManager);
        listAdapter adapter = new listAdapter(addressList);
        recyclerView.setAdapter(adapter);
    }
    public void updateDatabase(){
        addressList.clear();
        int count = bundle.getInt("count");
        String[] nameList = bundle.getStringArray("name");
        String[] mobileList =bundle.getStringArray("mobile");
        Log.d("Mainactivity", String.valueOf(nameList.length));
        for(int i =0; i < count; i++){

            addressList.add(new Address(nameList[i],mobileList[i]));
        }

    }
    public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder>{
        private final List<Address> mAddressList;
        class ViewHolder extends RecyclerView.ViewHolder{
            View addressView;
            TextView nameText;
            TextView numberText;
            View updownLine;
            public  ViewHolder(View view){
                super(view);
                addressView = view;
                nameText =  view.findViewById(R.id.nameText);
                numberText = view.findViewById(R.id.numberText);
                updownLine = itemView.findViewById(R.id.updownLine);
            }
        }
        public listAdapter(List<Address> addresslist){
            mAddressList = addresslist;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresslist,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Address address = mAddressList.get(position);
            holder.nameText.setText(address.getName());
            holder.numberText.setText(address.getMobile());
            if(position == mAddressList.size()-1) {
                holder.updownLine.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return mAddressList.size();
        }
    }

}