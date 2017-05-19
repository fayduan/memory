package cn.duanyufei.memory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.duanyufei.custom.ViewPagerFragment;
import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Motion;
import cn.duanyufei.util.ToastUtil;


public class PartFragment extends ViewPagerFragment {

    final static String TAG = "PartFragment";

    private int pos;

    private static final int DEL_TAG = 0;

    private List<Motion> ml;
    private DBDao dao;
    private ListView lv;
    private long delid;
    private MyAdapter adapter;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        delid = 0;

        pos = getArguments().getInt("pos", -1);

        lv = (ListView) view.findViewById(R.id.list);
        lv.setOnItemClickListener(itemListener);
        lv.setOnItemLongClickListener(itemLongListener);

        dao = DBDao.getInstance();
        ml = dao.findAllMotion();
        adapter = new MyAdapter();
        lv.setAdapter(adapter);

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == DEL_TAG) {
                    ml = dao.findAllMotion();
                    adapter.notifyDataSetChanged();
                }
            }
        };

        return view;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (ml == null) {
                return 0;
            }
            return ml.size();
        }

        @Override
        public Object getItem(int arg0) {
            return ml.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            final Motion motion = ml.get(arg0);
            View view = View.inflate(getActivity(), R.layout.list_motion, null);
            TextView txtMotion = (TextView) view.findViewById(R.id.txt_motion);
            TextView txtGroup = (TextView) view.findViewById(R.id.txt_group);
            TextView txtNumber = (TextView) view.findViewById(R.id.txt_number);
            TextView txtCurWeight = (TextView) view.findViewById(R.id.txt_cur_weight);
            LinearLayout llWeight = (LinearLayout) view.findViewById(R.id.ll_weight);
            txtMotion.setText(motion.getText());
            txtGroup.setText(motion.getGroups() + "");
            txtNumber.setText(motion.getNumber() + "");
            txtCurWeight.setText(motion.getCurWeight() + "");
            llWeight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RecordActivity.class);
                    intent.putExtra("motion", motion);
                    startActivity(intent);
                }
            });
            view.setTag(motion.getId());

            return view;
        }

    }

    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        //点击
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent changeIntent = new Intent();
            changeIntent.setClass(getActivity(), AddMotionActivity.class);
            changeIntent.putExtra("id", ml.get(position).getId());
            getActivity().startActivity(changeIntent);
        }
    };
    AdapterView.OnItemLongClickListener itemLongListener = new AdapterView.OnItemLongClickListener() {
        //长按
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            Motion m = ml.get(arg2);
            delid = m.getId();
            new AlertDialog.Builder(getActivity())
                    .setTitle("某人要弹出来的")
                    .setMessage("是手抖不？")
                    .setPositiveButton("嗯嗯是的", del_cancel)
                    .setNegativeButton("显然不是", del_ok)
                    .show();
            return true;
        }

    };

    public DialogInterface.OnClickListener del_cancel = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(getActivity().getApplicationContext(), "手残了吧...", Toast.LENGTH_SHORT).show();
        }
    };
    public DialogInterface.OnClickListener del_ok = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(getActivity().getApplicationContext(), "已删除", Toast.LENGTH_SHORT).show();
            delete(delid);
        }
    };

    private void delete(final long delId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.deleteMotion(delId);
                Message msg = Message.obtain();
                msg.obj = null;
                msg.what = DEL_TAG;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onShow() {
        ml = dao.findAllMotion();
        if (ml.size() == 0) {
            ToastUtil.show(R.string.msg_nolist);
        }
        adapter.notifyDataSetChanged();
        Log.i(TAG, "onShow: " + pos);
    }

    @Override
    public void onHide() {

    }
}
