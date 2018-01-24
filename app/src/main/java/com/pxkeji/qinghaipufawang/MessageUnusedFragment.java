package com.pxkeji.qinghaipufawang;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pxkeji.qinghaipufawang.data.Message;
import com.pxkeji.qinghaipufawang.data.adapter.MessageAdapter;
import com.pxkeji.qinghaipufawang.ui.fragment.BaseFragment;
import com.pxkeji.qinghaipufawang.util.HttpUtil;
import com.pxkeji.qinghaipufawang.util.LogUtil;
import com.pxkeji.qinghaipufawang.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/23.
 */

public class MessageUnusedFragment extends BaseFragment {

    private List<Message> mList = new ArrayList<>();

    private MessageAdapter mAdapter;

    private RecyclerView mRecyclerView;

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MessageAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);


    }



    @Override
    public int getLayoutId() {
        return R.layout.fragment_message_unused;
    }

    @Override
    protected void getDataFromServer() {
        getMessages();
    }

    private void getMessages() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int userId = prefs.getInt("user_id", 0);

        String url = StringUtil.addURLParam(Config.USER_API_URL, "action", "query_hongbao_list");
        url = StringUtil.addURLParam(url, "type", "2");
        url = StringUtil.addURLParam(url, "uid", String.valueOf(userId));
        LogUtil.w("message url", url);

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LogUtil.w("message", responseText);

                try{
                    JSONObject responseObject = new JSONObject(responseText);
                    if ("1".equals(responseObject.getString("code"))) {
                        JSONArray messageJsonArray = responseObject.getJSONArray("list");
                        int msgCount = messageJsonArray.length();

                        for (int i = 0; i < msgCount; i++) {
                            JSONObject msgObject = messageJsonArray.getJSONObject(i);
                            Message message = new Message.Builder()
                                    .type(1)
                                    .amount(Double.parseDouble(msgObject.getString("Amount")))
                                    .source(msgObject.getString("RedType"))
                                    .date(msgObject.getString("FailureDate"))
                                    .build();
                            mList.add(message);

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
