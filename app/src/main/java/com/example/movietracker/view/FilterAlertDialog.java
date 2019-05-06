package com.example.movietracker.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

/**
 * The type Filter alert dialog.
 */
public class FilterAlertDialog {

    private  List<Option> optionList;
    private  AlertDialog alertDialog;

    private OnDoneButtonClickedListener onClickListener;

    private static class SingletonHelper {
        private static final FilterAlertDialog INSTANCE = new FilterAlertDialog();
    }

    public static FilterAlertDialog getInstance(){
        return FilterAlertDialog.SingletonHelper.INSTANCE;
    }

    private FilterAlertDialog() {
    }

    /**
     * Init.
     */
    public void init() {
        pupulateModel();
    }

    /**
     * Show filter alert dialog.
     *
     * @param context       the context
     * @param clickListener the click listener
     */
    public void showFilterAlertDialog(Context context, OnDoneButtonClickedListener clickListener) {
        this.onClickListener = clickListener;
        createFilterDialog(context);
    }

    /**
     * Populating filter alert dialog model with initial data
     */
    private void pupulateModel() {
        optionList = new ArrayList<>();
        for(int i = 0; i< SortBy.values().length; i++) {
            optionList.add(new Option(i == 0,SortBy.values()[i], Order.DESC));
        }
    }

    /**
     *customizing alert dialog, adding custom tittle view and creating dialogBuilder
     */
    private void customizeFilterAlertDialog(View dialogCustomVIew, Context context) {

        AlertDialog.Builder alertDialogBuilder
                = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialogCustomVIew);

        RelativeLayout customTitleView = new RelativeLayout(context);
        TextView textView = new TextView(context);

        customTitleView.setGravity(Gravity.CENTER);
        customTitleView.setBackground(
                context.getResources().getDrawable(R.color.filter_alert_dialog_header_color));
        textView.setText(context.getString(R.string.filter_header_title));
        textView.setPadding(0, 40, 0, 40);
        textView.setTextSize(24);
        customTitleView.addView(textView);

        alertDialogBuilder.setCustomTitle(customTitleView);

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createFilterDialog(Context context) {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogCustomVIew = li.inflate(R.layout.filter_dialog, null);

        customizeFilterAlertDialog(dialogCustomVIew, context);
        initializeFilterDialogContent(dialogCustomVIew, context);
    }


    /**
     *initializing saveScoreButton, radioGroup, switcher in alert dialog and setting listeners to them
     */
    private void  initializeFilterDialogContent(View dialogCustomVIew, Context context) {

        Button saveScoreButton = dialogCustomVIew.findViewById(R.id.button_save_filter);
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

        saveScoreButton.setOnClickListener(v -> onClickListener.onAlertDialogDoneButtonClicked(alertDialog));

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

    /**
     * Dismiss selection in filter dialog.
     */
    public void dismissSelection() {
        for (Option option: optionList) {
            if(option.isSelected()) {
                option.setSelected(false);
            }
            option.setSortOrder(Order.DESC);
        }

        optionList.get(0).setSelected(true);
    }

    /**
     * Gets filter selected options.
     *
     * @return the filter options
     */
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
        void onAlertDialogDoneButtonClicked(AlertDialog alertDialog);
    }
}
