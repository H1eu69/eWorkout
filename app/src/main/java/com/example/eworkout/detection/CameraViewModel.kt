package com.example.eworkout.detection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.training.util.MathUlti
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

/**
 *  This ViewModel is used to store pose landmarker helper settings
 */
class CameraViewModel : ViewModel() {

    private var _model = PoseLandmarkerHelper.MODEL_POSE_LANDMARKER_FULL
    private var _delegate: Int = PoseLandmarkerHelper.DELEGATE_CPU
    private var _minPoseDetectionConfidence: Float =
        PoseLandmarkerHelper.DEFAULT_POSE_DETECTION_CONFIDENCE
    private var _minPoseTrackingConfidence: Float = PoseLandmarkerHelper
        .DEFAULT_POSE_TRACKING_CONFIDENCE
    private var _minPosePresenceConfidence: Float = PoseLandmarkerHelper
        .DEFAULT_POSE_PRESENCE_CONFIDENCE
    private var _poseState: MutableLiveData<PoseState> = MutableLiveData()

    var repCount = 0
    val repCountLiveData: MutableLiveData<Int> = MutableLiveData(repCount)
    val poseState: LiveData<PoseState> get() = _poseState

    private var _pushupPhase: MutableLiveData<PushUpPhase> = MutableLiveData(PushUpPhase.DESCENDING)
    val pushupPhase: LiveData<PushUpPhase> get() = _pushupPhase

    var lastAngle = 0.0

    private var detector = PushUpDetector(170.0, 0.0, 0.0)


    val currentDelegate: Int get() = _delegate
    val currentModel: Int get() = _model
    val currentMinPoseDetectionConfidence: Float
        get() =
            _minPoseDetectionConfidence
    val currentMinPoseTrackingConfidence: Float
        get() =
            _minPoseTrackingConfidence
    val currentMinPosePresenceConfidence: Float
        get() =
            _minPosePresenceConfidence

    fun setDelegate(delegate: Int) {
        _delegate = delegate
    }

    fun setMinPoseDetectionConfidence(confidence: Float) {
        _minPoseDetectionConfidence = confidence
    }

    fun setMinPoseTrackingConfidence(confidence: Float) {
        _minPoseTrackingConfidence = confidence
    }

    fun setMinPosePresenceConfidence(confidence: Float) {
        _minPosePresenceConfidence = confidence
    }

    fun setModel(model: Int) {
        _model = model
    }

    fun detectPushUp(poseLandmarkerResult: PoseLandmarkerResult)
    {
        for(landmark in poseLandmarkerResult.landmarks()) {

            val rightShoulder = landmark[12]
            val rightElbow = landmark[14]
            val rightWrist = landmark[16]

            // Angle of shoulder, elbow and wrist
            val rightArmAngle = MathUlti.calculateAngle(
                MathUlti.Point(rightShoulder.x(), rightShoulder.y()),
                MathUlti.Point(rightElbow.x(), rightElbow.y()),
                MathUlti.Point(rightWrist.x(), rightWrist.y()),
            )

            val leftShoulder = landmark[11]
            val leftElbow = landmark[13]
            val leftWrist = landmark[15]

            // Angle of shoulder, elbow and wrist
            val leftArmAngle = MathUlti.calculateAngle(
                MathUlti.Point(leftShoulder.x(), leftShoulder.y()),
                MathUlti.Point(leftElbow.x(), leftElbow.y()),
                MathUlti.Point(leftWrist.x(), leftWrist.y()),
            )
            var armAngle: Double
            if(rightArmAngle >= 0)
            {
                armAngle =
                when(rightArmAngle)
                {
                    in 0.0..180.0 -> rightArmAngle
                    else -> 360 - rightArmAngle
                }
            }
            else
            {
                armAngle =
                    when(leftArmAngle)
                    {
                        in 0.0..180.0 -> leftArmAngle
                        else -> 360 - leftArmAngle
                    }
            }
            Log.d("test right angle", rightArmAngle.toString())
            Log.d("test left angle", leftArmAngle.toString())
            Log.d("test arm angle", armAngle.toString())

            val rightHip = landmark[24]
            val rightKnee = landmark[26]

            // Angle of shoulder, elbow and wrist
            val rightBackAngle = MathUlti.calculateAngle(
                MathUlti.Point(rightShoulder.x(), rightShoulder.y()),
                MathUlti.Point(rightHip.x(), rightHip.y()),
                MathUlti.Point(rightKnee.x(), rightKnee.y()),
            )

            val leftHip = landmark[23]
            val leftKnee = landmark[25]

            val leftBackAngle = MathUlti.calculateAngle(
                MathUlti.Point(rightShoulder.x(), rightShoulder.y()),
                MathUlti.Point(leftHip.x(), leftHip.y()),
                MathUlti.Point(leftKnee.x(), leftKnee.y()),
            )

            val backAngle =
                when(rightBackAngle)
                {
                    in 0.0..180.0 -> rightBackAngle
                    else -> 360 - rightBackAngle
                }
            Log.d("push up back angle", backAngle.toString())

            if((armAngle in 40.0..180.0 && backAngle > 160))
            {
                Log.d("push up last angle", lastAngle.toString())
                Log.d("push up arm angle", armAngle.toString())

                if(armAngle >= 40 && armAngle < lastAngle && _pushupPhase.value!!.name == "DESCENDING")
                {
                    detector.ascendedAngle = armAngle
                }
                else if(armAngle <= 180 && armAngle > lastAngle && _pushupPhase.value!!.name == "ASCENDING")
                {
                    detector.endAngle = armAngle
                }
                else if(armAngle <= 110 && armAngle > lastAngle)
                {
                    _pushupPhase.value = PushUpPhase.ASCENDING
                }
                lastAngle = armAngle
                _poseState.value = PoseState.NO_WRONG
                Log.d("push up progress", detector.toString())

                if(detector.isOnePushUp()){
                    Log.d("push up done", detector.toString())
                    _pushupPhase.value = PushUpPhase.DONE
                    _pushupPhase.value = PushUpPhase.DESCENDING
                    increaseRepsCount()
                    resetDetector()
                }
            }
            else if(armAngle !in 40.0..180.0 && backAngle <= 160)
            {
                Log.d("push up error full", armAngle.toString())
                Log.d("push up error full", backAngle.toString())
                Log.d("push up error full", detector.toString())

                _poseState.value = PoseState.PUSH_UP_FULL_WRONG
            }
            else if(armAngle !in 40.0..180.0)
            {
                Log.d("push up error arm", armAngle.toString())
                Log.d("push up error arm", backAngle.toString())
                Log.d("push up error arm", detector.toString())

                _poseState.value = PoseState.PUSH_UP_ARM_WRONG
            }
            else if(backAngle <= 160){
                Log.d("push up error back", armAngle.toString())
                Log.d("push up error back", backAngle.toString())
                Log.d("push up error back", detector.toString())

                _poseState.value = PoseState.PUSH_UP_BACK_WRONG
            }
        }
    }
    private fun increaseRepsCount()
    {
        repCount++
        repCountLiveData.value = repCount
    }
    private fun resetDetector()
    {
        detector = PushUpDetector(170.0, 0.0, 0.0)
    }
}