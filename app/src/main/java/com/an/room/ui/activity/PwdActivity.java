package com.an.room.ui.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.an.room.AppConstants;
import com.an.room.R;
import com.an.room.model.Note;
import com.an.room.util.AppUtils;
import com.an.room.util.NavigatorUtils;

public class PwdActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, AppConstants {

    private TextView toolbarTitle, btnDone;
    private ImageView btnClose;
    private EditText editPwd;

    private Note note;
    private boolean pwdVisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);

        toolbarTitle = findViewById(R.id.title);
        toolbarTitle.setText(getString(R.string.toolbar_pwd));

        btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        editPwd = findViewById(R.id.edit_pwd);
        editPwd.setOnTouchListener(this);

        note = (Note) getIntent().getSerializableExtra(INTENT_TASK);
        AppUtils.openKeyboard(getApplicationContext());
    }


    private void togglePwd() {
        if(!pwdVisible) {
            pwdVisible = Boolean.TRUE;
            editPwd.setTransformationMethod(null);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pwd).mutate();
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.line), PorterDuff.Mode.MULTIPLY));
            editPwd.setCompoundDrawablesWithIntrinsicBounds(null,null, drawable, null);

        } else {
            pwdVisible = Boolean.FALSE;
            editPwd.setTransformationMethod(new PasswordTransformationMethod());
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pwd).mutate();
            drawable.setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY));
            editPwd.setCompoundDrawablesWithIntrinsicBounds(null,null, drawable, null);
        }
        editPwd.setSelection(editPwd.length());
    }



    @Override
    public void onClick(View view) {
        AppUtils.hideKeyboard(this);

        if(view == btnClose) {
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);

        } else if(view == btnDone) {
            //Evaluate the password
            if(note.getPassword().equals(AppUtils.generateHash(editPwd.getText().toString()))) {
                NavigatorUtils.redirectToViewNoteScreen(this, note);

            } else AppUtils.showMessage(getApplicationContext(), getString(R.string.error_pwd));
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(view.getId() == R.id.edit_pwd && event.getRawX() >= (editPwd.getRight() - editPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                togglePwd();
                return true;
            }
        }
        return false;
    }
}
