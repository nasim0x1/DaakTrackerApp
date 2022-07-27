package bd.smartpost.tracker.ui.view.home.parcels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bd.smartpost.tracker.R
import bd.smartpost.tracker.data.model.Parcel
import bd.smartpost.tracker.databinding.FragmentArchiveParcelsBinding
import bd.smartpost.tracker.ui.adapter.ParcelsAdapter
import bd.smartpost.tracker.ui.view.home.HomeFragmentDirections
import bd.smartpost.tracker.ui.view.home.SharedHomeViewModel
import bd.smartpost.tracker.utils.ParcelTypes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveParcelsFragment:Fragment(R.layout.fragment_archive_parcels),
    ParcelsAdapter.ParcelListClickListener {
    private val viewModel: SharedHomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentArchiveParcelsBinding.bind(view)
        val archiveParcelsAdapter = ParcelsAdapter(this)

        binding.apply {
            archiveParcelList.apply {
                layoutManager = LinearLayoutManager(this@ArchiveParcelsFragment.context)
                adapter = archiveParcelsAdapter
                hasFixedSize()
            }
        }
        viewModel.getArchiveParcels.observe(viewLifecycleOwner){
            archiveParcelsAdapter.submitList(it)
            binding.archiveParcelList.smoothScrollToPosition(0)
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