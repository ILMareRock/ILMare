package moe.mzry.ilmare.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Text box
 */
public class PopupTextBox extends EditText {

    private Context context;
    private EventHandler eventHandler;

    public PopupTextBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            eventHandler.onPopupTextBoxBackPressed();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            eventHandler.onPopupTextBoxEnterPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public interface EventHandler {
        void onPopupTextBoxEnterPressed();
        void onPopupTextBoxBackPressed();
    }
}
