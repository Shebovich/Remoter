package com.example.remoteandroid.data.mappers

import com.connectsdk.core.AppInfo
import com.example.remoteandroid.domain.models.ApplicationInfo

class ApplicationsMapper {

    fun mapToApplicationInfo(appInfo: AppInfo): ApplicationInfo {
        return ApplicationInfo(
            id = appInfo.id,
            name = appInfo.name,
            avatarUrl = "url"
        )
    }
}