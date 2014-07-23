package com.special.ResideMenuDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午3:28
 * Mail: specialcyci@gmail.com
 */
public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings, container, false);
    }

    private static final int POS_SINGLE_IMG = 0;
    private static final int POS_MULTI_IMG = 1;
    private static final int POS_FROM_URL = 2;
    private static final int POS_DEFAULT_IMG = 3;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView)getView().findViewById(android.R.id.list);
        if (null != listView) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.main_options, android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    broadcastOption(position);
                }
            });
        }
    }

    public static View getTargetView(LayoutInflater inflater, Intent intent) {
        View view = null;
        int pos = null == intent ? POS_DEFAULT_IMG : intent.getIntExtra(SettingsFragment.class.getSimpleName(), POS_DEFAULT_IMG);
        switch (pos) {
            default:
            case POS_DEFAULT_IMG:
                break;
            case POS_SINGLE_IMG:
                view = inflater.inflate(R.layout.single_image, null);
                break;
            case POS_MULTI_IMG:
                view = inflater.inflate(R.layout.multi_image, null);
                break;
            case POS_FROM_URL:
                view = inflater.inflate(R.layout.from_url, null);
                break;
        }

        return view;
    }


    private void broadcastOption(int position) {
        Intent intent = new Intent();
        intent.setAction(SettingsFragment.class.getSimpleName());
        intent.putExtra(SettingsFragment.class.getSimpleName(), position);
        getActivity().sendBroadcast(intent);
    }

    public static void startListen(Context context, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SettingsFragment.class.getSimpleName());
        context.getApplicationContext().registerReceiver(receiver, filter);
    }

    public static void stopListen(Context context, BroadcastReceiver receiver) {
        context.getApplicationContext().unregisterReceiver(receiver);
    }
}
