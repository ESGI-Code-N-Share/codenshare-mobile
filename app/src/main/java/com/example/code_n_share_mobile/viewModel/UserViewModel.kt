package com.example.code_n_share_mobile.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_n_share_mobile.models.EditUser
import com.example.code_n_share_mobile.models.User
import com.example.code_n_share_mobile.repositories.UserRepository
import com.example.code_n_share_mobile.utils.extractErrorMessage
import kotlinx.coroutines.launch

data class FollowResult(val success: Boolean, val error: String?)
data class UnfollowResult(val success: Boolean, val error: String?)

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<User>>()
    val searchResults: LiveData<List<User>> get() = _searchResults

    private val _followers = MutableLiveData<List<User>>()
    val followers: LiveData<List<User>> get() = _followers

    private val _following = MutableLiveData<List<User>>()
    val following: LiveData<List<User>> get() = _following

    private val _followResult = MutableLiveData<FollowResult>()
    val followResult: LiveData<FollowResult> get() = _followResult

    private val _unfollowResult = MutableLiveData<UnfollowResult>()
    val unfollowResult: LiveData<UnfollowResult> get() = _unfollowResult

    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                val users = userRepository.searchUsers(query)
                _searchResults.postValue(users)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("UserViewModel", "Error searching users: $errorMessage")
                _searchResults.postValue(emptyList())
            }
        }
    }

    fun followUser(followerId: String, followedId: String) {
        viewModelScope.launch {
            try {
                userRepository.followUser(followerId, followedId)
                _followResult.postValue(FollowResult(success = true, error = null))
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("UserViewModel", "Error following user: $errorMessage")
                _followResult.postValue(FollowResult(success = false, error = errorMessage))
            }
        }
    }

    fun unfollowUser(followerId: String, followedId: String) {
        viewModelScope.launch {
            try {
                userRepository.unfollowUser(followerId, followedId)
                _unfollowResult.postValue(UnfollowResult(success = true, error = null))
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("UserViewModel", "Error unfollowing user: $errorMessage")
                _unfollowResult.postValue(UnfollowResult(success = false, error = errorMessage))
            }
        }
    }

    fun getFollowers(userId: String) {
        viewModelScope.launch {
            try {
                val followers = userRepository.getFollowers(userId)
                _followers.postValue(followers)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("UserViewModel", "Error fetching followers: $errorMessage")
                _followers.postValue(emptyList())
            }
        }
    }

    fun getFollowing(userId: String) {
        viewModelScope.launch {
            try {
                val following = userRepository.getFollowing(userId)
                _following.postValue(following)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("UserViewModel", "Error fetching following: $errorMessage")
                _following.postValue(emptyList())
            }
        }
    }

    fun updateUserProfile(userId: String, user: EditUser) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(userId, user)
                Log.d("UserViewModel", "User profile updated successfully")
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("UserViewModel", "Error updating user profile: $errorMessage")
            }
        }
    }
}
