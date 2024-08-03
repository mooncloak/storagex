package com.mooncloak.kodetools.storagex.keyvalue.redis

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RedisConfig public constructor(
    @SerialName(value = PROPERTY_USERNAME) public val username: String,
    @SerialName(value = PROPERTY_PASSWORD) public val password: String,
    @SerialName(value = PROPERTY_URL) public val url: String
) {

    public companion object {

        internal const val PROPERTY_USERNAME = "username"
        internal const val PROPERTY_PASSWORD = "password"
        internal const val PROPERTY_URL = "url"
    }
}

public val RedisConfig.secureConnectionString: String
    get() = "rediss://$username:$password@$url"
