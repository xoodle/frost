package com.studentsearch.xoodle.studentsearch;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

  private TextSwitcher deptSwitcher;
  private TextSwitcher hallSwitcher;
  private TextSwitcher rollSwitcher;
  private TextSwitcher usernameSwitcher;
  private TextSwitcher bloodGroupSwitcher;
  private TextSwitcher placeSwitcher;

  private String name_fb;
  private String email;

  private ImageView deptImage;

  public static Intent getNewIntent(Context context) {
    Intent i = new Intent(context, DetailsActivity.class);
    return i;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    studentList = SearchResultActivity.studentDataArrayList;
    currentPosition = getIntent().getIntExtra(ConstantUtils.CARD_SLIDER_POSITION, 0);
    sliderAdapter = new SliderAdapter(this, studentList, new OnCardClickListener());
    initRecyclerView();
    initNameText();
    initSwitchers();
    initImages();
    ImageView imageLogo1 = (ImageView)findViewById(R.id.facebook);
    imageLogo1.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        name_fb = (studentList.get(currentPosition).getName());
        String url = "https://m.facebook.com/public/"+name_fb;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
      }
    });
    ImageView imageLogo2 = (ImageView)findViewById(R.id.mail);
    imageLogo2.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        email = studentList.get(currentPosition).getUserName();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email+"@iitk.ac.in" });
        startActivity(Intent.createChooser(intent, ""));
      }
    });
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
    deptSwitcher = (TextSwitcher) findViewById(R.id.ts_dept);
    deptSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
    deptSwitcher.setCurrentText(studentList.get(currentPosition).getDept().toUpperCase());

    hallSwitcher = (TextSwitcher) findViewById(R.id.ts_hall);
    hallSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
    hallSwitcher.setCurrentText(studentList.get(currentPosition).getRoomNo() + ", " + studentList.get(currentPosition).getHall().toUpperCase());

    rollSwitcher = (TextSwitcher) findViewById(R.id.ts_roll_no);
    rollSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
    rollSwitcher.setCurrentText(studentList.get(currentPosition).getRollNo().toUpperCase());

    usernameSwitcher = (TextSwitcher) findViewById(R.id.ts_username);
    usernameSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
    usernameSwitcher.setCurrentText(studentList.get(currentPosition).getUserName() + "@iitk.ac.in");

    bloodGroupSwitcher = (TextSwitcher) findViewById(R.id.ts_blood_group);
    bloodGroupSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
    bloodGroupSwitcher.setCurrentText(studentList.get(currentPosition).getBloodGroup());

    placeSwitcher = (TextSwitcher) findViewById(R.id.ts_place);
    placeSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
    placeSwitcher.setCurrentText(studentList.get(currentPosition).getAddress().toUpperCase());

  }

  private void initImages() {
    deptImage = (ImageView) findViewById(R.id.dept_image);
    deptImage.setImageDrawable(getDrawable(R.drawable.ic_dept));
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

    StudentData student = studentList.get(pos);

    setNameText(student.getName().toUpperCase(), left2right);

    deptImage.setImageDrawable(getDrawable(R.drawable.ic_dept));

    deptSwitcher.setInAnimation(DetailsActivity.this, animV[0]);
    deptSwitcher.setOutAnimation(DetailsActivity.this, animV[1]);
    deptSwitcher.setText(student.getDept().toUpperCase());

    hallSwitcher.setInAnimation(DetailsActivity.this, animV[0]);
    hallSwitcher.setOutAnimation(DetailsActivity.this, animV[1]);
    hallSwitcher.setText(student.getRoomNo() + ", " + student.getHall().toUpperCase());

    rollSwitcher.setInAnimation(DetailsActivity.this, animV[0]);
    rollSwitcher.setOutAnimation(DetailsActivity.this, animV[1]);
    rollSwitcher.setText(student.getRollNo().toUpperCase());

    usernameSwitcher.setInAnimation(DetailsActivity.this, animV[0]);
    usernameSwitcher.setOutAnimation(DetailsActivity.this, animV[1]);
    usernameSwitcher.setText(student.getUserName() + "@iitk.ac.in");

    bloodGroupSwitcher.setInAnimation(DetailsActivity.this, animV[0]);
    bloodGroupSwitcher.setOutAnimation(DetailsActivity.this, animV[1]);
    bloodGroupSwitcher.setText(student.getBloodGroup());

    placeSwitcher.setInAnimation(DetailsActivity.this, animV[0]);
    placeSwitcher.setOutAnimation(DetailsActivity.this, animV[1]);
    placeSwitcher.setText(student.getAddress().toUpperCase());

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
