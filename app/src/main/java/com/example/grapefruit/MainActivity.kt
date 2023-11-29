package com.example.grapefruit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

import com.example.grapefruit.navigation.AppNavigationGraph

import com.example.grapefruit.ui.theme.ComposeBasicsTheme

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import com.google.android.gms.tasks.Task

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val RC_SIGN_IN = 2

    private var mGoogleSignInClient : GoogleSignInClient? = null
    private var signedInAccount : GoogleSignInAccount? = null

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signedInAccount = GoogleSignIn.getLastSignedInAccount(this)

        if (signedInAccount == null){
            signIn()
        }else{
            setContent{
                AppContent()
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            signedInAccount = completedTask.getResult(ApiException::class.java)
            GoogleSignIn.getLastSignedInAccount(applicationContext)

            // Signed in successfully, show authenticated UI.
            setContent {
                AppContent()
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }
    }
}


@Composable
fun AppContent(){
    ComposeBasicsTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White))
        {
            AppEntryPoint()
        }
    }
}

@Composable
fun AppEntryPoint(){
    AppNavigationGraph()
}