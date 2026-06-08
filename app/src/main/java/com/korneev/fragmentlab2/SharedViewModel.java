package com.korneev.fragmentlab2;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> text = new MutableLiveData<>("");
    private final MutableLiveData<Integer> color = new MutableLiveData<>(android.graphics.Color.BLACK);
    private final MutableLiveData<Boolean> clearTrigger = new MutableLiveData<>(false);
    public void setData(String input, int colorInt) {
        text.setValue(input);
        color.setValue(colorInt);
        clearTrigger.setValue(false);
    }
    public void requestClear() {
        text.setValue("");
        clearTrigger.setValue(true);
    }
    public LiveData<String> getText() { return text; }
    public LiveData<Integer> getColor() { return color; }
    public LiveData<Boolean> getClearTrigger() { return clearTrigger; }
}