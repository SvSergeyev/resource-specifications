package tech.sergeyev.education.app.spring.base

import ws.IWsSession
import ws.IWsSessionRepo

class SpringWsSessionRepo: IWsSessionRepo {
    private val sessions: MutableSet<IWsSession> = mutableSetOf()
    override fun add(session: IWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}