package com.grosianu.jobseeker.ui.login.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentLoginBinding
import com.grosianu.jobseeker.ui.home.HomeActivity
import kotlinx.coroutines.flow.MutableStateFlow

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isEmailValid()
        isPasswordValid()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val intent = it.data
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                try {
                    val account = task.result
                    //Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken.toString())
                } catch (e: ApiException) {
                    //Log.w(TAG, "Google sign in failed", e)
                }
            }
        }

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding?.loginGoogleBtn?.setOnClickListener {
            resultLauncher.launch(googleSignInClient.signInIntent)
        }
        binding?.loginBtn?.setOnClickListener {
            firebaseAuthWithEmail()
        }
        binding?.createAccountBtn?.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            this.findNavController().navigate(action)
        }
        binding?.forgotPasswordBtn?.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModel.auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = viewModel.auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", it.exception)
                    Toast.makeText(
                        requireContext(),
                        "Sign In Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun firebaseAuthWithEmail() {
        if (isLoginFormValid()) {
            val email: String = binding?.loginEmailInputEdit?.text.toString()
            val password: String = binding?.loginPasswordInputEdit?.text.toString()

            viewModel.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {
                        Log.d(TAG, "signInWithEmailAndPassword:success")
                        val user = viewModel.auth.currentUser
                        updateUI(user)
                    } else {
                        Log.d(TAG, "signInWithEmailAndPassword:failure", it.exception)
                        Toast.makeText(
                            requireContext(),
                            "Account does not exist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun isLoginFormValid(): Boolean {
        if (binding?.loginEmailInput?.isErrorEnabled == true ||
            binding?.loginPasswordInput?.isErrorEnabled == true) {
            return false
        }
        return true
    }

    private fun isEmailValid() {
        binding?.loginEmailInputEdit?.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    binding?.loginEmailInput?.isErrorEnabled = true
                    binding?.loginEmailInput?.error = "The email address is invalid"
                } else {
                    binding?.loginEmailInput?.isErrorEnabled = false
                    binding?.loginEmailInput?.error = null
                }
            }
        }
    }

    private fun isPasswordValid() {
        binding?.loginPasswordInputEdit?.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding?.loginPasswordInput?.isErrorEnabled = true
                binding?.loginPasswordInput?.error = "Password field cannot be empty"
            } else {
                binding?.loginPasswordInput?.isErrorEnabled = false
                binding?.loginPasswordInput?.error = null
            }
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}