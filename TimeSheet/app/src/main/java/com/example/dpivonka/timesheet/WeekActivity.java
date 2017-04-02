package com.example.dpivonka.timesheet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dpivonka on 3/25/17.
 */

public class WeekActivity extends AppCompatActivity {

    private TinyDB tinydb;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        //initalize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        //get data object
        data = (Data) tinydb.getObject("data", Data.class);

        //get username
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("userName");

        //layout features
        ListView lv = (ListView) findViewById(R.id.listView1);
        TextView tv = (TextView) findViewById(R.id.missed_week);
        Button mNextButton = (Button) findViewById(R.id.nextButton);
        final CheckBox mCurrentWeekCheck = (CheckBox) findViewById(R.id.currentWeek);

        //hide missing
        tv.setVisibility(View.GONE);

        //weeks and missed weeks
        ArrayList<Week> weeks = data.ActiveSemester().getWeeks();
        final ArrayList<Integer> missedwWeeks = new ArrayList<>();

        //figure out missed weeks
        int week_num = 1;
        for(Week w: data.ActiveSemester().weeks){
            for(User u: w.employees){
                if(u.getUsername().equals(userName) && u.signed == false){
                    missedwWeeks.add(week_num);
                }
            }
            week_num++;
        }

        //adjust views accordingly
        if(!missedwWeeks.isEmpty()){
            tv.setVisibility(View.VISIBLE);

            Model[] modelItems = new Model[missedwWeeks.size()];

            for(int x = 0; x < missedwWeeks.size(); x++){
                modelItems[x] = new Model("Week "+ missedwWeeks.get(x).toString(), 0);
            }

            CustomAdapter adapter = new CustomAdapter(this, modelItems);
            lv.setAdapter(adapter);
        }

        //next button
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!missedwWeeks.isEmpty()||mCurrentWeekCheck.isChecked()){
                    Intent ii = new Intent(WeekActivity.this, SignActivity.class);
                    if(mCurrentWeekCheck.isChecked()){
                        ii.putExtra("current",true);
                    }else{
                        ii.putExtra("current",false);
                    }
                    ii.putExtra("weeks",missedwWeeks);
                    startActivity(ii);
                }else{
                    Toast.makeText(getApplicationContext(), "Please select a week to sign for", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class Model{
        String name;
        int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */

        Model(String name, int value){
            this.name = name;
            this.value = value;
        }
        public String getName(){
            return this.name;
        }
        public int getValue(){
            return this.value;
        }

    }

    public class CustomAdapter extends ArrayAdapter{
        Model[] modelItems = null;
        Context context;
        public CustomAdapter(Context context, Model[] resource) {
            super(context,R.layout.missed_week_row,resource);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.modelItems = resource;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.missed_week_row, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.textView1);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
            name.setText(modelItems[position].getName());
            if(modelItems[position].getValue() == 1)
                cb.setChecked(true);
            else
                cb.setChecked(false);
            return convertView;
        }
    }
}
