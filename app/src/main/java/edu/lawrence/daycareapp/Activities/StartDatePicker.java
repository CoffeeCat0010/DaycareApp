package edu.lawrence.daycareapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.function.Consumer;
import java.util.function.Function;

import edu.lawrence.daycareapp.R;

public class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private int titleRes;
    Consumer<String> callback;


    public StartDatePicker(int itemRes, Consumer<String> dateSetHandler) {
        titleRes = itemRes;
        callback = dateSetHandler;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        DatePickerDialog result = new StartDatePickerDialog(getActivity(), titleRes, this, year, month, day);
        return result;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String result = year + "-" + (month+1) + "-" + dayOfMonth;
        callback.accept(result);
        StartDatePicker.this.dismiss();
    }
}
