package com.mikhail.sportsnewshistoryrecords.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikhail.sportsnewshistoryrecords.R;
import com.mikhail.sportsnewshistoryrecords.adapters.LeaguesNewsAdapter;
import com.mikhail.sportsnewshistoryrecords.api.NytSearchAPI;
import com.mikhail.sportsnewshistoryrecords.fragments.details_fragment.LeaguesDetailViewFragment;
import com.mikhail.sportsnewshistoryrecords.interfaces.LeaguesActivityControl;
import com.mikhail.sportsnewshistoryrecords.model.search.ArticleSearch;
import com.mikhail.sportsnewshistoryrecords.model.search.Doc;
import com.mikhail.sportsnewshistoryrecords.model.search.Multimedia;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mikhail on 4/28/16.
 */
public class LeaguesFragment extends Fragment {
    private int mFragmentType;
    public RecyclerView recyclerView;
    public ArrayList<Doc> searchSportsResults;
    protected SwipeRefreshLayout swipeContainer;
    public static final String NYT_HOCKEY = "Hockey";
    public static final String NYT_MLS = "MLS";
    public static final String NYT_FOOTBALL = "NFL Football";
    public static final String NYT_BASKETBALL = "NBA Basketball";
    public static final String NYT_BASEBALL = "Baseball";
    public static final String NYT_SPANISH = "Spanish soccer La liga";
    public static final String NYT_ENGLISH = "English Premier league";
    public static final String NYT_ITALIAN = "Italian Soccer Serie A";
    public static final String NYT_GERMAN = "Bundesliga";

    LeaguesNewsAdapter leaguesNewsAdapter;
//    NewsDetailsFragment newsDetailsFragment;
    View v;
    ArticleSearch nytSportsSearch;
    LeaguesDetailViewFragment sportsLeaguesArticleDetailViewFragment;
    LeaguesActivityControl leaguesActivityControl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.recycleview_activity_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle_view);

        searchSportsResults = new ArrayList<>();

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        setPullRefresh();

        leaguesNewsAdapter = new LeaguesNewsAdapter(searchSportsResults);
//        italianSoccerSearch();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        leaguesFragmentSetOnItemClickListener();

        return v;
    }


    public void leaguesFragmentSetOnItemClickListener() {



        if (leaguesNewsAdapter != null) {
            leaguesNewsAdapter.setOnItemClickListener(new LeaguesNewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    searchSportsResults.get(position);
                    String imageUrl;
                    imageUrl = "";

                    Multimedia[] multimedias = searchSportsResults.get(position).getMultimedia();
                    if (multimedias != null && multimedias.length > 0){
                        imageUrl = multimedias[0].getUrl();
                    }
                    Bundle article = new Bundle(); //will bundle the 5 fields of articleSearchObjects in a string array
                    String[] articleDetails = {searchSportsResults.get(position).getHeadline().getMain(),
                            searchSportsResults.get(position).getWeb_url(),
                            imageUrl,
                            searchSportsResults.get(position).getLead_paragraph()};
                    article.putStringArray("searchedArticle", articleDetails);


                    leaguesActivityControl.showFragment(article);

//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    intent.putExtras(article);
//                    getActivity().startActivity(intent);

                    // todo all this code below goes into LeaguesActivity and will be called when
                    // todo interface pases the bundle in

                }
            });
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try { leaguesActivityControl = (LeaguesActivityControl) getActivity();

        }catch (ClassCastException ex ){
            throw ex;
        }
    }

    private void setPullRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiCall();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.darker_gray,
                android.R.color.white);

    }

    private void italianSoccerSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_ITALIAN);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {

                ArticleSearch nytSportsSearch = response.body();
                if (nytSportsSearch == null) {
                    return;
                }
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

//                ArticleSearch nytSportsSearch = response.body();
//
//                if (nytSportsSearch == null) {
//                    return;
//                }
//                searchSportsResults.clear();
//                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                handleData(nytSportsSearch.getResponse().getDocs());
//                //                leaguesNewsAdapter.updateData(searchSportsResults);

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }

    private void spanishSoccerSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_SPANISH);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
//                handleData(nytSportsSearch.getResponse().getDocs());
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                leaguesNewsAdapter.updateData(searchSportsResults);
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }

    private void mlsSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_MLS);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
//                handleData(nytSportsSearch.getResponse().getDocs());
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                leaguesNewsAdapter.updateData(searchSportsResults);
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }
    private void bundesligaSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_GERMAN);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
//                handleData(nytSportsSearch.getResponse().getDocs());
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                leaguesNewsAdapter.updateData(searchSportsResults);
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }

    private void nbaSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_BASKETBALL);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
                setPullRefresh();
//                handleData(nytSportsSearch.getResponse().getDocs());
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                leaguesNewsAdapter.updateData(searchSportsResults);
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }

    private void footballSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_FOOTBALL);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
//                handleData(nytSportsSearch.getResponse().getDocs());
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                leaguesNewsAdapter.updateData(searchSportsResults);
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }

    private void baseballSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_BASEBALL);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
//                handleData(nytSportsSearch.getResponse().getDocs());
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                leaguesNewsAdapter.updateData(searchSportsResults);
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }

    private void hockeySearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_HOCKEY);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
//                handleData(nytSportsSearch.getResponse().getDocs());
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                leaguesNewsAdapter.updateData(searchSportsResults);
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

            }


            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }


    private void englishSoccerSearch() {

        NytSearchAPI.NytAPIRetrofitSimple nytSportsSearch = NytSearchAPI.create();

        Call<ArticleSearch> call = nytSportsSearch.response(NYT_ENGLISH);

        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(Call<ArticleSearch> call, Response<ArticleSearch> response) {
                ArticleSearch nytSportsSearch = response.body();
//                handleData(nytSportsSearch.getResponse().getDocs());
                searchSportsResults.clear();
                Collections.addAll(searchSportsResults, nytSportsSearch.getResponse().getDocs());
//                leaguesNewsAdapter.updateData(searchSportsResults);
                if (recyclerView != null) {
                    recyclerView.setAdapter(leaguesNewsAdapter);
                }
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ArticleSearch> call, Throwable t) {

            }
        });

    }


    public void setFragmentType(int type) {
        mFragmentType = type;
        apiCall();
    }

    private void apiCall() {
        switch (mFragmentType) {
            case R.id.nfl:
                footballSearch();
                break;
            case R.id.nba:
                nbaSearch();
                break;
            case R.id.mlb:
                baseballSearch();
                break;
            case R.id.nhl:
                hockeySearch();
                break;
            case R.id.mls:
                mlsSearch();
                break;
            case R.id.english_soccer:
                englishSoccerSearch();
                break;
            case R.id.spanish_soccer:
                spanishSoccerSearch();
                break;
            case R.id.italian_soccer:
                italianSoccerSearch();
                break;
            case R.id.german_soccer:
                bundesligaSearch();
                break;
        }
    }


    private void handleData(Doc[] response) {
        if (leaguesNewsAdapter == null && recyclerView != null) {
            leaguesNewsAdapter = new LeaguesNewsAdapter(searchSportsResults);
            recyclerView.setAdapter(leaguesNewsAdapter);
        } else if (leaguesNewsAdapter != null) {
            leaguesNewsAdapter.setArticles(searchSportsResults);
            leaguesNewsAdapter.notifyDataSetChanged();
        } else {
            leaguesNewsAdapter = new LeaguesNewsAdapter(searchSportsResults);
        }
    }
}


//List<Doc> articlesWithImages = articlesWithImages(response);
//LeaguesNewsAdapter leaguesNewsAdapter = new LeaguesNewsAdapter(articlesWithImages);
//recyclerView.setAdapter(leaguesNewsAdapter);
//        }
//        });
//        }
//
//private List<Doc> articlesWithImages(ArticleSearch response) {
//        Doc[] docs = response.getResponse().getDocs();
//        List<Doc> docsWithImages = new ArrayList<>();
//
//        for(Doc doc: docs) {
//        if(doc.getMultimedia() != null && doc.getMultimedia().length != 0) {
//        docsWithImages.add(doc);
//        }
//        }

//    public void englishSoccerSearch() {
//        NytSearchAPI.NytRx nytSports = NytSearchAPI.createRx();
//
//        Observable<ArticleSearch> observable = nytSports.response(NYT_ENGLISH);
//
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ArticleSearch>() {
//                    @Override
//                    public void onCompleted() {
//
//                        Log.d("LeaguesFragment", "Query Succes!");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d("LeaguesFragment", "Error!");
//                    }
//
//                    @Override
//                    public void onNext(ArticleSearch response) {
//                        ArticleSearch nytSportsSearch = response.body();
//                        handleData(nytSportsSearch);
//                    }
//                });
//    }