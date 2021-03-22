package com.viewlift.models.network.socket

import androidx.annotation.NonNull
import com.google.android.exoplayer2.util.Log
import com.google.gson.GsonBuilder
import com.viewlift.models.billing.appcms.purchase.SocketResponse
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * This class is for making socket connection to server and listens for purchase event completion
 * @author  Wishy
 * @since   2020-10-23
 */

class AppCMSSocket() : WebSocketListener() {
    val TAG = "AppCMSSocket"
    var socketListener: SocketListener? = null

    companion object {
        val CLOSE_CODE = 1000
    }

    /** set listener to reciever callback of complete and error
     * @param listener - listener for callback
     * */
    fun setListener(listener: SocketListener) {
        socketListener = listener
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.i(TAG.plus(" ").plus("onClosed"), code.toString().plus(" ").plus(reason))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.i(TAG.plus(" ").plus("onClosing"), code.toString().plus(" ").plus(reason))
        socketListener?.onComplete()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        if (response != null) {
            Log.e(TAG.plus(" ").plus("onFailure"), response.message)
            if (response.networkResponse != null)
                Log.e(TAG.plus(" ").plus("onFailure"), response.networkResponse.toString())
            if (response.body != null)
                Log.e(TAG.plus(" ").plus("onFailure"), response.body.toString())
        }
        t.message?.let { Log.e(TAG.plus(" ").plus("onFailure"), it) }
        t.message?.let { socketListener?.onError(it) }

    }

    override fun onMessage(webSocket: WebSocket, message: String) {
        Log.i(TAG.plus(" ").plus("onMessage"), message)
        if (!message.isEmpty()) {
            val socketResponse: SocketResponse = GsonBuilder().create().fromJson(message, SocketResponse::class.java)
            if (socketResponse.status.equals("completed")) {

                webSocket.close(CLOSE_CODE, "Close socket from onMessage.")
            }
        }

    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.i(TAG.plus(" ").plus("onOpen"), response.message)
        socketListener?.onOpen(webSocket)
    }


    interface SocketListener {
        fun onOpen(@NonNull webSocket: WebSocket)
        fun onComplete()
        fun onError(@NonNull error: String)
    }
}