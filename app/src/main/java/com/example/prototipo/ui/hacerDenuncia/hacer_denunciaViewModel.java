package com.example.prototipo.ui.hacerDenuncia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class hacer_denunciaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public hacer_denunciaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}