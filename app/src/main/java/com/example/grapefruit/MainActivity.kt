package com.example.grapefruit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.grapefruit.navigation.NavGraph
import com.example.grapefruit.ui.common.HomeScreen_Preview
import com.example.grapefruit.ui.common.NormalTextView_Error_Preview
import com.example.grapefruit.ui.common.NormalTextView_Preview
import com.example.grapefruit.ui.theme.ComposeBasicsTheme
import com.example.grapefruit.ui.theme.GrapefruitTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.services.sheets.v4.SheetsScopes

class MainActivity : ComponentActivity() {
    private var mGoogleSignInClient : GoogleSignInClient? = null

    private var signedInAccount : GoogleSignInAccount? = null

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    val RC_SIGN_IN = 2



    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ComposeBasicsTheme {
                Greeting("Android")
            }
        }
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signedInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (signedInAccount == null){
            signIn()
        }

        setContent {
            ComposeBasicsTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
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

    @OptIn(ExperimentalMaterial3Api::class)
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            signedInAccount = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            //updateUI(account)
            setContent {
                ComposeBasicsTheme {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeBasicsTheme {
        Greeting("Android")
    }
}