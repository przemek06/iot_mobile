package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : JpaRepository<Topic, Int> {
    fun findAllByProjectId(projectId: Int) : List<Topic>
    fun existsByNameAndProjectId(name: String, projectId: Int) : Boolean

}