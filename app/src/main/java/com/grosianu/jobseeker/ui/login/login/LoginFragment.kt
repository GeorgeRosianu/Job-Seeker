package com.grosianu.jobseeker.ui.login.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentLoginBinding
import com.grosianu.jobseeker.ui.home.HomeActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

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

        // Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
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
        )
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.loginGoogleBtn.setOnClickListener {
            resultLauncher.launch(googleSignInClient.signInIntent)
        }
        binding.loginBtn.setOnClickListener {
            firebaseAuthWithEmail()
        }
        binding.createAccountBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            this.findNavController().navigate(action)
        }
        binding.forgotPasswordBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        val email: String = binding.loginEmailInputEdit.text.toString()
        val password: String = binding.loginPasswordInputEdit.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(
                requireContext(),
                "Please fill the required fields",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), OnCompleteListener {
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
                })
        }
    }

//    private fun uploadUserData() {
//        FirebaseUtils().fireStoreDatabase.collection("users")
//            .add()
//    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}