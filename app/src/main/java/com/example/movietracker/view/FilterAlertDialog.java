package com.example.movietracker.view;

import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import com.example.movietracker.R;
import com.example.movietracker.view.model.Option;
import com.example.movietracker.view.model.Order;
import com.example.movietracker.view.model.SortBy;

import java.util.ArrayList;
import java.util.List;

public class FilterAlertDialog {

    private  List<Option> optionList;
    private  AlertDialog alertDialog;
    private  Context context;
    private OnDoneButtonClickedListener onClickListener;

    private static class SingletonHelper {
        private static final FilterAlertDialog INSTANCE = new FilterAlertDialog();
    }

    public static FilterAlertDialog getInstance(){
        return FilterAlertDialog.SingletonHelper.INSTANCE;
    }

    private FilterAlertDialog() {
    }

    public void init() {
        pupulateModel();
    }

    public void showFilterAlertDialog(Context context, OnDoneButtonClickedListener clickListener) {
        this.context = context;
        this.onClickListener = clickListener;
        createFilterDialog();
    }

    private void pupulateModel() {
        optionList = new ArrayList<>();
        for(int i = 0; i< SortBy.values().length; i++) {
            optionList.add(new Option(i == 0,SortBy.values()[i], Order.DESC));
        }
    }

    private void createDialog(View dialogCustomVIew) {

        AlertDialog.Builder alertDialogBuilder
                = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialogCustomVIew);
        //alertDialogBuilder.setTitle("Sort Movies By");

        RelativeLayout customTitleView = new RelativeLayout(this.context);
        TextView textView = new TextView(this.context);

        customTitleView.setGravity(Gravity.CENTER);
        customTitleView.setBackground(
                this.context.getResources().getDrawable(R.color.filter_alert_dialog_header_color));
        textView.setText("Sort Movies By");
        textView.setPadding(0, 40, 0, 40);
        textView.setTextSize(24);
        customTitleView.addView(textView);

        alertDialogBuilder.setCustomTitle(customTitleView);

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createFilterDialog() {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogCustomVIew = li.inflate(R.layout.filter_dialog, null);

        createDialog(dialogCustomVIew);
        initializeFilterDialogContent(dialogCustomVIew);
    }

    private void  initializeFilterDialogContent(View dialogCustomVIew) {

        Button saveScoreButton = dialogCustomVIew.findViewById(R.id.button_save_score);
        RadioGroup radioGroup = dialogCustomVIew.findViewById(R.id.radioButtonsGroup);
        Switch switcher = dialogCustomVIew.findViewById(R.id.switch1);
        radioGroup.removeAllViews();

        for (int i=0; i< optionList.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(optionList.get(i).getSortBy().getDisplayName());
            radioButton.setId(i);

            radioGroup.addView(radioButton);
            if (optionList.get(i).isSelected()) {
                radioGroup.check(radioButton.getId());
                switcher.setChecked(optionList.get(i).getSortOrder().getBoolValue());
            }
        }

        saveScoreButton.setOnClickListener(v -> onClickListener.OnAlertDialogDoneButtonClicked(alertDialog));

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            for(int i =0; i<optionList.size(); i++) {
                if(checkedId == i) {
                    optionList.get(i).setSelected(true);
                } else {
                    optionList.get(i).setSelected(false);
                }
            }
        });

        switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Order order = isChecked? Order.ASC : Order.DESC;
           for(Option option : optionList) {
               option.setSortOrder(order);
           }
        });
    }

    public void dismissSelection() {
        for (Option option: optionList) {
            if(option.isSelected()) {
                option.setSelected(false);
            }
            option.setSortOrder(Order.DESC);
        }

        optionList.get(0).setSelected(true);
    }

    public Option getFilterOptions() {
        Option option = new Option();
        for(Option option1: optionList) {
            if (option1.isSelected()) {
                option = option1;
            }
        }
        return option;
    }

    public interface OnDoneButtonClickedListener {
            void OnAlertDialogDoneButtonClicked(AlertDialog alertDialog);
    }
}
