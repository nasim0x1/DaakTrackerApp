package bd.smartpost.tracker.ui.view.options

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import bd.smartpost.tracker.R
import bd.smartpost.tracker.databinding.FragmentParcelOptionsBinding
import bd.smartpost.tracker.utils.myAlertDialog
import bd.smartpost.tracker.utils.showSnakeMessage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParcelOptionsFragment : BottomSheetDialogFragment() {

    private val viewModel: ParcelOptionsViewModel by viewModels()
    private val args: ParcelOptionsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parcel_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentParcelOptionsBinding.bind(view)

        binding.apply {

            if (args.parcel.isActive) {
                activeSection.visibility = View.VISIBLE
            } else {
                archiveSection.visibility = View.VISIBLE
            }
            trackingId.text = args.parcel.tracking_id

            rename.setOnClickListener { renameDialog() }

            cancel.setOnClickListener { findNavController().popBackStack() }

            update.setOnClickListener { viewModel.update(args.parcel) }

            archive.setOnClickListener { viewModel.moveToArchive(args.parcel) }

            restore.setOnClickListener { viewModel.restoreFromArchive(args.parcel) }

            delete.setOnClickListener { viewModel.delete(args.parcel) }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.events.collect {
                when (it) {
                    is ParcelOptionsViewModel.ParcelOptionEvents.DeleteConformation -> {
                        myAlertDialog(
                            requireActivity().getString(R.string.are_you_sure_you_want_to_deleted) +
                                    " ${it.parcel.tracking_id}?",
                            "yes",
                            "no"
                        ) {
                            viewModel.confirmDelete(it.parcel)
                        }
                    }
                    is ParcelOptionsViewModel.ParcelOptionEvents.OnArchived -> {
                        showSnakeMessage(requireActivity().getString(R.string.archived_successfully))
                        findNavController().popBackStack()
                    }
                    is ParcelOptionsViewModel.ParcelOptionEvents.OnDeleted -> {

                        showSnakeMessage(it.tracking_id + " ${requireContext().getString(R.string.deleted)}")
                        findNavController().popBackStack()
                    }
                    is ParcelOptionsViewModel.ParcelOptionEvents.OnRenamed -> {
                        showSnakeMessage(requireActivity().getString(R.string.renamed_successfully))
                        findNavController().popBackStack()
                    }
                    is ParcelOptionsViewModel.ParcelOptionEvents.OnRestored -> {
                        showSnakeMessage(requireActivity().getString(R.string.restored_successfully))
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun renameDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setTitle("Title")
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(args.parcel.title)
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, _ ->
            viewModel.rename(args.parcel, input.text.toString())
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }


}