package com.example.alerts;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


@SuppressWarnings("ALL")
public class StandardAlert extends BaseAlert {

    private String titleText;
    private String contentText;
    private int iconResId = -1;

    private String hinText;
    private int inputType;

    private String cancelText;
    private String confirmText;

    private OnClickListener mOnCancel;
    private OnClickListener mOnConfirm;
    private OnClickListenerWithText mOnConfirmWithText;

    private TextView tvTitle;
    private TextView tvContent;
    private EditText etInput;
    private Button btnCancel;
    private Button btnConfirm;
    private ImageView ivIcon;
    private ProgressBar progressBar;

    private AlertType type;

    public StandardAlert(Context context, AlertType type) {
        super(context);
        this.type = type;
    }

    @Override
    public int getLayout() {
        return R.layout.alert_standard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        etInput = (EditText) findViewById(R.id.etInput);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (titleText != null) {
            tvTitle.setText(titleText);
            tvTitle.setVisibility(View.VISIBLE);
        }

        if (contentText != null) {
            tvContent.setText(contentText);
            tvContent.setVisibility(View.VISIBLE);
        }

        if (cancelText != null) {
            btnCancel.setText(cancelText);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnCancel != null) {
                        mOnCancel.onClick(StandardAlert.this);
                    } else {
                        StandardAlert.this.dismiss();
                    }
                }
            });
        }

        if (confirmText != null) {
            btnConfirm.setText(confirmText);
            btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnConfirm != null) {
                        mOnConfirm.onClick(StandardAlert.this);
                    } else if (mOnConfirmWithText != null) {
                        if (etInput.getText().toString().isEmpty()) {
                            Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                            etInput.startAnimation(shake);
                        } else {
                            mOnConfirmWithText.onClick(StandardAlert.this, etInput.getText().toString());
                        }
                    } else {
                        StandardAlert.this.dismiss();
                    }
                }
            });
        }

        if (iconResId != -1) {
            ivIcon.setImageResource(iconResId);
            ivIcon.setVisibility(View.VISIBLE);
        }

        switch (type) {
            case Normal:
                btnConfirm.setBackgroundResource(R.drawable.bg_btn_confirm);
                break;
            case Warning:
                btnConfirm.setBackgroundResource(R.drawable.bg_btn_warning);
                break;
            case InputText:
                etInput.setHint(hinText);
                etInput.setInputType(inputType);
                etInput.setVisibility(View.VISIBLE);
                break;
            case Progress:
                progressBar.setVisibility(View.VISIBLE);
                ivIcon.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                break;
        }

    }

    public StandardAlert setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public StandardAlert setContentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    public StandardAlert setIconResId(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    public StandardAlert setHinText(String hinText) {
        this.hinText = hinText;
        return this;
    }

    public StandardAlert setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }

    public StandardAlert setCancelButton(String cancelText, OnClickListener listener) {
        this.cancelText = cancelText;
        this.mOnCancel = listener;
        return this;
    }

    public StandardAlert setConfirmButton(String confirmText, OnClickListener listener) {
        this.confirmText = confirmText;
        this.mOnConfirm = listener;
        return this;
    }

    public StandardAlert setConfirmButtonWithText(String confirmText, OnClickListenerWithText listener) {
        this.confirmText = confirmText;
        this.mOnConfirmWithText = listener;
        return this;
    }

    public interface OnClickListener {

        void onClick(StandardAlert alert);

    }

    public interface OnClickListenerWithText {

        void onClick(StandardAlert alert, String text);

    }

}
