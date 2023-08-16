package com.miniproject.kubeBoard.entity.deployment

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.miniproject.kubeBoard.service.CommonService
import io.fabric8.kubernetes.api.model.apps.Deployment
import javax.persistence.*

@Entity(name = "deployment")
class DeploymentData(
        val deploymentName: String,
        val namespace: String,
        val createdTime: String,
        val annotation: String,
        val replicaCount: Int,
        val readyReplicas: Int,
        val availableReplicas: Int,
        val labels: String,
        val uid: String,
        val strategyType: String,
        val strategySelector: String,
        val revisionHistoryLimit: Int,//리비전 내역 한도
        //rollingUpdate
        val maxSurge: String,//최대 증가율(surge)
        val maxUnavilable: String,//최대 비가용

        @OneToMany(mappedBy = "deploymentData", cascade = [CascadeType.ALL], orphanRemoval = true)
        @JsonManagedReference
        var deploymentConditions: MutableList<ConditionData> = mutableListOf(),


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val deploymentIdx: Long? = null,
) {
    companion object {
        fun of(deployment: Deployment): DeploymentData {
            val deploymentData = DeploymentData(
                    deploymentName = deployment.metadata.name,
                    namespace = deployment.metadata.namespace,
                    createdTime = deployment.metadata.creationTimestamp,
                    replicaCount = deployment.status.replicas,
                    readyReplicas = deployment.status.readyReplicas,
                    labels = CommonService.getLabel(deployment.metadata.labels),
                    uid = deployment.metadata.uid,
                    strategyType = deployment.spec.strategy.type,
                    strategySelector = CommonService.getLabel(deployment.spec.selector.matchLabels),
                    revisionHistoryLimit = deployment.spec.revisionHistoryLimit,
                    annotation = CommonService.getLabel(deployment.metadata.annotations),
                    maxSurge = deployment.spec.strategy.rollingUpdate.maxSurge.value.toString(),
                    maxUnavilable = deployment.spec.strategy.rollingUpdate.maxUnavailable.value.toString(),
                    availableReplicas = deployment.status.availableReplicas,
                    )
            deploymentData.deploymentConditions.addAll(getDeploymentConditions(deployment,deploymentData))
            return deploymentData
        }

        private fun getDeploymentConditions(deployment: Deployment, deploymentData: DeploymentData): Collection<ConditionData> {
            val conditionList = mutableListOf<ConditionData>()
            for (condition in deployment.status.conditions) {
                conditionList.add(ConditionData.of(condition, deploymentData))
            }
            return conditionList
        }
    }
}