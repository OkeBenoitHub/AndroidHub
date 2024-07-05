package com.okebenoithub.android.www.utils.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.okebenoithub.android.www.R
import javax.inject.Inject

/**
 * Firebase Authentication :: contain every recurring task dealing with authentication
 */
class FirebaseAuthUtil @Inject constructor(){

    /**
     * Get current user
     * @return current user
     */
    private fun getCurrentUser(): FirebaseUser? {
        return getAuthInstance().currentUser
    }

    /**
     * Get current auth state
     * @return void
     */
    private fun getAuthInstance(): FirebaseAuth {
        return Firebase.auth
    }

    /**
     * Check if current user is signed in
     * @return true or false
     */
    fun checkIfCurrentUserIsSignedIn(): Boolean {
        return getCurrentUser() != null
    }

    // interface that will check for sign in process status
    interface SignUserInProcessCallback {
        fun onSignUserInProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Sign User into his or her account
     * @param userEmail    :: user email
     * @param userPassword :: user password
     * @param signUserInProcessCallback :: callback
     */
    fun signInUserWithEmailAndPassword(
        context: Context,
        userEmail: String,
        userPassword: String,
        signUserInProcessCallback: SignUserInProcessCallback
    ) {
        getAuthInstance().signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    signUserInProcessCallback.onSignUserInProcessStatus(true, null)
                } else {
                    // If sign in fails, generate the appropriate error message.
                    when (val e = task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            when (e.errorCode) {
                                "ERROR_USER_NOT_FOUND" -> {
                                    signUserInProcessCallback.onSignUserInProcessStatus(
                                        false,
                                        context.getString(R.string.email_not_found_for_user_error)
                                    )
                                }
                                "ERROR_USER_DISABLED" -> {
                                    signUserInProcessCallback.onSignUserInProcessStatus(
                                        false,
                                        context.getString(R.string.account_disabled_text)
                                    )
                                }
                            }
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            signUserInProcessCallback.onSignUserInProcessStatus(
                                false,
                                context.getString(R.string.incorrect_password_error)
                            )
                        }
                        else -> {
                            signUserInProcessCallback.onSignUserInProcessStatus(
                                false,
                                e?.localizedMessage
                            )
                        }
                    }
                }
            }
    }

    // interface that will check for sign up process status
    interface SignUserUpProcessCallback {
        fun onSignUserUpProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }
    /**
     * Sign new user with email and password
     * @param userEmail    :: user email
     * @param userPassword :: user password
     * @param signUserUpProcessCallback :: callback
     */
    fun signUpNewUserWithEmailAndPassword(context: Context, userEmail: String, userPassword: String, signUserUpProcessCallback: SignUserUpProcessCallback) {
        getAuthInstance().createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-in user's information
                    signUserUpProcessCallback.onSignUserUpProcessStatus(true, null)
                } else {
                    val e = task.exception
                    if (e is FirebaseAuthInvalidUserException) {
                        when(e.errorCode) {
                            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                signUserUpProcessCallback.onSignUserUpProcessStatus(false,context.getString(R.string.email_not_found_for_user_error))
                            }
                        }
                    } else {
                        signUserUpProcessCallback.onSignUserUpProcessStatus(false,e?.localizedMessage)
                    }
                }
            }
    }

    // interface that will check for sign in process status
    interface SendUserPasswordResetEmailCallback {
        fun onUserPasswordResetEmailSent(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Send password reset email to user
     * @param userEmail :: user email
     * @param sendUserPasswordResetEmailCallback :: callback
     */
    fun sendUserPasswordResetEmail(
        context: Context,
        userEmail: String,
        sendUserPasswordResetEmailCallback: SendUserPasswordResetEmailCallback
    ) {
        getAuthInstance().useAppLanguage()
        getAuthInstance().sendPasswordResetEmail(userEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendUserPasswordResetEmailCallback.onUserPasswordResetEmailSent(
                        task.isSuccessful,
                        null
                    )
                } else {
                    val e = task.exception
                    if (e is FirebaseAuthInvalidUserException) {
                        when (e.errorCode) {
                            "ERROR_USER_NOT_FOUND" -> {
                                sendUserPasswordResetEmailCallback.onUserPasswordResetEmailSent(
                                    false,
                                    context.getString(R.string.email_not_found_for_user_error)
                                )
                            }
                            "ERROR_USER_DISABLED" -> {
                                sendUserPasswordResetEmailCallback.onUserPasswordResetEmailSent(
                                    false,
                                    context.getString(R.string.account_disabled_text)
                                )
                            }
                        }
                    } else {
                        sendUserPasswordResetEmailCallback.onUserPasswordResetEmailSent(
                            false,
                            e?.localizedMessage
                        )
                    }
                }
            }
    }

    // interface that will check for update user email process status
    interface UpdateUserEmailProcessCallback {
        fun onUpdateEmailProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Update user email address
     * @param newEmail :: new user email
     * @param updateUserEmailProcessCallback :: callback
     */
    fun updateUserEmail(newEmail: String, updateUserEmailProcessCallback: UpdateUserEmailProcessCallback) {
        getCurrentUser()!!.updateEmail(newEmail) // to review later
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserEmailProcessCallback.onUpdateEmailProcessStatus(true,null)
                }
            }.addOnFailureListener { e ->
                updateUserEmailProcessCallback.onUpdateEmailProcessStatus(false,e.localizedMessage)
            }
    }

    // interface that will check for update user password process status
    interface UpdateUserPasswordProcessCallback {
        fun onUpdatePasswordProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Update user password
     * @param newPassword :: new user password
     * @param updateUserPasswordProcessCallback :: callback
     */
    fun updateUserPassword(newPassword: String, updateUserPasswordProcessCallback: UpdateUserPasswordProcessCallback) {
        getCurrentUser()!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserPasswordProcessCallback.onUpdatePasswordProcessStatus(true, null)
                }
            }.addOnFailureListener { e ->
                updateUserPasswordProcessCallback.onUpdatePasswordProcessStatus(false, e.localizedMessage)
            }
    }

    // interface that will check for delete current user process status
    interface DeleteUserAccountProcessCallback {
        fun onDeleteUserAccountProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Delete current user from our system
     * @param deleteUserAccountProcessCallback :: callback
     */
    fun deleteUserAccount(deleteUserAccountProcessCallback: DeleteUserAccountProcessCallback) {
        getCurrentUser()!!.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    deleteUserAccountProcessCallback.onDeleteUserAccountProcessStatus(true,null)
                }
            }.addOnFailureListener { e ->
                deleteUserAccountProcessCallback.onDeleteUserAccountProcessStatus(false,e.localizedMessage)
            }
    }

    // interface that will check for sign user out
    interface SignUserOutProcessCallBack {
        fun onSignUserOutProcessStatus(isSuccessful: Boolean, errorMessage: String?)
    }

    /**
     * Sign user out from his or her account in Firebase
     * @param signUserOutProcessCallBack :: callback
     */
    fun signUserOut(signUserOutProcessCallBack: SignUserOutProcessCallBack) {
        getAuthInstance().signOut()
        signUserOutProcessCallBack.onSignUserOutProcessStatus(true,null)
    }
}