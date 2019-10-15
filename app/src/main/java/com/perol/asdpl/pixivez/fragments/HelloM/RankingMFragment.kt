package com.perol.asdpl.pixivez.fragments.HelloM


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.RankingAdapter
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.viewmodel.RankingMViewModel
import com.perol.asdpl.pixivez.viewmodel.factory.RankingShareViewModel
import kotlinx.android.synthetic.main.fragment_ranking_m.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [RankingMFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RankingMFragment : LazyV4Fragment() {


    var picDate: String? = null
    var viewmodel: RankingMViewModel? = null
    var sharemodel: RankingShareViewModel? = null
    lateinit var rankingAdapter: RankingAdapter
    lateinit var sharedPreferencesServices: SharedPreferencesServices
    private var param1: String? = null
    override fun loadData() {
        viewmodel!!.first(param1!!, picDate)
    }

    fun onClick(data: String?) {

        sharemodel?.picdateshare?.value = data
    }

    fun lazyLoad() {
        viewmodel = ViewModelProviders.of(this).get(RankingMViewModel::class.java)
        sharemodel = ViewModelProviders.of(activity!!).get(RankingShareViewModel::class.java)
        sharemodel!!.picdateshare.observe(this, Observer {
            viewmodel!!.datapick(param1!!, it)
        })
        viewmodel!!.addillusts.observe(this, Observer {
            if (it != null) {
                rankingAdapter.addData(it)
            }
        })
        viewmodel!!.illusts.observe(this, Observer {
            swiperefresh_rankingm.isRefreshing = false
            rankingAdapter.setNewData(it)
        })
        viewmodel!!.bookmarknum.observe(this, Observer {
            if (it != null) {
                viewmodel!!.OnItemChildLongClick(it)
            }
        })
        viewmodel!!.nexturl.observe(this, Observer {
            if (it == null) {
                rankingAdapter.loadMoreEnd()
            } else {
                rankingAdapter.loadMoreComplete()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.findViewById<ImageView>(R.id.imageview_triangle).apply {
            setOnClickListener {
                val calendar = Calendar.getInstance();
                val year = calendar.get(Calendar.YEAR);
                val month = calendar.get(Calendar.MONTH) + 1;
                val day = calendar.get(Calendar.DAY_OF_MONTH);
                val dateDialog = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { p0, year, monthr, day ->
                    val month = monthr + 1
                    Log.d("date", "$year-$month-$day")
                    onClick("$year-$month-$day")
                }, year, month, day)
                dateDialog.datePicker.maxDate = System.currentTimeMillis()
                dateDialog.show()

            }
        }
        swiperefresh_rankingm.setOnRefreshListener {
            viewmodel!!.OnRefresh(param1!!, picDate)
        }
        rankingAdapter.setOnLoadMoreListener({
            viewmodel!!.OnLoadMore()
        }, recyclerview_rankingm)
        recyclerview_rankingm.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = rankingAdapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }

        lazyLoad()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rankingAdapter = RankingAdapter(R.layout.view_ranking_item, null, PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("r18on",false))
        return inflater.inflate(R.layout.fragment_ranking_m, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RankingMFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                RankingMFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)

                    }
                }
    }
}
