package com.greedygame.newsbreeze.ui.list;

import androidx.lifecycle.LifecycleOwner;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.greedygame.newsbreeze.R;
import com.greedygame.newsbreeze.data.model.Article;
import com.greedygame.newsbreeze.ui.detail.SingletonClass;
import com.greedygame.newsbreeze.util.CommonUtils;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.RepoViewHolder> implements Filterable {

    private ICardClickListener ICardClickListener;
    private List<Article> articleList = new ArrayList<>();
    private static int upvoteCount= 0;
    private int rowIndex = -1;
    private List<Article> filteredNewsList = new ArrayList<>();

    NewsListAdapter(NewsListViewModel viewModel, LifecycleOwner lifecycleOwner, ICardClickListener ICardClickListener, boolean isFromSavedList) {
        this.ICardClickListener = ICardClickListener;

        if(isFromSavedList) {
            articleList.clear();
            articleList.addAll(SingletonClass.getInstance().getArticleList());
            notifyDataSetChanged();
        } else {
            viewModel.getRepos().observe(lifecycleOwner, repos -> {
                articleList.clear();
                filteredNewsList.clear();
                notifyDataSetChanged();
                if (repos != null) {
                    articleList.addAll(repos.getArticles());
                    filteredNewsList.addAll(articleList);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new RepoViewHolder(view, ICardClickListener,filteredNewsList);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        holder.bind(filteredNewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredNewsList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredNewsList = articleList;
                } else {
                    List<Article> filteredList = new ArrayList<>();
                    for (Article article : articleList) {
                        if (article.getTitle().contains(charSequence)) {
                            filteredList.add(article);
                        }
                    }

                    filteredNewsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredNewsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredNewsList = (ArrayList<Article>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }


    final class RepoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_description) TextView tvDescription;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.iv_logo) ImageView ivLogo;
        @BindView(R.id.btnRead) Button btnRead;
        @BindView(R.id.btnSave) Button btnSave;
        @BindView(R.id.ivUpvote) ImageView ivUpvote;

        private Article article;

        RepoViewHolder(View itemView, ICardClickListener ICardClickListener, List<Article> articleList) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if(article != null) {
                    ICardClickListener.onCardClick(article);
                }
            });

            btnRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(article != null) {
                        ICardClickListener.onCardClick(article);
                    }
                }
            });

        }

        void bind(Article article) {
            this.article = article;
            tvTitle.setText(this.article.getTitle());
            tvDate.setText(CommonUtils.getDateFromDateTime("yyyy-MM-dd'T'HH:mm:ss","dd-MM-yy",article.getPublishedAt()));
            tvDescription.setText(this.article.getDescription());
            Glide.with(ivLogo.getContext()).load(this.article.getUrlToImage()).error(R.drawable.ic_error).into(ivLogo);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    article.setSavedState(true);
                    articleList.set(getAdapterPosition(),article);
                    SingletonClass.getInstance().saveNews(article);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
