package com.example.joseleite.chatbot_mobile

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.view.ChatView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ACCESS_TOKEN = " 07e8e851ca5d4215ac88c346cbdf9b4a"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val human = ChatUser(
                1,
                "You",
                BitmapFactory.decodeResource(resources,
                        R.drawable.ic_account_circle)
        )

        val agent = ChatUser(
                2,
                "Agent",
                BitmapFactory.decodeResource(resources,
                        R.drawable.ic_account_circle)
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FuelManager.instance.baseHeaders = mapOf(
                "Authorization" to "Bearer $ACCESS_TOKEN"
        )
        FuelManager.instance.basePath =
                "https://api.dialogflow.com/v1/"

        FuelManager.instance.baseParams = listOf(
                "v" to "20170712",                  // latest protocol
                "sessionId" to UUID.randomUUID(),   // random ID
                "lang" to "pt"                      // English language
        )
        val my_chat_view: ChatView = findViewById(R.id.my_chat_view) as ChatView
        my_chat_view.setOnClickSendButtonListener(
                View.OnClickListener {
                    my_chat_view.send(Message.Builder()
                            .setUser(human)
                            .setText(my_chat_view.inputText)
                            .build()
                    )

                    // More code here
                    Fuel.get("/query",
                            listOf("query" to my_chat_view.inputText))
                            .responseJson { _, _, result ->
                                val reply = result.get().obj()//.toString
                                        .getJSONObject("result")
                                        .getJSONObject("fulfillment")
                                        .getString("speech")

                                // More code here
                                my_chat_view.send(Message.Builder()
                                        .setRight(true)
                                        .setUser(agent)
                                        .setText(reply)
                                        .build()
                                )
                            }
                }
        )





    }



}
