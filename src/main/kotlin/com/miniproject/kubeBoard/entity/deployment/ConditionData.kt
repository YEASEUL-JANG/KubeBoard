package com.miniproject.kubeBoard.entity.deployment

import com.fasterxml.jackson.annotation.JsonBackReference
import io.fabric8.kubernetes.api.model.Condition
import io.fabric8.kubernetes.api.model.apps.DeploymentCondition
import javax.persistence.*

@Entity(name = "deployment_condition")
class ConditionData(
        val lastTransitionTime: String,
        val message: String,
        val reason: String,
        val type: String,
        val status: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "deployment_idx")
        @JsonBackReference
        val deploymentData: DeploymentData,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val conditionIdx: Long?=null,

) {
    companion object{
        fun of(condition: DeploymentCondition, deploymentData: DeploymentData): ConditionData {
            return ConditionData(
                    lastTransitionTime = condition.lastTransitionTime,
                    message = condition.message,
                    reason = condition.reason,
                    status = condition.status,
                    type = condition.type,
                    deploymentData = deploymentData,
            )
        }
    }

}