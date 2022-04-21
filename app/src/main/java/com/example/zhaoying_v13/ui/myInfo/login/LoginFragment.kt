package com.example.zhaoying_v13.ui.myInfo.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.zhaoying_v13.R
import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.databinding.FragmentLoginBinding
import com.example.zhaoying_v13.ui.myInfo.login.model.UserLoginInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //数据库初始化处理
        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = LoginViewModelFactory(dataSource, application)
        loginViewModel = ViewModelProvider(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.phoneNumber
        val passwordEditText = binding.password
        val loginButton = binding.loginButton
        val loadingProgressBar = binding.loading
        val registerText=binding.textRegister

        loginViewModel.loggedInUser.observe(viewLifecycleOwner,
            Observer { loggedInUser ->
                loggedInUser ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                if (loggedInUser.status=="200"){
                    Log.i("SLEF_TAG","loggedInUser.status==\"200\"")
                    updateUiWithUser(loggedInUser)
                    //保证程序结束时job已经完成
                    lifecycleScope.launch {
                        val job=launch() {
                            loginViewModel.updateDatabaseWithUser(loggedInUser)
                        }
                        job.join()
                        requireActivity().finish()
                    }

                }
                if (loggedInUser.status=="B404")
                    showLoginFailed("密码不正确")
                if (loggedInUser.status=="A404")
                    showLoginFailed("账户不存在")
            })

        loginButton.setOnClickListener {
            //loadingBar显示
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
        //跳转注册界面
        binding.textRegister.setOnClickListener{
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }

    private fun updateUiWithUser(loggedInUser: UserLoginInfo) {
        val welcome = getString(R.string.welcome) + loggedInUser.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_SHORT).show()
    }



    private fun showLoginFailed(errorString: String) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}