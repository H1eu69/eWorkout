package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.custom_workout.model.AddToCartState
import com.example.eworkout.custom_workout.viewModel.PickExercisesSharedViewModel
import com.example.eworkout.custom_workout.viewModel.SetDetailViewModel
import com.example.eworkout.databinding.FragmentCustomCreateSetDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CustomCreateSetDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomCreateSetDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCustomCreateSetDetailBinding? = null
    private val binding: FragmentCustomCreateSetDetailBinding get() = _binding!!
    private val _shareViewModel: PickExercisesSharedViewModel by navGraphViewModels(R.id.custom_set)
    private val _viewModel: SetDetailViewModel by viewModels()
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomCreateSetDetailBinding.inflate(inflater, container, false)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomCreateSetDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomCreateSetDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
        observeViewModel()
        setOnClickListener()
        checkRepsButton()
    }

    private fun getBundle()
    {
        _viewModel.initExerciseToAddDetail(arguments)
    }

    private fun setOnClickListener()
    {
        binding.exoPlayPauseBtn.setOnClickListener {
            hideThumbnail()
            startPlayer()
        }
        binding.btnNavigateUp.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnRep.setOnClickListener {
            _viewModel.changeState(AddToCartState.REPS_CHOOSE)
        }
        binding.btnTime.setOnClickListener {
            _viewModel.changeState(AddToCartState.TIME_CHOOSE)
        }
        binding.btnMinus.setOnClickListener {
            _viewModel.decreaseRepByOne()
        }
        binding.btnAdd.setOnClickListener {
            _viewModel.increaseRepByOne()
        }
        binding.btnAddToCart.setOnClickListener {
            val exercises = _viewModel.exerciseToAddDetail.value!!
            _shareViewModel.addExerciseToCart(exercises)
            findNavController().popBackStack()
        }
    }

    private fun checkRepsButton()
    {
        binding.toggleButton.check(binding.btnRep.id)
    }

    private fun observeViewModel()
    {
        _viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(state: AddToCartState) {
        when(state.name)
        {
            "REPS_CHOOSE" -> {
                binding.timeRepCountTextView.text = "x" + _viewModel.exerciseToAddDetail.value?.reps.toString()
                _viewModel.changeRepTypeDetail()
            }
            "TIME_CHOOSE" -> {
                binding.timeRepCountTextView.text = _viewModel.exerciseToAddDetail.value?.reps.toString() + "s"
                _viewModel.changeRepTypeDetail()
            }
        }
    }

    private fun hideThumbnail()
    {
        binding.viewThumbnailBackground.visibility = View.GONE
        binding.imageBackgroundThumbnail.visibility = View.GONE
        binding.imageThumbnail.visibility = View.GONE
        binding.exoPlayPauseBtn.visibility = View.GONE
        binding.playerView.visibility = View.VISIBLE
    }

    private fun startPlayer() {
        try{
            val uri = _viewModel.exerciseToAddDetail.value?.video
            val mediaItem = MediaItem.fromUri(uri!!)

            exoPlayer = ExoPlayer.Builder(requireContext()).build()
            exoPlayer?.playWhenReady = true
            binding.playerView.player = exoPlayer
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.seekTo(playbackPosition)
            exoPlayer?.playWhenReady = playWhenReady
            exoPlayer?.prepare()
        }
        catch(e: Exception){
            e.localizedMessage?.let { Log.d("Error", it) }
        }
    }
    private fun  relasePlayer(){
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        relasePlayer()
    }

    override fun onPause() {
        super.onPause()
        relasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        relasePlayer()
    }
}