package com.example.eworkout.training.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentWorkoutDetail2Binding
import com.example.eworkout.training.adapter.InstructionsAdapter
import com.example.eworkout.training.model.WorkoutDetail2State
import com.example.eworkout.training.viewmodel.Workout2ViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@UnstableApi /**
 * A simple [Fragment] subclass.
 * Use the [WorkoutDetail2.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutDetail2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var exerciseId: String? = null
    private var _binding: FragmentWorkoutDetail2Binding? = null
    val binding get() = _binding!!
    private val _viewModel: Workout2ViewModel by viewModels()

    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            exerciseId = it.getString("exercise_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetail2Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WorkoutDetail2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WorkoutDetail2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setOnClickListener()
    }


    private fun observeViewModel()
    {
        _viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun setOnClickListener()
    {
        binding.exoPlayPauseBtn.setOnClickListener {
            _viewModel.get()
        }
        binding.btnNavigateUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleState(state: WorkoutDetail2State) {
        when(state.name)
        {
            "LOADING" -> {_viewModel.getExerciseInfoById(exerciseId!!)}
            "LOADED" -> {
                setupRecyclerView()
                showUI()
            }
            "VIDEO_LOADED" -> {
                hideThumbnail()
                preparePlayer()
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

    private fun preparePlayer() {
        try{
            val uri = _viewModel.uri.value
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

    private fun showUI()
    {
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.dataLayout.visibility = View.VISIBLE
    }

    private fun setupRecyclerView()
    {
        val list = _viewModel.instructionSteps
        binding.recyclerview.adapter = InstructionsAdapter(list)
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