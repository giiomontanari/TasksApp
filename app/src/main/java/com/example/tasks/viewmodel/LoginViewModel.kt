package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mPriorityRepository = PriorityRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin;

    private val mLoggedUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = mLoggedUser;
    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email, password, object : ApiListener<HeaderModel> {
            override fun onSucess(model: HeaderModel) {

                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token);
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey);
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name);

                RetrofitClient.addHeader(model.token, model.personKey);

                mLogin.value = ValidationListener();
            }

            override fun onFailure(str: String) {
                mLogin.value = ValidationListener(str);
            }

        });
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val token = mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY);
        val personKey = mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY);
        val logged = (token != "" && personKey != "");

        RetrofitClient.addHeader(token, personKey);

        if (!logged) {
            mPriorityRepository.all();
        }

        mLoggedUser.value = logged;
    }

}