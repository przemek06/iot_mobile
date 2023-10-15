package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.ProjectDTO
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
class Project (
    @Column(nullable = false)
    var name: String,
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var createdBy: User,
    @Column(nullable = false, unique = true)
    var connectionKey: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
    constructor() : this("", User(), "")

    fun toDTO() : ProjectDTO {
        return ProjectDTO(
            id, name, createdBy.toUserInfoDTO(), connectionKey
        )
    }
}