package com.ms.playstop.model


data class Profile(
    var name : String? = null,
    var email : String? = null,
    var gender : Int = GENDER_UNKNOWN,
    var token : String? = null,
    var refreshToken : String? = null,
    var expiresIn : Long = 300000,
    var isActive: Boolean = true,
    var phone: String? = null,
    var isPhoneVerified: Boolean = false,
    var isEmailVerified: Boolean = false,
    var endSubscriptionDate: String? = null,
    var isSubscriptionExpired: Boolean? = null
) {
    companion object {
        val SAVE_KEY = "Profile Save Key"
        const val GENDER_MAIL = 1
        const val GENDER_FEMALE = 2
        const val GENDER_UNKNOWN = 0
    }
}