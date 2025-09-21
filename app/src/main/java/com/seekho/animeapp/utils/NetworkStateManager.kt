package com.seekho.animeapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected
    
    private val _networkType = MutableLiveData<NetworkType>()
    val networkType: LiveData<NetworkType> = _networkType
    
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    
    enum class NetworkType {
        WIFI, CELLULAR, ETHERNET, NONE
    }
    
    init {
        updateNetworkState()
        startNetworkMonitoring()
    }
    
    /**
     * Check if device is currently connected to internet
     */
    fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    
    /**
     * Get current network type
     */
    fun getCurrentNetworkType(): NetworkType {
        val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE
        
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
            else -> NetworkType.NONE
        }
    }
    
    /**
     * Check if connected to WiFi (for high-quality video streaming)
     */
    fun isConnectedToWiFi(): Boolean {
        return getCurrentNetworkType() == NetworkType.WIFI
    }
    
    /**
     * Check if connected to mobile data
     */
    fun isConnectedToMobileData(): Boolean {
        return getCurrentNetworkType() == NetworkType.CELLULAR
    }
    
    /**
     * Start monitoring network changes
     */
    private fun startNetworkMonitoring() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                updateNetworkState()
            }
            
            override fun onLost(network: Network) {
                super.onLost(network)
                updateNetworkState()
            }
            
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                updateNetworkState()
            }
        }
        
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
    }
    
    /**
     * Update network state and notify observers
     */
    private fun updateNetworkState() {
        val isConnected = isNetworkAvailable()
        val networkType = getCurrentNetworkType()
        
        _isConnected.postValue(isConnected)
        _networkType.postValue(networkType)
    }
    
    /**
     * Stop network monitoring (call in Application.onTerminate)
     */
    fun stopNetworkMonitoring() {
        networkCallback?.let { callback ->
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}
