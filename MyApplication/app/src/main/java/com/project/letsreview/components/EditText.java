package com.project.letsreview.components;

import android.content.Context;
import android.util.AttributeSet;

import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by saurabhgupta on 19/04/17.
 */

public class EditText extends MaterialEditText{

    public EditText(Context context){
        super(context);
    }


    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setError(CharSequence errorText) {
        super.setError(errorText);
        requestFocus();
    }
}
