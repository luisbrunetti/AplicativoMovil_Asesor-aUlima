package com.example.alerts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


abstract class BaseAlert extends Dialog {

    BaseAlert(Context context) {
        super(context, R.style.AppTheme_FlatAlert);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(getLayout());
    }

    public abstract int getLayout();

}
