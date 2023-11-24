package com.atom.atomstep.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.atom.atomstep.base.BaseViewModel
import com.atom.atomstep.data.entity.DrinkEntity
import com.atom.atomstep.data.entity.StepEntity
import com.atom.atomstep.data.repo.DrinkRepository
import com.atom.atomstep.data.repo.StepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

/**
 *  author : liuxe
 *  date : 2023/11/16 17:27
 *  description :
 */
class DrinkVM : BaseViewModel() {
    private val repository = DrinkRepository()

    fun queryDrinkEntityByDay() = liveData(Dispatchers.IO) {
        emit(repository.queryDrinkEntityByDay())
    }

    /**
     * 查询今天喝水数据
     * @return Flow<List<StepEntity>>
     */
    fun queryDrinkToday() = liveData(Dispatchers.IO) {
        repository.queryDrinkByDay().collectLatest {
            emit(it)
        }
    }

    /**
     * 插入
     * @param step StepEntity
     * @return Flow<String>
     */
    fun insertDrink(drink: DrinkEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertDrink(drink)
    }

    /**
     * 更新
     * @param step StepEntity
     * @return Flow<String>
     */
    fun updateDrink(drink: DrinkEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateDrink(drink)
    }
}