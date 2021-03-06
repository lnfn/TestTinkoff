package com.eugenetereshkov.testtinkoff.ui.depositionpointscontainer


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.ui.depositionpointslist.DepositionPointsListFragment
import com.eugenetereshkov.testtinkoff.ui.depositionpointsmap.DepositionPointsMapFragment
import com.eugenetereshkov.testtinkoff.ui.global.BaseFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_deposition_points_container.*
import javax.inject.Inject


class DepositionPointsContainerFragment : BaseFragment(),
        HasSupportFragmentInjector {

    companion object {
        const val TAG = "deposition_points_container_fragment"

        fun newInstance() = DepositionPointsContainerFragment()
    }

    override val idResLayout: Int = R.layout.fragment_deposition_points_container

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private val adapter by lazy { ViewPagerAdapter(childFragmentManager) }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.title = getString(R.string.app_name)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for (childFragment in childFragmentManager.fragments) {
            childFragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> DepositionPointsMapFragment.newInstance()
            1 -> DepositionPointsListFragment.newInstance()
            else -> throw IllegalArgumentException("Not found $position page")
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence = when (position) {
            0 -> getString(R.string.on_map)
            1 -> getString(R.string.list)
            else -> throw IllegalArgumentException("Not found $position page")
        }
    }
}
