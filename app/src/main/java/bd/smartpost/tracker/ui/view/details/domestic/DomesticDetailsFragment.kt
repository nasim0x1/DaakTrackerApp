package bd.smartpost.tracker.ui.view.details.domestic

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import bd.smartpost.tracker.R
import bd.smartpost.tracker.databinding.FragmentDomesticDetailsBinding
import bd.smartpost.tracker.ui.view.MainActivity
import bd.smartpost.tracker.ui.view.details.DetailsViewModel
import bd.smartpost.tracker.utils.showSnakeMessage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DomesticDetailsFragment : Fragment(R.layout.fragment_domestic_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private val args:DomesticDetailsFragmentArgs by navArgs()
    private var isRefreshable:Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDomesticDetailsBinding.bind(view)

        binding.apply {
            updater.setOnRefreshListener {
                if (isRefreshable && args.parcel.isActive){
                    viewModel.update(args.parcel)
                }else{
                    showSnakeMessage("Unable to refresh parcel in archive")
                    updater.isRefreshing = false
                }
            }
        }

        val menuHost = requireActivity()
        menuHost.apply { menuProvider(this)}

        viewModel.getCurrentParcelUpdate(args.parcel.tracking_id)?.observe(viewLifecycleOwner){
            if (it == null){
                findNavController().popBackStack()
            }else{
                if (!it.title.isNullOrBlank() && args.title != it.title){
                    (activity as MainActivity).supportActionBar?.title = it.title
                }
                isRefreshable = it.isActive
            }

        }
    }
    private fun menuProvider(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.detail_option_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.option ->{
                        val action = DomesticDetailsFragmentDirections.actionDomesticDetailsFragmentToParcelOptionsFragment(args.parcel)
                        findNavController().navigate(action)
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.viewModelStore.clear()
    }

}