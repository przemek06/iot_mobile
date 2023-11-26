package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : JpaRepository<Topic, Int> {
    fun findAllByProjectId(projectId: Int) : List<Topic>
    fun existsByUniqueNameAndProjectId(uniqueName: String, projectId: Int) : Boolean

    @Query("SELECT COUNT(DISTINCT ic.id) + COUNT(DISTINCT oc.id) " +
            "FROM Topic t " +
            "LEFT JOIN OutputComponent oc ON t.id = oc.topic.id " +
            "LEFT JOIN InputComponent ic ON t.id = ic.topic.id " +
            "WHERE t.id = :id")
    fun countTopicUsage(@Param("id") id: Int) : Int

    fun findAllByUniqueNameIn(uniqueNames: List<String>) : List<Topic>
    fun findByUniqueName(uniqueName: String) : Topic?

}