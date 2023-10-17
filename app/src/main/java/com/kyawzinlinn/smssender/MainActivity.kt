package com.kyawzinlinn.smssender

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kyawzinlinn.smssender.ui.screen.SmsApp
import com.kyawzinlinn.smssender.ui.theme.SMSSenderTheme

class MainActivity : ComponentActivity() {
    private lateinit var messageReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            SMSSenderTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    SmsApp()
                }
            }
        }
    }
}
