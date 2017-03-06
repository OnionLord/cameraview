/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.cameraview.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.cameraview.AspectRatio;


/**
 * A simple dialog that allows user to pick an aspect ratio.
 */
public class JpegQualityFragment extends DialogFragment {

    private static final String ARG_CURRENT_JPEG_Quality = "current_jpeg_quality";

    private Listener mListener;

    public static JpegQualityFragment newInstance(int currentQuality) {
        final JpegQualityFragment fragment = new JpegQualityFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_JPEG_Quality, currentQuality);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final int[] qualities = {10, 20, 30, 40, 50, 60, 70 ,80, 90, 100};

        final int current = args.getInt(ARG_CURRENT_JPEG_Quality);
        final JpegQualityAdapter adapter = new JpegQualityAdapter(qualities, current);
        return new AlertDialog.Builder(getActivity())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        mListener.onJpegQualitySelected(qualities[position]);
                    }
                })
                .create();
    }

    private static class JpegQualityAdapter extends BaseAdapter {

        private final int[] mQualities;
        private final int mCurrentQuality;

        JpegQualityAdapter(int[] ratios, int current) {
            mQualities = ratios;
            mCurrentQuality = current;
        }

        @Override
        public int getCount() {
            return mQualities.length;
        }

        @Override
        public Integer getItem(int position) {
            return mQualities[position];
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            JpegQualityAdapter.ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new JpegQualityAdapter.ViewHolder();
                holder.text = (TextView) view.findViewById(android.R.id.text1);
                view.setTag(holder);
            } else {
                holder = (JpegQualityAdapter.ViewHolder) view.getTag();
            }
            int quality = getItem(position);
            StringBuilder sb = new StringBuilder(Integer.toString(quality));
            if (quality == mCurrentQuality) {
                sb.append(" *");
            }
            holder.text.setText(sb);
            return view;
        }

        private static class ViewHolder {
            TextView text;
        }

    }

    public interface Listener {
        void onJpegQualitySelected(@NonNull int quality);
    }

}
