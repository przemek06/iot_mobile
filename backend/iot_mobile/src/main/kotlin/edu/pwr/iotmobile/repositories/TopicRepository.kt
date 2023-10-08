package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.Topic
import org.springframework.data.jpa.repository.JpaRepository

interface TopicRepository: JpaRepository<Topic, Int> {
}