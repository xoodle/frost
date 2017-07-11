package com.studentsearch.xoodle.studentsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import com.studentsearch.xoodle.studentsearch.adapter.SliderAdapter;
import com.studentsearch.xoodle.studentsearch.cardslider.CardSliderLayoutManager;
import com.studentsearch.xoodle.studentsearch.cardslider.CardSnapHelper;
import com.studentsearch.xoodle.studentsearch.utils.ConstantUtils;
import java.util.ArrayList;

/**
 * Created by kaushal on 7/7/17.
 */

public class DetailsActivity extends AppCompatActivity {
  ArrayList<StudentData> studentList;
  private SliderAdapter sliderAdapter;
  private CardSliderLayoutManager layoutManger;
  private RecyclerView recyclerView;
  private int currentPosition;

  public static Intent getNewIntent(Context context) {
    Intent i = new Intent(context, DetailsActivity.class);
    return i;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    studentList =  getIntent().getParcelableArrayListExtra(ConstantUtils.STUDENT_LIST);
    sliderAdapter = new SliderAdapter(this, studentList, new OnCardClickListener());
    initRecyclerView();
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
    new CardSnapHelper().attachToRecyclerView(recyclerView);
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
}
