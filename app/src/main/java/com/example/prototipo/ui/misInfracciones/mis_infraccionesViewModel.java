package com.example.prototipo.ui.misInfracciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class mis_infraccionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public mis_infraccionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}