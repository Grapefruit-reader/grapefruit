package com.example.grapefruit.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.grapefruit.data.viewmodel.WriteVoteViewModel
import com.example.grapefruit.model.User

@Composable
fun VoteScreen(
    writeVoteViewModel: WriteVoteViewModel = hiltViewModel()
) {
    var buttonState = writeVoteViewModel.buttonState.collectAsState()


    fun isEnabled(): Boolean{
        return when (buttonState.value) {
            WriteVoteViewModel.ButtonStates.ACTIVE -> true
            else -> false
        }
    }

    @Composable
    fun VoteButton (
        onClick : () -> Unit,
        text: String
    ){
        Button(
            onClick = {
                writeVoteViewModel.buttonState.value = WriteVoteViewModel.ButtonStates.DISABLED
                onClick()
            },
            enabled = isEnabled(),
        )
        {
            Text(text = text)
        }
    }

    @Composable
    fun YesButton (){
        VoteButton(onClick = { writeVoteViewModel.writeYesFromData() }, text = "Yes" )
    }

    @Composable
    fun NoButton (){
        VoteButton(onClick = { writeVoteViewModel.writeNoFromData() }, text = "No" )
    }

    @Composable
    fun ResidesButton (){
        VoteButton(onClick = { writeVoteViewModel.writeResidesFromData() }, text = "Resides" )
    }



    Column {

        Text(text = "asd")
        Row {


            YesButton()

            NoButton()

            ResidesButton()


            Button(onClick = {
                writeVoteViewModel.setData(User("asd", 23.3))
            }) {
                Text(text = "Activate Button")
            }
        }
    }
}








@Preview
@Composable
fun VoteScreenPrewiew() {
    VoteScreen()
}