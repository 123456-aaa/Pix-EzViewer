package com.perol.asdpl.pixivez.fragments.HelloM


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.UserShowAdapter
import com.perol.asdpl.pixivez.objects.LazyV4Fragment
import com.perol.asdpl.pixivez.viewmodel.HelloRecomUserViewModel
import kotlinx.android.synthetic.main.fragment_recom_user.*



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloRecomUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloRecomUserFragment : LazyV4Fragment() {
    override fun loadData() {
        viewmodel!!.reData()
    }

    var viewmodel: HelloRecomUserViewModel? = null
    fun lazyLoad() {

        viewmodel!!.adddata.observe(this, Observer {
            if (it != null) {
                userShowAdapter.addData(it)
            } else {
                userShowAdapter.loadMoreFail()
            }
        })
        viewmodel!!.data.observe(this, Observer {
            userShowAdapter.setNewData(it)
            swipe.isRefreshing = false
        })
        viewmodel!!.nexturl.observe(this, Observer {
            if (it != null) {
                userShowAdapter.loadMoreComplete()
            } else {
                userShowAdapter.loadMoreEnd()
            }
        })


    }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewmodel = ViewModelProviders.of(this).get(HelloRecomUserViewModel::class.java)
        lazyLoad()
    }

    val userShowAdapter = UserShowAdapter(R.layout.view_usershow_item)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            adapter = userShowAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
        userShowAdapter.setOnLoadMoreListener({
            viewmodel!!.getNext()
        }, recyclerView)
        swipe.setOnRefreshListener {
            viewmodel!!.reData()

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recom_user, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloRecomUserFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HelloRecomUserFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
