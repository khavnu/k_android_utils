package com.khapv.search_view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.khapv.expandable_layout.R;
import com.khapv.search_view.expandable_layout.HorizontalExpandableLayout;

public class ExpandableSearchView extends LinearLayout implements HorizontalExpandableLayout.OnExpansionUpdateListener {

    private HorizontalExpandableLayout expandableLayout;

    RelativeLayout layoutSearch;
    protected ImageView imgSearch;
    private AutoCompleteTextView edtSearch;

    LinearLayout layoutButtonContainer;

    private OnSearchActionListener searchActionListener;

    public ExpandableSearchView(Context context) {
        super(context);
        initView();
    }

    public ExpandableSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExpandableSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ExpandableSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.expandable_search_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        expandableLayout = findViewById(R.id.expandable_layout);

        layoutSearch = findViewById(R.id.layout_search);
        imgSearch = findViewById(R.id.img_search);
        edtSearch = expandableLayout.findViewById(R.id.edt_search);

        layoutButtonContainer = expandableLayout.findViewById(R.id.layout_button_container);

        imgSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearch.hasFocus()) {
                    edtSearch.setText("");

                    edtSearch.clearFocus();
                    hideKeyboard(edtSearch);
                } else {
                    edtSearch.requestFocus();
                    showKeyboard(edtSearch);
                }
            }
        });

        edtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("CheckFocus", v + ": " + edtSearch + ": " + hasFocus);
                if (v == edtSearch) {
                    if (hasFocus) {
                        expandableLayout.expand(true);
                    } else {
                        expandableLayout.collapse(true);
                    }
                    updateImgSearch(hasFocus);
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchActionListener != null) {
                    searchActionListener.onTextChange(s);
                }

                if (!hasFocus()) {
                    requestFocus();
                    expandableLayout.expand();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onExpansionUpdate(float expansionFraction, int state) {
    }

    private @DrawableRes int closeIconRes = R.drawable.baseline_close_24;
    private @DrawableRes int searchIconRes = R.drawable.baseline_search_24;

    private void updateImgSearch(boolean hasFocus) {
        if (hasFocus) {
            imgSearch.setImageResource(R.drawable.baseline_close_24);
        } else {
            imgSearch.setImageResource(R.drawable.baseline_search_24);
        }
    }

    private void hideKeyboard(View view) {
        if (getContext() != null) {
            if (view != null) {
                try {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch (Exception var3) {
                }
            }

        }
    }

    private void showKeyboard(EditText editText) {
        if (getContext() != null && editText != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, 1);
        }
    }

    public void clearFocusWhenClickOutside() {
        clearFocus();
        hideKeyboard(edtSearch);
    }

    public void addButton(View imageView) {
        layoutButtonContainer.addView(imageView);
        expandableLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                expandableLayout.notifyLayoutButtonContainerChanged(layoutButtonContainer);
            }
        }, 100);
    }

    public void setButtonContainerPadding(int left, int top, int right, int bottom) {
        layoutButtonContainer.setPadding(left, top, right, bottom);
    }

    public boolean isSearching() {
        if (edtSearch.getText().toString().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setHintText(String hintText) {
        edtSearch.setHint(hintText);
    }

    public void setHintText(@StringRes int hintTextResID) {
        edtSearch.setHint(hintTextResID);
    }

    public void setSearchIcon(@NonNull @DrawableRes int _searchIconRes) {
        searchIconRes = _searchIconRes;
    }

    public void setCloseIconResIcon(@NonNull @DrawableRes int _closeSearchIcon) {
        closeIconRes = _closeSearchIcon;
    }

    public void setSearchBoxBg(@DrawableRes int searchBoxBg) {
        layoutSearch.setBackgroundResource(searchBoxBg);
    }

    public void setDuration(int duration) {
        expandableLayout.setDuration(duration);
    }

    public void setOnSearchActionListener(OnSearchActionListener listener) {
        searchActionListener = listener;
    }
}
