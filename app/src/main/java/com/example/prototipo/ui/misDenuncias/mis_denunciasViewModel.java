package com.example.prototipo.ui.misDenuncias;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class mis_denunciasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public mis_denunciasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}