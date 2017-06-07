package com.awesomethings.demoapp.ui.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.awesomethings.demoapp.R;
import com.awesomethings.demoapp.events.bus.MyEventBus;
import com.awesomethings.demoapp.repository.models.response_models.MarkersDataResponseModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by dev-00 on 6/7/17.
 */

public class CustomFieldItemView extends LinearLayout {

    private MarkersDataResponseModel.Fields fieldModel;

    @BindView(R.id.field_draw_space_layout_id) LinearLayout containerLayout;
    @BindView(R.id.field_label_txt_id) TextView labelTextView;

    public CustomFieldItemView(Context context) {
        super(context);
        init(context);
    }

    public void drawField(final MarkersDataResponseModel.Fields fieldViewModel) {
        this.fieldModel = fieldViewModel;
        if (fieldViewModel != null) {
            FieldType type = FieldType.valueOf(fieldViewModel.getType().toUpperCase());
            labelTextView.setText(fieldViewModel.getName());
            switch (type) {
                case TEXT:
                    final TextView nameLabel = new TextView(getContext());
                    nameLabel.setText(fieldViewModel.getValue());
                    containerLayout.addView(nameLabel);
                    break;
                case SELECT:
                    for (final String it : fieldViewModel.getValue().split(",")) {
                        final CheckBox checkBox = new CheckBox(getContext());
                        checkBox.setText(it);
                        containerLayout.addView(checkBox);
                    }
                    break;
                case RADIO:
                    final RadioGroup radioGroup = new RadioGroup(getContext());
                    radioGroup.setOrientation(HORIZONTAL);
                    for (final String it : fieldViewModel.getValue().split(",")) {
                        final RadioButton rButton = new RadioButton(getContext());
                        rButton.setText(it);
                        radioGroup.addView(rButton);
                    }
                    containerLayout.addView(radioGroup);
                    break;
                case BUTTON:
                    labelTextView.setVisibility(GONE);
                    final Button button = new Button(getContext());
                    button.setText("Submit");
                    containerLayout.addView(button);
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyEventBus.getBus().post(new MyEventBus.FieldsValidCheckerEventModel());
                        }
                    });
                    break;
            }
        }
    }

    private void init(final Context context) {
        final View itemView = LayoutInflater.from(context).inflate(R.layout.custom_filed_item_layout, this);
        ButterKnife.bind(this, itemView);
    }

    public boolean isMandatoryFieldsFilled() throws Exception {
        if (Boolean.parseBoolean(fieldModel.getMandatory())) {
            if (containerLayout.getChildCount() > 0) {
                final View childView = containerLayout.getChildAt(0);
                if (childView instanceof CheckBox) {
                    return isSelectTypeFieldsSelected();
                }else if (childView instanceof RadioButton){
                    return isRadioTypeFieldsSelected();
                }else if (childView instanceof TextView){
                    return isTextTypeFieldFilled();
                }
            }
            throw new Exception("unknown field type");
        }
        return true;
    }

    private boolean isSelectTypeFieldsSelected() {
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            final View view = containerLayout.getChildAt(i);
            final CheckBox checkBox = (CheckBox) view;
            if (checkBox.isChecked()) {
                return true;
            }
        }
        return false;
    }

    private boolean isRadioTypeFieldsSelected() {
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            final View view = containerLayout.getChildAt(i);
            final RadioButton radioButton = (RadioButton) view;
            if (radioButton.isChecked()) {
                return true;
            }
        }
        return false;
    }

    private boolean isTextTypeFieldFilled() {
        if (containerLayout.getChildCount() > 0) {
            final View view = containerLayout.getChildAt(0);
            if (view instanceof TextView) {
                final TextView txtView = (TextView) view;
                return !txtView.getText().toString().isEmpty();
            }
        }
        return false;
    }

    private enum FieldType {
        TEXT("text"),
        SELECT("select"),
        RADIO("radio"),
        BUTTON("button");

        private String type;

        FieldType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}
