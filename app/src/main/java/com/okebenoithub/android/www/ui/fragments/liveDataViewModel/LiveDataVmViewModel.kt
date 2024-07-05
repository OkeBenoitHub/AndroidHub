package com.okebenoithub.android.www.ui.fragments.liveDataViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.okebenoithub.android.www.components.Event

class LiveDataVmViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    // define score b mutable live data
    private val _scoreB = MutableLiveData<String>()
    val scoreB: LiveData<String> get() = _scoreB // get score b

    // define score a mutable live data
    private val _scoreA = MutableLiveData<String>()
    val scoreA: LiveData<String> get() = _scoreA // get score a

    // define win score label mutable live data
    private val _winScoreLabel = MutableLiveData<String>()
    val winScoreLabel: LiveData<String> get() = _winScoreLabel

    // define lose score label mutable live data
    private val _loseScoreLabel = MutableLiveData<String>()
    val loseScoreLabel: LiveData<String> get() = _loseScoreLabel

    init {
        // init score b and a
        _scoreB.value = "0"
        _scoreA.value = "0"
        // init win score label
        _winScoreLabel.value = "Win"
        // init lose score label
        _loseScoreLabel.value = "Lose"
    }

    /**
     * Increase team a score
     */
    fun increaseTeamBScore(points: Int) {
        // increase score b
        _scoreB.value = _scoreB.value?.toInt()?.plus(points).toString()
    }

    /**
     * Increase team a score
     */
    fun increaseTeamAScore(points: Int) {
        // increase score a
        _scoreA.value = _scoreA.value?.toInt()?.plus(points).toString()
    }

    private var _winnerOrTight = MutableLiveData<Event<String?>>()
    val winnerOrTight: LiveData<Event<String?>> get() = _winnerOrTight

    /**
     * Check winner or tight between the 2 teams
     * @return string: Team A or B
     */
    fun checkWinnerOrTight() {
        // check winner or tight between the 2 teams and store it in live data event
        var winnerOrTight = if (scoreA.value!!.toInt() > scoreB.value!!.toInt()) "Team A" else "Team B"
        if (scoreA.value!!.toInt() == scoreB.value!!.toInt()) {winnerOrTight = "Tight"}
        _winnerOrTight.value = Event(winnerOrTight)
        // set win score label and lose score label
        _winScoreLabel.value = if (winnerOrTight == "Team A") "Win: Team A" else "Win: Team B"
        _loseScoreLabel.value = if (winnerOrTight == "Team A") "Lose: Team B" else "Lose: Team A"

    }

    private val _stopGame = MutableLiveData<Event<Boolean>>()
    val stopGame: LiveData<Event<Boolean>> get() = _stopGame

    /**
     * Stop game
     */
    fun stopGame() {
        // stop game
        _stopGame.value = Event(true)
    }

    private val _startGame = MutableLiveData<Event<Boolean>>()
    val startGame: LiveData<Event<Boolean>> get() = _startGame

    /**
     * Start game
     */
    fun startGame() {
        // start game
        _startGame.value = Event(true)
        // reset both scores
        _scoreA.value = "0"
        _scoreB.value = "0"
    }
}

class ViewModelSample: ViewModel() {
    private val userLiveData = MutableLiveData<User>()
    // get user name from live data
    val userName: LiveData<String> = userLiveData.map { user -> user.name }
    // get user age from live data
    val userAge: LiveData<Int> = userLiveData.map { user -> user.age }
    // get user email from live data
    val userEmail: LiveData<String> = userLiveData.map { user -> user.email }
    // get user is minor from live data
    val userIsMinor: LiveData<Boolean> = userLiveData.map { user -> user.isMinor }
}

data class User(
    val name: String,
    val age: Int,
    val email: String,
    val isMinor: Boolean
)

class ViewModelSample2 : ViewModel() {

    // LiveData to trigger the data source
    private val _trigger = MutableLiveData<String>()

    // Simulate a data source based on a trigger LiveData
    private val _data: LiveData<String> = _trigger.switchMap { trigger ->
        getData(trigger)
    }

    // Publicly exposed LiveData
    val data: LiveData<String> get() = _data

    // Function to set the trigger value
    fun setTrigger(trigger: String) {
        _trigger.value = trigger
    }

    // Simulate a function that fetches data based on the trigger
    private fun getData(trigger: String): LiveData<String> {
        val result = MutableLiveData<String>()
        result.value = "Data for trigger: $trigger"
        return result
    }
}