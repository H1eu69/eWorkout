package com.seuit.eworkout.custom_workout.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.seuit.eworkout.R
import com.seuit.eworkout.custom_workout.model.ChooseImageState
import com.seuit.eworkout.custom_workout.viewModel.ChooseImageViewModel
import com.seuit.eworkout.databinding.FragmentCustomSetChooseImageBinding
import java.io.InputStream
import kotlin.random.Random


/**
 * A simple [Fragment] subclass.
 * Use the [CustomSetChooseImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomSetChooseImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCustomSetChooseImageBinding? = null
    private val binding: FragmentCustomSetChooseImageBinding get() = _binding!!

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        loadImage(uri)
        viewModel.setUri(uri)
    }

    private val viewModel: ChooseImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomSetChooseImageBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateSetChooseImageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomSetChooseImageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setListener()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(it: ChooseImageState?) {
        when(it?.name)
        {
            "CREATED_NEW_IMAGE" -> {
                findNavController().navigate(R.id.action_global_customSetFragment)
            }
        }
    }

    private fun setListener()
    {
        binding.imageView.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.btnNext.setOnClickListener {
            if(viewModel.uriLiveData.value != null && !Uri.EMPTY.equals(viewModel.uriLiveData.value))
                viewModel.updateImage(arguments)
            else
                pickRandomImage()
        }
        binding.textViewSkip.setOnClickListener {
            pickRandomImage()
        }
    }

    private fun loadImage(uri: Uri?)
    {
        var requestOption = RequestOptions()
        requestOption = requestOption.transform(CenterCrop(), RoundedCorners(80))

        Glide.with(requireContext())
            .load(uri)
            .apply(requestOption)
            .into(binding.imageView)
    }

    private fun pickRandomImage()
    {
        val random = Random.nextInt(1,4)
        when(random)
        {
            1 -> {
                createBitmap(R.drawable.vectorab)
            }
            2 -> {
                createBitmap(R.drawable.vectorfullbody)
            }
            3 -> {
                createBitmap(R.drawable.vectorlowerbody)
            }
            4 -> {
                createBitmap(R.drawable.vectorupperbody)
            }
        }
    }

    private fun createBitmap(int: Int)
    {
        var drawable = ContextCompat.getDrawable(requireContext(), int)

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        viewModel.updateImage(arguments, bitmap)
    }
}