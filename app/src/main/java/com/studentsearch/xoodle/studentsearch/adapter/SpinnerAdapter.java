package com.studentsearch.xoodle.studentsearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studentsearch.xoodle.studentsearch.utils.ConstantUtils;
import com.studentsearch.xoodle.studentsearch.utils.MappingUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** Adapter for filter spinners */

public class SpinnerAdapter extends ArrayAdapter<String> {
  private String tag;
  private Map<String, String> map;
  
  public SpinnerAdapter(Context context, int resource, List<String> items, String tag) {
    super(context, resource, items);
    this.tag = tag;
    map = getMapping();
    Collections.sort(items, getComparator());
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    TextView textView = (TextView) super.getView(position, convertView, parent);
    if(map != null) {
      String value = map.get(getItem(position));
      if(value == null) {
        value = getItem(position);
      }
      textView.setText(value);
    }
    return textView;
  }

  @Override
  public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
    if(map != null) {
      String value = map.get(getItem(position));
      if(value == null) {
        value = getItem(position);
      }
      textView.setText(value);
    }
    return textView;
  }

  private Map<String, String> getMapping() {
    MappingUtils mappingUtils = MappingUtils.getInstance();
    switch(tag) {
      case ConstantUtils.HALL:
        return mappingUtils.getHallMap();
      case ConstantUtils.DEPT:
        return mappingUtils.getDeptMap();
      case ConstantUtils.BLOOD_GROUP:
        return mappingUtils.getBloodGroupMap();
      case ConstantUtils.GENDER:
        return mappingUtils.getGenderMap();
      case ConstantUtils.PROGRAMME:
        return mappingUtils.getProgrammeMap();
      case ConstantUtils.YEAR:
        return mappingUtils.getYearMap();
      default:
        return null;
    }
  }

  private Comparator<String> getComparator() {
    switch(tag) {
      case ConstantUtils.HALL:
        return new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            if(o1.equals(""))
              return -1;
            else if(o2.equals(""))
              return 1;

            o1 = ( getMapping().get(o1) != null ) ? (getMapping().get(o1)) : (o1);
            o2 = ( getMapping().get(o2) != null ) ? (getMapping().get(o2)) : (o2);

            if(o1.equals("GH")) return -1;
            if(o2.equals("GH")) return 1;
            if(o1.contains("Hall")) {
              if(o2.contains("Hall")) {
                if(o1.length() == o2.length()) {
                  return o1.compareTo(o2);
                } else {
                  return o1.length() - o2.length();
                }
              } else {
                return -1;
              }
            } else if(o2.contains("HALL")) {
              return 1;
            } else {
              return 0;
            }
          }
        };
      case ConstantUtils.DEPT:
        return new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            if(o1.equals(""))
              return -1;
            else if(o2.equals(""))
              return 1;

            if(getMapping().get(o1) == null && getMapping().get(o2) == null) {
              return o1.compareToIgnoreCase(o2);
            } else if(getMapping().get(o1) != null && getMapping().get(o2) == null) {
              return -1;
            } else if(getMapping().get(o1) == null && getMapping().get(o2) != null) {
              return 1;
            } else {
              return o1.compareToIgnoreCase(o2);
            }
          }
        };
      case ConstantUtils.YEAR:
        return new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            if(o1.equals(""))
              return -1;
            else if(o2.equals(""))
              return 1;

            o1 = ( getMapping().get(o1) != null ) ? (getMapping().get(o1)) : (o1);
            o2 = ( getMapping().get(o2) != null ) ? (getMapping().get(o2)) : (o2);
            return o2.compareTo(o1);
          }
        };
      case ConstantUtils.PROGRAMME:
        return new Comparator<String>() {
          String[] orderedList = {"BTech", "BS", "MSc(2 yr)", "MTech", "MT(Dual)", "PhD"};

          @Override
          public int compare(String o1, String o2) {
            if(o1.equals(""))
              return -1;
            else if(o2.equals(""))
              return 1;

            int index1 = Arrays.asList(orderedList).indexOf(o1);
            int index2 = Arrays.asList(orderedList).indexOf(o2);
            if(index1 < 0 && index2 >= 0) {
              return 1;
            } else if(index1 >=0 && index2 < 0) {
              return -1;
            } else if(index1 < 0 && index2 < 0) {
              return 0;
            } else {
              return index1 - index2;
            }
          }
        };
      default:
        return new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            if(o1.equals(""))
              return -1;
            else if(o2.equals(""))
              return 1;
            else
              return 0;
          }
        };
    }
  }
}
