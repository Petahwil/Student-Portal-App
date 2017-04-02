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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dpivonka on 3/25/17.
 */

public class WeekActivity extends AppCompatActivity {

    private TinyDB tinydb;
    private Data data;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEmployeeDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        //initalize firebase reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        //initalize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        //get data object
        data = (Data) tinydb.getObject("data", Data.class);

        //get username
        Bundle bundle = getIntent().getExtras();
        final String userName = bundle.getString("userName");

        //layout features
        final ListView lv = (ListView) findViewById(R.id.listView1);
        TextView tv = (TextView) findViewById(R.id.missed_week);
        Button mNextButton = (Button) findViewById(R.id.nextButton);
        final CheckBox mCurrentWeekCheck = (CheckBox) findViewById(R.id.currentWeek);

        //hide missing
        tv.setVisibility(View.GONE);

        //weeks and missed weeks
        final ArrayList<Integer> checkedWeeks = new ArrayList<>();
        final ArrayList<Integer> missedwWeeks = new ArrayList<>();

        //check what week it is and update if nessasary
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date endDate = null;
        try {
            endDate = sdf.parse(data.ActiveSemester().weeks.get(data.ActiveSemester().currentWeek).getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(endDate)) {
            for(int x = data.ActiveSemester().currentWeek; x < data.ActiveSemester().weeks.size(); x++){
                try {
                    endDate = sdf.parse(data.ActiveSemester().weeks.get(x).getEndDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(new Date().before(endDate)){
                    data.ActiveSemester().setCurrentWeek(x);

                    //update tiny db
                    tinydb.putObject("data", data);

                    //push changes to firebase
                    mEmployeeDatabaseReference.child("Data").setValue(data);

                    break;
                }
            }
        }

        //detect if current weeh has been signed already and prevent from signing for it again
        boolean currentSigned = false;
        for(User u: data.ActiveSemester().weeks.get(data.ActiveSemester().currentWeek).employees){
            if(u.getUsername().equals(userName)){
                if(u.isSigned()){
                    currentSigned = true;
                    mCurrentWeekCheck.setChecked(false);

                    mCurrentWeekCheck.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mCurrentWeekCheck.isChecked()){
                                Toast.makeText(getApplicationContext(), "Already signed for current week", Toast.LENGTH_SHORT).show();
                                mCurrentWeekCheck.setChecked(false);
                            }
                        }
                    });
                }
                break;
            }
        }

        //figure out missed weeks
        int week_num = 1;
        for (int x = 0; x < data.ActiveSemester().getCurrentWeek(); x++){
            for(User u: data.ActiveSemester().weeks.get(x).employees){
                if(u.getUsername().equals(userName) && u.signed == false){
                    missedwWeeks.add(week_num);
                }
            }
            week_num++;
        }

        //tell user they have nothing to sign for
        if(currentSigned && missedwWeeks.isEmpty()){
            Intent intent = new Intent(WeekActivity.this, MainActivity.class);
            Toast.makeText(getApplicationContext(), "Nothing To Sign For", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }

        //adjust views accordingly
        final Model[] modelItems = new Model[missedwWeeks.size()];
        final CustomAdapter adapter = new CustomAdapter(this, modelItems);

        if(!missedwWeeks.isEmpty()){
            tv.setVisibility(View.VISIBLE);

            for(int x = 0; x < missedwWeeks.size(); x++){
                modelItems[x] = new Model("Week "+ missedwWeeks.get(x).toString(), 0);
            }

            lv.setAdapter(adapter);
        }

        //next button
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get checked weeks
                for(int x = 0; x < missedwWeeks.size(); x++){
                    View vi = (View)lv.getChildAt(x);
                    CheckBox c = (CheckBox) vi.findViewById(R.id.checkBox1);
                    if(c.isChecked()){
                        checkedWeeks.add(x+1);
                        System.out.println(x+1);
                        System.out.println("added to list");
                    }
                }

                if(!checkedWeeks.isEmpty()||mCurrentWeekCheck.isChecked()){
                    Intent ii = new Intent(WeekActivity.this, SignActivity.class);
                    if(mCurrentWeekCheck.isChecked()){
                        ii.putExtra("current",true);
                    }else{
                        ii.putExtra("current",false);
                    }
                    ii.putIntegerArrayListExtra("weeks", checkedWeeks);
                    ii.putExtra("userName",userName);
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
