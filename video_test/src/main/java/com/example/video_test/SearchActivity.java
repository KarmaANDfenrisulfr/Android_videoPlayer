package com.example.video_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;

import static com.example.video_test.IndexActivity.setTranslucentStatus;

public class SearchActivity extends AppCompatActivity {


    private SelfDialog selfDialog;
    private List<SearchListInfo> SearchInfo;
    private RecyclerView SearchList;
    private String account;
    private TextView searchHistorySwitch;
    private EditText searchBar;
    private TextView deleteSearchHistory;
    private TextView cancel;
    private SearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);//沉浸式状态栏
        setContentView(R.layout.activity_search);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        initData();
        LitePal.getDatabase();
        SearchList = (RecyclerView) findViewById(R.id.searchList);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);//列表成3列排列
        SearchList.setLayoutManager(manager);
        adapter = new SearchAdapter(SearchInfo);
        SearchList.setAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //列表的单击、双击事件
        SearchList.addOnItemTouchListener(new RecyclerItemClickListener(this, SearchList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // ...
                SearchListInfo info = SearchInfo.get(position);
                searchBar.setText(info.getSearchHistory());
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
                SearchListInfo info = SearchInfo.get(position);
                deleteSingleHistory(info.getSearchHistory(),position);
                Toast.makeText(SearchActivity.this,"删除成功",Toast.LENGTH_LONG).show();
            }
        }));

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(searchBar.getText().toString()!=null) {
                        addSearchHistory();
                        Intent intent=new Intent(SearchActivity.this, SearchResultActivity.class);
                        intent.putExtra("keyword",searchBar.getText().toString());
                        startActivity(intent);
                        finish();//会关闭掉当前Activity作品完成要finish掉
                    }
                    else{
                        Toast.makeText(SearchActivity.this,"请输入搜索关键字",Toast.LENGTH_LONG).show();
                    }
                }

                return false;
            }
        });
        searchHistorySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchHistorySwitch.getTag().toString().equals("ON")) {
                    ViewGroup.LayoutParams params=SearchList.getLayoutParams();
                    params.height=350;
                    SearchList.setLayoutParams(params);
                    //SearchList.setMinimumHeight();
                    searchHistorySwitch.setTag("OFF");
                    searchHistorySwitch.setText("收起");
                }
                else{
                    ViewGroup.LayoutParams params=SearchList.getLayoutParams();
                    params.height=140;
                    SearchList.setLayoutParams(params);
                    searchHistorySwitch.setTag("ON");
                    searchHistorySwitch.setText("展开");
                }
            }
        });
        deleteSearchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllHistory();
            }
        });
    }

    private void initData(){
        searchHistorySwitch=(TextView)findViewById(R.id.searchHistorySwitch);
        deleteSearchHistory=(TextView)findViewById(R.id.deleteSearchHistory);
        cancel=(TextView)findViewById(R.id.cancel);
        searchBar=(EditText)findViewById(R.id.searchBar);
        account=getIntent().getStringExtra("account");
        SearchInfo=new ArrayList<SearchListInfo>();
        loadSearchHistory();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        private TextView searchHistory;

        public SearchViewHolder(View item){
            super(item);
            searchHistory=(TextView)item.findViewById(R.id.searchHistory);

        }
    }

    public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{
        private List<SearchListInfo> searchInfo;

        public SearchAdapter(List<SearchListInfo> searchInfo) {
            this.searchInfo = searchInfo;
        }

        public void removeData(int position) {
            searchInfo.remove(position);
            //删除某个item
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }

        //删除所有item
        public void clear() {
            int size = this.searchInfo.size();
            this.searchInfo.clear();
            notifyItemRangeRemoved(0, size);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_list_item,
                            parent, false);
            SearchViewHolder holder = new SearchViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
            SearchListInfo info = searchInfo.get(position);
            holder.searchHistory.setText(info.getSearchHistory());
        }

        @Override
        public int getItemCount() {
            return searchInfo.size();
        }
    }

    private void loadSearchHistory(){
        Cursor cursor= LitePal.findBySQL("select * from SearchHistory where account="+account+" order by id desc limit 20");
        // 解析数据
        if( cursor.moveToFirst() ){
            do{
                String searchConnent = cursor.getString(
                        cursor.getColumnIndex("search")
                );
                SearchInfo.add(new SearchListInfo(searchConnent));
            }while(cursor.moveToNext());
        }
    }

    private void addSearchHistory(){
        String str=searchBar.getText().toString();
        List<SearchHistory> find=LitePal.select("search")
                .where("search = ? and account = ?",str,account)
                .find(SearchHistory.class);
        if(find!=null){
            LitePal.deleteAll(SearchHistory.class,"search = ? and account = ?",str,account);
            SearchHistory history=new SearchHistory();
            history.setSearch(str);
            history.setAccount(account);
            history.save();
        }
        else{
            SearchHistory history=new SearchHistory();
            history.setSearch(str);
            history.setAccount(account);
            history.save();
        }
    }

    //清空搜索历史
    private void deleteSingleHistory(String item,int position){
        LitePal.deleteAll(SearchHistory.class,"search = ? and account = ?",item,account);
        adapter.removeData(position);
    }

    //列表里的item点击事件
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mListener;

        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (childView != null && mListener != null) {
                        mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());

            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public void deleteAllHistory() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        selfDialog = new SelfDialog(SearchActivity.this);
        selfDialog.setMessage("清空历史记录");
        selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                LitePal.deleteAll(SearchHistory.class,"account = ?",account);
                adapter.clear();
                selfDialog.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                selfDialog.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        selfDialog.show();
    }



}
