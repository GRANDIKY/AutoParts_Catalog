    package com.example.autoparts_catalog.viewmodels
    
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.viewModelScope
    import com.example.autoparts_catalog.models.CarService
    import com.example.autoparts_catalog.models.Cars
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch
    
    class CarsListViewModel(private val carService: CarService): ViewModel()  {
        private val _carsState = MutableStateFlow<List<Cars>>(emptyList())
        val carsState: StateFlow<List<Cars>> = _carsState
    
        fun fetchCarFromFirestor() {
            viewModelScope.launch {
                val cars = carService.fetchCarsFromFirestore()
                _carsState.value = cars
            }
        }
    
        fun saveSelectedCar(car: Cars) {
            carService.saveSelectedCar(car)
        }
    
        fun getSelectedCar(): Cars? {
            return carService.getSelectedCar()
        }
    
        companion object {
            fun provideFactory(carService: CarService): ViewModelProvider.Factory =
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (modelClass.isAssignableFrom(CarsListViewModel::class.java)) {
                            "UNCHECKED_CAST"
                            return CarsListViewModel(carService) as T
                        }
                        throw IllegalArgumentException("Unknown ViewModel class")
                    }
                }
        }
    }