package com.thesim.simsimulator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * HelpFragment contains information to help user get started with SIM Simulator system.
 */
public class HelpFragment extends Fragment {

  View view;

  ImageView ivLogo;
  TextView tvTitle;
  TextView tvContent;

  boolean darkThemeActivated;

  public HelpFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_help, container, false);

    darkThemeActivated = false;

    ivLogo = (ImageView) view.findViewById(R.id.help_logo);

    tvTitle = (TextView) view.findViewById(R.id.help_title);
    tvContent = (TextView) view.findViewById(R.id.help_content);

    ivLogo.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (darkThemeActivated) {
          darkThemeActivated = false;
          view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
          tvTitle.setTextColor(getResources().getColor(R.color.fontDark));
          tvContent.setTextColor(getResources().getColor(R.color.fontDark));
          Toast.makeText(getActivity().getBaseContext(), "Normal theme activated", Toast.LENGTH_SHORT).show();
        } else {
          darkThemeActivated = true;
          view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
          tvTitle.setTextColor(getResources().getColor(R.color.fontLight));
          tvContent.setTextColor(getResources().getColor(R.color.fontLight));
          Toast.makeText(getActivity().getBaseContext(), "Dark mode for help activated", Toast.LENGTH_SHORT).show();
        }
        return true;
      }
    });

    return view;
  }
}
