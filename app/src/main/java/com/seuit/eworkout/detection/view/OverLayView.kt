package com.seuit.eworkout.detection.view
/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.seuit.eworkout.detection.model.PoseState
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.max
import kotlin.math.min

class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: PoseLandmarkerResult? = null
    private var pointPaint = Paint()
    private var linePaint = Paint()
    private var wrongPoseLinePaint = Paint()
    private var wrongPosePointPaint = Paint()
    private var rightPoseLinePaint = Paint()
    private var rightPosePointPaint = Paint()

    private var _state: PoseState? = null

    private var scaleFactor: Float = 1f
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1

    init {
        initPaints()
    }

    fun setState(state: PoseState)
    {
        _state = state
    }

    fun clear() {
        results = null
        pointPaint.reset()
        linePaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        linePaint.color = Color.WHITE
        linePaint.strokeWidth = LANDMARK_STROKE_WIDTH
        linePaint.style = Paint.Style.STROKE

        pointPaint.color = Color.YELLOW
        pointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        pointPaint.style = Paint.Style.FILL

        wrongPosePointPaint.color = Color.RED
        wrongPosePointPaint.strokeWidth = WRONG_LANDMARK_STROKE_WIDTH
        wrongPosePointPaint.style = Paint.Style.STROKE

        wrongPoseLinePaint.color = Color.RED
        wrongPoseLinePaint.strokeWidth = WRONG_LANDMARK_STROKE_WIDTH
        wrongPoseLinePaint.style = Paint.Style.FILL

        rightPosePointPaint.color = Color.GREEN
        rightPosePointPaint.strokeWidth = WRONG_LANDMARK_STROKE_WIDTH
        rightPosePointPaint.style = Paint.Style.STROKE

        rightPoseLinePaint.color = Color.GREEN
        rightPoseLinePaint.strokeWidth = WRONG_LANDMARK_STROKE_WIDTH
        rightPoseLinePaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        results?.let { poseLandmarkerResult ->
            drawPushUp(canvas, poseLandmarkerResult)
            when(_state?.name)
            {
                "NO_WRONG" -> drawRightFull(canvas, poseLandmarkerResult)
                "PUSH_UP_BACK_WRONG" -> drawErrorBack(canvas, poseLandmarkerResult)
                "PUSH_UP_FULL_WRONG" -> drawErrorFull(canvas, poseLandmarkerResult)
                "PUSH_UP_ARM_WRONG" -> drawErrorArm(canvas, poseLandmarkerResult)
            }
        }
    }

    private fun drawWrongPoint(canvas: Canvas, bodyPart: NormalizedLandmark)
    {
        canvas.drawPoint(
            bodyPart.x() * imageWidth * scaleFactor,
            bodyPart.y() * imageHeight * scaleFactor,
            wrongPosePointPaint
        )
    }

    private fun drawWrongLine(canvas: Canvas, bodyPartStart: NormalizedLandmark, bodyPartEnd: NormalizedLandmark)
    {
        canvas.drawLine(
            bodyPartStart
                .x() * imageWidth * scaleFactor,
            bodyPartStart
                .y() * imageHeight * scaleFactor,
            bodyPartEnd
                .x() * imageWidth * scaleFactor,
            bodyPartEnd
                .y() * imageHeight * scaleFactor,
            wrongPoseLinePaint
        )
    }
    private fun drawRightPoint(canvas: Canvas, bodyPart: NormalizedLandmark)
    {
        canvas.drawPoint(
            bodyPart.x() * imageWidth * scaleFactor,
            bodyPart.y() * imageHeight * scaleFactor,
            rightPosePointPaint
        )
    }

    private fun drawRightLine(canvas: Canvas, bodyPartStart: NormalizedLandmark, bodyPartEnd: NormalizedLandmark)
    {
        canvas.drawLine(
            bodyPartStart
                .x() * imageWidth * scaleFactor,
            bodyPartStart
                .y() * imageHeight * scaleFactor,
            bodyPartEnd
                .x() * imageWidth * scaleFactor,
            bodyPartEnd
                .y() * imageHeight * scaleFactor,
            rightPoseLinePaint
        )
    }

    private fun drawErrorArm(canvas: Canvas, poseLandmarkerResult: PoseLandmarkerResult){
        for (landmark in poseLandmarkerResult.landmarks()) {
            val rightShoulder = landmark[12]
            val rightElbow = landmark[14]
            val rightWrist = landmark[16]

            val leftShoulder = landmark[11]
            val leftElbow = landmark[13]
            val leftWrist = landmark[15]

            drawWrongPoint(canvas, rightShoulder)
            drawWrongPoint(canvas, rightElbow)
            drawWrongPoint(canvas, rightWrist)
            drawWrongPoint(canvas, leftShoulder)
            drawWrongPoint(canvas, leftElbow)
            drawWrongPoint(canvas, leftWrist)

            drawWrongLine(canvas, rightShoulder, rightElbow)
            drawWrongLine(canvas, rightElbow, rightWrist)
            drawWrongLine(canvas, leftShoulder, leftElbow)
            drawWrongLine(canvas, leftElbow, leftWrist)
        }
    }

    private fun drawErrorBack(canvas: Canvas, poseLandmarkerResult: PoseLandmarkerResult){
        for (landmark in poseLandmarkerResult.landmarks()) {
            val rightShoulder = landmark[12]
            val rightHip = landmark[24]
            val rightKnee = landmark[26]

            val leftShoulder = landmark[11]
            val leftHip = landmark[23]
            val leftKnee = landmark[25]

            drawWrongPoint(canvas, rightShoulder)
            drawWrongPoint(canvas, rightHip)
            drawWrongPoint(canvas, rightKnee)
            drawWrongPoint(canvas, leftShoulder)
            drawWrongPoint(canvas, leftHip)
            drawWrongPoint(canvas, leftKnee)

            drawWrongLine(canvas, rightShoulder, rightHip)
            drawWrongLine(canvas, rightHip, rightKnee)
            drawWrongLine(canvas, leftShoulder, leftHip)
            drawWrongLine(canvas, leftHip, leftKnee)
        }
    }

    private fun drawErrorFull(canvas: Canvas, poseLandmarkerResult: PoseLandmarkerResult){
        drawErrorArm(canvas, poseLandmarkerResult)
        drawErrorBack(canvas, poseLandmarkerResult)
    }

    private fun drawRightArm(canvas: Canvas, poseLandmarkerResult: PoseLandmarkerResult){
        for (landmark in poseLandmarkerResult.landmarks()) {
            val rightShoulder = landmark[12]
            val rightElbow = landmark[14]
            val rightWrist = landmark[16]

            val leftShoulder = landmark[11]
            val leftElbow = landmark[13]
            val leftWrist = landmark[15]

            drawRightPoint(canvas, rightShoulder)
            drawRightPoint(canvas, rightElbow)
            drawRightPoint(canvas, rightWrist)
            drawRightPoint(canvas, leftShoulder)
            drawRightPoint(canvas, leftElbow)
            drawRightPoint(canvas, leftWrist)

            drawRightLine(canvas, rightShoulder, rightElbow)
            drawRightLine(canvas, rightElbow, rightWrist)
            drawRightLine(canvas, leftShoulder, leftElbow)
            drawRightLine(canvas, leftElbow, leftWrist)
        }
    }

    private fun drawRightBack(canvas: Canvas, poseLandmarkerResult: PoseLandmarkerResult){
        for (landmark in poseLandmarkerResult.landmarks()) {
            val rightShoulder = landmark[12]
            val rightHip = landmark[24]
            val rightKnee = landmark[26]

            val leftShoulder = landmark[11]
            val leftHip = landmark[23]
            val leftKnee = landmark[25]

            drawRightPoint(canvas, rightShoulder)
            drawRightPoint(canvas, rightHip)
            drawRightPoint(canvas, rightKnee)
            drawRightPoint(canvas, leftShoulder)
            drawRightPoint(canvas, leftHip)
            drawRightPoint(canvas, leftKnee)

            drawRightLine(canvas, rightShoulder, rightHip)
            drawRightLine(canvas, rightHip, rightKnee)
            drawRightLine(canvas, leftShoulder, leftHip)
            drawRightLine(canvas, leftHip, leftKnee)
        }
    }

    private fun drawRightFull(canvas: Canvas, poseLandmarkerResult: PoseLandmarkerResult){
        drawRightArm(canvas, poseLandmarkerResult)
        drawRightBack(canvas, poseLandmarkerResult)
    }

    private fun drawPushUp(canvas: Canvas, poseLandmarkerResult: PoseLandmarkerResult) {
        for (landmark in poseLandmarkerResult.landmarks()) {
            for (normalizedLandmark in landmark) {
                canvas.drawPoint(
                    normalizedLandmark.x() * imageWidth * scaleFactor,
                    normalizedLandmark.y() * imageHeight * scaleFactor,
                    pointPaint
                )
            }

            PoseLandmarker.POSE_LANDMARKS.forEach {
                canvas.drawLine(
                    poseLandmarkerResult.landmarks().get(0).get(it!!.start())
                        .x() * imageWidth * scaleFactor,
                    poseLandmarkerResult.landmarks().get(0).get(it.start())
                        .y() * imageHeight * scaleFactor,
                    poseLandmarkerResult.landmarks().get(0).get(it.end())
                        .x() * imageWidth * scaleFactor,
                    poseLandmarkerResult.landmarks().get(0).get(it.end())
                        .y() * imageHeight * scaleFactor,
                    linePaint
                )
            }
        }
    }


    fun setResults(
        poseLandmarkerResults: PoseLandmarkerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode = RunningMode.IMAGE
    ) {
        results = poseLandmarkerResults
        this.imageHeight = imageHeight
        this.imageWidth = imageWidth

        scaleFactor = when (runningMode) {
            RunningMode.IMAGE,
            RunningMode.VIDEO -> {
                min(width * 1f / imageWidth, height * 1f / imageHeight)
            }

            RunningMode.LIVE_STREAM -> {
                // PreviewView is in FILL_START mode. So we need to scale up the
                // landmarks to match with the size that the captured images will be
                // displayed.
                max(width * 1f / imageWidth, height * 1f / imageHeight)
            }
        }
        invalidate()
    }

    companion object {
        private const val LANDMARK_STROKE_WIDTH = 12F
        private const val WRONG_LANDMARK_STROKE_WIDTH = 12F
    }
}