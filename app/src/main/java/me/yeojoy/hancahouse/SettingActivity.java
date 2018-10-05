package me.yeojoy.hancahouse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.util.AlarmUtil;
import me.yeojoy.hancahouse.util.PreferenceHelper;

public class SettingActivity extends AppCompatActivity implements Constants {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private TextView mTextViewTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_setting);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewCrawlerTitle = findViewById(R.id.text_view_crawler_switch_title);
        TextView textViewCrawlerDescription = findViewById(R.id.text_view_crawler_switch_description);
        Switch crawlerSwitch = findViewById(R.id.switch_crawler_start);

        final Group visibilityGroup = findViewById(R.id.group_visibility);

        boolean isCrawlerStatusOn = PreferenceHelper.isCrawlerStatusOn(this);

        visibilityGroup.setVisibility(isCrawlerStatusOn ? View.VISIBLE : View.GONE);
        crawlerSwitch.setChecked(isCrawlerStatusOn);

        textViewCrawlerTitle.setOnClickListener(view -> crawlerSwitch.performClick());
        textViewCrawlerDescription.setOnClickListener(view -> crawlerSwitch.performClick());
        crawlerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            visibilityGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked) {
                startCrawler();
                PreferenceHelper.setCrawlerStatusOn(this);
            } else {
                stopCralwer();
                PreferenceHelper.setCrawlerStatusOff(this);
            }
        });

        mTextViewTime = findViewById(R.id.text_view_time);
        mTextViewTime.setText(getString(R.string.time_duration_formatter, PreferenceHelper.getCrawlerDurationTime(this)));
        mTextViewTime.setOnClickListener(view -> showHourPickerDialog());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startCrawler() {
        AlarmUtil.startCrawlerWithTime(this);
        Toast.makeText(this, R.string.toast_alarm_start, Toast.LENGTH_SHORT).show();
    }

    private void stopCralwer() {
        AlarmUtil.stopCrawler(this);
        Toast.makeText(this, R.string.toast_alarm_stop, Toast.LENGTH_SHORT).show();
    }

    private void restartCrawlerWithTime(int hour) {

        PreferenceHelper.setCrawlerDurationTime(this, hour);

        AlarmUtil.stopCrawler(this);
        AlarmUtil.startCrawlerWithTime(this);

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, getString(R.string.toast_alarm_restart_formatter, hour),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showHourPickerDialog() {
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(TIMER_MIN_HOUR);
        numberPicker.setMaxValue(TIMER_MAX_HOUR);
        numberPicker.setValue(PreferenceHelper.getCrawlerDurationTime(this));

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            Log.d(TAG, "New Value >>>>> " + newVal);
        });

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_hour_picker_title)
                .setView(numberPicker)
                .setPositiveButton(R.string.dialog_hour_picker_ok, (dialog, which) -> {
                    int hour = numberPicker.getValue();
                    restartCrawlerWithTime(hour);
                    mTextViewTime.setText(getString(R.string.time_duration_formatter, hour));
                })
                .setNegativeButton(R.string.dialog_hour_picker_cancel, null)
                .create();

        alertDialog.show();
    }
}
