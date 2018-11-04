package com.toolazytotype;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.toolazytotype.quid.QuidQuestionAnalyzer;


public class TextExtractorService extends AccessibilityService {

    private static final String LOG_TAG = "TextExtractorService";

    private TextAnalyzer textAnalyzer;
    private Action callbackAction;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source == null || source.getText() == null || !(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)) {
            Log.v(LOG_TAG, "Event rejected in onAccessibilityEvent");
        } else {
            Log.v(LOG_TAG, "Text Detected=" + source.getText());
            textAnalyzer.analyze(source.getText().toString());
        }
    }

    @Override
    public void onInterrupt() {
        Log.e(LOG_TAG,"Service Interrupted: Have never actually had this happen.");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        callbackAction = new TCPSender();
        textAnalyzer = new QuidQuestionAnalyzer(callbackAction);
        Log.i(LOG_TAG,"Accessibility service connected.");
    }
}
