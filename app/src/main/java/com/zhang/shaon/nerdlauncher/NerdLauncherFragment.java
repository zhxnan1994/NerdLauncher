package com.zhang.shaon.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhang on 2017-11-24.
 */

public class NerdLauncherFragment extends Fragment {

    private static final String TAG = "NerdLauncherFragment";

   RecyclerView mRecyclerView;

   public static NerdLauncherFragment newInstance(){
       return new NerdLauncherFragment();
   }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        mRecyclerView=(RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }

    private void setupAdapter(){
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm=getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            PackageManager pm=getActivity().getPackageManager();
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString());
            }
        });
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
        Log.i(TAG, "Found " + activities.size() + " activities.");
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;
        private ImageView mAppIcon;

        public ActivityHolder(View itemView) {
            super(itemView);
            mNameTextView=(TextView) itemView.findViewById(R.id.app_name);
            mAppIcon=(ImageView) itemView.findViewById(R.id.app_icon);
        }

        public void bindActivity(ResolveInfo resolveInfo) {
            mResolveInfo=resolveInfo;
            PackageManager pm=getActivity().getPackageManager();
            String appNames = mResolveInfo.loadLabel(pm).toString();
            mAppIcon.setImageDrawable(mResolveInfo.loadIcon(pm));
            mNameTextView.setText(appNames);
            mNameTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo=mResolveInfo.activityInfo;

            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName,
                            activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities){
            mActivities=activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_activity_item, parent, false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }

}
