package com.edwise.dedicamns.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edwise.dedicamns.R;
import com.edwise.dedicamns.beans.DayRecord;

public class DayListAdapter extends ArrayAdapter<DayRecord> {
    private final Activity context;
    private final List<DayRecord> days;

    static class ViewHolder {
	public TextView dayNum;
	public TextView dayName;
	public TextView hours;
	public TextView projectId;
    }

    public DayListAdapter(Activity context, List<DayRecord> days) {
	super(context, R.layout.month_view_row, days);
	this.context = context;
	this.days = days;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View rowView = convertView;
	if (rowView == null) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    rowView = inflater.inflate(R.layout.month_view_row, null);
	    ViewHolder viewHolder = new ViewHolder();
	    viewHolder.dayNum = (TextView) rowView.findViewById(R.id.dayNum);
	    viewHolder.dayName = (TextView) rowView.findViewById(R.id.dayName);
	    viewHolder.hours = (TextView) rowView.findViewById(R.id.hours);
	    viewHolder.projectId = (TextView) rowView
		    .findViewById(R.id.projectId);
	    rowView.setTag(viewHolder);
	}

	ViewHolder holder = (ViewHolder) rowView.getTag();
	DayRecord record = days.get(position);
	holder.dayNum.setText(record.getDayNum() + "");
	holder.dayName.setText(record.getDayName());
	holder.hours.setText(record.getHours());
	holder.projectId.setText(record.getProjectId());

	return rowView;
    }

}
