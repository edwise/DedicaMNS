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
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.utils.DayUtils;

public class DayListAdapter extends ArrayAdapter<DayRecord> {
	private final Activity context;
	private final List<DayRecord> days;

	private static int SDK_VERSION = android.os.Build.VERSION.SDK_INT;
	private static int JELLYBEAN_VERSION = android.os.Build.VERSION_CODES.JELLY_BEAN;

	private int currentSelection;

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

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
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
			viewHolder.projectId = (TextView) rowView.findViewById(R.id.projectId);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		DayRecord record = days.get(position);
		holder.dayNum.setText(String.valueOf(record.getDayNum()));
		holder.dayName.setText(record.getDayName());
		holder.hours.setText(record.getHours());
		holder.projectId.setText(DayUtils.getSomeProjectIds(record.getActivities()));

		Drawable drawable = null;
		if (record.getIsHoliday()) {
			// Sky Blue
			drawable = rowView.getResources().getDrawable(R.drawable.selector_holiday);
		} else if (record.getIsWeekend()) {
			// Light Sky Blue
			drawable = rowView.getResources().getDrawable(R.drawable.selector_weekend);
		} else {
			if (record.getActivities().size() == 0) {
				// Azure
				drawable = rowView.getResources().getDrawable(R.drawable.selector_blank);
			} else {
				// Light Cyan
				drawable = rowView.getResources().getDrawable(R.drawable.selector_filled);
			}
		}

		if (SDK_VERSION < JELLYBEAN_VERSION) {
			rowView.setBackgroundDrawable(drawable);
		} else {
			rowView.setBackground(drawable);
		}

		return rowView;
	}

	public int getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(int currentSelection) {
		this.currentSelection = currentSelection;
	}

}
