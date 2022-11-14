/*
 * Copyright 2022 DroidconKE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android254.data.repos

import com.android254.data.dao.SessionDao
import com.android254.data.network.apis.SessionRemoteSource
import com.android254.data.network.util.NetworkError
import com.android254.data.repos.mappers.toDomainModel
import com.android254.data.repos.mappers.toEntity
import com.android254.domain.models.ResourceResult
import com.android254.domain.models.SessionDomainModel
import com.android254.domain.repos.SessionsRepo
import javax.inject.Inject

class SessionsManager @Inject constructor(
    private val api: SessionRemoteSource,
    private val dao: SessionDao
) : SessionsRepo {
    override suspend fun fetchAndSaveSessions(): ResourceResult<List<SessionDomainModel>> {
        return try {
            val response = api.fetchSessions()

            val data = response.data

            if (data.isEmpty()) {
                ResourceResult.Empty()
            }

            val sessions = data.map {
                it.toEntity()
            }

            dao.insert(sessions)

            ResourceResult.Success(
                data = sessions.map {
                    it.toDomainModel()
                }
            )
        } catch (e: Exception) {
            when (e) {
                is NetworkError -> ResourceResult.Error("Network error", networkError = true)
                else -> ResourceResult.Error("Error fetching sessions", networkError = false)
            }
        }
    }
}