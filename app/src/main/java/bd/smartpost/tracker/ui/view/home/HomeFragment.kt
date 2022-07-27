package bd.smartpost.tracker.ui.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import bd.smartpost.tracker.R
import bd.smartpost.tracker.databinding.FragmentHomeBinding
import bd.smartpost.tracker.ui.view.home.parcels.ActiveParcelsFragment
import bd.smartpost.tracker.ui.view.home.parcels.ArchiveParcelsFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        binding.apply {
            viewPager.apply {
                adapter = FragmentAdapter(this@HomeFragment)
                isUserInputEnabled = false
                isSaveEnabled = true
            }
            TabLayoutMediator(tabLayout,viewPager){tab,pos->
                tab.text = if (pos == 0) "Active" else "Archive"
            }.attach()
        }

    }
    class FragmentAdapter(fragment: Fragment):FragmentStateAdapter(fragment){
        override fun getItemCount()= 2

        override fun createFragment(position: Int) :Fragment = when(position){
            0 -> ActiveParcelsFragment()
            1 -> ArchiveParcelsFragment()
            else -> ActiveParcelsFragment()
        }
    }
}