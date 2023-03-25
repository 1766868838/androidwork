package cn.edu.hznu.sixth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends AppCompatActivity {

    public class Fruit{
        private String name;
        private String cname;
        private int imageId;
        public Fruit(String name,String cname,int imageId){
            this.name = name;
            this.cname = cname;
            this.imageId = imageId;
        }
        public String getName(){
            return name;
        }
        public String getCname(){
            return cname;
        }
        public int getImageId(){
            return imageId;
        }

    }

    public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder>{

        private List<Fruit> mFruitList;
        class ViewHolder extends RecyclerView.ViewHolder {
            View fruitView;
            ImageView fruitImage;
            TextView fruitName;
            TextView fruitCname;

            public ViewHolder(View view) {
                super(view);
                fruitView = view;
                fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
                fruitName = (TextView) view.findViewById(R.id.fruit_name);
                fruitCname = (TextView) view.findViewById(R.id.fruit_cname);
            }
        }
        public FruitAdapter(List<Fruit> fruitList) {
            mFruitList = fruitList;
        }


        @Override
        public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item,parent,
                    false);
            final ViewHolder holder = new ViewHolder(view);
            holder.fruitView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Fruit fruit = mFruitList.get(position);
                    Toast.makeText(view.getContext(), fruit.getName(), Toast.LENGTH_SHORT).
                            show();
                }
            });
            holder.fruitImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Fruit fruit = mFruitList.get(position);
                    Toast.makeText(v.getContext(), "you clicked image of " + fruit.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            holder.fruitCname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Fruit fruit = mFruitList.get(position);
                    Toast.makeText(v.getContext(), fruit.getCname(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            return holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Fruit fruit = mFruitList.get(position);
            holder.fruitImage.setImageResource(fruit.getImageId());
            holder.fruitName.setText(fruit.getName());
            holder.fruitCname.setText(fruit.getCname());
        }
        @Override
        public int getItemCount() {
            return mFruitList.size();
        }


    }
    private List<Fruit> fruitList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFruit();
        Button button = (Button) findViewById(R.id.search);
        EditText editText = (EditText) findViewById(R.id.edit);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        FruitAdapter adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);


        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String str = String.valueOf(editText.getText());
                initFruit();
                List<Fruit> fruitList1 = new ArrayList<>();
                fruitList.stream()
                        .filter(fruit -> fruit.getName().toLowerCase().contains(str.toLowerCase())||
                                fruit.getCname().toLowerCase().contains(str.toLowerCase()))
                        .forEach(fruitList1::add);
                FruitAdapter adapter = new FruitAdapter(fruitList1);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void initFruit(){
        fruitList.clear();
        Fruit apple = new Fruit("Apple", "苹果" ,R.drawable.apple_pic);
        fruitList.add(apple);
        Fruit banana = new Fruit("Banana","香蕉", R.drawable.banana_pic);
        fruitList.add(banana);
        Fruit orange = new Fruit("Orange","橘子", R.drawable.orange_pic);
        fruitList.add(orange);
        Fruit watermelon = new Fruit("Watermelon","西瓜", R.drawable.watermelon_pic);
        fruitList.add(watermelon);
        Fruit pear = new Fruit("Pear","梨子",R.drawable.pear_pic);
        fruitList.add(pear);
        Fruit grape = new Fruit("Grape","葡萄", R.drawable.grape_pic);
        fruitList.add(grape);
        Fruit pineapple = new Fruit("Pineapple","菠萝",R.drawable.pineapple_pic);
        fruitList.add(pineapple);
        Fruit strawberry = new Fruit("Strawberry","草莓",R.drawable.strawberry_pic);
        fruitList.add(strawberry);
        Fruit cherry = new Fruit("Cherry","樱桃",R.drawable.cherry_pic);
        fruitList.add(cherry);
        Fruit mango = new Fruit("Mango","芒果",R.drawable.mango_pic);
        fruitList.add(mango);
    }
}