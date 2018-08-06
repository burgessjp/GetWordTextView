package me.solidev.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by _SOLID
 * Date:2016/11/9
 * Time:11:02
 * <p>
 * Desc:A TextView that can get every word in content.
 * </p>
 */

public class GetWordTextView extends android.support.v7.widget.AppCompatTextView {

    private CharSequence mText;
    private BufferType mBufferType;

    private OnWordClickListener mOnWordClickListener;
    private SpannableString mSpannableString;

    private BackgroundColorSpan mSelectedBackSpan;
    private ForegroundColorSpan mSelectedForeSpan;

    private int highlightColor;
    private String highlightText;
    private int selectedColor;
    private int language;//0:english,1:chinese

    public GetWordTextView(Context context) {
        this(context, null);
    }

    public GetWordTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GetWordTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GetWordTextView);
        highlightColor = ta.getColor(R.styleable.GetWordTextView_highlightColor, Color.RED);
        highlightText = ta.getString(R.styleable.GetWordTextView_highlightText);
        selectedColor = ta.getColor(R.styleable.GetWordTextView_selectedColor, Color.BLUE);
        language = ta.getInt(R.styleable.GetWordTextView_language, 0);
        ta.recycle();
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        this.mText = text;
        mBufferType = type;
        setHighlightColor(Color.TRANSPARENT);
        setMovementMethod(LinkMovementMethod.getInstance());
        setText();
    }

    private void setText() {
        mSpannableString = new SpannableString(mText);
        //set highlight text
        setHighLightSpan(mSpannableString);
        //separate word
        if (language == 0) {//deal english
            dealEnglish();
        } else {//deal chinese
            dealChinese();
        }
        super.setText(mSpannableString, mBufferType);

    }

    private void dealChinese() {
        for (int i = 0; i < mText.length(); i++) {
            char ch = mText.charAt(i);
            if (Utils.isChinese(ch)) {
                mSpannableString.setSpan(getClickableSpan(), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void dealEnglish() {
        List<WordInfo> wordInfoList = Utils.getEnglishWordIndices(mText.toString());
        for (WordInfo wordInfo : wordInfoList) {
            mSpannableString.setSpan(getClickableSpan(), wordInfo.getStart(), wordInfo.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
    }


    private void setHighLightSpan(SpannableString spannableString) {
        if (TextUtils.isEmpty(highlightText)) {
            return;
        }
        int hIndex = mText.toString().indexOf(highlightText);
        while (hIndex != -1) {
            spannableString.setSpan(new ForegroundColorSpan(highlightColor), hIndex, hIndex + highlightText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            hIndex += highlightText.length();
            hIndex = mText.toString().indexOf(highlightText, hIndex);
        }
    }

    private void setSelectedSpan(TextView tv) {
        if (mSelectedBackSpan == null || mSelectedForeSpan == null) {
            mSelectedBackSpan = new BackgroundColorSpan(selectedColor);
            mSelectedForeSpan = new ForegroundColorSpan(Color.WHITE);
        } else {
            mSpannableString.removeSpan(mSelectedBackSpan);
            mSpannableString.removeSpan(mSelectedForeSpan);
        }
        mSpannableString.setSpan(mSelectedBackSpan, tv.getSelectionStart(), tv.getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSpannableString.setSpan(mSelectedForeSpan, tv.getSelectionStart(), tv.getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        GetWordTextView.super.setText(mSpannableString, mBufferType);
    }

    public void dismissSelected() {
        mSpannableString.removeSpan(mSelectedBackSpan);
        mSpannableString.removeSpan(mSelectedForeSpan);
        GetWordTextView.super.setText(mSpannableString, mBufferType);
    }

    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                int startIndex = tv.getSelectionStart();
                int endIndex = tv.getSelectionEnd();
                if (TextUtils.isEmpty(getText()) || startIndex == -1 || endIndex == -1) {
                    return;
                }

                String word = tv
                        .getText()
                        .subSequence(startIndex,
                                endIndex).toString();
                setSelectedSpan(tv);

                if (mOnWordClickListener != null) {
                    mOnWordClickListener.onClick(word);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
            }
        };
    }


    public void setOnWordClickListener(OnWordClickListener listener) {
        this.mOnWordClickListener = listener;
    }

    public void setHighLightText(String text) {
        highlightText = text;
    }

    public void setHighLightColor(int color) {
        highlightColor = color;
    }

    public interface OnWordClickListener {
        void onClick(String word);
    }
}
