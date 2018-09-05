package com.staqu.healthmonitoring.network.model

data class Check(
        var update_url: String,
        var ping_url: String,
        var pause_url: String,
        var grace: Long,
        var n_pings: Long,
        var next_ping: String?,
        var timeout: Long,
        var tags: String?,
        var schedule: String?,
        var status: String,
        var last_ping: String?,
        var name: String
) : BaseResponse()