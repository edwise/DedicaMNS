/**
 * 
 */
package com.edwise.dedicamns.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edwise.dedicamns.R;
import com.edwise.dedicamns.beans.ActivityDay;
import com.edwise.dedicamns.utils.DayUtils;

/**
 * @author edwise
 * 
 */
public class ActivityListAdapter extends ArrayAdapter<ActivityDay> {

    private final Activity context;
    private final List<ActivityDay> activities;

    private static int SDK_VERSION = android.os.Build.VERSION.SDK_INT;
    private static int JELLYBEAN_VERSION = android.os.Build.VERSION_CODES.JELLY_BEAN;

    static class ViewHolder {
	public TextView actHours;
	public TextView actProjectId;
	public TextView actSubProjectId;
	public TextView actTask;
    }

    public ActivityListAdapter(Activity context, List<ActivityDay> activities) {
	super(context, R.layout.general_detail_day_row, activities);
	this.context = context;
	this.activities = activities;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View rowView = convertView;
	if (rowView == null) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    rowView = inflater.inflate(R.layout.general_detail_day_row, null);
	    ViewHolder viewHolder = new ViewHolder();
	    viewHolder.actHours = (TextView) rowView.findViewById(R.id.actHours);
	    viewHolder.actProjectId = (TextView) rowView.findViewById(R.id.actProjectId);
	    viewHolder.actSubProjectId = (TextView) rowView.findViewById(R.id.actSubProjectId);
	    viewHolder.actTask = (TextView) rowView.findViewById(R.id.actTask);
	    rowView.setTag(viewHolder);
	}

	ViewHolder holder = (ViewHolder) rowView.getTag();
	ActivityDay activityDay = activities.get(position);
	holder.actHours.setText(String.valueOf(activityDay.getHours()));
	holder.actProjectId.setText(activityDay.getProjectId());
	holder.actSubProjectId.setText(activityDay.getSubProjectId());
	holder.actTask.setText(DayUtils.limitSizeString(activityDay.getTask())); 

	Drawable drawable = null;
	
	if (position == 0 || position % 2 == 0) {
	    drawable = rowView.getResources().getDrawable(R.drawable.selector_filled);
	}
	else {
	    drawable = rowView.getResources().getDrawable(R.drawable.selector_weekend);
	}

	if (SDK_VERSION < JELLYBEAN_VERSION) {
	    rowView.setBackgroundDrawable(drawable);
	} else {
	    rowView.setBackground(drawable);
	}

	return rowView;
    }
}
