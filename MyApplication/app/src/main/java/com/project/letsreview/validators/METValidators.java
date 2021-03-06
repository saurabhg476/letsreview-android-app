package com.project.letsreview.validators;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Patterns;

import com.project.letsreview.R;
import com.rengwuxian.materialedittext.validation.METValidator;

import java.util.regex.Pattern;

/**
 * Created by saurabhgupta on 08/04/17.
 */

public class METValidators {
    private static final String PERSON_NAME_PATTERN = "^([a-zA-Z]{1})([a-zA-Z. ]{0,29})$";
    private static final String PHONE_NUMBER_PATTERN = "^([7-9]{1})([0-9]{9})$";
    private static final String USER_NAME_PATTERN = "^[a-zA-Z0-9]{1,30}$";
    private static final String PASSWORD_PATTERN = "^.{8,30}$";
    private static final String TOPIC_NAME_PATTERN = "^.{1,30}$";
    private static final String REVIEW_BODY_PATTERN = "^.{1,5000}$";
    private static final String TOPIC_SUMMARY_PATTERN ="^.{1,64}$";
    private static final String TOPIC_DESCRIPTION_PATTERN = "^.{1,5000}$";

    private static METValidators instance;

    private METValidator nameValidator;
    private METValidator phoneNoValidator;
    private METValidator emailIdValidator;
    private METValidator usernameValidator;
    private METValidator passwordValidator;
    private METValidator topicNameValidator;
    private METValidator reviewBodyValidator;
    private METValidator topicSummaryValidator;
    private METValidator topicDescriptionValidator;

    public static METValidators getMETValidators(Context context){
        if(instance == null){
            instance = new METValidators(context);
        }
        return instance;
    }


    private METValidators(Context context){
        initialiseNameValidator(context);
    }

    private void initialiseNameValidator(Context context) {
        nameValidator = constructValidator(true, Pattern.compile(PERSON_NAME_PATTERN),context.getString(R.string.error_msg_valid_name));
        phoneNoValidator = constructValidator(false,Pattern.compile(PHONE_NUMBER_PATTERN),context.getString(R.string.error_msg_valid_phone_no));
        emailIdValidator = constructValidator(true,Patterns.EMAIL_ADDRESS,context.getString(R.string.error_msg_valid_email_id));
        usernameValidator = constructValidator(true,Pattern.compile(USER_NAME_PATTERN),context.getString(R.string.error_msg_valid_username));
        passwordValidator = constructValidator(true,Pattern.compile(PASSWORD_PATTERN),context.getString(R.string.error_msg_valid_password));
        topicNameValidator = constructValidator(true,Pattern.compile(TOPIC_NAME_PATTERN),context.getString(R.string.error_msg_valid_topic_name));
        reviewBodyValidator = constructValidator(false,Pattern.compile(REVIEW_BODY_PATTERN),context.getString(R.string.error_msg_valid_review));
        topicSummaryValidator = constructValidator(true,Pattern.compile(TOPIC_SUMMARY_PATTERN),context.getString(R.string.error_msg_valid_summary));
        topicDescriptionValidator = constructValidator(false,Pattern.compile(TOPIC_DESCRIPTION_PATTERN),context.getString(R.string.error_msg_valid_topic_description));
    }

    private METValidator constructValidator(final boolean mandatory, final Pattern pattern, String errorMsg){
        return new METValidator(errorMsg) {

            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                if(mandatory){
                    return !isEmpty && pattern.matcher(text).matches();
                } else{
                    if(isEmpty)
                        return true;
                    else{
                        return pattern.matcher(text).matches();
                    }
                }
            }
        };
    }

    public METValidator getNameValidator() {
        return nameValidator;
    }

    public METValidator getPhoneNoValidator() {
        return phoneNoValidator;
    }

    public METValidator getEmailIdValidator() {
        return emailIdValidator;
    }

    public METValidator getUsernameValidator() {
        return usernameValidator;
    }

    public METValidator getPasswordValidator() {
        return passwordValidator;
    }

    public METValidator getTopicNameValidator() {
        return topicNameValidator;
    }

    public METValidator getReviewBodyValidator(){
        return reviewBodyValidator;
    }

    public METValidator getTopicSummaryValidator(){
        return topicSummaryValidator;
    }

    public METValidator getTopicDescriptionValidator(){
        return topicDescriptionValidator;
    }


}
