package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Int> {

    @Query(
        nativeQuery = true,
        value = "SELECT m.* FROM topic t " +
                "JOIN message m ON t.id = m.topic_id " +
                "WHERE t.id IN :topicIds " +
                "AND m.id IN " +
                "(SELECT ranked.id " +
                " FROM (SELECT mm.id, mm.topic_id, ROW_NUMBER() OVER (PARTITION BY mm.topic_id ORDER BY mm.ts_sent DESC) AS row_num " +
                "       FROM message mm " +
                "       WHERE mm.topic_id IN :topicIds) ranked " +
                " WHERE ranked.row_num <= :n)"
    )
    fun findNLastMessagesForTopics(@Param("topicIds") topicIds: List<Int>,@Param("n") n: Int): List<Message>
}