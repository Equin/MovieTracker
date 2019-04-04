package com.example.movietracker.view.custom_view;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;

import com.example.movietracker.AndroidApplication;
import com.example.movietracker.R;
import com.example.movietracker.data.net.RestClientImpl;

import java.util.ArrayList;
import java.util.List;


//TODO Q. Или сделать так же как с GenreView типа кастомной View ???
public class FilterAlertDialog {

    enum Order{
        ASC(true),
        DESC(false);

        private boolean val;

        Order(boolean val) {
            this.val = val;
        }

        public boolean getBoolValue() {
            return val;
        }
    }

    private  List<Option> optionList;
    private  AlertDialog alertDialog;
    private  Context context;


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

    public void showFilterAlertDialog(Context context) {
        this.context = context;
        createFilterDialog();
    }

    private void pupulateModel() {
        String[] buttonsName = {"Popularity", "Rating", "Release Date", "Title"};
        optionList = new ArrayList<>();
        for(int i = 0; i< buttonsName.length; i++) {
            optionList.add(new Option(i == 0,buttonsName[i], Order.DESC));
        }
    }

    private void createDialog(View dialogCustomVIew) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme));
        alertDialogBuilder.setView(dialogCustomVIew);
        alertDialogBuilder.setTitle("Sort Movies By");

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
            radioButton.setText(optionList.get(i).getName());
            radioButton.setId(i);

            radioGroup.addView(radioButton);
            if (optionList.get(i).isSelected()) {
                radioGroup.check(radioButton.getId());
                switcher.setChecked(optionList.get(i).getSortOrder().getBoolValue());
            }
        }

        saveScoreButton.setOnClickListener(v -> alertDialog.dismiss());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            for(int i =0; i<optionList.size(); i++) {
                if(checkedId == i) {
                    optionList.get(i).setSelected(true);
                } else {
                    optionList.get(i).setSelected(false);
                }
            }
        });

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Order order = isChecked? Order.ASC : Order.DESC;
               for(Option option : optionList) {
                   option.setSortOrder(order);
               }
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
}
