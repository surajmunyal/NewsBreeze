package com.greedygame.newsbreeze.ui.detail;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.greedygame.newsbreeze.R;
import com.greedygame.newsbreeze.base.BaseFragment;
import com.greedygame.newsbreeze.data.model.Article;
import com.greedygame.newsbreeze.util.CommonUtils;

import java.util.Objects;

import butterknife.BindView;

public class NewsDetailFragment extends BaseFragment {

    private Article article;


    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.ivNewsIcon)
    ImageView ivNewsBanner;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.btnSave)
    TextView btnSave;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvContent)
    TextView tvContent;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_detail;
    }

    public static NewsDetailFragment newInstance(Bundle bundle){
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        newsDetailFragment.setArguments(bundle);
        return newsDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null) {
            article = getArguments().getParcelable("articleDetails");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataToViews();
    }

    private void initDataToViews() {
        tvTitle.setText(article.getTitle());
        tvContent.setText(article.getContent());
        tvDate.setText(CommonUtils.getDateFromDateTime("yyyy-MM-dd'T'HH:mm:ss","dd-MM-yy",article.getPublishedAt()));
        Glide.with(ivNewsBanner.getContext()).load(this.article.getUrlToImage()).error(R.drawable.ic_error).into(ivNewsBanner);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }
}
