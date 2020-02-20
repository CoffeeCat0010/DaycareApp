package edu.lawrence.daycareapp.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class StartDatePickerDialog extends DatePickerDialog {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public StartDatePickerDialog(@NonNull Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public StartDatePickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setTitle(themeResId);
    }

    public StartDatePickerDialog(@NonNull Context context, @Nullable OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);
    }

    public StartDatePickerDialog(@NonNull Context context, int themeResId, @Nullable OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, themeResId, listener, year, monthOfYear, dayOfMonth);
        setTitle(themeResId);
    }
}
