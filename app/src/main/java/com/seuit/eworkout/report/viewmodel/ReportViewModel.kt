package com.seuit.eworkout.report.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seuit.eworkout.report.model.ReportState
import com.seuit.eworkout.report.util.MathRounder
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.seuit.eworkout.report.util.CalendarUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReportViewModel: ViewModel() {
    val auth: FirebaseAuth = Firebase.auth
    val firestore = Firebase.firestore
    var totalKcal: MutableLiveData<Double> = MutableLiveData(0.0)
    var totalTime: MutableLiveData<Double> = MutableLiveData(0.0)
    var totalExercises: MutableLiveData<Int> = MutableLiveData(0)
    var heaviestWeight : MutableLiveData<Double> = MutableLiveData(0.0)
    var lightestWeight : MutableLiveData<Double> = MutableLiveData(0.0)
    var currentWeight : MutableLiveData<Double> = MutableLiveData(0.0)
    var currentHeight : MutableLiveData<Double> = MutableLiveData(0.0)
    var currentBmi : MutableLiveData<Double> = MutableLiveData(0.0)

    val pointList = mutableListOf<Double?>()

    val weightDataList = mutableListOf<Double>()
    val heightDataList = mutableListOf<Double>()

    val averageList1 = mutableListOf<Double>()
    val averageList2 = mutableListOf<Double>()
    val averageList3 = mutableListOf<Double>()
    val averageList4 = mutableListOf<Double>()
    val averageList5 = mutableListOf<Double>()
    val averageList6 = mutableListOf<Double>()
    val averageList7 = mutableListOf<Double>()
    val averageList8 = mutableListOf<Double>()
    val averageList9 = mutableListOf<Double>()
    val averageList10 = mutableListOf<Double>()
    val averageList11 = mutableListOf<Double>()
    val averageList12 = mutableListOf<Double>()

    private var heightWeightID: String? = null
    private val _state: MutableLiveData<ReportState> = MutableLiveData(ReportState.LOADING)
    val state: LiveData<ReportState> get() = _state

    val calendarRef = firestore.collection("Calendar")
    val heightWeightRef = firestore.collection("Height_and_Weight")

    fun getReportData(){
        viewModelScope.launch(Dispatchers.IO) {
            clearPoints()
            getDataFromCalendar()
            getDataFromHAW()
        }
    }

    private suspend fun getDataFromCalendar()
    {
        val documents = calendarRef
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .get().await()

        var kcal = 0.0
        var time = 0.0
        var numberOfExercise = 0
        for (it in documents) {
            val calo = it.get("total_calories") as Double
            kcal += calo

            val milliseconds = it.get("total_time") as Double
            time += milliseconds / 60000

            val ex = it.get("number_of_exercise") as Double
            numberOfExercise += ex.toInt()
        }
        totalKcal.postValue(Math.round(kcal * 100) / 100.0)
        totalTime.postValue(Math.round(time * 100) / 100.0)
        totalExercises.postValue(numberOfExercise)
    }

    private suspend fun getDataFromHAW() //HAW = Height and Weight
    {
        val documents = heightWeightRef
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .orderBy("time")
            .get().await()

        var bmi = 0.0
        for (it in documents) {
            bmi = it.get("bmi") as Double
            val _weight: Double = it.get("weight") as Double
            val _height: Double = it.get("height") as Double

            val _month = it.getDate("time")
            val month = _month.toString().subSequence(4, 7)

            weightDataList.add(_weight)
            heightDataList.add(_height)

            when (month) {
                "Jan" -> {
                    averageList1.add(_weight)
                }

                "Feb" -> {
                    averageList2.add(_weight)
                }

                "Mar" -> {
                    averageList3.add(_weight)
                }

                "Apr" -> {
                    averageList4.add(_weight)
                }

                "May" -> {
                    averageList5.add(_weight)
                }

                "Jun" -> {
                    averageList6.add(_weight)
                }

                "Jul" -> {
                    averageList7.add(_weight)
                }

                "Aug" -> {
                    averageList8.add(_weight)
                }

                "Sep" -> {
                    averageList9.add(_weight)
                }

                "Oct" -> {
                    averageList10.add(_weight)
                }

                "Nov" -> {
                    averageList11.add(_weight)
                }
                "Dec" -> {
                    averageList12.add(_weight)
                }
            }
        }
        bmi = Math.round(bmi * 100) / 100.0
        currentBmi.postValue(bmi)
        addDataToPointList()
        _state.postValue(ReportState.LOADED)
    }
    
    private fun addDataToPointList()
    {
        //If list empty do not draw it
        if (averageList1.isNotEmpty())
            pointList.add(MathRounder.round(averageList1.average()))

        if (averageList2.isNotEmpty())
            pointList.add(MathRounder.round(averageList2.average()))

        if (averageList3.isNotEmpty())
            pointList.add(MathRounder.round(averageList3.average()))

        if (averageList4.isNotEmpty())
            pointList.add(MathRounder.round(averageList4.average()))

        if (averageList5.isNotEmpty())
            pointList.add(MathRounder.round(averageList5.average()))

        if (averageList6.isNotEmpty())
            pointList.add(MathRounder.round(averageList6.average()))

        if (averageList7.isNotEmpty())
            pointList.add(MathRounder.round(averageList7.average()))

        if (averageList8.isNotEmpty())
            pointList.add(MathRounder.round(averageList8.average()))

        if (averageList9.isNotEmpty())
            pointList.add(MathRounder.round(averageList9.average()))

        if (averageList10.isNotEmpty())
            pointList.add(MathRounder.round(averageList10.average()))

        if (averageList11.isNotEmpty())
            pointList.add(MathRounder.round(averageList11.average()))

        if (averageList12.isNotEmpty())
            pointList.add(MathRounder.round(averageList12.average()))

        if (weightDataList.isNotEmpty()) {
            currentWeight.postValue( weightDataList.last())
            getHeaviestWeight(weightDataList)
            getLightestWeight(weightDataList)
        } else {
            weightDataList.add(0.0)
        }

        if (heightDataList.isNotEmpty()) {
            currentHeight.postValue(heightDataList.last())
        } else {
            heightDataList.add(0.0)
        }
    }

    private fun clearPoints()
    {
        averageList1.clear()
        averageList2.clear()
        averageList3.clear()
        averageList4.clear()
        averageList5.clear()
        averageList6.clear()
        averageList7.clear()
        averageList8.clear()
        averageList9.clear()
        averageList10.clear()
        averageList11.clear()
        averageList12.clear()
        pointList.clear()
    }

    private fun getHeaviestWeight(weight_list : List<Double>){
        var max = 0.0
        for(data in weight_list)
        {
            if(data > max)
                max = data
        }
        heaviestWeight.postValue(max)
    }

    private fun getLightestWeight(weight_list : List<Double>){
        var min = weight_list.first()
        for(data in weight_list)
        {
            if(data < min)
                min = data
        }
        lightestWeight.postValue(min)
    }

    fun updateWeightHeight(current_weight: Float, current_height: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val startToday = Timestamp(CalendarUtil.getStartOfToday())
            val endToday = Timestamp(CalendarUtil.getEndOfToday())

            val querySnapshot = heightWeightRef.whereGreaterThan("time", startToday)
                .whereLessThan("time", endToday)
                .get().await()

            val _BMI: Float = current_weight / ((current_height / 100) * (current_height / 100))
            val data = hashMapOf(
                "user_id" to auth.currentUser!!.uid,
                "height" to current_height,
                "weight" to current_weight,
                "time" to Timestamp.now(),
                "bmi" to _BMI
            )
            var id = ""
            for(doc in querySnapshot.documents) {
                id = doc.id
            }
            if(id.isBlank()) {
                //Update
                heightWeightRef.add(data)
            }
            else {
                //Add
                heightWeightRef.document(id).update(data as Map<String, Any>)
            }
        }
    }

    fun changeStateTo(state: ReportState){
        _state.value = state
    }

}