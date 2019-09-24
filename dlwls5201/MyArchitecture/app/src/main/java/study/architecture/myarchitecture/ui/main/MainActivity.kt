package study.architecture.myarchitecture.ui.main

import android.os.Bundle
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import study.architecture.myarchitecture.R
import study.architecture.myarchitecture.base.BaseActivity
import study.architecture.myarchitecture.data.Injection
import study.architecture.myarchitecture.databinding.ActivityMainBinding
import study.architecture.myarchitecture.util.Filter

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by lazy {
        MainViewModel(
            Injection.provideFolderRepository(this)
        ).apply {
            binding.mainModel = this
        }
    }

    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewPager()
        initAdapterCallback()
        mainViewModel.loadData()
    }

    override fun onDestroy() {
        mainViewModel.clearDisposable()
        super.onDestroy()
    }

    private fun initViewPager() {
        binding.vpMain.adapter = mainAdapter
    }

    private fun initAdapterCallback() {
        mainViewModel.selectField.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                (sender as ObservableField<Pair<Filter.SelectArrow, Boolean>>).get()?.let {

                    val filter = it.first
                    val selected = it.second

                    val order = if (selected) {
                        Filter.ASC
                    } else {
                        Filter.DESC
                    }

                    for (i in 0 until (mainAdapter.count)) {
                        mainAdapter.getFragment(i)?.let { fragment ->
                            fragment.get()?.showTickerListOrderByField(filter, order)
                        }
                    }
                }
            }

        })
    }
}
