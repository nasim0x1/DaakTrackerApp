package bd.smartpost.tracker.ui.view.add

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import bd.smartpost.tracker.R
import bd.smartpost.tracker.databinding.FragmentAddParcelBinding
import bd.smartpost.tracker.utils.ParcelTypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddParcelFragment : BottomSheetDialogFragment() {

    private val clipboard by lazy { requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager }
    private val viewModel:AddParcelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_parcel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddParcelBinding.bind(view)

        binding.apply {
            id.setEndIconOnClickListener {
                trackingIdInput.apply {
                    setText(paste())
                    requestFocus()
                    setSelection(this.text?.length ?:0 )
                }
            }
            add.apply {
                setOnClickListener {
                    val title = titleInput.text.toString()
                    val trackingId = trackingIdInput.text.toString().trim().uppercase()
                    val typeGroup: RadioButton = binding.root.findViewById(binding.trackingIdType.checkedRadioButtonId)
                    val type = if (typeGroup.text.toString() == requireContext().getString(R.string.text_domestic))
                        ParcelTypes.Domestic else ParcelTypes.International
                    viewModel.addNewParcel(title,trackingId,type)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.events.collect{event->
                when(event){
                    is AddParcelViewModel.AddParcelEvents.OnInvalidTrackingId -> when(event.error){
                        AddParcelViewModel.TrackingIdError.Empty -> binding.id.error =
                            this@AddParcelFragment.getString(R.string.tracking_can_not_be_empty)
                        AddParcelViewModel.TrackingIdError.InvalidLength ->binding.id.error =
                            this@AddParcelFragment.getString(R.string.valid_tracking_id_length_is_13)
                        AddParcelViewModel.TrackingIdError.InvalidFormat -> binding.id.error =
                            this@AddParcelFragment.getString(R.string.tracking_id_format)
                        AddParcelViewModel.TrackingIdError.Exist -> binding.id.error =
                            this@AddParcelFragment.getString(R.string.exist)
                    }
                    is AddParcelViewModel.AddParcelEvents.OnParcelAddSuccessfully -> findNavController().popBackStack()
                }
            }
        }
    }

    private fun paste(): String {
        val clip = clipboard.primaryClip
        return (clip?.getItemAt(0)).let {
            it?.text.toString()
        }
    }
}