package bd.smartpost.tracker.ui.view.home.parcels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bd.smartpost.tracker.R
import bd.smartpost.tracker.data.model.Parcel
import bd.smartpost.tracker.databinding.FragmentActiveParcelsBinding
import bd.smartpost.tracker.ui.adapter.ParcelsAdapter
import bd.smartpost.tracker.ui.view.home.HomeFragmentDirections
import bd.smartpost.tracker.ui.view.home.SharedHomeViewModel
import bd.smartpost.tracker.utils.ParcelTypes
import bd.smartpost.tracker.utils.isInternetAvailable
import bd.smartpost.tracker.utils.showSnakeMessage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActiveParcelsFragment:Fragment(R.layout.fragment_active_parcels),
    ParcelsAdapter.ParcelListClickListener {
    private val viewModel: SharedHomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentActiveParcelsBinding.bind(view)
        val activeParcelsAdapter = ParcelsAdapter(this)

        binding.apply {
            activeParcelList.apply {
                layoutManager = LinearLayoutManager(this@ActiveParcelsFragment.context)
                adapter = activeParcelsAdapter
                hasFixedSize()
            }
            updater.apply {
                setOnRefreshListener {
                    if (requireContext().isInternetAvailable()){
                        viewModel.updateAll()
                    }else{
                        showSnakeMessage("Your device not connected with internet!")
                        isRefreshing = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.events.collect{
                when(it){
                    is SharedHomeViewModel.HomeEvents.OnUpdateCompleted -> binding.updater.isRefreshing = false
                }
            }
        }

        viewModel.getActiveParcels.observe(viewLifecycleOwner){parcels->
            activeParcelsAdapter.submitList(parcels){
                if (viewModel.lastParcelsSize < parcels.size){
                    binding.activeParcelList.smoothScrollToPosition(0)
                    viewModel.lastParcelsSize = parcels.size
                }
            }

        }
    }

    override fun onItemClick(title: String, parcel: Parcel) {
        val action = when(parcel.type){
            ParcelTypes.Domestic -> HomeFragmentDirections.actionHomeFragmentToDomesticDetailsFragment(title,parcel)
            ParcelTypes.International -> HomeFragmentDirections.actionHomeFragmentToInternationalDetailsFragment(title,parcel)
        }
        findNavController().navigate(action)
    }

    override fun onItemOptionClick(parcel: Parcel) {
        val action = HomeFragmentDirections.actionHomeFragmentToParcelOptionsFragment(parcel)
        findNavController().navigate(action)
    }
}