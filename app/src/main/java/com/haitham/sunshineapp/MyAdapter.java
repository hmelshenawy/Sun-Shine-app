package com.haitham.sunshineapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class MyAdapter extends  ArrayAdapter<Forcast_List> {

    int pos = 0;


    public MyAdapter(Context context, ArrayList<Forcast_List> weatherList) {

        super(context, R.layout.list_item_layout, weatherList);

    }


    public void selectedItem(int pos) {

        if (this.pos != pos) {

            this.pos = pos;

        }else{

            this.pos = 1000;

        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater myinflator = LayoutInflater.from(getContext());

        if (pos == position){

            View view2 =  myinflator.inflate ( R.layout.list_item3, parent, false );

            TextView tvDay = view2.findViewById ( R.id.tvDay );
            tvDay.setText ( mydate  (position ));

            TextView tvTemp = view2.findViewById ( R.id.tvTempF );
            tvTemp.setText (getItem ( position ).temp + "°C" );

            TextView tv_CurrTemp = view2.findViewById(R.id.tv_currTemp);
            tv_CurrTemp.setText(getItem(position).curr_temp+"°");

            TextView tvHumi = view2.findViewById(R.id.tvhumi);
            tvHumi.setText( getItem ( position ).humidity + "%");

            TextView tvCondition = view2.findViewById(R.id.tvcond);
            tvCondition.setText(getItem(position).condition);

            ImageView img2 = view2.findViewById(R.id.img3);
            condition_Logo(position, view2,R.id.img3);

           Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
           animation.setDuration(300);
           view2.startAnimation(animation);
            return view2;
        }
        else{

        View customView = myinflator.inflate(R.layout.list_item_layout, parent, false);

        TextView myTv = (TextView) customView.findViewById(R.id.list_item_forcast);
        myTv.setText(getItem(position).temp);  //+"°C"

        TextView tvdt = (TextView) customView.findViewById ( R.id.tvDt );
        tvdt.append ( dateModifier ( getItem ( position ).dt_txt ) + "\n");
        tvdt.append ( mydate ( position ) );

        TextView tvHumidity = (TextView) customView.findViewById(R.id.tvHumidity);

        TextView tvCondition = customView.findViewById ( R.id.tv_condition );
        tvCondition.setText ( getItem ( position ). condition );
        tvHumidity.setText ( getItem ( position ). humidity + "%");

        ImageView img = (ImageView) customView.findViewById(R.id.ivcloud);
        condition_Logo(position, customView,R.id.ivcloud);


        return customView;
        }
    }


    public void condition_Logo(int position, View customView, int i) {


        ImageView img =  customView.findViewById(i);


        if (getItem(position).condition.contains("Clear")) {

            img.setImageResource(R.drawable.ic_clear);
        }
        else if (getItem(position).condition.contains("Clouds")) {

            img.setImageResource(R.drawable.ic_light_clouds);
        }
        else if (getItem(position).condition.contains("Cloudy")) {

            img.setImageResource(R.drawable.cloudy);
        }
        else if (getItem(position).condition.contains("Light drizzle")) {

            img.setImageResource(R.drawable.lightdrizzle);
        }
        else if (getItem(position).condition.contains("Light rain shower") ||
                getItem(position).condition.contains("Patchy rain possible")
        ) {
            img.setImageResource(R.drawable.lightrainshower);

        }
        else if (getItem(position).condition.contains("Moderate rain at times") ||
                getItem(position).condition.contains("Moderate or heavy rain shower")||
                getItem(position).condition.contains("Moderate rain")
        ) {
            img.setImageResource(R.drawable.moderate_raintimes);

        }
        else if (getItem(position).condition.contains("Fog") ||
                getItem(position).condition.contains("Mist")

        ) {
            img.setImageResource(R.drawable.fog);

        }
        else if (getItem(position).condition.contains("Overcast")) {

            img.setImageResource(R.drawable.overcast);

        }
        else if (getItem(position).condition.contains("Heavy rain at times") ||
                getItem(position).condition.contains("Rain")
        ) {

            img.setImageResource(R.drawable.heavyrain);


        }
        else if (getItem(position).condition.contains("Thundery outbreaks possible")) {

            img.setImageResource(R.drawable.thundryoutbreak);

        }
        else {
            img.setImageResource(R.drawable.ic_light_rain);
        }
    }


    public String mydate(int i) {
        String dayName = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)+i;


        if (day>7&& day<14){day = day-7;}

        switch (day) {
            case Calendar.SUNDAY:

                dayName = "Sunday";
                break;
            case Calendar.MONDAY:

                dayName = "Monday";
                break;

            case Calendar.TUESDAY:

                dayName = "Tuesday";
                break;

            case Calendar.WEDNESDAY:

                dayName = "Wednesday";
                break;

            case Calendar.THURSDAY:

                dayName = "Thursday";
                break;

            case Calendar.FRIDAY:

                dayName = "Friday";
                break;

            case Calendar.SATURDAY:

                dayName = "Saturday";
                break;

        }
//        if (i==0) dayName="Today";
//        if (i ==1) dayName="Tomorrow";
        return dayName;
    }


    public String dateModifier(String date){

        String sub =date.substring(0,2);
        String month = "";

        switch (sub){

            case "01":
                {
                    month = "Jan";
                }
            break;
            case "02":
            {
                month = "Feb";
            }
            break;
            case "03":
            {
                month = "Mar";
            }
            break;
            case "04":
            {
                month = "Apr";
            }
            break;
            case "05":
            {
                month = "May";
            }
            break;
            case "06":
            {
                month = "Jun";
            }
            break;
            case "07":
            {
                month = "Jul";
            }
            break;
            case "08" :
            {
                month = "Aug";
            }
            break;
            case "09":
            {
                month = "Sep";
            }
            break;
            case "10":
            {
                month = "Oct";
            }
            break;
            case "11":
            {
                month = "Nov";
            }
            break;
            case "12":
            {
                month = "Dec";
            }
            break;
        }



        return month+" " +date.substring(3);
    }


}
