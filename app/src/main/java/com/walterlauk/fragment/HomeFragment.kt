package com.walterlauk.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.walterlauk.R
import com.walterlauk.adapter.NewsAdapter
import com.walterlauk.api.ApiCall
import com.walterlauk.databinding.HomeFragmentBinding
import com.walterlauk.models.NewsData
import com.walterlauk.models.ResponseNews
import com.walterlauk.utils.AppPref
import com.walterlauk.utils.Constants.Companion.BASE_URL
import com.walterlauk.utils.DividerItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : BaseFragment(), View.OnClickListener {

    lateinit var homeFragmentBinding: HomeFragmentBinding
    var newsList = ArrayList<com.walterlauk.utils.NewsData>()
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeFragmentBinding = HomeFragmentBinding.inflate(inflater, container, false)
        return homeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFragmentBinding.cardLocation1.setOnClickListener(this)
        homeFragmentBinding.cardLocation2.setOnClickListener(this)

        homeFragmentBinding.prgHome.visibility = VISIBLE
        callNewsApi()
        setNewsAdpater()
    }

    private fun setNewsAdpater() {
        newsAdapter = NewsAdapter(requireActivity(), newsList)

        val dividerItemDecoration: ItemDecoration =
            DividerItemDecorator(ContextCompat.getDrawable(requireContext(), R.drawable.divider))
        homeFragmentBinding.rvNews.addItemDecoration(dividerItemDecoration)

        val layoutManager = LinearLayoutManager(activity)
        homeFragmentBinding.rvNews.layoutManager = layoutManager
        homeFragmentBinding.rvNews.adapter = newsAdapter
    }

    private fun callNewsApi() {
        ApiCall.initApiCall(BASE_URL)
            .getNews("Bearer" + AppPref.getValue(AppPref.TOKEN, "")!!, 3, 0)
            .enqueue(object : Callback<com.walterlauk.utils.ResponseNews> {
                override fun onResponse(
                    call: Call<com.walterlauk.utils.ResponseNews>,
                    response: Response<com.walterlauk.utils.ResponseNews>
                ) {
                    homeFragmentBinding.prgHome.visibility = GONE
                    if (response.isSuccessful) {
                        newsList.addAll(response.body()!!.data!!)
                        newsAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<com.walterlauk.utils.ResponseNews>, t: Throwable) {
                    homeFragmentBinding.prgHome.visibility = GONE
                }
            })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.card_location1 -> {
                val uri =
                    "http://maps.google.com/maps?q=loc:" + "53.538816" + "," + "9.975868"
                requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
            }
            R.id.card_location2 -> {
                val uri =
                    "http://maps.google.com/maps?q=loc:" + "53.5022121" + "," + "9.9611754"
                requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
            }
        }
    }
}