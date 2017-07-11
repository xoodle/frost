package com.studentsearch.xoodle.studentsearch;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.studentsearch.xoodle.studentsearch.adapter.SliderAdapter;
import com.studentsearch.xoodle.studentsearch.cardslider.CardSliderLayoutManager;
import com.studentsearch.xoodle.studentsearch.cardslider.CardSnapHelper;
import com.studentsearch.xoodle.studentsearch.utils.ConstantUtils;
import com.studentsearch.xoodle.studentsearch.utils.MappingUtils;

import java.util.ArrayList;

/**
 * Created by kaushal on 7/7/17.
 */

public class DetailsActivity extends AppCompatActivity {
  private MappingUtils mappingUtils = new MappingUtils();

  ArrayList<StudentData> studentList;
  private SliderAdapter sliderAdapter;
  private CardSliderLayoutManager layoutManger;
  private RecyclerView recyclerView;
  private int currentPosition;

  private TextView name1TextView;
  private TextView name2TextView;
  private int nameOffset1;
  private int nameOffset2;
  private long nameAnimDuration;

  private TextSwitcher yearSwitcher;
  private TextSwitcher deptSwitcher;

  private TextView hallText;
  private TextView rollNo;
  private TextView bloodGroup;
  private TextView userName;
  private TextView address;


  public static Intent getNewIntent(Context context) {
    Intent i = new Intent(context, DetailsActivity.class);
    return i;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    studentList =  getIntent().getParcelableArrayListExtra(ConstantUtils.STUDENT_LIST);
    currentPosition = getIntent().getIntExtra(ConstantUtils.CARD_SLIDER_POSITION, 0);
    sliderAdapter = new SliderAdapter(this, studentList, new OnCardClickListener());
    initRecyclerView();
    initNameText();
    initSwitchers();
    initTextViews();
  }

  private void initRecyclerView() {
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    recyclerView.setAdapter(sliderAdapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          onActiveCardChange();
        }
      }
    });
    layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();
    layoutManger.scrollToPosition(currentPosition);
    new CardSnapHelper().attachToRecyclerView(recyclerView);
  }

  private void initNameText() {
    nameAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
    nameOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
    nameOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
    name1TextView = (TextView) findViewById(R.id.tv_name_1);
    name2TextView = (TextView) findViewById(R.id.tv_name_2);

    name1TextView.setX(nameOffset1);
    name2TextView.setX(nameOffset2);
    name1TextView.setText(studentList.get(currentPosition).getName().toUpperCase());
    name2TextView.setAlpha(0f);

    name1TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
    name2TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
  }

  private void initSwitchers() {
//    yearSwitcher = (TextSwitcher) findViewById(R.id.ts_year);
//    yearSwitcher.setFactory(new TextViewFactory(R.style.TemperatureTextView, true));
//    yearSwitcher.setCurrentText(studentList.get(currentPosition).getYear());

    deptSwitcher = (TextSwitcher) findViewById(R.id.ts_dept);
    deptSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
    deptSwitcher.setCurrentText(studentList.get(currentPosition).getDept().toUpperCase());
  }

  private void initTextViews() {
    hallText = (TextView) findViewById(R.id.tv_hall);
    rollNo = (TextView) findViewById(R.id.tv_roll_no);
    bloodGroup = (TextView) findViewById(R.id.tv_blood_group);
    userName = (TextView) findViewById(R.id.tv_user_name);
    address = (TextView) findViewById(R.id.tv_address);

    StudentData student = studentList.get(currentPosition);
    hallText.setText("\u25CF " + student.getHall());
    rollNo.setText("\u25CF " + student.getRollNo());
    bloodGroup.setText("\u25CF " + student.getBloodGroup());
    userName.setText("\u25CF " + student.getUserName());
    address.setText("\u25CF " + student.getAddress());
  }

  private void setNameText(String text, boolean left2right) {
    final TextView invisibleText;
    final TextView visibleText;
    if (name1TextView.getAlpha() > name2TextView.getAlpha()) {
      visibleText = name1TextView;
      invisibleText = name2TextView;
    } else {
      visibleText = name2TextView;
      invisibleText = name1TextView;
    }

    final int vOffset;
    if (left2right) {
      invisibleText.setX(0);
      vOffset = nameOffset2;
    } else {
      invisibleText.setX(nameOffset2);
      vOffset = 0;
    }

    invisibleText.setText(text);

    final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
    final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
    final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", nameOffset1);
    final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

    final AnimatorSet animSet = new AnimatorSet();
    animSet.playTogether(iAlpha, vAlpha, iX, vX);
    animSet.setDuration(nameAnimDuration);
    animSet.start();
  }

  private void onActiveCardChange() {
    final int pos = layoutManger.getActiveCardPosition();
    if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
      return;
    }
    onActiveCardChange(pos);
  }

  private void onActiveCardChange(int pos) {
    int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
    int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};
    final boolean left2right = pos < currentPosition;
    if (left2right) {
      animH[0] = R.anim.slide_in_left;
      animH[1] = R.anim.slide_out_right;
      animV[0] = R.anim.slide_in_bottom;
      animV[1] = R.anim.slide_out_top;
    }

    StudentData student = studentList.get(pos % studentList.size());

    setNameText(student.getName().toUpperCase(), left2right);

//    yearSwitcher.setInAnimation(DetailsActivity.this, animH[0]);
//    yearSwitcher.setOutAnimation(DetailsActivity.this, animH[1]);
//    String year = student.makeAndGetYear();
//    yearSwitcher.setText(year);

    deptSwitcher.setInAnimation(DetailsActivity.this, animV[0]);
    deptSwitcher.setOutAnimation(DetailsActivity.this, animV[1]);
    deptSwitcher.setText(student.getDept().toUpperCase());

    hallText.setText("\u25CF " + student.getHall());
    rollNo.setText("\u25CF " + student.getRollNo());
    bloodGroup.setText("\u25CF " + student.getBloodGroup());
    userName.setText("\u25CF " + student.getUserName());
    address.setText("\u25CF " + student.getAddress());

    currentPosition = pos;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return true;
  }

  private class OnCardClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      // some action
    }
  }

  private class TextViewFactory implements  ViewSwitcher.ViewFactory {

    @StyleRes
    final int styleId;
    final boolean center;

    TextViewFactory(@StyleRes int styleId, boolean center) {
      this.styleId = styleId;
      this.center = center;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View makeView() {
      final TextView textView = new TextView(DetailsActivity.this);

      if (center) {
        textView.setGravity(Gravity.CENTER);
      }

      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        textView.setTextAppearance(DetailsActivity.this, styleId);
      } else {
        textView.setTextAppearance(styleId);
      }

      return textView;
    }

  }
}
