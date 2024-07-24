package com.example.code_n_share_mobile.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.code_n_share_mobile.BuildConfig
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.models.Message
import com.example.code_n_share_mobile.view.adapter.MessageAdapter
import com.example.code_n_share_mobile.viewModel.MessageViewModel
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageActivity : AppCompatActivity() {

    private val messageViewModel: MessageViewModel by viewModel()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var etMessageContent: EditText
    private lateinit var btnSendMessage: ImageView
    private lateinit var socket: Socket
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        recyclerView = findViewById(R.id.recycler_view_messages)
        loadingProgressBar = findViewById(R.id.loading_progress_bar)
        etMessageContent = findViewById(R.id.et_message_content)
        btnSendMessage = findViewById(R.id.btn_send_message)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) ?: return
        val userEmail = sharedPreferences.getString("email", null) ?: return
        val conversationId = intent.getStringExtra("conversationId") ?: return

        messageAdapter = MessageAdapter(emptyList())
        recyclerView.adapter = messageAdapter

        messageViewModel.loadMessages(userId, userEmail, conversationId)

        messageViewModel.messages.observe(this) { messages: List<Message> ->
            Log.d("MessageActivity", "Messages observed: ${messages.size}")
            loadingProgressBar.visibility = View.GONE
            messageAdapter.updateMessages(messages)
            recyclerView.scrollToPosition(messages.size - 1)
        }

        btnSendMessage.setOnClickListener {
            val content = etMessageContent.text.toString().trim()
            if (content.isNotEmpty()) {
                messageViewModel.sendMessage(userId, userEmail, conversationId, content)
                etMessageContent.text.clear()
            }
        }

        loadingProgressBar.visibility = View.VISIBLE

        // initialize Socket.IO
        try {
            socket = IO.socket(BuildConfig.CODENSHARE_API_URL)
            socket.connect()
            socket.on(Socket.EVENT_CONNECT) {
                Log.d("SocketIO", "Connected to socket")
            }.on("new_message") { args ->
                if (args[0] != null) {
                    val newMessageJson = args[0] as JSONObject
                    val newMessage = gson.fromJson(newMessageJson.toString(), Message::class.java)
                    newMessage.isSentByCurrentUser = newMessage.sender?.email == userEmail
                    runOnUiThread {
                        messageViewModel.loadMessages(userId, userEmail, conversationId)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("Socket", "Error initializing socket: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::socket.isInitialized) {
            socket.disconnect()
            socket.off("new_message")
        }
    }
}
